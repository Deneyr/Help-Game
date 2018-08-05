/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmetics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 *
 * @author Deneyr
 */
public abstract class CosmeticObject2D extends Object2D{
    
    protected WeakReference<Object2DStateListener> object2DStateListener;
    
    protected CosmeticState cosmeticState;
    
    protected float radius;
    
    public CosmeticObject2D(){
        this.priority = 5;
        
        this.radius = 10f;
    }
    
    public void initialize(World world, Object2D giver, Object2D receiver, Vector2 position, Vector2 speed, float strength ) {
        
        float radius = this.radius;
        
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
            CircleShape circle = new CircleShape();
            circle.setRadius(radius * P2M);
            circle.setPosition(new Vector2(0, 0));

            FixtureDef fixtureDef = new FixtureDef();

            fixtureDef.shape = circle;
            fixtureDef.density = 2f; 
            fixtureDef.friction = 0.1f;
            fixtureDef.restitution = 0.8f; 

            Fixture fix = body.createFixture(fixtureDef);

            this.collisionFixture = new ArrayList<Fixture>();
            this.collisionFixture.add(fix);
            fix.setUserData(this);
            fix.setSensor(true);

            this.physicBody = body;
                     
            
        }else{
            position.x *= P2M; 
            position.y *= P2M;
            this.physicBody.setTransform(position, 0);
        }
        
        // Set physic
        if(this.IsDynamicObject()){
            this.physicBody.applyLinearImpulse(speed, Vector2.Zero, true);   
        }else{
            this.physicBody.setLinearVelocity(speed);
        }  
        
        // Reset alpha & scaling
        this.setAlpha(1f);
        this.setScale(1f);
                     
        this.cosmeticState = CosmeticState.ALIVE;
    }
    
    public boolean IsDynamicObject(){
        return false;
    }
    
    /**
     * @return the cosmeticState
     */
    public CosmeticState getCosmeticState() {
        return cosmeticState;
    }

    /**
     * @param cosmeticState the cosmeticState to set
     */
    public void setCosmeticState(CosmeticState cosmeticState) {
        this.cosmeticState = cosmeticState;
    }
    
    @Override
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        
        if(reset){
            super.setCollisionFilterMask(fixtureDef, true);
            return;
        }
        
        fixtureDef.filter.categoryBits = 0x0004;
        fixtureDef.filter.maskBits = 0x0002;
    }
    
    protected void notifyObject2DStateListener(Object2DStateListener.Object2DState lObject2DState, int lCounter){
        if(this.object2DStateListener != null && this.object2DStateListener.get() != null){
            this.object2DStateListener.get().onStateChanged(this, lObject2DState, lCounter);
        }
    }
    
    @Override
    public void addObject2DStateListener(Object2DStateListener object2DStateListener){
        if(object2DStateListener != null){
            this.object2DStateListener = new WeakReference(object2DStateListener);
        }
    }
    
    public enum CosmeticState{
        ALIVE,
        ONDEATH,
        DEAD
    }
    
}
