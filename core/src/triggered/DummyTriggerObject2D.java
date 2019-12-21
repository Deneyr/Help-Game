/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public abstract class DummyTriggerObject2D extends TriggeredObject2D{

    private GameEventListener.EventType eventType;
    private String details;
    
    private boolean repeatEvent;
    
    private int nbMaxHit;
    private int currentNbHit;
    
    public DummyTriggerObject2D(World world, float posX, float posY, GameEventListener.EventType eventType, String details, Shape shape, boolean repeatEvent){
        super();
        
        this.eventType = eventType;
        this.details = details;
        
        this.nbMaxHit = 2;
        this.currentNbHit = 0;
        
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero, shape);
    }  
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed, Shape shape){
        if(this.physicBody == null){
            // Part Physic
            BodyDef bodyDef = new BodyDef();
            
            if(this.IsDynamicObject()){
                bodyDef.type = BodyDef.BodyType.DynamicBody;         
            }else{
                bodyDef.type = BodyDef.BodyType.KinematicBody;
            }
            bodyDef.position.set(position.x * P2M, position.y * P2M); 

            Body body = world.createBody(bodyDef);

            // Collision fixture
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;

            body.setFixedRotation(true);
            Fixture fix = body.createFixture(fixtureDef);

            fix.setSensor(true);
            fix.setUserData(this);
            
            this.triggerActionFixture = null;

            this.physicBody = body;
            
            // Set physic
            if(this.IsDynamicObject()){
                this.physicBody.applyLinearImpulse(speed, Vector2.Zero, true);   
            }else{
                this.physicBody.setLinearVelocity(speed);
            }
                       
            // Reset alpha & scaling
            this.setAlpha(1f);
            this.setScale(1f);
        }
        
        this.isTriggered = false;
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        if(!this.isTriggered){
            
            if(this.currentNbHit < this.nbMaxHit){
                this.currentNbHit++;
            }else{
                this.notifyGameEventListener(this.eventType, this.details, Vector2.Zero);

                if(!this.repeatEvent){
                    this.isTriggered = true;
                }
            }
            
            return true;
        }
        return false;
    }
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        if(!this.isTriggered){
            this.notifyGameEventListener(this.eventType, this.details, Vector2.Zero);

            if(!this.repeatEvent){
                this.isTriggered = true;
            }
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        // Nothing to do.
    }
}
