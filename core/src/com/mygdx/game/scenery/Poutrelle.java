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
import com.mygdx.game.ActionFixtures;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixtures;
import com.mygdx.game.SolidObject2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author françois
 */
public class Poutrelle extends SolidObject2D{
    private static final Texture POUTRELLETEXT = new Texture("urbanObj" + File.separator + "Obstacle_Poutrelle.png");
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private ActionFixtures upActionFixture;
    
    public Poutrelle(World world, float posX, float posY){
        
        // Part graphic
        this.texture = POUTRELLETEXT;
        
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
        ground.setAsBox(382 * P2M * SCALE_X, 117 / 8f * P2M * SCALE_Y, new Vector2(0, 117 * 3/ 8f * P2M * SCALE_Y), 0);
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
        
        ground.setAsBox(382 * P2M * SCALE_X, 117 / 8f * P2M * SCALE_Y, new Vector2(0, -117 * 3/ 8f * P2M * SCALE_Y), 0);
        fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.physicBody = groundBody;
        this.physicBody.setLinearVelocity(new Vector2(-2f, 0));

        // ActionFixture
        Set<Fixture> setFixtures = new HashSet();
        
        ground.setAsBox(382 * P2M * SCALE_X, 117 / 8f * P2M * SCALE_Y, new Vector2(0, (-117 * 3/ 8f + 3)  * P2M * SCALE_Y), 0);
        fix = groundBody.createFixture(fixtureDef); 
        setFixtures.add(fix);
        ground.setAsBox(382 * P2M * SCALE_X, 117 / 8f * P2M * SCALE_Y, new Vector2(0, (117 * 3/ 8f + 3)  * P2M * SCALE_Y), 0);
        fix = groundBody.createFixture(fixtureDef); 
        setFixtures.add(fix);
        this.upActionFixture = new KinematicActionFixtures(setFixtures);
        
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
        if(this.physicBody.getPosition().x > -30){
            this.physicBody.setLinearVelocity(new Vector2(-4f, 0));
        }else if(this.physicBody.getPosition().x < -40){
            this.physicBody.setLinearVelocity(new Vector2(4f, 0));
        }
        
        this.upActionFixture.applyAction(deltaTime, this);
    }
}