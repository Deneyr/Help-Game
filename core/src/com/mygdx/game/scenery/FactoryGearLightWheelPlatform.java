/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GearActionFixture;
import static com.mygdx.game.HelpGame.P2M;
import static com.mygdx.game.scenery.FactoryGearScrewPlatform.SCALE_X;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Deneyr
 */
public class FactoryGearLightWheelPlatform extends FactoryGearWheelPlatform {
    
    public FactoryGearLightWheelPlatform(World world, float posX, float posY, float angle, float scale, float rotationSpeed, int testBlockIndex) {
        super(world, posX, posY, angle, scale, rotationSpeed, testBlockIndex);
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
        fix.setSensor(true);
        this.collisionFixture.add(fix);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.physicBody = groundBody;
        this.physicBody.setTransform(this.getPositionBody(), this.angle);
        
        if(this.rotationSpeed != 0){
            this.physicBody.setAngularVelocity(this.rotationSpeed);
        }
        
        this.gearActionFixture = null;
    }
}
