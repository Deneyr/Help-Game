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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author françois
 */
public abstract class Object2D implements Disposable{
    
    protected int priority;
    
    protected Body physicBody;
    
    protected Texture texture;
    
    protected List<Animation> listAnimations = new ArrayList<Animation>();
    protected int currentAnimation;
    protected float animationTime;
    protected boolean pause;
    
    protected List<Fixture> collisionFixture;
    
    private float alpha;
    
    public Object2D(){
        this.currentAnimation = -1;
        this.animationTime = 0.f;
        this.collisionFixture = null;
        this.priority = 2;
        this.pause = true;
        this.physicBody = null;
        
        this.alpha = 1f;
    }
    
    public void updateLogic(float deltaTime){
        if(this.currentAnimation >=0 && !this.pause){
            this.animationTime += deltaTime;
        }
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
        
        if(sprite != null){
            sprite.setRotation((float)Math.toDegrees(this.physicBody.getAngle()));
            sprite.setPosition(this.physicBody.getPosition().x / P2M - sprite.getWidth() / 2.f, this.physicBody.getPosition().y / P2M - sprite.getHeight() / 2.f);
        }    
        
        if(sprite != null){
            if(Math.abs(this.getAlpha() - sprite.getColor().a) > 0.01 && this.getAlpha() <= 1f && this.getAlpha() >= 0f){
                sprite.setAlpha(this.getAlpha());
            }
        }
        
        return sprite;
    }
    
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        // To override
        return false;
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
        return new Vector2(this.physicBody.getPosition());
    }
    
    public BodyType getBodyType(){
        return this.physicBody.getType();
    }
    
    public void removeBody(World world){
        this.dispose();
        
        if(this.physicBody != null){
            world.destroyBody(this.physicBody);
            this.physicBody = null;
        }
    }
    
    public int getPriority(){
        return this.priority;
    }
    
    
    public boolean isCurrentAnimationOver(){
        if(this.currentAnimation < 0)
            return true;
        
        return this.listAnimations.get(this.currentAnimation).isAnimationFinished(this.animationTime);
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
        return alpha;
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
}
