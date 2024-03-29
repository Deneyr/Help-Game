/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class UpTriggeredObject2D extends TriggeredObject2D{

    public static final String UPTEXTURE = "Collectible_Piece_spritemap.png";
    
    public UpTriggeredObject2D(){
        super();
        
        // Part graphic
        this.assignTextures();
        
    }
    
    public UpTriggeredObject2D(World world, float posX, float posY){      
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);   
        
        this.physicBody.setType(BodyDef.BodyType.StaticBody);
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(UPTEXTURE, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 36, 34);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);


            array = new Array<TextureRegion>(tmp[1]);
            array.removeRange(4, 7);
            this.listAnimations.add(new Animation(0.2f, array));
        }
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        float radius = 18;
        
        super.initialize(world, position, speed, radius * 1.1f);
        
        this.changeAnimation(0, false);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(radius * P2M);
        circle.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.8f; 
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && objThatTriggered instanceof Grandma){
            
            Grandma grandma = (Grandma) objThatTriggered;
            
            this.notifyGameEventListener(GameEventListener.EventType.SCORE, String.valueOf(10), new Vector2(grandma.getPositionBody()));
            this.notifyGameEventListener(GameEventListener.EventType.ACTION, "scoreIncrease", new Vector2(grandma.getPositionBody()));
            
            this.changeAnimation(1, false);

            super.trigger(objThatTriggered);
        }
    }
    
    
    
    @Override
    public boolean IsDynamicObject(){
        return true;
    }
}
