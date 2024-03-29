/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class TeethTriggeredObject2D extends TriggeredObject2D{

    public static final String TEETHTEXTURE = "Dent_Anim-01.png";
      
    public TeethTriggeredObject2D(){
        super();
        
        // Part graphic
        this.assignTextures();
    }
    
    public TeethTriggeredObject2D(World world, float posX, float posY){      
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero); 
        
        this.physicBody.setType(BodyDef.BodyType.StaticBody);
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        
        float radius = 15;
        
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
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(TEETHTEXTURE, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 36, 36);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.LOOP));

            array = new Array<TextureRegion>(tmp[1]);
            this.listAnimations.add(new Animation(0.2f, array));
        }
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && objThatTriggered instanceof Grandma){
            
            Grandma grandma = (Grandma) objThatTriggered;
            
            if(grandma.getLifePoints() < grandma.getLifePointsMax()){
                
                grandma.setLifePoints(grandma.getLifePoints() + 1);

                this.changeAnimation(1, false);
                
                super.trigger(objThatTriggered);
            }
        }
    }
    
    
    
    @Override
    public boolean IsDynamicObject(){
        return true;
    }
    
    
}
