/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameEventListener.EventType;
import static com.mygdx.game.HelpGame.P2M;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import ressourcesmanagers.GraphicalComponent;

/**
 *
 * @author françois
 */
public abstract class Object2D implements Disposable, GraphicalComponent{
    // end collision mask
    
    protected int priority;
    
    protected Body physicBody;
    
    protected Texture texture;
    
    protected List<Animation> listAnimations = new ArrayList<Animation>();
    protected int currentAnimation;
    protected float animationTime;
    protected boolean pause;
    
    protected List<Fixture> collisionFixture;
    
    private float alpha;
    protected float scale;
    
    // listener
    protected WeakReference<GameEventListener> gameEventListener;
    
    public Object2D(){
        this.currentAnimation = -1;
        this.animationTime = 0.f;
        this.collisionFixture = null;
        this.priority = 2;
        this.pause = true;
        this.physicBody = null;
        
        this.alpha = 1f;
        
        this.scale = 1f;
    }
    
    public void updateLogic(float deltaTime){
        if(this.currentAnimation >=0 && !this.pause){
            this.animationTime += deltaTime;
        }
    }
    
    public void ReinitPlatform(World world){
        
    }
    
    public Sprite createCurrentSprite(){
        Sprite sprite;
        
        if(this.currentAnimation < 0){
            if(this.texture != null){
                sprite = new Sprite(this.texture);
            }else{
                sprite = null;
            }
        }else{
            TextureRegion region = this.listAnimations.get(this.currentAnimation).getKeyFrame(this.animationTime);
            sprite = new Sprite(region);  
        }
        
        if(sprite != null && this.physicBody != null){
            sprite.setRotation((float)Math.toDegrees(this.physicBody.getAngle()));
            sprite.setPosition(this.physicBody.getPosition().x / P2M - sprite.getWidth() / 2.f, this.physicBody.getPosition().y / P2M - sprite.getHeight() / 2.f);
        }    
        
        if(sprite != null){
            if(Math.abs(this.getAlpha() - sprite.getColor().a) > 0.01 && this.getAlpha() <= 1f && this.getAlpha() >= 0f){
                sprite.setAlpha(this.getAlpha());
            }
            
            if(Math.abs(this.scale - 1f) > 0.01){
                sprite.setScale(this.scale);
            }
        }
        
        return sprite;
    }
    
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        // To override
        return false;
    }
    
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        // To override
    }
    
    // getter & setter
    public void changeAnimation(int index, boolean isPause){
        this.currentAnimation = index;
        this.animationTime = 0.f;
        this.pause = isPause;
    }
    
    public void changeAnimation(int index, boolean isPause, float initTime){
        this.currentAnimation = index;
        this.animationTime = initTime;
        this.pause = isPause;
    }
    
    
    public Vector2 getPositionBody(){
        if(this.physicBody != null){
            return new Vector2(this.physicBody.getPosition());
        }
        return new Vector2();
    }
    
    public Vector2 getBodyVelocity(){
        if(this.physicBody != null){
            return new Vector2(this.physicBody.getLinearVelocity());
        }
        return new Vector2();
    }
    
     public float getAngleBody(){
        if(this.physicBody != null){
            return this.physicBody.getAngle();
        }
        return 0;
    }
    
    public BodyType getBodyType(){
        return this.physicBody.getType();
    }
    
    public void removeBody(World world){     
        if(this.physicBody != null){
            this.dispose();
            
            world.destroyBody(this.physicBody);
            this.physicBody = null;
        }
    }
    
    public boolean IsPhysicValid(){
        return this.physicBody != null;
    }
    
    public int getPriority(){
        return this.priority;
    }
    
    public void setPriority(int priority){
        this.priority = priority;
    }
    
    public boolean isCurrentAnimationOver(){
        if(this.currentAnimation < 0)
            return true;
        
        return this.listAnimations.get(this.currentAnimation).isAnimationFinished(this.animationTime);
    }
   
    @Override
    public void assignTextures(){
        // nothing to do;
    }
    
    // dispose function
    @Override
    public void dispose(){
        if(this.collisionFixture != null){
            for(Fixture fixture : this.collisionFixture){
                this.physicBody.destroyFixture(fixture);
            }
            
            this.collisionFixture.clear();
        }
    }

    /**
     * @return the alpha
     */
    public float getAlpha() {
        return this.alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        
        if(this.alpha < 0){
            this.alpha = 0f;
        }else if(this.alpha > 1){
            this.alpha = 1f;
        }

    }
    
    
    
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        if(reset){
            fixtureDef.filter.categoryBits = 0x0001;
            fixtureDef.filter.groupIndex = 0;
            fixtureDef.filter.maskBits = -1;
        }
    }
    
    /**
     * @return the scale
     */
    public float getScale() {
        return this.scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(float scale) {    
        
        this.scale = scale;
    }
    
    public void setTransform(float x, float y, float angle){
        if(this.physicBody != null){
            if(this.physicBody.isAwake() == false){
                this.physicBody.setAwake(true);
            }
            this.physicBody.setTransform(x, y, angle);
        }
    }
    
    public void setVelocity(float x, float y){
        if(this.physicBody != null){
            this.physicBody.setLinearVelocity(x, y);
        }
    }
    
    // listener
    public void addGameEventListener(GameEventListener listener){
        if(listener != null){
            this.gameEventListener = new WeakReference(listener);
        }
    }
    
    protected void notifyGameEventListener(EventType type, String details, Vector2 location){
        if(this.gameEventListener != null && this.gameEventListener.get() != null){
            this.gameEventListener.get().onGameEvent(type, details, location);
        }
    }
    
    public void addObject2DStateListener(Object2DStateListener object2DStateListener){
        // nothing to do
    }
       
}
