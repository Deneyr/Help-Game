/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;

/**
 *
 * @author Deneyr
 */
public class FanObject2D extends Object2D{
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private Joint joint;

    private float scaleX;
    private boolean isIncrement;

    public FanObject2D(Body ownerBody, World world, float posX, float posY, float offsetY){

        this.priority = 1;          

        // Part physic
        BodyDef groundBodyDef = new BodyDef();    
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);
        groundBody.setType(BodyDef.BodyType.DynamicBody);


        PolygonShape ground = new PolygonShape();
        ground.setAsBox(25 * P2M * SCALE_X, 10 * P2M * SCALE_Y, new Vector2(0, 0), 0);
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef); 
        fix.setSensor(true);
        fix.setUserData(this);

        this.physicBody = groundBody;

        // Create joint

        WeldJointDef jointDef = new WeldJointDef ();
        jointDef.bodyA = ownerBody;
        jointDef.bodyB = this.physicBody;
        jointDef.localAnchorA.set(new Vector2(-1 * P2M, offsetY * P2M));
        jointDef.localAnchorB.set(new Vector2(0, 0));
        jointDef.collideConnected = false;

        this.scaleX = 0;
        this.isIncrement = true;

        this.joint = world.createJoint(jointDef);           
    }

    public void assignTextures(Texture texture){
        if(texture != null){
             this.texture = texture;
        }
    }

    @Override
    public void updateLogic(float deltaTime){  
        super.updateLogic(deltaTime);

        float sign = 1;
        if(this.isIncrement == false){
            sign = -1;
        }

        this.scaleX = this.scaleX + sign * deltaTime * 10f;

        if(this.isIncrement){
            if(this.scaleX > 1){
                this.isIncrement = false;
                this.scaleX = 1;
            }
        }else{
            if(this.scaleX < 0){
                this.isIncrement = true;
                this.scaleX = 0;
            }
        }
    }

    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * this.scaleX * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }

    @Override
    public void removeBody(World world){

        world.destroyJoint(this.joint);

        super.removeBody(world);
    }
}