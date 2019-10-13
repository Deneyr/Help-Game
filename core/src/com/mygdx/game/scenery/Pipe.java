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
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class Pipe extends SolidObject2D{
    private static final String PIPETEXT = "urbanObj/Obstacle_Tuyaux.png";
    private static final String STRINGTEXT = "urbanObj/Decors_Filins.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private boolean isThereString;
    private StringObject2D stringObject2D;
    
    public Pipe(World world, float posX, float posY, float scale, boolean isThereString){
        
        this.isThereString = isThereString;
        
        this.scale = scale;
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.KinematicBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 3;
        
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        
        ground.setAsBox(128 * P2M * this.scale * SCALE_X, 40 * P2M * this.scale * SCALE_Y, new Vector2(0, 0), 0);
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);

        this.collisionFixture.add(fix);

        this.physicBody = groundBody;
        //this.physicBody.setLinearVelocity(new Vector2(0.5f, 0));
        
        this.stringObject2D = null;
        if(this.isThereString){
            this.stringObject2D = new StringObject2D(this.physicBody, world, posX, posY + (200 * this.scale * SCALE_Y * StringObject2D.SCALE_Y + 30 * this.scale * SCALE_Y), 200 * this.scale * SCALE_Y * StringObject2D.SCALE_Y + 30 * this.scale * SCALE_Y, this.scale);
        }
        
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(PIPETEXT, this);  
        
        Texture stringTexture = null;
        if(this.isThereString){
            stringTexture = TextureManager.getInstance().getTexture(STRINGTEXT, this);
        }
        
        if(stringTexture != null){
             this.stringObject2D.assignTextures(stringTexture);
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
        
        if(this.stringObject2D != null){
            this.stringObject2D.removeBody(world);
        }
        super.removeBody(world);
    }
    
    public class StringObject2D extends Object2D{
        
        private Joint joint;
                
        private static final float SCALE_Y = 4f;
        
        public StringObject2D(Body ownerBody, World world, float posX, float posY, float offsetY, float scale){
            
            this.priority = 1;          
            
            this.scale = scale;
            
            // Part physic
            BodyDef groundBodyDef = new BodyDef();    
            groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.DynamicBody);
       
            
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(88 * P2M * this.scale * Pipe.SCALE_X, 199 * P2M * this.scale * StringObject2D.SCALE_Y * Pipe.SCALE_Y, new Vector2(0, 0), 0);
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
            jointDef.localAnchorA.set(new Vector2(0, offsetY * P2M));
            jointDef.localAnchorB.set(new Vector2(0, 0));
            jointDef.collideConnected = false;
        
            this.joint = world.createJoint(jointDef);
           
        }
        
        public void assignTextures(Texture texture){

            if(texture != null){
                this.texture = texture;
            }
        }
        
        @Override
        public Sprite createCurrentSprite(){
            Sprite sprite = super.createCurrentSprite();
            sprite.setScale(sprite.getScaleX(), sprite.getScaleY() * StringObject2D.SCALE_Y);
            return sprite;
        }
        
        @Override
        public void removeBody(World world){

            world.destroyJoint(this.joint);
            
            super.removeBody(world);
        }
    }
}
