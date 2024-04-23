/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixture;
import com.mygdx.game.Object2D;
import static com.mygdx.game.scenery.Ventilo.SCALE_X;
import java.util.HashSet;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class FactoryMovingPlatform extends APlatformObject{
    private static final String PLATFORMTEXT = "factory/Help_Props_160x70_PlateformeVolante.png";
    private static final String FANTEXT = "factory/Help_Props_50x20_PlateformeVolantePales.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private FanObject2D fan;
    
    public FactoryMovingPlatform(World world, float posX, float posY, float angle, float scale, float directionAngle, float speed, float ratio, float maxRadius) {
        super(world, posX, posY, angle, scale, directionAngle, speed, ratio, maxRadius);
        
        this.fan = new FanObject2D(this.physicBody, world, posX, posY, -28);
        
        this.assignTextures();
    }
    
    @Override
    protected void createCollisions(Body groundBody){
                // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(80 * SCALE_X * P2M * this.scale, 15 * SCALE_Y * P2M * this.scale, new Vector2(0, -17f * SCALE_Y * P2M * this.scale), 0);
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

            ground.setAsBox(75 * SCALE_X * P2M * this.scale, 15 * SCALE_Y * P2M * this.scale, new Vector2(0, -10f * SCALE_Y * P2M * this.scale), 0);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.kinematicActionFixture = new KinematicActionFixture(setFixtures);
        }
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(PLATFORMTEXT, this);
        
        Texture fanText = TextureManager.getInstance().getTexture(FANTEXT, this);
        
        if(fanText != null){
            this.fan.assignTextures(fanText);
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){  
        super.updateLogic(deltaTime);
        
        this.fan.updateLogic(deltaTime);
    }
    
    @Override
    public void removeBody(World world){      
        if(this.fan != null){
            this.fan.removeBody(world);
        }
        super.removeBody(world);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }  
}
