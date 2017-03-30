/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import java.util.Comparator;
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
    
    public Object2D(){
        this.currentAnimation = -1;
        this.animationTime = 0.f;
        this.collisionFixture = null;
        this.priority = 2;
        this.pause = true;
    }
    
    public void updateLogic(float deltaTime){
        if(this.currentAnimation >=0 && !this.pause)
            this.animationTime += deltaTime;
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
        world.destroyBody(this.physicBody);
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
        // to override
    }
}
