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
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author françois
 */
public abstract class TriggeredObject2D extends Object2D{
    
    // listeners
    protected WeakReference<Object2DStateListener> object2DStateListener;
    // end part listeners
    
    protected TriggerActionFixture triggerActionFixture;
    
    protected boolean isTriggered;
    
    public TriggeredObject2D(){
        super();    
        
        this.priority = 3;
        this.isTriggered = false;
    }
    
    public void onObj2DEnteredArea(Object2D obj){
        // nothing to do
    }
    
    public void onObj2DExitedArea(Object2D obj){
        // nothing to do
    }
    
    public void setObject2DStateListener(Object2DStateListener obj){
        this.object2DStateListener = new WeakReference(object2DStateListener);
    }
    
    public abstract void initialize(World world, Vector2 position, Vector2 speed);
    
    public void initialize(World world, Vector2 position, Vector2 speed, float radius){
        if(this.physicBody == null){
            // Part Physic
            BodyDef bodyDef = new BodyDef();
            this.SetBody(bodyDef);
            
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
            this.SetCollisionMask(fixtureDef);
            
            fixtureDef.shape = circle;

            body.setFixedRotation(true);
            Fixture fix = body.createFixture(fixtureDef);

            Set<Fixture> setTrigger = new HashSet<Fixture>();
            setTrigger.add(fix);
            this.triggerActionFixture = new TriggerActionFixture(setTrigger);

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
    
    public void initialize(World world, Vector2 position, Vector2 speed, Shape shape){
        if(this.physicBody == null){
            // Part Physic
            BodyDef bodyDef = new BodyDef();
            this.SetBody(bodyDef);
            
            if(this.IsDynamicObject()){
                bodyDef.type = BodyDef.BodyType.DynamicBody;         
            }else{
                bodyDef.type = BodyDef.BodyType.KinematicBody;
            }
            bodyDef.position.set(position.x * P2M, position.y * P2M); 

            Body body = world.createBody(bodyDef);

            // Collision fixture
            FixtureDef fixtureDef = new FixtureDef();
            
            this.SetCollisionMask(fixtureDef);
            fixtureDef.shape = shape;

            body.setFixedRotation(true);
            Fixture fix = body.createFixture(fixtureDef);

            Set<Fixture> setTrigger = new HashSet<Fixture>();
            setTrigger.add(fix);
            this.triggerActionFixture = new TriggerActionFixture(setTrigger);

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
    
    public boolean IsDynamicObject(){
        return false;
    }
    
    protected void SetCollisionMask(FixtureDef fixtureDef){
        // Nothing to do.
    }
    
    protected void SetBody(BodyDef bodyDef){
        // Nothing to do.
    }
    
    public void onOutOfScreen(double dist){
        // nothing to do
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        this.triggerActionFixture.applyAction(deltaTime, this);
    }
    
    public void trigger(Object2D objThatTriggered){
        this.isTriggered = true;
        
        if(this.object2DStateListener != null && this.object2DStateListener.get() != null){
            this.object2DStateListener.get().onStateChanged(this, Object2DStateListener.Object2DState.TOOK_BY_PLAYER, 1);
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
    
    @Override
    public void addObject2DStateListener(Object2DStateListener object2DStateListener){
        if(object2DStateListener != null){
            this.object2DStateListener = new WeakReference(object2DStateListener);
        }
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
    
    public void reflectBullet(Object2D reflecter){
        // nothing to do
    }
}
