/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author françois
 */
public abstract class TriggeredObject2D extends Object2D{
    
    // listeners
    protected WeakReference<Object2DStateListener> object2DStateListener;
    // end part listeners
    
    private TriggerActionFixture triggerActionFixture;
    
    public TriggeredObject2D(){
        super();    
    }
    
    public void setObject2DStateListener(Object2DStateListener obj){
        this.object2DStateListener = new WeakReference(object2DStateListener);
    }
    
    public abstract void initialize(World world, Vector2 position, Vector2 speed);
    
    public void initialize(World world, Vector2 position, Vector2 speed, float radius){
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

            body.setFixedRotation(true);
            Fixture fix = body.createFixture(fixtureDef);

            Set<Fixture> setTrigger = new HashSet<Fixture>();
            setTrigger.add(fix);
            this.triggerActionFixture = new TriggerActionFixture(setTrigger);

            this.physicBody = body;
        }
    }
    
    public boolean IsDynamicObject(){
        return false;
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        this.triggerActionFixture.applyAction(deltaTime, this);
    }
    
    public void trigger(Object2D objThatTriggered){
        if(this.object2DStateListener.get() != null){
            this.object2DStateListener.get().notifyStateChanged(this, Object2DStateListener.Object2DState.DEATH, 0);
        }
    }
    
    // dispose function
    @Override
    public void dispose(){
        if(this.triggerActionFixture != null){
            this.triggerActionFixture.dispose(this.physicBody);
        }
        
        super.dispose();
    }
    
    
    public static TriggeredObject2D createNewTriggeredObject2D(){
        return null;
    }
}
