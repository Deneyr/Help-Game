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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import static com.mygdx.game.HelpGame.P2M;
import cosmetics.HitCosmeticObject2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author françois
 */
public abstract class Character2D extends Object2D{
    
    // listeners
    protected List<WeakReference<Object2DStateListener>> listObject2DStateListener;
    // end part listeners
    
    protected Fixture feetFixture;
    
    protected int nbStaticObjUnderFeet;
    
    protected LifeState lifeState;
    protected int lifePoints;
    final private int lifePointMax;
    
    protected SideCharacter side;
    
    protected boolean isAnOpponent;
    
    protected float timeInvulnerabilitySec = 0.5f;
    protected boolean isInvulnerable;
    protected float scaleDamageForce;
    protected float scaleBounceForce;
    
    protected boolean canBeBounced;
    
    protected boolean hasLifeBar;
    
    // Part cinematic
    protected boolean isCinematicEntity;

    protected float slowingDown;
    
    protected String name;
    
    public Character2D(int lifePoints){
        super();
        
        this.priority = 2;
        
        this.nbStaticObjUnderFeet = 0;
        this.lifeState = LifeState.ALIVE;
        
        this.lifePoints = lifePoints;
        this.lifePointMax = lifePoints;
        
        this.isAnOpponent = true;
        
        this.isInvulnerable = false;
        this.canBeBounced = true;
        
        this.timeInvulnerabilitySec = 0.5f;
        
        this.scaleDamageForce = 1.f;
        this.scaleBounceForce = 1.f;
        
        this.hasLifeBar = true;
        
        this.listObject2DStateListener = new ArrayList<WeakReference<Object2DStateListener>>();
        
        this.isCinematicEntity = false;
        
        this.slowingDown = 1;
        
        this.name = "Character2D_" + UUID.randomUUID().toString();
    }
    
