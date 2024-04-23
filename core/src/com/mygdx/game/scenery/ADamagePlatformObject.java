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
import com.mygdx.game.DamageActionFixture;
import static com.mygdx.game.HelpGame.P2M;

/**
 *
 * @author Deneyr
 */
public abstract class ADamagePlatformObject extends APlatformObject{
    
    protected BPtlatformState platformState;
    
    protected float appearingPeriod;
    private float currentAppearingPeriod;
    
    protected float cooldownPeriod;
    private float currentCooldownPeriod;
    
    protected DamageActionFixture damageFixture;
    
    private Fixture boundingBoxFixture;
    
    public ADamagePlatformObject(World world, float posX, float posY, float angle, float scale, float directionAngle, float speed, float ratio, float maxRadius, float appearingPeriod, float cooldownPeriod) {
        super(world, posX, posY, angle, scale, directionAngle, speed, ratio, maxRadius);
        
        this.appearingPeriod = appearingPeriod;
        this.currentAppearingPeriod = 0;
        
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
        
        this.updateDPtlatformState(deltaTime);
    }
    
    private void updateDPtlatformState(float deltaTime){
        switch(this.platformState){
            case FREE:
                this.updateDPtlatformFreeState(deltaTime);
                break;
            case APPEARING_FIRST:
                this.updateDPtlatformAppearingFirstState(deltaTime);
                break;
            case APPEARING_SECOND:
                this.updateDPtlatformAppearingSecondState(deltaTime);
                break;
            case COOLDOWN:
                this.updateDPtlatformCooldownState(deltaTime);
                break;
        }
    }
    
    private void updateDPtlatformFreeState(float deltaTime){
        if(this.IsPlatformActivated(deltaTime)){            
            this.currentAppearingPeriod = 0;
            
            this.platformState = BPtlatformState.APPEARING_FIRST;
            
            this.onAppearingFirstState(deltaTime);
        }
    }
    
    protected abstract boolean IsPlatformActivated(float deltaTime);
    
    protected abstract void onAppearingFirstState(float deltaTime);
    
    private void updateDPtlatformAppearingFirstState(float deltaTime){
        this.currentAppearingPeriod += deltaTime;
        
        if(this.currentAppearingPeriod >= this.appearingPeriod / 2){

            this.platformState = BPtlatformState.APPEARING_SECOND;
            
            this.onAppearingSecondState(deltaTime);
        }      
    }
    
    protected abstract void onAppearingSecondState(float deltaTime);
    
    private void updateDPtlatformAppearingSecondState(float deltaTime){
        this.currentAppearingPeriod += deltaTime;
        
        this.applyDamage(deltaTime);
        
        if(this.currentAppearingPeriod >= this.appearingPeriod){
            this.currentCooldownPeriod = 0;
            
            this.platformState = BPtlatformState.COOLDOWN;
                                       
            this.onCooldownState(deltaTime);
        }
    }
    
    protected void applyDamage(float deltaTime){ 
        this.damageFixture.applyAction(deltaTime, this);
    }
    
    protected abstract void onCooldownState(float deltaTime);
    
    private void updateDPtlatformCooldownState(float deltaTime){
        this.currentCooldownPeriod += deltaTime;
        
        if(this.currentCooldownPeriod >= this.cooldownPeriod){
            this.platformState = BPtlatformState.FREE;
            
            this.onFreeState(deltaTime);
        }
    }
    
    protected abstract void onFreeState(float deltaTime);
    
    /*
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
    }*/
    
    protected enum BPtlatformState{
        FREE,
        APPEARING_FIRST,
        APPEARING_SECOND,
        COOLDOWN
    }
    
    @Override
    public void dispose(){
        this.physicBody.destroyFixture(this.boundingBoxFixture);
        
        this.damageFixture.dispose(this.physicBody);
        
        super.dispose();
    }
}
