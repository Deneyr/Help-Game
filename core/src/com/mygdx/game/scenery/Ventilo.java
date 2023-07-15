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
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.SolidObject2D;
import com.mygdx.game.WindActionFixture;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class Ventilo extends SolidObject2D{
    
    protected final String id = UUID.randomUUID().toString();
    
    private static final String VENTILOTEXT = "destroyable/spritemapVentillo_V4-01.png";
    
    protected static final float SCALE_X = 1f;
    protected static final float SCALE_Y = 1f;
    
    private WindActionFixture windActionFixture;
    
    private float strength;
    
    protected List<WindObject2D> wind;
    
    protected boolean isWorking;
    private boolean isIncrement;
    
    public Ventilo(World world, float posX, float posY, float angle, float strength, int windLength, boolean start){
        
        this.strength = strength;
        
        this.isWorking = start;
        this.isIncrement = true;
        
        // Part physic    
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.KinematicBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 0;
        
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setAsBox((152/8f + 7) * P2M * SCALE_X, (142)/2 * P2M * SCALE_Y, new Vector2(-152/8f * P2M * SCALE_X, 0), 0);
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
        
        ground.setAsBox((152/8f + 15) * P2M * SCALE_X, (152/2f + 20)/2 * P2M * SCALE_Y, new Vector2(152/8f * P2M * SCALE_X, 0), 0);
        fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.physicBody = groundBody;
        
        // ActionFixture
        Set<Fixture> setFixtures = new HashSet();
        
        ground.setAsBox((152f/2) * windLength * P2M * SCALE_X, (152f + 10)/2 * P2M * SCALE_Y, new Vector2(152f * (0.4f + windLength/2f) * P2M * SCALE_X, 0), 0);
        fix = groundBody.createFixture(fixtureDef); 
        setFixtures.add(fix);
        this.windActionFixture = new WindActionFixture(setFixtures, this.strength);
        
        this.wind = new ArrayList<WindObject2D>();
        for(int i = 1; i <= windLength; i++){
            this.wind.add(new WindObject2D( this.physicBody, world, posX + (152 * i * SCALE_X), posY, 152 * i * SCALE_X)); 
        }
        
        // Transform
        if(angle != 0){
            this.physicBody.setTransform(this.physicBody.getPosition(), angle);
        }
        
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(VENTILOTEXT, this);
        
        if(this.texture != null){
            
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 152, 152);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, 3);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 2);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
            
            // Child
            for(WindObject2D wind : this.wind){
                wind.assignTextures(this.texture);
            }
            
            this.InitAnimation();
        }
    }
    
    protected void InitAnimation(){
        if(this.isWorking){
            this.changeAnimation(1, false); 
            
            for(WindObject2D windUnit : this.wind){
                windUnit.changeAnimation(0, false);
            }
        }else{
            this.changeAnimation(0, false);
            
            for(WindObject2D windUnit : this.wind){
                windUnit.changeAnimation(-1, false);
            }
        }
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
        
        if(this.isWorking){
            this.updateObject2D(deltaTime);
            
            this.windActionFixture.applyAction(deltaTime, this);
            
            this.notifyGameEventListener(GameEventListener.EventType.LOOP, "ventiloWind" + ":" + this.id, this.getPositionBody());
        }
        
        for(WindObject2D wind : this.wind){
            wind.updateLogic(deltaTime);
        }
    }
    
    protected void updateObject2D(float deltaTime){
        float sign = 1;
        if(this.isIncrement == false){
            sign = -1;
        }

        this.scale = this.scale + sign * deltaTime * 1.4f;

        float upperBound = 1.02f;
        float lowerBound = 0.98f;
        
        if(this.isIncrement){
            if(this.scale > upperBound){
                this.isIncrement = false;
                this.scale = upperBound;
            }
        }else{
            if(this.scale < lowerBound){
                this.isIncrement = true;
                this.scale = lowerBound;
            }
        }
    }
    
    @Override
    public void removeBody(World world){
        
        for(Object2D obj : this.wind){
            obj.removeBody(world);
        }
        super.removeBody(world);
    }
    
    public class WindObject2D extends Object2D{
        
        private Joint joint;
        
        public WindObject2D(Body ownerBody, World world, float posX, float posY, float offsetX){
            
            this.priority = 1;          
            
            // Part physic
            BodyDef groundBodyDef = new BodyDef();    
            groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.DynamicBody);
       
            
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(152 * P2M * SCALE_X, 152 * P2M * SCALE_Y, new Vector2(0, 0), 0);
            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = ground;
            fixtureDef.density = 1f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef); 
            fix.setSensor(true);
            fix.setUserData(this);
            
            this.physicBody = groundBody;
            
            // Create joint
            
            WeldJointDef jointDef = new WeldJointDef ();
            jointDef.bodyA = ownerBody;
            jointDef.bodyB = this.physicBody;
            jointDef.localAnchorA.set(new Vector2(offsetX * P2M, 0));
            jointDef.localAnchorB.set(new Vector2(0, 0));
            jointDef.collideConnected = false;
        
            this.joint = world.createJoint(jointDef);
           
        }
        
        public void assignTextures(Texture texture){

            if(texture != null){
                 TextureRegion[][] tmp = TextureRegion.split(texture, 152, 152);
                
                this.listAnimations.add(new Animation(0.1f, new Array(tmp[1])));
                this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);

                this.changeAnimation(0, false);
            }
        }
        
        @Override
        public void removeBody(World world){

            world.destroyJoint(this.joint);
            
            super.removeBody(world);
        }
    }
}
