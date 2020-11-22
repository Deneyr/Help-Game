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
public class MetalBeam extends SolidObject2D{
    private static final String[] OBSTACLEMETALBEAM_ARRAY = {
    "urbanObj/Help_Props_Poutre1Cote_240x80.png",
    "urbanObj/Help_Props_Poutre1Face_90x80.png",
    "urbanObj/Help_Props_Poutre2Cote_240x60.png",
    "urbanObj/Help_Props_poutre2Face_90x70.png"};  
    
    private int metalBeamIndex;
    
    private float angle;
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    public MetalBeam(World world, float posX, float posY, float angle, int index){
        
        this.metalBeamIndex = index;
        
        this.angle = angle;
        
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
        
        this.priority = 3;
        
        // Create a polygon shape
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        PolygonShape ground;
        Fixture fix;
        
        switch(this.metalBeamIndex){
            case 0:
                ground = new PolygonShape();
                ground.setAsBox(120 * P2M * SCALE_X, 35 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 1:          
                this.priority = 4;
                
                ground = new PolygonShape();
                ground.setAsBox(30 * P2M * SCALE_X, 35 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 2:               
                ground = new PolygonShape();
                ground.setAsBox(120 * P2M * SCALE_X, 25 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 3:
                this.priority = 4;
                
                ground = new PolygonShape();
                ground.setAsBox(40 * P2M * SCALE_X, 30 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;            
        }
        
        this.physicBody = groundBody;
        
        this.physicBody.setTransform(this.getPositionBody(), this.angle);
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(OBSTACLEMETALBEAM_ARRAY[this.metalBeamIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}