    public String getName(){
        return this.name;
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
    
    @Override
    public void updateLogic(float deltaTime){
        if(Math.abs(1 - this.slowingDown) < 0.01){
            super.updateLogic(deltaTime);
        }else{
            super.updateLogic(deltaTime * this.slowingDown);
        }
    }

    public void setPosition(Vector2 newPosition){
        if(newPosition != null && this.physicBody != null){
            this.physicBody.setTransform(new Vector2(newPosition), this.physicBody.getAngle());
        }
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

    /**
     * @return the hasLifeBar
     */
    public boolean HasLifeBar() {
        return hasLifeBar;
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
                
                this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitPunch", this.getPositionBody());
                
                this.physicBody.applyLinearImpulse(dirDamage.scl(ratioDamage * 100.f * this.scaleDamageForce), Vector2.Zero, true);
                
                
                Vector2 receiverPosition = this.getPositionBody();
                Vector2 distance = receiverPosition.sub(damageOwner.getPositionBody());
                
                upVector = new Vector2(0, 1);
                angle = dirDamage.angle(upVector) * 0.7f;
                dirDamage = dirDamage.rotate(angle);
                Vector2 speed = (dirDamage).nor().scl(3 + ratioDamage * 2f);
                
                distance.y = 0;
                this.notifyCosmeticObj2D2CreateListener(damageOwner, this, HitCosmeticObject2D.class, damageOwner.getPositionBody().add(distance.scl(0.9f)).scl(1 / P2M), Vector2.Zero, ratioDamage);
                
            }
            
            if(oldLifePoints >= this.lifePointMax / 4
                    && this.lifePoints < this.lifePointMax / 4){
                this.notifyGameEventListener(GameEventListener.EventType.LOWLIFE, this.getName(), new Vector2(this.getPositionBody()));
            }
            
            return true;
        }
        
        return false;
    }
    
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner, Vector2 ptApplication){
        if(damageOwner instanceof Character2D && damageOwner != this){
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
                
                this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitPunch", new Vector2(this.getPositionBody()));
                
                this.physicBody.applyLinearImpulse(dirDamage.scl(ratioDamage * 100.f * this.scaleDamageForce), ptApplication, true);
            }
            return true;
        }
        
        return false;
    }
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        if(this.canBeBounced){
            this.canBeBounced = false;
            
            Vector2 upVector = new Vector2(0, 1);
            float angle = bounceVector.angle(upVector) / 2f;
            bounceVector = bounceVector.rotate(angle);
            
            bounceVector.scl(this.scaleBounceForce);
            
            if(!bounceVector.epsilonEquals(Vector2.Zero, 0.01f)){
                this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitBounce", new Vector2(this.getPositionBody()));
                
                this.physicBody.applyLinearImpulse(bounceVector, Vector2.Zero, true);
            }
            
            Timer.schedule(new Task(){
                @Override
                public void run() {
                    Character2D.this.canBeBounced = true;
                }
            }, 0.2f);
        }
    }
    
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner, Vector2 ptApplication){
        if(this.canBeBounced){
            this.canBeBounced = false;
            
            Vector2 upVector = new Vector2(0, 1);
            float angle = bounceVector.angle(upVector) / 2f;
            bounceVector = bounceVector.rotate(angle);
            
            bounceVector.scl(this.scaleBounceForce);
            
            if(!bounceVector.epsilonEquals(Vector2.Zero, 0.01f)){
                this.physicBody.applyLinearImpulse(bounceVector, ptApplication, true);
            }
            
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
    
    // dispose function
    @Override
    public void dispose(){
        
        if(this.feetFixture != null){
            this.physicBody.destroyFixture(this.feetFixture);
            
            this.feetFixture = null;
        }
        
        super.dispose();
    }
    
    @Override
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        
        if(reset){
            super.setCollisionFilterMask(fixtureDef, true);
            return;
        }
        
        fixtureDef.filter.categoryBits = 0x0001;
    }
    
    // cinematic
    
    public void isCinematicEntity(boolean isCinematic){
        this.isCinematicEntity = isCinematic;
    }
    
    public void resetSpeed(){
        if(this.physicBody != null){
            this.physicBody.setLinearVelocity(0, 0);
        }
    }
    
    public abstract void setInfluenceList(List<String> influences);
    
    // listeners
    
    @Override
    public void addObject2DStateListener(Object2DStateListener object2DStateListener){
        if(object2DStateListener != null){
            this.listObject2DStateListener.add(new WeakReference(object2DStateListener));
        }
    }
    
    protected void notifyObject2DStateListener(Object2DStateListener.Object2DState state, int animCounter){
        for(WeakReference<Object2DStateListener> refObject2DStateListener : this.listObject2DStateListener){
            if(refObject2DStateListener.get() != null){
                refObject2DStateListener.get().onStateChanged(this, state, animCounter);
            }
        }
    }
    
    protected void notifyObject2DStateListener(Object2DStateListener.Object2DState state, int animCounter, boolean canReplace){
        for(WeakReference<Object2DStateListener> refObject2DStateListener : this.listObject2DStateListener){
            if(refObject2DStateListener.get() != null){
                refObject2DStateListener.get().onStateChanged(this, state, animCounter, canReplace);
            }
        }
    }
    
    protected void notifyObject2D2CreateListener(Class obj2DClass, Vector2 position, Vector2 speed){
        for(WeakReference<Object2DStateListener> refObject2DStateListener : this.listObject2DStateListener){
            if(refObject2DStateListener.get() != null){
                refObject2DStateListener.get().onObject2D2Create(this, obj2DClass, position, speed);
            }
        }
    } 
    
    protected void notifyCosmeticObj2D2CreateListener(Object2D giver, Object2D receiver, Class obj2DClass, Vector2 position, Vector2 speed, float strenght){
        for(WeakReference<Object2DStateListener> refObject2DStateListener : this.listObject2DStateListener){
            if(refObject2DStateListener.get() != null){
                refObject2DStateListener.get().onCosmeticObj2D2Create(giver, receiver, obj2DClass, position, speed, strenght);
            }
        }
    } 
}
