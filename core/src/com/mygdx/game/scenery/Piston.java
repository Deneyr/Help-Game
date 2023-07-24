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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.DamageActionFixture;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixture;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class Piston extends ObstacleObject2D{
    private static final String[] OBJECT_ARRAY = {
        "factory/Help_Piston_Piston_Rod_40x160.png",
        "factory/Help_Piston_Piston_200x250.png"};
    
    private PistonHead pistonHead;
    
    private float scaleY;  
    private float height;
    
    public Piston(World world, float posX, float posY, float angle, float scaleY, float speed, float delay, float ratio){
        
        this.scale = 1;       
        this.texturePath = OBJECT_ARRAY[0];      
        this.side = 1;    
        this.indexObject = 0;
        this.scaleY = scaleY;
        this.height = 76 * P2M * this.scale * this.scaleY;
        
        this.pistonHead = new PistonHead(world, posX, posY, angle, this.height, speed, delay, ratio);
        
        // Part graphic
        this.assignTextures();
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
        groundBodyDef.angle = angle;
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create collisions (to override).
        this.createCollisions(groundBody);
        
        for(Fixture fixture: groundBody.getFixtureList()){
            fixture.setUserData(this);
            this.collisionFixture.add(fixture);
        }

        this.physicBody = groundBody;
        //this.physicBody.setLinearVelocity(new Vector2(0.5f, 0));           
    }

    @Override
    protected void createCollisions(Body groundBody) {
        // Create a polygon shape
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
        
        switch(this.indexObject){
            case 0:
                ground.setAsBox(20 * P2M * this.scale, this.height, new Vector2(0, 0), 0);
                
                Fixture fix = groundBody.createFixture(fixtureDef); 
                
                break;
        }
    }
    
    @Override
    public void assignTextures(){
        super.assignTextures();
        
        Texture pistonHeadText = TextureManager.getInstance().getTexture(OBJECT_ARRAY[1], this);
        
        if(this.texture != null
            && pistonHeadText != null){
            
            this.pistonHead.assignTextures(pistonHeadText);
        }
    }
      
    @Override
    public void updateLogic(float deltaTime){  
        super.updateLogic(deltaTime);
        
        this.pistonHead.updateLogic(deltaTime);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();

        sprite.setScale(sprite.getScaleX(), sprite.getScaleY() * this.scaleY);

        return sprite;
    }
    
    @Override
    public void dispose(){       
        this.pistonHead.dispose();
        
        super.dispose();
    }
    
    @Override
    public void removeBody(World world){
        if(this.pistonHead != null){
            this.pistonHead.removeBody(world);
        }
        super.removeBody(world);
    }
    
    @Override
    public void ReinitPlatform(World world){
        Vector2 pistonPosition = this.getPositionBody();
        this.pistonHead.updatePistonHead(pistonPosition.x / P2M, pistonPosition.y / P2M, this.physicBody.getAngle());
        
        this.pistonHead.ReinitPlatform(world);
    }

    public class PistonHead extends SolidObject2D{ 
        
        private static final float SCALE_X = 1f;
        private static final float SCALE_Y = 1f;
        
        private Vector2 startPosition;
        
        private float angle;
        private float speed;
        private float height;
        private float pistonHeight;
        private float ratio;
        private float delay;
        
        private Vector2 direction;
        
        private DamageActionFixture damageActionFixture;
        private KinematicActionFixture kinematicActionFixture;
        
        private StateNode pistonStateNode;
    
        public PistonHead(World world, float posX, float posY, float angle, float height, float speed, float delay, float ratio){

            this.angle = angle;
            this.speed = speed;
            this.height = height;
            this.pistonHeight = 112 * P2M * this.scale;
            this.ratio = ratio;
            this.delay = delay;
            
            this.startPosition = new Vector2(posX * P2M, posY * P2M); 
            
            Vector2 directionNormalized = (new Vector2(0, 1)).rotateRad(angle);  
            this.startPosition = this.startPosition.add((new Vector2(directionNormalized)).scl(height));
                       
            this.direction = (new Vector2(directionNormalized)).scl(this.speed);

            // Part physic
            BodyDef groundBodyDef = new BodyDef();  
            // Set its world position
            groundBodyDef.position.set((new Vector2(this.startPosition)).add(directionNormalized.scl(this.ratio * this.pistonHeight)));

            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);  

            groundBody.setType(BodyDef.BodyType.KinematicBody);

            this.collisionFixture = new ArrayList<Fixture>();

            this.priority = 3;

            // Create a polygon shape
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(82f * P2M * this.scale, 112f * P2M * this.scale, new Vector2(0, 0), 0);
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

            // Crush damage fictures (one top, one bottom)
            ground = new PolygonShape();
            ground.setAsBox(72f * P2M * this.scale, 2f * P2M * this.scale, new Vector2(0, 98 * P2M * this.scale), 0);
            fixtureDef.shape = ground;
            Set<Fixture> setFixtures = new HashSet();
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            ground.setAsBox(72f * P2M * this.scale, 2f * P2M * this.scale, new Vector2(0, -98 * P2M * this.scale), 0);
            fixtureDef.shape = ground;
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.damageActionFixture = new DamageActionFixture(setFixtures, 4);
            
            // Kinematic action fixtures (one left, one right)
            ground = new PolygonShape();
            ground.setAsBox(5f * P2M * this.scale, 110f * P2M * this.scale, new Vector2(-75 * P2M * this.scale, 0), 0);
            fixtureDef.shape = ground;
            setFixtures = new HashSet();
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            ground.setAsBox(5f * P2M * this.scale, 110f * P2M * this.scale, new Vector2(75 * P2M * this.scale, 0), 0);
            fixtureDef.shape = ground;
            fix = groundBody.createFixture(fixtureDef); 
            setFixtures.add(fix);
            this.kinematicActionFixture = new KinematicActionFixture(setFixtures);
            
            this.physicBody = groundBody;
            this.physicBody.setTransform(this.getPositionBody(), this.angle);
            
            this.physicBody.setLinearVelocity(this.direction);
            
            this.pistonStateNode = new StateNode(Piston.PistonState.MOVE);
        }
        
        public void assignTextures(Texture texture){
            if(texture != null){
                this.texture = texture;
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

            this.damageActionFixture.applyAction(deltaTime, this);
            this.kinematicActionFixture.applyAction(deltaTime, this);
            
            StateNode nextStateNode = this.pistonStateNode.updateStateNode(deltaTime, this);
            
            if(nextStateNode != null){
                this.pistonStateNode = nextStateNode;
            }
        }
        
        public void updatePistonHead(float posX, float posY, float angle){
            this.angle = angle;
            
            this.startPosition = new Vector2(posX * P2M, posY * P2M); 
            
            Vector2 directionNormalized = (new Vector2(0, 1)).rotateRad(angle);  
            this.startPosition = this.startPosition.add((new Vector2(directionNormalized)).scl(height));
            
            Vector2 newPosition = (new Vector2(this.startPosition)).add(directionNormalized.scl(this.ratio * this.pistonHeight));
            this.physicBody.setTransform(newPosition, this.angle);
        }
        
        @Override
        public void ReinitPlatform(World world){
            this.direction = (new Vector2(0, 1)).rotateRad(this.angle);
            Vector2 directionNormalized = new Vector2(this.direction);            
            this.direction = this.direction.scl(this.speed);

            Vector2 initPosition = (new Vector2(this.startPosition)).add(directionNormalized.scl(this.ratio * this.pistonHeight));

            this.setTransform(initPosition.x, initPosition.y, this.angle);
            this.physicBody.setLinearVelocity(this.direction);
            
            this.pistonStateNode = new StateNode(Piston.PistonState.MOVE);
        }
        
        protected class StateNode{
            private Piston.PistonState stateNode;
            
            private float currentStopTime;

            public StateNode(Piston.PistonState state){
                this.stateNode = state;
                
                this.currentStopTime = 0;
            }

            // Part nextNode
            public StateNode updateStateNode(float deltaTime, PistonHead parent){
                switch(this.getStateNode()){
                    case MOVE:
                        return this.updateNodeMove(deltaTime, parent);
                    case STOP:
                        return this.updateNodeStop(deltaTime, parent);

                } 
                return null;
            }

            private StateNode updateNodeStop(float deltaTime, PistonHead parent){

                if(parent.physicBody != null){
                    this.currentStopTime += deltaTime;

                    if(this.currentStopTime > parent.delay){
                        parent.physicBody.setLinearVelocity(parent.direction);

                        return new StateNode(Piston.PistonState.MOVE);
                    }
                }
                
                return null;
            }

            private StateNode updateNodeMove(float deltaTime, PistonHead parent){

                if(parent.physicBody != null){
                    
                    Vector2 displacementDirection = parent.getPositionBody().sub(parent.startPosition);
                    float sameDirection = displacementDirection.dot(parent.direction);

                    if(sameDirection > 1){         
                        float length2 = displacementDirection.len2();
                        if(length2 > parent.pistonHeight * parent.pistonHeight){
                            Vector2 finalPosition = (new Vector2(parent.startPosition)).add((new Vector2(parent.direction)).setLength(parent.pistonHeight));                     
                            parent.setTransform(finalPosition.x, finalPosition.y, parent.angle);
                            
                            parent.direction = parent.direction.scl(-1);
                            parent.physicBody.setLinearVelocity(Vector2.Zero);
                            
                            return new StateNode(Piston.PistonState.STOP);
                        }
                    } 
                }
                
                return null;
            }


            /**
             * @return the stateNode
             */
            public Piston.PistonState getStateNode() {
                return stateNode;
            }
        }
    
    }
    
    protected enum PistonState{
        MOVE,
        STOP
    }
}