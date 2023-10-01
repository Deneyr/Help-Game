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
public class LowerCityPlank extends SolidObject2D{
    private static final String[] PLANK_ARRAY = {
    "lowerCity/Help_Props__Pont_680x180.png",
    "lowerCity/Help_Props__Plank_140x50.png"};  
    
    private int testBlockIndex;
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    public LowerCityPlank(World world, float posX, float posY, int index, float scale){
        
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
                fixtureDef.shape = ground;
                ground.setAsBox(80 * this.scale * P2M * SCALE_X, 12 * this.scale * P2M * SCALE_Y, new Vector2(-80 * this.scale * P2M * SCALE_X, 54 * this.scale * P2M * SCALE_Y), (float)Math.toRadians(8));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                fixtureDef.shape = ground;
                ground.setAsBox(80 * this.scale * P2M * SCALE_X, 12 * this.scale * P2M * SCALE_Y, new Vector2(80 * this.scale * P2M * SCALE_X, 54 * this.scale * P2M * SCALE_Y), (float)Math.toRadians(-8));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                
                ground.setAsBox(40 * this.scale * P2M * SCALE_X, 12 * this.scale * P2M * SCALE_Y, new Vector2(-205 * this.scale * P2M * SCALE_X, 30 * this.scale * P2M * SCALE_Y), (float)Math.toRadians(18));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(40 * this.scale * P2M * SCALE_X, 12 * this.scale * P2M * SCALE_Y, new Vector2(205 * this.scale * P2M * SCALE_X, 30 * this.scale * P2M * SCALE_Y), (float)Math.toRadians(-18));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                
                ground.setAsBox(50 * this.scale * P2M * SCALE_X, 12 * this.scale * P2M * SCALE_Y, new Vector2(-282 * this.scale * P2M * SCALE_X, -16 * this.scale * P2M * SCALE_Y), (float)Math.toRadians(40));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground.setAsBox(50 * this.scale * P2M * SCALE_X, 12 * this.scale * P2M * SCALE_Y, new Vector2(282 * this.scale * P2M * SCALE_X, -16 * this.scale * P2M * SCALE_Y), (float)Math.toRadians(-40));
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                break;
            case 1:
                ground = new PolygonShape();
                fixtureDef.shape = ground;
                ground.setAsBox(60 * this.scale * P2M * SCALE_X, 14 * this.scale * P2M * SCALE_Y, new Vector2(-4 * this.scale * P2M * SCALE_X, 0 * this.scale * P2M * SCALE_Y), 0);
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
        }
        
        this.physicBody = groundBody;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(PLANK_ARRAY[this.testBlockIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}