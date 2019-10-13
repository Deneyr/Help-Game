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
import com.mygdx.game.KinematicActionFixtures;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class Poutrelle extends SolidObject2D{
    private static final String POUTRELLETEXT = "urbanObj/Obstacle_Poutrelle.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private KinematicActionFixtures kinematicActionFixture;
    
    private float angle;
    private float speed;
    private float maxRadius; 
    private Vector2 direction;
    
    private Vector2 startPosition;
    
    public Poutrelle(World world, float posX, float posY, float angle, Vector2 direction, float speed, float maxRadius, float scale){
        
        this.scale = scale;
        this.angle = angle;
        this.speed = speed;
        this.maxRadius = maxRadius;
        
        if(direction != null 
                && (direction.x != 0 || direction.y != 0)){
            this.direction = direction.rotateRad(angle);
        }else{
            this.direction = new Vector2(1, 0).rotateRad(angle);
        }
        
        this.startPosition = new Vector2(posX * P2M, posY * P2M);
        
        // Part graphic
        this.assignTextures();
        
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
        ground.setAsBox(382 * P2M * SCALE_X * this.scale, 117 / 8f * P2M * SCALE_Y * this.scale, new Vector2(0, 117 * 3/ 8f * P2M * SCALE_Y * this.scale), 0);
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
        
        ground.setAsBox(382 * P2M * SCALE_X * this.scale, 117 / 8f * P2M * SCALE_Y * this.scale, new Vector2(0, -117 * 3/ 8f * P2M * SCALE_Y * this.scale), 0);
        fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.physicBody = groundBody;
        this.physicBody.setTransform(this.getPositionBody(), this.angle);
        
        if(this.speed > 0){
            this.physicBody.setLinearVelocity(new Vector2(this.direction).scl(this.speed));

            // ActionFixture
            Set<Fixture> setFixtures = new HashSet();

            ground.setAsBox(382 * P2M * SCALE_X * this.scale, 117 / 8f * P2M * SCALE_Y * this.scale, new Vector2(0, (-117 * 3/ 8f + 3) * P2M * SCALE_Y * this.scale), 0);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            ground.setAsBox(382 * P2M * SCALE_X * this.scale, 117 / 8f * P2M * SCALE_Y * this.scale, new Vector2(0, (117 * 3/ 8f + 3) * P2M * SCALE_Y * this.scale), 0);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.kinematicActionFixture = new KinematicActionFixtures(setFixtures);
        }
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(POUTRELLETEXT, this);
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
        
        if(this.speed > 0){
            
            Vector2 displacementVector = new Vector2(this.direction);

            Vector2 distVector = this.getPositionBody().sub(this.startPosition);
            if(distVector.len2() > this.maxRadius * this.maxRadius){
                
                float scalarSign = 1;
                if(distVector.dot(displacementVector) > 0){
                    scalarSign = -1;
                }

                this.physicBody.setLinearVelocity(displacementVector.scl(this.speed * scalarSign));
            }

            this.kinematicActionFixture.applyAction(deltaTime, this);
        }
    }
    
    @Override
    public void dispose(){
        
        if(this.kinematicActionFixture != null){
            this.kinematicActionFixture.dispose(this.physicBody);
        }
        
        super.dispose();
    }
}