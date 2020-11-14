/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import characters.OpponentCAC1;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class ChimneySmokeTriggeredObject2D extends TriggeredObject2D{

    public static final String CHIMNEYSMOKETEXTURE = "ChimneySmoke.png";
    
    private int damageInflicted;
    
    public ChimneySmokeTriggeredObject2D(){
        super();
        
        this.damageInflicted = 1;
        
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(CHIMNEYSMOKETEXTURE, this);
        
        if(this.texture != null){

        }
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        float radius = 13;

        super.initialize(world, position, speed, radius * 1.1f);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(radius * P2M);
        circle.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.8f; 
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        fix.setSensor(true);     
        
        this.physicBody.setTransform(this.physicBody.getPosition(), (float) (2 * Math.PI * Math.random()));
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && 
                (objThatTriggered instanceof Character2D)){
            
            Vector2 targetPhysicBody = new Vector2(objThatTriggered.getPositionBody());
            Vector2 dirDamage = targetPhysicBody.sub(this.getPositionBody());
            dirDamage = dirDamage.nor();
            
            if(objThatTriggered instanceof Grandma){
                Grandma grandma = (Grandma) objThatTriggered;
                
                grandma.applyDamage(this.damageInflicted, dirDamage, this);

            }else if(objThatTriggered instanceof OpponentCAC1){
                Character2D chara = (Character2D) objThatTriggered;
                
                chara.applyDamage(20, dirDamage, this);
            }
            super.trigger(objThatTriggered);
        }
    }
    
    @Override
    public void onOutOfScreen(double dist){
        if(this.object2DStateListener.get() != null){
            this.object2DStateListener.get().onStateChanged(this, Object2DStateListener.Object2DState.DEATH, 0);
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        if(sprite != null){
            sprite.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        }
        return sprite;
    }
    
    @Override
    public void reflectBullet(Object2D reflecter){
        if(!this.isTriggered){
            this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitProjectile", new Vector2(this.getPositionBody()));
            
            this.physicBody.setLinearVelocity(Vector2.Zero);

            super.trigger(reflecter);
        }
    }
}
