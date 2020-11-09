/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class TrampolineTriggeredObject2D extends TriggeredObject2D{

    private static final String TRAMPOLINETEXT = "urbanObj/Help_Props_300x32_Trampoline.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private float angle;
    
    public TrampolineTriggeredObject2D(World world, float posX, float posY, float angle){
        
        this.angle = angle;
        
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(TRAMPOLINETEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 100, 30);

            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.add(tmp[0][0]);
            this.listAnimations.add(new Animation(0.15f, array));
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        PolygonShape damageShape = new PolygonShape();

        damageShape.setAsBox(30 * P2M * SCALE_X, 8 * P2M * SCALE_Y);
        
        super.initialize(world, position, speed, damageShape);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = damageShape;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 
              
        Fixture fix = this.physicBody.createFixture(fixtureDef);       
        
        fix.setUserData(this);
        
        this.physicBody.setTransform(this.getPositionBody(), this.angle);
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && 
                (objThatTriggered instanceof Character2D)){
            
            Vector2 dirVector = (new Vector2(0, 50)).rotate((float) Math.toDegrees(this.angle * 2)); 

            objThatTriggered.applyBounce(dirVector, this);
            
            this.changeAnimation(0, false);
        }
    }
}
