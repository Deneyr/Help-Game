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
import com.mygdx.game.GearActionFixture;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixture;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Deneyr
 */
public abstract class AGearObject extends SolidObject2D{
    
    protected GearActionFixture gearActionFixture;
    
    protected float angle;
    protected float rotationSpeed;
    
    protected Vector2 startPosition;
    
    public AGearObject(World world, float posX, float posY, float angle, float scale, float rotationSpeed){        
        this.scale = scale;
        this.angle = angle;
        
        this.rotationSpeed = (float) Math.toRadians(rotationSpeed);
        
        this.startPosition = new Vector2(posX * P2M, posY * P2M);
        
        // Part physic       
        BodyDef groundBodyDef = new BodyDef();  
        groundBodyDef.position.set(this.startPosition.x, this.startPosition.y);
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.KinematicBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 3;
        
        this.createCollisions(groundBody);
    }
    
    protected abstract void createCollisions(Body groundBody);
    /*{
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setRadius(250 * P2M * this.scale);
        //ground.setAsBox(382 * P2M * this.scale, 117 / 8f * P2M * this.scale, new Vector2(0, 117 * 3/ 8f * P2M * this.scale), 0);
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
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.physicBody = groundBody;
        this.physicBody.setTransform(this.getPositionBody(), this.angle);
        
        if(this.rotationSpeed > 0){
            this.physicBody.setAngularVelocity(this.rotationSpeed);

            // ActionFixture
            Set<Fixture> setFixtures = new HashSet();

            ground.setAsBox(382 * P2M * this.scale, 117 / 8f * P2M * this.scale, new Vector2(0, (-117 * 3/ 8f + 3) * P2M * this.scale), 0);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.gearActionFixture = new GearActionFixture(setFixtures);
        }
    }*/
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        if(this.gearActionFixture != null){           
            this.gearActionFixture.applyAction(deltaTime, this);
        }
    }
    
    @Override
    public void ReinitPlatform(World world){
        this.setTransform(this.startPosition.x, this.startPosition.y, this.angle);
        this.physicBody.setAngularVelocity(this.rotationSpeed);
    }
    
    @Override
    public void dispose(){
        if(this.gearActionFixture != null){
            this.gearActionFixture.dispose(this.physicBody);
        }
        
        super.dispose();
    }
}
