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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class Crane extends SolidObject2D{
    private static final String BASECRANETEXT = "urbanObj/Decors_Grue_base.png";
    private static final String STRINGCRANETEXT = "urbanObj/Decors_Grue_filin.png";
    private static final String HOOKCRANETEXT = "urbanObj/Decors_Grue_Crochet.png";
    private static final String FRONTCRANETEXT = "urbanObj/Decors_Grue_Avant.png";
    private static final String BACKCRANETEXT = "urbanObj/Decors_Grue_Arriere.png";
    private static final String TOPCRANETEXT = "urbanObj/Decors_Grue_Sommet.png";

    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    private float heightCraneRate;
    private float heightStringRate;
    private boolean isLeft;
    
    private FrontCraneObject2D frontCraneObject2D;
    private BackCraneObject2D backCraneObject2D;
    private TopCraneObject2D topCraneObject2D;
    
    public Crane(World world, float posX, float posY, float heightCraneRate, float heightStringRate, boolean isLeft){
        
        this.heightCraneRate = heightCraneRate;
        this.heightStringRate = heightStringRate;
        this.isLeft = isLeft;
        
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
        
        ground.setAsBox(80 * P2M * SCALE_X, 60 * P2M * SCALE_Y, new Vector2(0, -535 * P2M * SCALE_Y), 0);
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        
        ground = new PolygonShape();
        ground.setAsBox(80 * P2M * Crane.SCALE_X, 607 * P2M * Crane.SCALE_Y, new Vector2(0, 0), 0);
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        fixtureDef = new FixtureDef();
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        fix = groundBody.createFixture(fixtureDef); 
        fix.setSensor(true);
        fix.setUserData(this);

        this.collisionFixture.add(fix);

        this.physicBody = groundBody;
        //this.physicBody.setLinearVelocity(new Vector2(0.5f, 0));
        
        this.frontCraneObject2D = new FrontCraneObject2D(this.physicBody, world, posX, posY, 1200 * P2M * SCALE_Y, this.heightCraneRate, this.isLeft);
        this.backCraneObject2D = new BackCraneObject2D(this.physicBody, world, posX, posY, 1200 * P2M * SCALE_Y, this.heightCraneRate, this.isLeft);
        this.topCraneObject2D = new TopCraneObject2D(this.physicBody, world, posX, posY, 1200 * P2M * SCALE_Y);
        
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(BASECRANETEXT, this);
        
        Texture stringCraneTexture = TextureManager.getInstance().getTexture(STRINGCRANETEXT, this); 
        Texture hookCraneTexture = TextureManager.getInstance().getTexture(HOOKCRANETEXT, this); 
        Texture frontCraneTexture = TextureManager.getInstance().getTexture(FRONTCRANETEXT, this); 
        Texture backCraneTexture = TextureManager.getInstance().getTexture(BACKCRANETEXT, this); 
        Texture topCraneTexture = TextureManager.getInstance().getTexture(TOPCRANETEXT, this);
        
        if(frontCraneTexture != null
                && stringCraneTexture != null
                && hookCraneTexture != null){
            this.frontCraneObject2D.assignTextures(frontCraneTexture, stringCraneTexture, hookCraneTexture);
        }
        
        if(backCraneTexture != null){
            this.backCraneObject2D.assignTextures(backCraneTexture);
        }
        
        if(topCraneTexture != null){
            this.topCraneObject2D.assignTextures(topCraneTexture);
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
        
        if(this.frontCraneObject2D != null){
            this.frontCraneObject2D.removeBody(world);
        }
        
        if(this.backCraneObject2D != null){
            this.backCraneObject2D.removeBody(world);
        }
        
        if(this.topCraneObject2D != null){
            this.topCraneObject2D.removeBody(world);
        }
        super.removeBody(world);
    }
    
    public class FrontCraneObject2D extends SolidObject2D{
        
        private float parentPosX;
        private float parentPosY;
        private float maxHeight;
        
        private boolean isLeft;
        
        private StringCraneObject2D stringCraneObject2D;
        
        public FrontCraneObject2D(Body ownerBody, World world, float posX, float posY, float maxHeight, float heightCraneRate, boolean isLeft){
            
            this.priority = 1;          
            
            this.maxHeight = maxHeight;
            this.parentPosX = posX;
            this.parentPosY = posY;
            
            this.isLeft = isLeft;
            
            // Part physic
            this.collisionFixture = new ArrayList<Fixture>();
            
            BodyDef groundBodyDef = new BodyDef();  
            if(this.isLeft){
                groundBodyDef.position.set(new Vector2((-690 * Crane.SCALE_X + this.parentPosX) * P2M, (this.parentPosY - 200 * Crane.SCALE_Y) * P2M + (this.maxHeight / 2) * heightCraneRate));
            }else{
                groundBodyDef.position.set(new Vector2((690 * Crane.SCALE_X + this.parentPosX) * P2M, (this.parentPosY - 200 * Crane.SCALE_Y) * P2M + (this.maxHeight / 2) * heightCraneRate)); 
            }
            
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.KinematicBody);
       
            
            PolygonShape ground = new PolygonShape();
            if(this.isLeft){
                ground.set(new Vector2[]{
                    new Vector2(-580f * P2M * Crane.SCALE_X, 200f * P2M * Crane.SCALE_Y),
                    new Vector2(-650f * P2M * Crane.SCALE_X, 100f * P2M * Crane.SCALE_Y),
                    new Vector2(750f * P2M * Crane.SCALE_X, 100f * P2M * Crane.SCALE_Y),
                    new Vector2(750f * P2M * Crane.SCALE_X, 200f * P2M * Crane.SCALE_Y),
                });
                
                //ground.setAsBox(700 * P2M * Crane.SCALE_X, 50 * P2M * Crane.SCALE_Y, new Vector2(50 * P2M * SCALE_X, 150 * P2M * Crane.SCALE_Y), 0);
            }else{
                ground.set(new Vector2[]{
                    new Vector2(580f * P2M * Crane.SCALE_X, 200f * P2M * Crane.SCALE_Y),
                    new Vector2(650f * P2M * Crane.SCALE_X, 100f * P2M * Crane.SCALE_Y),
                    new Vector2(-750f * P2M * Crane.SCALE_X, 100f * P2M * Crane.SCALE_Y),
                    new Vector2(-750f * P2M * Crane.SCALE_X, 200f * P2M * Crane.SCALE_Y),
                });
                //ground.setAsBox(700 * P2M * Crane.SCALE_X, 50 * P2M * Crane.SCALE_Y, new Vector2(-50 * P2M * SCALE_X, 150 * P2M * Crane.SCALE_Y), 0);
            }
            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = ground;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            this.collisionFixture.add(fix);
                   
            ground = new PolygonShape();
            if(this.isLeft){
                ground.setAsBox(100 * P2M * Crane.SCALE_X, 150 * P2M * Crane.SCALE_Y, new Vector2(550 * P2M * Crane.SCALE_X, 0), 0);
            }else{
                ground.setAsBox(100 * P2M * Crane.SCALE_X, 150 * P2M * Crane.SCALE_Y, new Vector2(-550 * P2M * Crane.SCALE_X, 0), 0);
            }
            fixtureDef.shape = ground;
            fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            this.collisionFixture.add(fix);
            
            this.physicBody = groundBody;
            
            this.stringCraneObject2D = new StringCraneObject2D(this.physicBody, world, groundBodyDef.position.x / P2M, groundBodyDef.position.y / P2M + 50, 1300 * P2M * SCALE_X, -0.5f, 1f, isLeft);             
        }
        
        public void assignTextures(Texture texture, Texture stringTexture, Texture hookTexture){

            if(texture != null){
                this.texture = texture;
            }
            
            this.stringCraneObject2D.assignTextures(stringTexture);
        }
        
        @Override
        public void removeBody(World world){

            if(this.stringCraneObject2D != null){
                this.stringCraneObject2D.removeBody(world);
            }
            super.removeBody(world);
        }
        
        @Override
        public Sprite createCurrentSprite(){
            Sprite sprite = super.createCurrentSprite();
            if(this.isLeft){
                sprite.setScale(-sprite.getScaleX() * Crane.SCALE_X, sprite.getScaleY() * Crane.SCALE_Y);
            }else{
                sprite.setScale(sprite.getScaleX() * Crane.SCALE_X, sprite.getScaleY() * Crane.SCALE_Y);

            }
            return sprite;
        }
    }
    
    public class BackCraneObject2D extends SolidObject2D{
        
        private float maxHeight;
        private float parentPosX;
        private float parentPosY;
        
        private boolean isLeft;
        
        public BackCraneObject2D(Body ownerBody, World world, float posX, float posY, float maxHeight, float heightCraneRate, boolean isLeft){
            
            this.priority = 1;          
            
            this.maxHeight = maxHeight;
            this.parentPosX = posX;
            this.parentPosY = posY;
            
            this.isLeft = isLeft;
            
            // Part physic
            this.collisionFixture = new ArrayList<Fixture>();
            
            BodyDef groundBodyDef = new BodyDef();  
            if(this.isLeft){
                groundBodyDef.position.set(new Vector2((370 * Crane.SCALE_X + this.parentPosX) * P2M, (this.parentPosY - 130 * Crane.SCALE_Y) * P2M + (this.maxHeight / 2) * heightCraneRate));
            }else{
                groundBodyDef.position.set(new Vector2((-370 * Crane.SCALE_X + this.parentPosX) * P2M, (this.parentPosY - 130 * Crane.SCALE_Y) * P2M + (this.maxHeight / 2) * heightCraneRate)); 
            }
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.KinematicBody);
       
            
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(340 * P2M * Crane.SCALE_X, 50 * P2M * Crane.SCALE_Y, new Vector2(0, 82 * P2M * Crane.SCALE_Y), 0);
            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = ground;
            fixtureDef.density = 1f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            this.collisionFixture.add(fix);
                               
            ground = new PolygonShape();
            if(this.isLeft){
                ground.setAsBox(60 * P2M * Crane.SCALE_X, 60 * P2M * Crane.SCALE_Y, new Vector2(140 * P2M * Crane.SCALE_X, -45 * P2M * Crane.SCALE_Y), 0);
            }else{
                ground.setAsBox(60 * P2M * Crane.SCALE_X, 60 * P2M * Crane.SCALE_Y, new Vector2(-140 * P2M * Crane.SCALE_X, -45 * P2M * Crane.SCALE_Y), 0);
            }
            fixtureDef.shape = ground;
            fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            this.collisionFixture.add(fix);
            
            this.physicBody = groundBody;
        }
        
        public void assignTextures(Texture texture){

            if(texture != null){
                this.texture = texture;
            }
        }
        
        @Override
        public Sprite createCurrentSprite(){
            Sprite sprite = super.createCurrentSprite();
            if(this.isLeft){
                sprite.setScale(-sprite.getScaleX() * Crane.SCALE_X, sprite.getScaleY() * Crane.SCALE_Y);
            }else{
                sprite.setScale(sprite.getScaleX() * Crane.SCALE_X, sprite.getScaleY() * Crane.SCALE_Y);

            }
            return sprite;
        }
    }
    
    public class TopCraneObject2D extends Object2D{
        
        private float parentPosX;
        private float parentPosY;
        private float maxHeight;
    
        public TopCraneObject2D(Body ownerBody, World world, float posX, float posY, float maxHeight){

            this.priority = 1;          

            this.maxHeight = maxHeight;
            this.parentPosX = posX;
            this.parentPosY = posY;

            // Part physic
            // Create a body from the defintion and add it to the world
            BodyDef groundBodyDef = new BodyDef();  
            groundBodyDef.position.set(new Vector2(this.parentPosX * P2M, (this.parentPosY + 145 * Crane.SCALE_Y) * P2M + this.maxHeight/2));
            
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.KinematicBody);

            PolygonShape ground = new PolygonShape();
            ground.setAsBox(60 * P2M * Crane.SCALE_X, 160 * P2M * Crane.SCALE_Y, new Vector2(0, 0), 0);

            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = ground;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            fix.setSensor(true);

            this.physicBody = groundBody;
        }

        public void assignTextures(Texture texture){

            if(texture != null){
                this.texture = texture;
            }
        }

        @Override
        public Sprite createCurrentSprite(){
            Sprite sprite = super.createCurrentSprite();
            sprite.setScale(sprite.getScaleX() * Crane.SCALE_X, sprite.getScaleY() * Crane.SCALE_Y);
            return sprite;
        }
    }
    
    public class StringCraneObject2D extends Object2D{
        
        private float parentPosX;
        private float parentPosY;
        private float maxWidth;
        private float ratioStringHeight;
    
        public StringCraneObject2D(Body ownerBody, World world, float posX, float posY, float maxWidth, float ratioWidth, float ratioStringHeight, boolean isLeft){

            this.priority = 1;          

            this.maxWidth = maxWidth;
            this.parentPosX = posX;
            this.parentPosY = posY;
            this.ratioStringHeight = ratioStringHeight;

            // Part physic
            // Create a body from the defintion and add it to the world
            BodyDef groundBodyDef = new BodyDef();  
            if(isLeft){
                groundBodyDef.position.set(new Vector2(this.parentPosX * P2M - this.maxWidth/2 * ratioWidth, (this.parentPosY * P2M - 318 * ratioStringHeight * P2M * Crane.SCALE_Y)));
            }else{
                groundBodyDef.position.set(new Vector2(this.parentPosX * P2M + this.maxWidth/2 * ratioWidth, (this.parentPosY * P2M - 318 * ratioStringHeight * P2M * Crane.SCALE_Y)));
            }
            
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.KinematicBody);

            PolygonShape ground = new PolygonShape();
            ground.setAsBox(25 * P2M * Crane.SCALE_X, 318 * P2M * Crane.SCALE_Y, new Vector2(0, 0), 0);

            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = ground;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            fix.setSensor(true);

            this.physicBody = groundBody;
        }

        public void assignTextures(Texture texture){

            if(texture != null){
                this.texture = texture;
            }
        }

        @Override
        public Sprite createCurrentSprite(){
            Sprite sprite = super.createCurrentSprite();
            sprite.setScale(sprite.getScaleX() * Crane.SCALE_X, sprite.getScaleY() * Crane.SCALE_Y * this.ratioStringHeight);
            return sprite;
        }
    }
}
