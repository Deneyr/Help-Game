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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.DamageActionFixture;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixture;
import java.util.HashSet;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class AutoSpikeMovingPlatform extends ADamagePlatformObject{
    private static final String SPIKEPLATFORMTEXT = "factory/Help_Props_480x70_PlateformePique.png";
    private static final String FANTEXT = "factory/Help_Props_50x20_PlateformeVolantePales.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private int hitDamage;
    
    private float offsetTime;
    private float currentOffsetTime;
    
    private FanObject2D fan;
    
    public AutoSpikeMovingPlatform(World world, float posX, float posY, float angle, float scale, float directionAngle, float speed, float ratio, float maxRadius, float offsetTime, float appearingPeriod, float cooldownPeriod, int hitDamage) {
        super(world, posX, posY, angle, scale, directionAngle, speed, ratio, maxRadius, appearingPeriod, cooldownPeriod);
        
        this.offsetTime = offsetTime;
        this.currentOffsetTime = 0;
        
        this.hitDamage = hitDamage;
        this.damageFixture.setDamageInflicted(this.hitDamage);
        
        this.fan = new FanObject2D(this.physicBody, world, posX, posY, -28);
        
        this.assignTextures();
    }
    
    @Override
    protected void createCollisions(Body groundBody){
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(75 * SCALE_X * P2M * this.scale, 8 * SCALE_Y * P2M * this.scale, new Vector2(0, -12 * SCALE_Y * P2M * this.scale), 0);
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
        
        // ActionFixture
        Set<Fixture> setFixtures = new HashSet();

        ground.setAsBox(75 * SCALE_X * P2M * this.scale, 8 * SCALE_Y * P2M * this.scale, new Vector2(0, -10 * SCALE_Y * P2M * this.scale), 0);
        fix = groundBody.createFixture(fixtureDef); 
        setFixtures.add(fix);
        this.damageFixture = new DamageActionFixture(setFixtures, this.hitDamage);
        
        //if(this.speed > 0){
            this.physicBody.setLinearVelocity(this.direction);

            // ActionFixture
            setFixtures = new HashSet();

            ground.setAsBox(75 * SCALE_X * P2M * this.scale, 8 * SCALE_Y * P2M * this.scale, new Vector2(0, -10 * SCALE_Y * P2M * this.scale), 0);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.kinematicActionFixture = new KinematicActionFixture(setFixtures);
        //}
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(SPIKEPLATFORMTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 160, 70);
        
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, array.size - 1);
            this.listAnimations.add(new Animation(0, array, Animation.PlayMode.NORMAL));
            
            array = new Array<TextureRegion>(tmp[0]);
            array.removeIndex(0);
            array.removeRange(1, array.size - 1);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.NORMAL));
            
            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, array.size - 2);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.NORMAL));
            
            this.changeAnimation(0, false);
        }
        
        Texture fanText = TextureManager.getInstance().getTexture(FANTEXT, this);
        
        if(fanText != null){
            this.fan.assignTextures(fanText);
        }
    }
    
    @Override
    protected boolean IsPlatformActivated(float deltaTime){
        if(this.currentOffsetTime >= this.offsetTime){
            return true;
        }else{
            this.currentOffsetTime += deltaTime;
            return false;
        }
    }
    
    @Override
    protected void onAppearingFirstState(float deltaTime) {
        this.changeAnimation(1, false);
        
        this.notifyGameEventListener(GameEventListener.EventType.ACTION, "setPikeTrap", new Vector2(this.getPositionBody()));
    }

    @Override
    protected void onAppearingSecondState(float deltaTime) {
        this.changeAnimation(2, false);
        
        this.notifyGameEventListener(GameEventListener.EventType.ACTION, "pikeTrap", new Vector2(this.getPositionBody()));
    }

    @Override
    protected void onCooldownState(float deltaTime) {
        this.changeAnimation(0, false);
    }
    
    @Override
    protected void onFreeState(float deltaTime) {
        
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
    
    @Override
    public void ReinitPlatform(World world){
        super.ReinitPlatform(world);
        
        this.currentOffsetTime = 0;
        
        this.platformState = ADamagePlatformObject.BPtlatformState.FREE;
    }
}
