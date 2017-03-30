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
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ActionFixtures;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixtures;
import com.mygdx.game.Object2D;
import com.mygdx.game.SolidObject2D;
import com.mygdx.game.WindActionFixture;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author françois
 */
public class Ventilo extends SolidObject2D{
    private static final Texture VENTILOTEXT = new Texture("destroyable" + File.separator + "spritemapVentilloV3-01.png");
    
    protected static final float SCALE_X = 1f;
    protected static final float SCALE_Y = 1f;
    
    private WindActionFixture windActionFixture;
    
    private float strength;
    
    protected List<WindObject2D> wind = new ArrayList<WindObject2D>();
    
    protected boolean isWorking;
    
    public Ventilo(World world, float posX, float posY, float strength, float angle, boolean start){
        
        this.strength = strength;
        
        // Part graphic
        this.texture = VENTILOTEXT;

        TextureRegion[][] tmp = TextureRegion.split(this.texture, 152, 152);
        // walk folded
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
        
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 2);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
        
        this.changeAnimation(1, false);
        
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
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = ground;
        fixtureDef2.density = 1f; 
        fixtureDef2.friction = 0.05f;
        fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef2); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        ground.setAsBox((152/8f + 15) * P2M * SCALE_X, (152/2f + 20)/2 * P2M * SCALE_Y, new Vector2(152/8f * P2M * SCALE_X, 0), 0);
        fix = groundBody.createFixture(fixtureDef2); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        this.physicBody = groundBody;
        
        // ActionFixture
        Set<Fixture> setFixtures = new HashSet();
        
        ground.setAsBox(152 * P2M * SCALE_X, (152f + 20)/2 * P2M * SCALE_Y, new Vector2((152) * P2M * SCALE_X, 0), 0);
        fix = groundBody.createFixture(fixtureDef2); 
        setFixtures.add(fix);
        this.windActionFixture = new WindActionFixture(setFixtures, this.strength);
        
        //Child
        array = new Array<TextureRegion>(tmp[1]);
        this.wind.add(new WindObject2D( this.physicBody, world, array, posX + (152 * SCALE_X), posY, 152 * SCALE_X));
        this.wind.add(new WindObject2D( this.physicBody, world, array, posX + (152*2 * SCALE_X), posY, 152 * 2 * SCALE_X));
        
        // Transform
        if(angle != 0){
            this.physicBody.setTransform(this.physicBody.getPosition(), angle);
        }
        
        this.isWorking = true;
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(SCALE_X, SCALE_Y);
        return sprite;
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        if(this.isWorking){
            this.windActionFixture.applyAction(deltaTime, this);
        }
        
        for(WindObject2D wind : this.wind){
            wind.updateLogic(deltaTime);
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
        public WindObject2D(Body ownerBody, World world, Array<TextureRegion> array, float posX, float posY, float offsetX){
            this.listAnimations.add(new Animation(0.1f, new Array(array)));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            
            this.changeAnimation(0, false);
            
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
            FixtureDef fixtureDef2 = new FixtureDef();
            fixtureDef2.shape = ground;
            fixtureDef2.density = 1f; 
            fixtureDef2.friction = 0.05f;
            fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef2); 
            fix.setSensor(true);
            fix.setUserData(this);
            
            this.physicBody = groundBody;
            
            WeldJointDef jointDef = new WeldJointDef ();
            jointDef.bodyA = ownerBody;
            jointDef.bodyB = this.physicBody;
            jointDef.localAnchorA.set(new Vector2(offsetX * P2M ,0));
            jointDef.localAnchorB.set(new Vector2(0 ,0));
            jointDef.collideConnected = false;
        
            world.createJoint(jointDef);
           
        }
        
    }
}
