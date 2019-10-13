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
public class Scaffolding extends SolidObject2D{
    private static final String[] SCAFFOLDING_ARRAY = {
    "urbanObj/Echafaudage-06.png",
    "urbanObj/Echafaudage-08.png",
    "urbanObj/Echafaudage-09.png",
    "urbanObj/Echafaudage-10.png"};  
    
    private int scaffoldingIndex;
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    public Scaffolding(World world, float posX, float posY, int index){
        
        this.scaffoldingIndex = index;
        
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
        
        this.priority = 1;
        
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        
        switch(this.scaffoldingIndex){
            case 0:
            case 1:
                ground.setAsBox(113 * P2M * SCALE_X, 284 * P2M * SCALE_Y, new Vector2(0, 0), 0);
                break;
            case 2:
            case 3:
                ground.setAsBox(113 * P2M * SCALE_X, 189 * P2M * SCALE_Y, new Vector2(0, 0), 0);  
                break;           
        }
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
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(SCAFFOLDING_ARRAY[this.scaffoldingIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}