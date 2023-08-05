/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GearActionFixture;
import static com.mygdx.game.HelpGame.P2M;
import java.util.HashSet;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class FactoryGearWheelPlatform extends AGearObject {
    
    private static final String[] OBSTACLETESTBLOCK_ARRAY = {
        "factory/Help_Props_RouageBlanc_260x260.png",
        "factory/Help_Props_RouageNoir3_260x260.png"};  
    
    protected int testBlockIndex;
    
    protected static final float SCALE_X = 1f;
    protected static final float SCALE_Y = 1f;
    
    public FactoryGearWheelPlatform(World world, float posX, float posY, float angle, float scale, float rotationSpeed, int testBlockIndex) {
        super(world, posX, posY, angle, scale, rotationSpeed);
        
        this.testBlockIndex = testBlockIndex;
        
        this.assignTextures();
    }
    
    @Override
    protected void createCollisions(Body groundBody){
        // Create a polygon shape
        CircleShape circle = new CircleShape();
        circle.setRadius(105 * this.scale * P2M * SCALE_X);
        circle.setPosition(new Vector2(0, 0));
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
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
        
        this.createTeethCollisions(groundBody);
        
        if(this.rotationSpeed != 0){
            this.physicBody.setAngularVelocity(this.rotationSpeed);

            // ActionFixture
            Set<Fixture> setFixtures = new HashSet();
            circle.setRadius(115 * this.scale * P2M * SCALE_X);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.gearActionFixture = new GearActionFixture(setFixtures);
        }
        
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(OBSTACLETESTBLOCK_ARRAY[this.testBlockIndex], this);        
    }
    
    protected void createTeethCollisions(Body groundBody){
        PolygonShape ground = new PolygonShape();
        
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body 
        
        int nbGearTeeth = 8;
        
        for(int i = 0; i < nbGearTeeth; i++){
            Vector2 offsetPosition = new Vector2(0, 110 * P2M * this.scale * SCALE_Y);
            
            float rotateAngle = 0;
            
            if(i > 0){
                rotateAngle = (float) ((i * Math.PI * 2) / nbGearTeeth);
                offsetPosition.rotateRad(rotateAngle);
            }
            
            ground.setAsBox(28 * P2M * this.scale * SCALE_X, 10 * P2M * this.scale * SCALE_Y, offsetPosition, rotateAngle);

            Fixture fix = groundBody.createFixture(fixtureDef);
            fix.setUserData(this);
            this.collisionFixture.add(fix);
        }
    }
}
