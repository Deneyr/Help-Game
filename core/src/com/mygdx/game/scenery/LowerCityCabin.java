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
 * @author Deneyr
 */
public class LowerCityCabin extends SolidObject2D{
    private static final String[] STILTS_ARRAY = {
    "lowerCity/Maison_Pauvre1.png",
    "lowerCity/Maison_Pauvre2.png",
    "lowerCity/Maison_Pauvre3.png",
    "lowerCity/Maison_Pauvre4.png"};  
    
    private int testBlockIndex;
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    public LowerCityCabin(World world, float posX, float posY, int index, float scale){
        
        this.testBlockIndex = index;
        
        this.scale = scale;
        
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
        PolygonShape ground = null;
        Fixture fix = null;
        
        switch(this.testBlockIndex){
            case 0:
                ground = new PolygonShape();
                ground.set(new float[]{
                    -332 * P2M * this.scale * SCALE_X, 148 * P2M * this.scale * SCALE_Y,
                    332 * P2M * this.scale * SCALE_X, 148 * P2M * this.scale * SCALE_Y,
                    131 * P2M * this.scale * SCALE_X, 348 * P2M * this.scale * SCALE_Y,
                    -131 * P2M * this.scale * SCALE_X, 348 * P2M * this.scale * SCALE_Y
                });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                fixtureDef.shape = ground;
                ground.setAsBox(298 * this.scale * P2M * SCALE_X, 90 * this.scale * P2M * SCALE_Y, new Vector2(0 * this.scale * P2M * SCALE_X, 58 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(148 * this.scale * P2M * SCALE_X, 80 * this.scale * P2M * SCALE_Y, new Vector2(4 * this.scale * P2M * SCALE_X, -117 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(239 * this.scale * P2M * SCALE_X, 75 * this.scale * P2M * SCALE_Y, new Vector2(2 * this.scale * P2M * SCALE_X, -274 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(94 * this.scale * P2M * SCALE_X, 12 * this.scale * P2M * SCALE_Y, new Vector2(-237 * this.scale * P2M * SCALE_X, -210 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                break;
            case 1:
                ground = new PolygonShape();
                ground.set(new float[]{
                    -235 * P2M * this.scale * SCALE_X, 256 * P2M * this.scale * SCALE_Y,
                    237 * P2M * this.scale * SCALE_X, 256 * P2M * this.scale * SCALE_Y,
                    178 * P2M * this.scale * SCALE_X, 338 * P2M * this.scale * SCALE_Y,
                    -181 * P2M * this.scale * SCALE_X, 338 * P2M * this.scale * SCALE_Y
                });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.set(new float[]{
                    -119 * P2M * this.scale * SCALE_X, -190 * P2M * this.scale * SCALE_Y,
                    -90 * P2M * this.scale * SCALE_X, -219 * P2M * this.scale * SCALE_Y,
                    100 * P2M * this.scale * SCALE_X, -219 * P2M * this.scale * SCALE_Y,
                    130 * P2M * this.scale * SCALE_X, -190 * P2M * this.scale * SCALE_Y
                });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.set(new float[]{
                    -90 * P2M * this.scale * SCALE_X, -219 * P2M * this.scale * SCALE_Y,
                    -117 * P2M * this.scale * SCALE_X, -249 * P2M * this.scale * SCALE_Y,
                    124 * P2M * this.scale * SCALE_X, -249 * P2M * this.scale * SCALE_Y,
                    100 * P2M * this.scale * SCALE_X, -219 * P2M * this.scale * SCALE_Y
                });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.set(new float[]{
                    -119 * P2M * this.scale * SCALE_X, -294 * P2M * this.scale * SCALE_Y,
                    -131 * P2M * this.scale * SCALE_X, -310 * P2M * this.scale * SCALE_Y,
                    141 * P2M * this.scale * SCALE_X, -310 * P2M * this.scale * SCALE_Y,
                    129 * P2M * this.scale * SCALE_X, -294 * P2M * this.scale * SCALE_Y
                });
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                fixtureDef.shape = ground;
                ground.setAsBox(188 * this.scale * P2M * SCALE_X, 105 * this.scale * P2M * SCALE_Y, new Vector2(5 * this.scale * P2M * SCALE_X, 150 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(90 * this.scale * P2M * SCALE_X, 118 * this.scale * P2M * SCALE_Y, new Vector2(5 * this.scale * P2M * SCALE_X, -72 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(118 * this.scale * P2M * SCALE_X, 28 * this.scale * P2M * SCALE_Y, new Vector2(3 * this.scale * P2M * SCALE_X, -272 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(136 * this.scale * P2M * SCALE_X, 14 * this.scale * P2M * SCALE_Y, new Vector2(5 * this.scale * P2M * SCALE_X, -326 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                break;
            case 2:

                break;
            case 3:

                break;
        }     
        
        this.physicBody = groundBody;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(STILTS_ARRAY[this.testBlockIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}