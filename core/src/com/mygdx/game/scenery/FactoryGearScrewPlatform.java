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
public class FactoryGearScrewPlatform extends AGearObject {
    private static final String[] OBSTACLETESTBLOCK_ARRAY = {
    "factory/Help_Props_VisBlanche_120x120.png",
    "factory/Help_Props_VisNoir_120x120.png"};  
    
    protected int testBlockIndex;
    
    protected static final float SCALE_X = 1f;
    protected static final float SCALE_Y = 1f;
    
    public FactoryGearScrewPlatform(World world, float posX, float posY, float angle, float scale, float rotationSpeed, int testBlockIndex) {
        super(world, posX, posY, angle, scale, rotationSpeed);
        
        this.testBlockIndex = testBlockIndex;
        
        this.assignTextures();
    }
    
    @Override
    protected void createCollisions(Body groundBody){
        // Create a polygon shape
        CircleShape circle = new CircleShape();
        circle.setRadius(50 * this.scale * P2M * SCALE_X);
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
        
        if(this.rotationSpeed != 0){
            this.physicBody.setAngularVelocity(this.rotationSpeed);

            // ActionFixture
            Set<Fixture> setFixtures = new HashSet();
            circle.setRadius(55 * this.scale * P2M * SCALE_X);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.gearActionFixture = new GearActionFixture(setFixtures);
        }
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(OBSTACLETESTBLOCK_ARRAY[this.testBlockIndex], this);        
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    } 
}
