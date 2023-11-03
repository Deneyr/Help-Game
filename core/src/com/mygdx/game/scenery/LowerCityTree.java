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
import com.badlogic.gdx.physics.box2d.CircleShape;
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
public class LowerCityTree extends SolidObject2D{
    private static final String[] TREE_ARRAY = {
    "lowerCity/Help_Props_ArbrePique_240x270.png",
    "lowerCity/Help_Props_ArbrePique_2_320x320.png",
    "lowerCity/Help_Props_ArbrePique_3_480x380.png"};  
    
    private int testBlockIndex;
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    public LowerCityTree(World world, float posX, float posY, int index, float scale){
        
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
        CircleShape circle = null;
        Fixture fix = null;
        
        switch(this.testBlockIndex){
            case 0:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(0 * P2M * this.scale, 10 * P2M * this.scale));           
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(11 * this.scale * P2M * SCALE_X, 78 * this.scale * P2M * SCALE_Y, new Vector2(0 * this.scale * P2M * SCALE_X, -78 * this.scale * P2M * SCALE_Y), 0);
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                break;
            case 1:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(48 * P2M * this.scale, 38 * P2M * this.scale));               
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-94 * P2M * this.scale, -77 * P2M * this.scale));               
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(11 * this.scale * P2M * SCALE_X, 100 * this.scale * P2M * SCALE_Y, new Vector2(50 * this.scale * P2M * SCALE_X, -62 * this.scale * P2M * SCALE_Y), 0);
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(50 * this.scale * P2M * SCALE_X, 11 * this.scale * P2M * SCALE_Y, new Vector2(-4 * this.scale * P2M * SCALE_X, -103 * this.scale * P2M * SCALE_Y), (float) Math.toRadians(-10));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 2:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(-28 * P2M * this.scale, 66 * P2M * this.scale));               
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-168 * P2M * this.scale, -76 * P2M * this.scale));               
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(170 * P2M * this.scale, 115 * P2M * this.scale));               
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(11 * this.scale * P2M * SCALE_X, 190 * this.scale * P2M * SCALE_Y, new Vector2(-28 * this.scale * P2M * SCALE_X, -96 * this.scale * P2M * SCALE_Y), 0);
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(55 * this.scale * P2M * SCALE_X, 11 * this.scale * P2M * SCALE_Y, new Vector2(-83 * this.scale * P2M * SCALE_X, -96 * this.scale * P2M * SCALE_Y), (float) Math.toRadians(-10));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(55 * this.scale * P2M * SCALE_X, 11 * this.scale * P2M * SCALE_Y, new Vector2(94 * this.scale * P2M * SCALE_X, 94 * this.scale * P2M * SCALE_Y), (float) Math.toRadians(20));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
        }
        
        this.physicBody = groundBody;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(TREE_ARRAY[this.testBlockIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}