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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.mygdx.game.Character2D;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;

/**
 *
 * @author françois
 */
public class CannonCorpus extends SolidObject2D{
    
    private static final Texture CANNONCORPUSTEXT = new Texture("CannonCorpus.png");
    private static final Texture CANNONTEXT = new Texture("Cannon.png");
    
    protected static final float SCALE_X = 1f;
    protected static final float SCALE_Y = 1f;
    
    private Cannon cannon;
    
    public CannonCorpus(World world, float posX, float posY, float angle){
        this.texture = CANNONCORPUSTEXT;
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.KinematicBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create a polygon shape
        CircleShape circle = new CircleShape();
        circle.setRadius(60 * P2M);
        circle.setPosition(new Vector2(0, -30 * P2M));
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.physicBody = groundBody;
        
        // Child
        this.cannon = new Cannon(this.physicBody, world, posX , posY);
        
        // Transform
        if(angle != 0){
            this.physicBody.setTransform(this.physicBody.getPosition(), angle);
        }
    }   
            
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
    @Override
    public void removeBody(World world){
        
        this.cannon.removeBody(world);
        
        super.removeBody(world);
    }
    
    public class Cannon extends Character2D{

        private Joint joint;
        
        
        public Cannon(Body ownerBody, World world, float posX, float posY) {
            super(100);
            
            this.texture = CANNONTEXT;
            
            this.isInvulnerable = true;
            this.hasLifeBar = false;
            
            // Part physic
            BodyDef groundBodyDef = new BodyDef();    
            groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.DynamicBody);
       
            this.priority = 0;
            
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(55 * P2M * SCALE_X, 25 * P2M * SCALE_Y, new Vector2(0, 0), 0);
            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef = new FixtureDef();
            
            this.setCollisionFilterMask(fixtureDef, false);
            
            fixtureDef.shape = ground;
            fixtureDef.density = 1f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            
            this.physicBody = groundBody;
            
            // joint creation
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = ownerBody;
            jointDef.bodyB = this.physicBody;
            jointDef.localAnchorA.set(new Vector2(0, -5 * P2M));
            jointDef.localAnchorB.set(new Vector2(50 * P2M, 0));
            jointDef.collideConnected = false;
            
            this.joint = world.createJoint(jointDef);
            
        }
    
        @Override
        public void removeBody(World world){

            world.destroyJoint(this.joint);
            
            super.removeBody(world);
        }
    
    }
    
}
