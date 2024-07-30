/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import java.util.ArrayList;

/**
 *
 * @author Deneyr
 */
public class TrackObject2D extends Object2D{
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private Joint joint;
    
    private boolean isDestroyed;
    
    private float parentSpeedX;

    public TrackObject2D(Body ownerBody, World world, float posX, float posY, float offsetX, float offsetY, int priority){
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = priority;
        this.isDestroyed = false;
        
        // Part physic
        BodyDef groundBodyDef = new BodyDef();  
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);
        groundBody.setType(BodyDef.BodyType.DynamicBody);

        
        // First wheel
        CircleShape circle = new CircleShape();
        circle.setRadius(45 * P2M);
        circle.setPosition(new Vector2(-130 * P2M, -75 * P2M));
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f; 
        Fixture fix = groundBody.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        
        // Second wheel
        circle = new CircleShape();
        circle.setRadius(45 * P2M);
        circle.setPosition(new Vector2(130 * P2M, -75 * P2M));
        fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f; 
        fix = groundBody.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        
        // Third wheel
        circle = new CircleShape();
        circle.setRadius(45 * P2M);
        circle.setPosition(new Vector2(0 * P2M, -75 * P2M));
        fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f; 
        fix = groundBody.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        
        
        this.physicBody = groundBody;
        // Create joint

        WeldJointDef jointDef = new WeldJointDef ();
        jointDef.bodyA = ownerBody;
        jointDef.bodyB = this.physicBody;
        jointDef.localAnchorA.set(new Vector2(offsetX * P2M, offsetY * P2M));
        jointDef.localAnchorB.set(new Vector2(0, 0));
        jointDef.collideConnected = false;

        this.joint = world.createJoint(jointDef);           
    }
    
    /**
     * @param isDestroyed the isDestroyed to set
     */
    public void setIsDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }
    
    @Override
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        
        if(reset){
            super.setCollisionFilterMask(fixtureDef, true);
            return;
        }
        
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = 0x0002;
    }

    public void assignTextures(Texture texture){
        if(texture != null){
             this.texture = texture;
             
             // Part graphic
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 400, 250);
            // walk
            Array<TextureRegion> array = new Array<TextureRegion>();
            array.add(tmp[tmp.length - 1][1]);
            array.add(tmp[tmp.length - 1][2]);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(0).setPlayMode(Animation.PlayMode.LOOP);
        
            this.changeAnimation(0, false);
        }
    }

    @Override
    public void updateLogic(float deltaTime){  
        super.updateLogic(deltaTime);
        
        Animation animation = this.listAnimations.get(0);
        if(this.parentSpeedX == 0){
            //animation.setFrameDuration(0.2f);
            this.pause = true;
        }else{
            //animation.setFrameDuration(0.2f / this.parentSpeedX);
            this.pause = false;
        }
    }

    
    @Override
    public Sprite createCurrentSprite(){
        if(this.isDestroyed){
            return null;
        }
        
        Sprite sprite = super.createCurrentSprite();
        return sprite;
    }

    @Override
    public void removeBody(World world){   
        world.destroyJoint(this.joint);

        super.removeBody(world);
    }

    /**
     * @param parentSpeedX the parentSpeedX to set
     */
    public void setParentSpeedX(float parentSpeedX) {
        this.parentSpeedX = parentSpeedX;
    }
}