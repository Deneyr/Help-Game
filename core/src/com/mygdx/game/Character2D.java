/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


/**
 *
 * @author françois
 */
public abstract class Character2D extends Object2D{
    protected Fixture feetFixture;
    
    protected int nbStaticObjUnderFeet;
    
    protected LifeState lifeState;
    private int lifePoints;
    final private int lifePointMax;
    
    protected SideCharacter side;
    
    protected boolean isAnOpponent;
    
    protected float timeInvulnerabilitySec = 0.5f;
    protected boolean isInvulnerable;
    protected float scaleDamageForce;
    
    protected boolean canBeBounced;
    
    public Character2D(int lifePoints){
        this.nbStaticObjUnderFeet = 0;
        this.lifeState = LifeState.ALIVE;
        
        this.lifePoints = lifePoints;
        this.lifePointMax = lifePoints;
        
        this.isAnOpponent = true;
        
        this.isInvulnerable = false;
        this.canBeBounced = true;
        
        this.timeInvulnerabilitySec = 0.5f;
        this.scaleDamageForce = 1.f;
    }
    
    public void addStaticObj(){
        this.nbStaticObjUnderFeet++;
    }
    
    public void removeStaticObj(){
        if(this.nbStaticObjUnderFeet > 0){
            this.nbStaticObjUnderFeet--;
        }
    }
    
    public boolean isFlying(){
        return this.nbStaticObjUnderFeet == 0;
    }
    
    public boolean isFixtureFeet(Fixture fixture){
        return this.feetFixture == fixture;
    }

    /**
     * @param lifePoints the lifePoints to set
     */
    public boolean setLifePoints(int lifePoints) {
        if(this.lifeState == LifeState.DEAD){
            return false;
        }
        
        int oldLifePoints = this.lifePoints;
        
        if(this.lifePoints > lifePoints){
            if(!this.isInvulnerable){
                if(lifePoints > 0){
                    this.lifePoints = lifePoints;
                }else{
                    this.lifePoints = 0;
                    this.lifeState = LifeState.DEAD;
                    this.onDeath();
                }
            }
        }else if(this.lifePoints < lifePoints){
            if(lifePoints <= this.lifePointMax){
                this.lifePoints = lifePoints;
            }else{
                this.lifePoints = this.lifePointMax;
            }
        }
        
        // Apply damage changes logic
        if(this.lifePoints < oldLifePoints){  
            this.isInvulnerable = true;
            
            Timer.schedule(new Task(){
                @Override
                public void run() {
                    Character2D.this.isInvulnerable = false;
                }
            }, this.timeInvulnerabilitySec);
            return true;
        }
        return false;
    }

    protected void onDeath(){
        // Nothing to do
    }
    
    /**
     * @return the lifePoints
     */
    public int getLifePoints() {
        return lifePoints;
    }
    
    protected enum LifeState{
        ALIVE,
        DEAD
    }
    
    public enum SideCharacter{
        LEFT,
        RIGHT
    }
    
    public boolean isOpponent(){
        return this.isAnOpponent;
    }
    
    public SideCharacter getSideCharacter(){
        return this.side;
    }
    
    public Sprite createLifeBar(){
        Sprite sprite = super.createCurrentSprite();
        
        Sprite lifeBar = new Sprite();
        lifeBar.setSize(100, 20);
        lifeBar.setColor(Color.GREEN);
        lifeBar.setPosition(sprite.getX(), sprite.getY());
        return lifeBar;
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        
        if(damageOwner instanceof Character2D){
            boolean isDamageEffective = ((Character2D) damageOwner).isAnOpponent ^ this.isAnOpponent;
            if(!isDamageEffective){
                return false;
            }
        }
                
        int oldLifePoints = this.lifePoints;
        
        this.setLifePoints(this.lifePoints - damage);
        
        if(oldLifePoints > this.lifePoints){
            Vector2 upVector = new Vector2(0, 1);
            float angle = dirDamage.angle(upVector) / 2f;
            dirDamage = dirDamage.rotate(angle);
            
            float ratioDamage = (oldLifePoints - this.lifePoints) / (float) this.lifePointMax;
            if(ratioDamage > 0 && !dirDamage.epsilonEquals(Vector2.Zero, 0.01f)){
                this.physicBody.applyLinearImpulse(dirDamage.scl(ratioDamage * 100.f * this.scaleDamageForce), Vector2.Zero, true);
            }
            return true;
        }
        
        return false;
    }
    
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        if(this.canBeBounced){
            this.canBeBounced = false;
            
            Vector2 upVector = new Vector2(0, 1);
            float angle = bounceVector.angle(upVector) / 2f;
            bounceVector = bounceVector.rotate(angle);

            this.physicBody.applyLinearImpulse(bounceVector, Vector2.Zero, true);
            
            Timer.schedule(new Task(){
                @Override
                public void run() {
                    Character2D.this.canBeBounced = true;
                }
            }, 0.2f);
        }
    }
    
    public int getLifePointsMax(){
        return this.lifePointMax;
    }
    
    public Vector2 getBodyVelocity(){
        return this.physicBody.getLinearVelocity();
    }
}
