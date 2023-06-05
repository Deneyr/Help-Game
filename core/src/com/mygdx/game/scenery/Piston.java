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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixture;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class Piston extends ObstacleObject2D{
    private static final String[] OBJECT_ARRAY = {
        "factory/Help_Piston_Piston_Rod_40x160.png",
        "factory/Help_Piston_Piston_200x250.png"};
    
    private PistonHead pistonHead;
    
    private float scaleY;  
    private float height;
    
    public Piston(World world, float posX, float posY, float angle, float scaleY, float speed, float ratio){
        
        this.scale = 1;       
        this.texturePath = OBJECT_ARRAY[0];      
        this.side = 1;    
        this.indexObject = 0;
        this.scaleY = scaleY;
        this.height = 76 * P2M * this.scale * this.scaleY;
        
        this.pistonHead = new PistonHead(world, posX, posY, angle, new Vector2(0, 1), this.height, speed, ratio);
        
        // Part graphic
        this.assignTextures();
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
        groundBodyDef.angle = angle;
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create collisions (to override).
        this.createCollisions(groundBody);
        
        for(Fixture fixture: groundBody.getFixtureList()){
            fixture.setUserData(this);
            this.collisionFixture.add(fixture);
        }

        this.physicBody = groundBody;
        //this.physicBody.setLinearVelocity(new Vector2(0.5f, 0));           
    }

    @Override
    protected void createCollisions(Body groundBody) {
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body 
        
        switch(this.indexObject){
            case 0:
                ground.setAsBox(20 * P2M * this.scale, this.height, new Vector2(0, 0), 0);
                
                Fixture fix = groundBody.createFixture(fixtureDef); 
                
                break;
        }
    }
    
    @Override
    public void assignTextures(){
        super.assignTextures();
        
        Texture pistonHeadText = TextureManager.getInstance().getTexture(OBJECT_ARRAY[1], this);
        
        if(this.texture != null
            && pistonHeadText != null){
            
            this.pistonHead.assignTextures(pistonHeadText);
        }
    }
      
    @Override
    public void updateLogic(float deltaTime){  
        super.updateLogic(deltaTime);
        
        this.pistonHead.updateLogic(deltaTime);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();

        sprite.setScale(sprite.getScaleX(), sprite.getScaleY() * this.scaleY);

        return sprite;
    }
    
    @Override
    public void dispose(){       
        this.pistonHead.dispose();
        
        super.dispose();
    }

    public class PistonHead extends SolidObject2D{ 
        
        private static final float SCALE_X = 1f;
        private static final float SCALE_Y = 1f;
        
        private Vector2 startPosition;
        
        private float angle;
        private float speed;
        private float height;
        
        private Vector2 direction;
    
        public PistonHead(World world, float posX, float posY, float angle, Vector2 direction, float height, float speed, float ratio){

            this.angle = angle;
            this.speed = speed;
            this.height = height;
            
            this.startPosition = new Vector2(posX * P2M, posY * P2M); 
            
            Vector2 directionNormalized = (new Vector2(direction)).rotateRad(angle);          
            this.direction = (new Vector2(directionNormalized)).scl(this.speed);

            // Part physic
            BodyDef groundBodyDef = new BodyDef();  
            // Set its world position
            groundBodyDef.position.set((new Vector2(this.startPosition)).add(directionNormalized.scl(ratio * this.height)));

            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);  

            groundBody.setType(BodyDef.BodyType.KinematicBody);

            this.collisionFixture = new ArrayList<Fixture>();

            this.priority = 3;

            // Create a polygon shape
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(82f * P2M * this.scale, 112f * P2M * this.scale, new Vector2(0, 0), 0);
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
            this.collisionFixture.add(fix);

            this.physicBody = groundBody;
            this.physicBody.setTransform(this.getPositionBody(), this.angle);
            
            this.physicBody.setLinearVelocity(this.direction);
        }
        
        public void assignTextures(Texture texture){
            if(texture != null){
                this.texture = texture;
            }
        }

        @Override
        public Sprite createCurrentSprite(){
            Sprite sprite = super.createCurrentSprite();
            sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
            return sprite;
        }

        @Override
        public void updateLogic(float deltaTime){
            super.updateLogic(deltaTime);

            Vector2 displacementDirection = this.getPositionBody().sub(this.startPosition);
            float sameDirection = displacementDirection.dot(this.direction);
                
            if(sameDirection > 1){         
                float length = displacementDirection.len();
                if(length > this.height){   
                    this.direction = this.direction.scl(-1);
                    this.physicBody.setLinearVelocity(this.direction);
                }
            }      
        }
    }
}