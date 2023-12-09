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
import com.mygdx.game.KinematicActionFixture;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Deneyr
 */
public abstract class APlatformObject extends SolidObject2D{
    protected KinematicActionFixture kinematicActionFixture;
    
    protected float angle;
    protected float directionAngle;
    protected float ratio;
    protected float speed;
    protected float maxRadius; 
    protected Vector2 direction;
    
    protected Vector2 startPosition;
    
    public APlatformObject(World world, float posX, float posY, float angle, float scale, float directionAngle, float speed, float ratio, float maxRadius){
        
        this.scale = scale;
        this.angle = angle;
        this.speed = speed;
        this.maxRadius = maxRadius;
        
        this.ratio = ratio;
        
        this.directionAngle = directionAngle;
        this.direction = new Vector2(1, 0).rotate(directionAngle);
        this.direction.scl(this.speed);
        
        this.startPosition = new Vector2(posX * P2M, posY * P2M);
        
        // Part physic       
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        Vector2 directionNormalized = new Vector2(this.direction);
        groundBodyDef.position.set((new Vector2(this.startPosition)).add(directionNormalized.scl(ratio * this.maxRadius)));
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.KinematicBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 3;
        
        this.createCollisions(groundBody);
    }
    
    protected void createCollisions(Body groundBody){
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(382 * P2M * this.scale, 117 / 8f * P2M * this.scale, new Vector2(0, 117 * 3/ 8f * P2M * this.scale), 0);
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
        
        if(this.speed > 0){
            this.physicBody.setLinearVelocity(this.direction);

            // ActionFixture
            Set<Fixture> setFixtures = new HashSet();

            ground.setAsBox(382 * P2M * this.scale, 117 / 8f * P2M * this.scale, new Vector2(0, (-117 * 3/ 8f + 3) * P2M * this.scale), 0);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.kinematicActionFixture = new KinematicActionFixture(setFixtures);
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        if(this.speed > 0){
            
            Vector2 displacementDirection = this.getPositionBody().sub(this.startPosition);
            float sameDirection = displacementDirection.dot(this.direction);
                
            if(sameDirection > 1){         
                float length2 = displacementDirection.len2();
                if(length2 > this.maxRadius * this.maxRadius){   
                    this.direction = this.direction.scl(-1);
                    this.physicBody.setLinearVelocity(this.direction);
                }
            }
            
            if(this.kinematicActionFixture != null){
                this.kinematicActionFixture.applyAction(deltaTime, this);
            }
        }
    }
    
    @Override
    public void ReinitPlatform(World world){
        this.direction = new Vector2(1, 0).rotate(this.directionAngle);
        Vector2 directionNormalized = new Vector2(this.direction);       
        this.direction.scl(this.speed);
        
        Vector2 initPosition = (new Vector2(this.startPosition)).add(directionNormalized.scl(this.ratio * this.maxRadius));
        
        this.setTransform(initPosition.x, initPosition.y, this.angle);
        this.physicBody.setLinearVelocity(this.direction);
    }
    
    @Override
    public void dispose(){      
        if(this.kinematicActionFixture != null){
            this.kinematicActionFixture.dispose(this.physicBody);
        }
        
        super.dispose();
    }
}
