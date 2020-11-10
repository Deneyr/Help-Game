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
public class IvyObstacle extends SolidObject2D{
    private static final String[] OBSTACLEHOUSE_ARRAY = {
    "urbanObj/Help_Props_30x125_EchelleLiere.png",
    "urbanObj/Help_Props_30x125_EchelleLiereFleur.png",
    "urbanObj/Help_Props_40x135_EchelleBosquetFleur.png",
    "urbanObj/Help_Props_100x60_BosquetFleur.png",
    "urbanObj/Help_Props_140x35_BosquetTailleFleur.png",
    "urbanObj/Help_Props_140x40_BosquetFleurLong.png"};  
    
    private int ivyIndex;
    
    private float angle;
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    public IvyObstacle(World world, float posX, float posY, float angle, int index){
        
        this.ivyIndex = index;
        
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
        
        switch(this.ivyIndex){
            case 0:
                ground = new PolygonShape();
                ground.setAsBox(12 * P2M * SCALE_X, 60 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 1:
                ground = new PolygonShape();
                ground.setAsBox(12 * P2M * SCALE_X, 60 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 2:
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * SCALE_X, 60 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 3:
                ground = new PolygonShape();
                ground.setAsBox(30 * P2M * SCALE_X, 18 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 4:
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * SCALE_X, 16 * P2M * SCALE_Y);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 5:
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * SCALE_X, 18 * P2M * SCALE_Y);
                
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
        this.texture = TextureManager.getInstance().getTexture(OBSTACLEHOUSE_ARRAY[this.ivyIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}
