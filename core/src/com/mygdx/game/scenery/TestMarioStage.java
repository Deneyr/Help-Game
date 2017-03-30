/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;

/**
 *
 * @author françois
 */
public class TestMarioStage extends SolidObject2D{
    private static final Texture MARIOSTAGETEXT = new Texture("decor.jpeg");
    
    public TestMarioStage(World world, float posX, float posY){
        
        // Part graphic
        this.texture = MARIOSTAGETEXT;
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  // Change !!!!!!
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyType.StaticBody);

        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(275 * P2M, 22 * P2M, new Vector2(0, -(206 - 22) * P2M), 0);
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = ground;
        fixtureDef2.density = 1f; 
        fixtureDef2.friction = 0.05f;
        fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef2); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);

        ground = new PolygonShape();
        ground.setAsBox(16 * P2M, 15 * P2M, new Vector2((245 - 275) * P2M, (412 - 264 - 206) * P2M), 0);
        fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = ground;
        fixtureDef2.density = 1f; 
        fixtureDef2.friction = 0.05f;
        fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        fix = groundBody.createFixture(fixtureDef2); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);

        ground = new PolygonShape();
        ground.setAsBox(16 * P2M, 15 * P2M, new Vector2((452 - 275) * P2M, (412 - 146 - 206) * P2M), 0);
        fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = ground;
        fixtureDef2.density = 1f; 
        fixtureDef2.friction = 0.05f;
        fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        fix = groundBody.createFixture(fixtureDef2); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);

        ground = new PolygonShape();
        ground.setAsBox(16 * 5 * P2M, 15 * P2M, new Vector2((452 - 275) * P2M, (412 - 264 - 206) * P2M), 0);
        fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = ground;
        fixtureDef2.density = 1f; 
        fixtureDef2.friction = 0.05f;
        fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        fix = groundBody.createFixture(fixtureDef2); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        // Test sensor bounding
        ground = new PolygonShape();
        ground.setAsBox(275 * P2M, 206 * P2M, new Vector2(0, 0), 0);
        fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = ground;
        fixtureDef2.density = 1f; 
        fixtureDef2.friction = 0.05f;
        fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        fix = groundBody.createFixture(fixtureDef2); 
        fix.setSensor(true);
        fix.setUserData(this);

        this.physicBody = groundBody;
      
    }
}
