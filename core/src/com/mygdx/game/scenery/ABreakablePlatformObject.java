/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;

/**
 *
 * @author Deneyr
 */
public abstract class ABreakablePlatformObject extends APlatformObject{
    
    protected BPtlatformState platformState;
    
    protected float disappearingPeriod;
    private float currentDisappearingPeriod;
    
    protected float cooldownPeriod;
    private float currentCooldownPeriod;
    
    private Fixture boundingBoxFixture;
    
    public ABreakablePlatformObject(World world, float posX, float posY, float angle, float scale, float directionAngle, float speed, float ratio, float maxRadius, float disappearingPeriod, float cooldownPeriod) {
        super(world, posX, posY, angle, scale, directionAngle, speed, ratio, maxRadius);
        
        this.disappearingPeriod = disappearingPeriod;
        this.currentDisappearingPeriod = 0;
        
        this.cooldownPeriod = cooldownPeriod;
        this.currentCooldownPeriod = 0;
        
        this.platformState = BPtlatformState.FREE;
        
        this.createBoundingBox(this.physicBody);
    }
    
    private void createBoundingBox(Body groundBody){
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        
        ground.setAsBox(100 * P2M * this.scale, 20 * P2M * this.scale);
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.isSensor = true;
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        
        this.boundingBoxFixture = fix;
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        this.updateBPtlatformState(deltaTime);
    }
    
    private void updateBPtlatformState(float deltaTime){
        switch(this.platformState){
            case FREE:
                this.updateBPtlatformFreeState(deltaTime);
                break;
            case DISAPPEARING:
                this.updateBPtlatformDisappearingState(deltaTime);
                break;
            case DISAPPEARED:
                this.updateBPtlatformDisappearedState(deltaTime);
                break;
            case REAPPARING:
                this.updateBPtlatformReapparingState(deltaTime);
                break;
        }
    }
    
    private void updateBPtlatformFreeState(float deltaTime){
        if(this.kinematicActionFixture.nbObject2DInside() > 0){            
            this.currentDisappearingPeriod = 0;
            
            this.platformState = BPtlatformState.DISAPPEARING;
            
            this.onDisappearingState(deltaTime);
        }
    }
    
    protected abstract void onDisappearingState(float deltaTime);
    
    private void updateBPtlatformDisappearingState(float deltaTime){
        this.currentDisappearingPeriod += deltaTime;
        
        if(this.currentDisappearingPeriod >= this.disappearingPeriod){
            this.currentCooldownPeriod = 0;
            
            this.platformState = BPtlatformState.DISAPPEARED;
            
            this.disableCollisions();
            
            this.onDisappearedState(deltaTime);
        }      
    }
    
    protected abstract void onDisappearedState(float deltaTime);
    
    private void updateBPtlatformDisappearedState(float deltaTime){
        this.currentCooldownPeriod += deltaTime;
        
        if(this.currentCooldownPeriod >= this.cooldownPeriod / 2){
            this.platformState = BPtlatformState.REAPPARING;
                                       
            this.onReapparingState(deltaTime);
        }
    }
    
    protected abstract void onReapparingState(float deltaTime);
    
    private void updateBPtlatformReapparingState(float deltaTime){
        this.currentCooldownPeriod += deltaTime;
        
        if(this.currentCooldownPeriod >= this.cooldownPeriod){
            this.platformState = BPtlatformState.FREE;
            
            this.enableCollisions();
            
            this.onFreeState(deltaTime);
        }
    }
    
    protected abstract void onFreeState(float deltaTime);

    protected void disableCollisions() {
        for(Fixture fixture: this.collisionFixture){
            //fixture.setSensor(true);
            this.physicBody.destroyFixture(fixture);
        }
        this.collisionFixture.clear();
        
        if(this.kinematicActionFixture != null){
            this.kinematicActionFixture.dispose(this.physicBody);
            this.kinematicActionFixture = null;
        }
    }

    private void enableCollisions() {
        this.createCollisions(this.physicBody);
    }
    
    protected enum BPtlatformState{
        FREE,
        DISAPPEARING,
        DISAPPEARED,
        REAPPARING
    }
    
    @Override
    public void dispose(){
        this.physicBody.destroyFixture(this.boundingBoxFixture);
        
        super.dispose();
    }
}
