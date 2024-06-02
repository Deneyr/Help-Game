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
public class WheelObject2D extends Object2D{
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private Joint joint;
    
    private float wheelRotationAngle;
    
    private float parentSpeedX;

    public WheelObject2D(Body ownerBody, World world, float posX, float posY, float offsetX, float offsetY, int priority){
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = priority;
        this.wheelRotationAngle = 0;

        // Part physic
        BodyDef groundBodyDef = new BodyDef();  
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);
        groundBody.setType(BodyDef.BodyType.DynamicBody);


        CircleShape circle = new CircleShape();
        circle.setRadius(38 * P2M);
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 5f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f; 
        Fixture fix = groundBody.createFixture(fixtureDef);
        
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

    public void assignTextures(Texture texture){
        if(texture != null){
             this.texture = texture;
             
             // Part graphic
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 80, 80);
            // walk
            for (TextureRegion item : tmp[0]) {
                Array<TextureRegion> array = new Array<TextureRegion>();
                array.add(item);
                this.listAnimations.add(new Animation(0.2f, array));
                this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
            }
        
            this.changeAnimation(0, true);
        }
    }

    @Override
    public void updateLogic(float deltaTime){  
        super.updateLogic(deltaTime);
        
        float wheelRotationVelocity = -this.parentSpeedX / (38 * P2M);     
        this.wheelRotationAngle = this.wheelRotationAngle + wheelRotationVelocity * deltaTime;
    }

    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setRotation((float)Math.toDegrees(this.physicBody.getAngle() + this.wheelRotationAngle));
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