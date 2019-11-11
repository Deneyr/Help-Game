/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class ObstacleHouse extends SolidObject2D{
    private static final String[] OBSTACLEHOUSE_ARRAY = {
    "building/Obstacle_Maison.png",
    "building/Obstacle_Maison2.png",
    "building/Obstacle_Maison3.png",
    "building/Obstacle_Maison4.png",
    "building/Obstacle_Maison5.png",
    "building/Obstacle_Maison6.png",
    "building/Obstacle_Maison7.png",
    "building/Obstacle_Maison8.png",
    "building/Obstacle_Maison9.png",
    "building/Obstacle_Maison10.png"};  
    
    private int houseIndex;
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    public ObstacleHouse(World world, float posX, float posY, int index){
        
        this.houseIndex = index;
        
        // Part graphic
        this.assignTextures();
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 4;
        
        // Create a polygon shape
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        PolygonShape ground;
        Fixture fix;
        
        switch(this.houseIndex){
            case 0:
                ground = new PolygonShape();
                    ground.set(new float[]{-496 * P2M * SCALE_X, 194 * P2M * SCALE_Y,
                        -308 * P2M * SCALE_X, 320 * P2M * SCALE_Y,
                        308 * P2M * SCALE_X, 320 * P2M * SCALE_Y,
                        496 * P2M * SCALE_X, 194 * P2M * SCALE_Y
                    });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(410 * P2M * SCALE_X, 255 * P2M * SCALE_Y, new Vector2(0, -65 * P2M * SCALE_Y), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 1:
                ground = new PolygonShape();
                    ground.set(new float[]{-496 * P2M * SCALE_X, 116 * P2M * SCALE_Y,
                        -308 * P2M * SCALE_X, 206 * P2M * SCALE_Y,
                        308 * P2M * SCALE_X, 206 * P2M * SCALE_Y,
                        496 * P2M * SCALE_X, 116 * P2M * SCALE_Y
                    });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(410 * P2M * SCALE_X, 166 * P2M * SCALE_Y, new Vector2(0, -40 * P2M * SCALE_Y), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 2:
                ground = new PolygonShape();
                    ground.set(new float[]{-347 * P2M * SCALE_X, 339 * P2M * SCALE_Y,
                        -164 * P2M * SCALE_X, 489 * P2M * SCALE_Y,
                        347 * P2M * SCALE_X, 489 * P2M * SCALE_Y,
                        347 * P2M * SCALE_X, 339 * P2M * SCALE_Y
                    });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(347 * P2M * SCALE_X, 414 * P2M * SCALE_Y, new Vector2(0, -75 * P2M * SCALE_Y), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 3:
                ground = new PolygonShape();
                ground.setAsBox(339 * P2M * SCALE_X, 555 * P2M * SCALE_Y, new Vector2(0, 0), 0); 
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 4:
                ground = new PolygonShape();
                ground.setAsBox(311 * P2M * SCALE_X, 615 * P2M * SCALE_Y, new Vector2(0, -51 * P2M * SCALE_Y), 0); 
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(51 * P2M * SCALE_X, 51 * P2M * SCALE_Y, new Vector2(-260 * P2M * SCALE_X, 615 * P2M * SCALE_Y), 0); 
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 5:
                ground = new PolygonShape();
                    ground.set(new float[]{-250 * P2M * SCALE_X, 432 * P2M * SCALE_Y,
                        250 * P2M * SCALE_X, 432 * P2M * SCALE_Y,
                        250 * P2M * SCALE_X, -432 * P2M * SCALE_Y,
                        -210 * P2M * SCALE_X, -432 * P2M * SCALE_Y
                    });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 6:
                ground = new PolygonShape();
                    ground.set(new float[]{-228 * P2M * SCALE_X, 548 * P2M * SCALE_Y,
                        228 * P2M * SCALE_X, 548 * P2M * SCALE_Y,
                        228 * P2M * SCALE_X, -548 * P2M * SCALE_Y,
                        -208 * P2M * SCALE_X, -548 * P2M * SCALE_Y
                    });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 7:
                ground = new PolygonShape();
                ground.setAsBox(268 * P2M * SCALE_X, 440 * P2M * SCALE_Y, new Vector2(0, 0), 0); 
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 8:
                ground = new PolygonShape();
                ground.setAsBox(321 * P2M * SCALE_X, 533 * P2M * SCALE_Y, new Vector2(0, 0), 0); 
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 9:
                ground = new PolygonShape();
                    ground.set(new float[]{-559 * P2M * SCALE_X, 488 * P2M * SCALE_Y,
                        441 * P2M * SCALE_X, 488 * P2M * SCALE_Y,
                        559 * P2M * SCALE_X, 368 * P2M * SCALE_Y,
                        -559 * P2M * SCALE_X, 368 * P2M * SCALE_Y
                    });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(559 * P2M * SCALE_X, 429 * P2M * SCALE_Y, new Vector2(0, -59 * P2M * SCALE_Y), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
        }
        
        this.physicBody = groundBody;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(OBSTACLEHOUSE_ARRAY[this.houseIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}