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
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixture;
import java.util.HashSet;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class LowerCityCloudPlatform extends ABreakablePlatformObject {

    private static final String CLOUDPLATFORMTEXT = "lowerCity/Help_NuagesNoir_Anim.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    public LowerCityCloudPlatform(World world, float posX, float posY, float angle, float scale, float directionAngle, float speed, float ratio, float maxRadius, float disappearingPeriod, float cooldownPeriod) {
        super(world, posX, posY, angle, scale, directionAngle, speed, ratio, maxRadius, disappearingPeriod, cooldownPeriod);
        
        this.assignTextures();
    }
    
    @Override
    protected void createCollisions(Body groundBody){
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(115 * SCALE_X * P2M * this.scale, 15 * SCALE_Y * P2M * this.scale);
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
        
        //if(this.speed > 0){
            this.physicBody.setLinearVelocity(this.direction);

            // ActionFixture
            Set<Fixture> setFixtures = new HashSet();

            ground.setAsBox(110 * SCALE_X * P2M * this.scale, 15 * SCALE_Y * P2M * this.scale, new Vector2(0, 2f * SCALE_Y * P2M * this.scale), 0);
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.kinematicActionFixture = new KinematicActionFixture(setFixtures);
        //}
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(CLOUDPLATFORMTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 300, 80);
        
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, array.size - 1);
            this.listAnimations.add(new Animation(0, array, Animation.PlayMode.NORMAL));
            
            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(5, array.size - 1);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.LOOP));
            
            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 3);
            Array<TextureRegion> array2 = new Array<TextureRegion>(tmp[1]);
            array.addAll(array2);
            this.listAnimations.add(new Animation(0.05f, array, Animation.PlayMode.NORMAL));
            
            this.listAnimations.add(new Animation(0.05f, array, Animation.PlayMode.REVERSED));
            
            this.changeAnimation(0, false);
        }
    }

    @Override
    protected void onDisappearingState(float deltaTime) {
        this.changeAnimation(1, false);
    }

    @Override
    protected void onDisappearedState(float deltaTime) {
        this.changeAnimation(2, false);
    }

    @Override
    protected void onReapparingState(float deltaTime) {
        this.changeAnimation(3, false);
    }
    
    @Override
    protected void onFreeState(float deltaTime) {
        
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
}
