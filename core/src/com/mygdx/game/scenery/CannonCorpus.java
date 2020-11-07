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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ressourcesmanagers.TextureManager;
import triggered.CannonBallTriggeredObject2D;

/**
 *
 * @author françois
 */
public abstract class CannonCorpus extends SolidObject2D{
    
    private static final String CANNONCORPUSTEXT = "CannonCorpus.png";
    private static final String CANNONTEXT = "Cannon.png";
    
    protected static final float SCALE_X = 1f;
    protected static final float SCALE_Y = 1f;
    
    protected Cannon cannon;
    
    
    public CannonCorpus(World world, Object2D target, float posX, float posY, float angle){
        
        // Part physic   
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.KinematicBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create a polygon shape
        CircleShape circle = new CircleShape();
        circle.setRadius(60 * P2M);
        circle.setPosition(new Vector2(0, -30 * P2M));
        
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
        
        // Transform
        if(angle != 0){
            this.physicBody.setTransform(this.physicBody.getPosition(), angle);
        }  
    }   
         
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(CANNONCORPUSTEXT, this);
        Texture cannon = TextureManager.getInstance().getTexture(CANNONTEXT, this);
        
        if(this.texture != null
                && cannon != null){
            
            this.cannon.assignTextures(cannon);
        }
    }
    
    // listener
    @Override
    public void addGameEventListener(GameEventListener listener){
        super.addGameEventListener(listener);
        
        if(listener != null){
            this.cannon.addGameEventListener(listener);
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
    @Override
    public void removeBody(World world){
        
        this.cannon.removeBody(world);
        
        super.removeBody(world);
    }
    
    @Override
    public void updateLogic(float deltaTime){  
        super.updateLogic(deltaTime);
        
        this.cannon.updateLogic(deltaTime);
    }
    
    @Override
    public void addObject2DStateListener(Object2DStateListener object2DStateListener){
        this.cannon.addObject2DStateListener(object2DStateListener);
    }
    
    public class Cannon extends Character2D{

        private Joint joint;
        
        private int angularDir = -1;
        
        protected Object2D target;

        private StateNode currentStateNode;      
        protected Set<CannonInfluence> influences = new HashSet<CannonInfluence>();
        
        private boolean canAttack;
        
        protected float attackCooldown;
        
        public Cannon(Body ownerBody, Object2D target, World world, float posX, float posY) {
            super(100);
            
            this.target = target;
            
            this.isInvulnerable = true;
            this.hasLifeBar = false;
            
            this.currentStateNode = new StateNode(CannonState.STOP);
            
            this.canAttack = true;
            
            this.attackCooldown = 2;
            
            // Part physic
            this.collisionFixture = new ArrayList<Fixture>();
            BodyDef groundBodyDef = new BodyDef();    
            groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.DynamicBody);
       
            this.priority = 0;
            
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(55 * P2M * SCALE_X, 25 * P2M * SCALE_Y, new Vector2(0, 0), 0);
            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef = new FixtureDef();
            
            this.setCollisionFilterMask(fixtureDef, false);
            
            fixtureDef.shape = ground;
            fixtureDef.density = 2f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef); 
            fix.setUserData(this);
            this.collisionFixture.add(fix);
            
            this.physicBody = groundBody;
            
            // joint creation
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = ownerBody;
            jointDef.bodyB = this.physicBody;
            jointDef.localAnchorA.set(new Vector2(0,  0));
            jointDef.localAnchorB.set(new Vector2(50 * P2M, 0));
            jointDef.collideConnected = false;
            
            this.physicBody.setTransform(this.getPositionBody(), (float) (ownerBody.getAngle() - Math.PI / 4));
            
            this.joint = world.createJoint(jointDef);
        }
        
        public void assignTextures(Texture texture){

            if(texture != null){
                this.texture = texture;
                
                TextureRegion[][] tmp = TextureRegion.split(this.texture, 152, 153);

                Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
                array.removeRange(1, 4);
                this.listAnimations.add(new Animation(0.2f, array));
                array = new Array<TextureRegion>(tmp[0]);
                this.listAnimations.add(new Animation(0.1f, array));        
                
                this.changeAnimation(0, false);
            }
        }
    
        @Override
        public void removeBody(World world){

            world.destroyJoint(this.joint);
            
            super.removeBody(world);
        }
        
        @Override
        public void setInfluenceList(List<String> lInfluences){
            this.influences.clear();
            for(String influence : lInfluences){
                influence = influence.toLowerCase();
                if(influence.equals("right")){
                    this.influences.add(CannonInfluence.GO_RIGHT);
                }else if(influence.equals("left")){
                    this.influences.add(CannonInfluence.GO_LEFT);
                }else if(influence.equals("attack")){
                    this.influences.add(CannonInfluence.ATTACK);
                }
            }
        }

        @Override
        public void updateLogic(float deltaTime){       
            super.updateLogic(deltaTime);
        
            this.createInfluences();
            this.influences2Actions(deltaTime);
            
            this.influences.clear();
        }
        
        protected void createInfluences(){
            
            if(this.target == null){
                return;
            }
            
            if(this.target.getPositionBody().sub(this.getPositionBody()).len() < 400 * P2M){
                Vector2 dirCannon = new Vector2(-1, 0).rotate((float) (this.physicBody.getAngle() * 180 / Math.PI));
                Vector2 dirTarget = new Vector2(Cannon.this.target.getPositionBody().sub(Cannon.this.getPositionBody())).nor();
                if(dirTarget.crs(dirCannon) > 0){
                    this.influences.add(CannonInfluence.GO_RIGHT);
                }else{
                    this.influences.add(CannonInfluence.GO_LEFT);
                }
                
                if(Math.abs(dirTarget.crs(dirCannon)) < 0.1f && dirTarget.dot(dirCannon) > 0){
                    this.influences.add(CannonInfluence.ATTACK);
                }
            }
        }
        
        protected void influences2Actions(float deltaTime){
            
            StateNode prevNode = this.currentStateNode;
            StateNode nextNode = this.currentStateNode.getNextStateNode();
            if(nextNode != null){
                this.currentStateNode = nextNode;
            }
            
            if(this.currentStateNode.getCurrentAnimation() != this.currentAnimation){
                this.changeAnimation(this.currentStateNode.getCurrentAnimation(), false);
            }
            
            this.updateAttack(prevNode, nextNode);
            
            this.currentStateNode.updatePhysic(deltaTime);
        }
        
        protected void updateAttack(StateNode prevNode, StateNode nextNode){
            if(this.lifeState == LifeState.DEAD){
                return;
            }

            if(this.canAttack
                    && prevNode.getStateNode() != CannonState.ATTACK 
                    && (nextNode != null && nextNode.getStateNode() == CannonState.ATTACK)){
                this.canAttack = false;
                Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            Vector2 dirBall = new Vector2(-1, 0).rotate((float) (Cannon.this.physicBody.getAngle() * 180 / Math.PI));
                            
                            Cannon.this.notifyObject2D2CreateListener(CannonBallTriggeredObject2D.class, Cannon.this.getPositionBody().add(dirBall.scl(Cannon.this.texture.getWidth() / 10.5f * P2M)).scl(1 / P2M), dirBall.scl(160 * P2M));
                            
                            Cannon.this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "cannon", Cannon.this.getPositionBody());
                        }
                        
                }, 0.5f);
                
                Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            Cannon.this.canAttack = true;
                        }
                        
                }, this.attackCooldown);
            }
        }
        
        @Override
        public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){

            float sign = -Math.signum((new Vector2(1f, 0f).dot(bounceVector)));

            Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);

            super.applyBounce(bounceVector, bounceOwner, ptApplication.add(this.physicBody.getPosition()));
        }
        
        protected class StateNode{
            private CannonState stateNode;

            public StateNode(CannonState state){
                this.stateNode = state;
            }

            // Part nextNode
            public StateNode getNextStateNode(){

                switch(this.getStateNode()){
                    case STOP:
                        return getNextNodeStop();
                    case MOVE:
                        return getNextNodeMove();
                    case ATTACK:
                        return getNextNodeAttack();

                } 
                return null;
            }

            private StateNode getNextNodeStop(){
                
                Iterator<CannonInfluence> it = Cannon.this.influences.iterator();
            
                while(it.hasNext()){
                    CannonInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                        case GO_LEFT :
                            return new StateNode(CannonState.MOVE);
                        case ATTACK :
                            if(Cannon.this.canAttack){
                                return new StateNode(CannonState.ATTACK);
                            }
                            
                    }
                }
                
                return null;
            }

            private StateNode getNextNodeAttack(){
                if(Cannon.this.isCurrentAnimationOver()){
                    return new StateNode(CannonState.STOP);
                }
                return null;
            }

            private StateNode getNextNodeMove(){

                Iterator<CannonInfluence> it = Cannon.this.influences.iterator();
            
                boolean isStillMoving = false;
                
                while(it.hasNext()){
                    CannonInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                        case GO_LEFT :
                            isStillMoving = true;
                            break;
                        case ATTACK :
                            if(Cannon.this.canAttack){
                                return new StateNode(CannonState.ATTACK);
                            }
                            
                    }
                }
                
                if(isStillMoving){
                    return null;
                }
                return new StateNode(CannonState.STOP);
            }

            // Part animation 
            public int getCurrentAnimation(){

                switch(this.getStateNode()){
                    case ATTACK:
                        return getAnimationAttack();

                } 
                return 0;
            }


            private int getAnimationAttack(){
                return 1;
            }

            // Part physic
            public void updatePhysic(float deltaTime){
                Iterator<CannonInfluence> it = Cannon.this.influences.iterator();
            
                while(it.hasNext()){
                    CannonInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                            Cannon.this.side = SideCharacter.RIGHT;
                            break;
                        case GO_LEFT :
                            Cannon.this.side = SideCharacter.LEFT;
                            break;
                            
                    }
                }
                
                switch(this.getStateNode()){
                    
                    case ATTACK:
                        if(!Cannon.this.physicBody.isFixedRotation()){
                            Cannon.this.physicBody.setFixedRotation(true);
                        }
                        break;
                    case MOVE:
                        if(Cannon.this.physicBody.isFixedRotation()){
                            Cannon.this.physicBody.setFixedRotation(false);
                            Cannon.this.physicBody.setAwake(true);
                        }
                        this.updateCannonMove(deltaTime);
                        break;
                    case STOP:
                        if(!Cannon.this.physicBody.isFixedRotation()){
                            Cannon.this.physicBody.setFixedRotation(true);
                        }
                        break;

                } 
            }
            
            
            private void updateCannonMove(float deltaTime){
                if(Cannon.this.side == SideCharacter.LEFT){
                    if(Cannon.this.physicBody.getAngle() < -(Math.PI / 2 - 0.2)){
                        Cannon.this.physicBody.applyAngularImpulse((float) (10000 * deltaTime * Math.PI / 180), true);
                    }

                    if(Cannon.this.physicBody.getAngle() > 0){
                        Cannon.this.angularDir = -1;
                    }
                }else{
                    if(Cannon.this.physicBody.getAngle() > -(Math.PI / 2 + 0.2)){
                        Cannon.this.physicBody.applyAngularImpulse((float) (-10000 * deltaTime * Math.PI / 180), true);
                    }

                    if(Cannon.this.physicBody.getAngle() < -Math.PI){
                        Cannon.this.angularDir = 1;
                    }
                }

            }

            /**
             * @return the stateNode
             */
            public CannonState getStateNode() {
                return stateNode;
            }
        }
        
    }
    
    public class AutoCannon extends Cannon{
        
        private int cannonDirTargeted;
        
        private boolean isMovable;
        
        private float parentAngle;
        
        public AutoCannon(Body ownerBody, Object2D target, World world, float posX, float posY, boolean isMovable, int startDirTargeted, float parentAngle) {
            super(ownerBody, target, world, posX, posY);
            
            if(isMovable){
                this.attackCooldown = 1f;               
            }else{
                this.attackCooldown = 2f;
            }
            
            this.cannonDirTargeted = startDirTargeted;
            
            this.isMovable = isMovable;
            
            this.isInvulnerable = false;
            
            this.parentAngle = (float) (parentAngle * 180 / Math.PI);
        }
        
        public AutoCannon(Body ownerBody, Object2D target, World world, float posX, float posY) {
            super(ownerBody, target, world, posX, posY);
            
            this.attackCooldown = 0.7f;
            
            this.cannonDirTargeted = 0;
            
            this.isMovable = true;
            
            this.isInvulnerable = false;
            
            this.parentAngle = 0;
        }
        
        @Override
        protected void createInfluences(){
            float angleTargeted = this.parentAngle - 90;
            float angleCannon = (float) (this.physicBody.getAngle() * 180 / Math.PI);
            int nextDirTargeted = this.cannonDirTargeted;
            switch(this.cannonDirTargeted){
                case -1:
                    angleTargeted = this.parentAngle;
                    break;
                case 0:
                    angleTargeted = this.parentAngle - 90; 
                    break;
                case 2:
                    angleTargeted = this.parentAngle - 90; 
                    break;
                case 1:
                    angleTargeted = this.parentAngle - 180;
                    break;
            }
            
            if(this.isMovable){
                switch(this.cannonDirTargeted){
                    case -1:
                        nextDirTargeted = 0;
                        break;
                    case 0:
                        nextDirTargeted = 1;
                        break;
                    case 2:
                        nextDirTargeted = -1;
                        break;
                    case 1:
                        nextDirTargeted = 2;
                        break;
                }
            }
            
            float diffAngle = angleTargeted - angleCannon;
            diffAngle = (diffAngle + 180) % 360 - 180;
            if(Math.abs(angleCannon - angleTargeted) < 2){
                if(Math.abs(angleCannon - angleTargeted) > 0.5){
                    this.physicBody.setTransform(this.getPositionBody(), (float) (angleTargeted * Math.PI / 180));
                }
                
                this.influences.add(CannonInfluence.ATTACK);
                
                this.cannonDirTargeted = nextDirTargeted;
            }else if(diffAngle < 0){
                this.influences.add(CannonInfluence.GO_RIGHT);
            }else{
                this.influences.add(CannonInfluence.GO_LEFT);
            }
        }
        
        @Override
        public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){

            if(this.isInvulnerable){
                return false;
            }
            
            if(damageOwner instanceof Character2D){
                
                this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitPunch", new Vector2(this.getPositionBody()));

                Vector2 dirCannon = new Vector2(0, 1).rotate(this.parentAngle);
                float signHitDir = dirDamage.crs(dirCannon);
                
                switch(this.cannonDirTargeted){
                    case -1:
                        if(signHitDir > 0){
                            this.cannonDirTargeted = 0;
                        }
                        break;
                    case 0:
                    case 2:
                        if(signHitDir > 0){
                            this.cannonDirTargeted = 1;
                        }else{
                            this.cannonDirTargeted = -1;
                        }
                        break;
                    case 1:
                        if(signHitDir < 0){
                            this.cannonDirTargeted = 0;
                        }
                        break;
                }
                
                this.isInvulnerable = true;
            
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        AutoCannon.this.isInvulnerable = false;
                    }
                }, this.timeInvulnerabilitySec);
                
                return true;
            }

            return false;
        }
        
        @Override
        public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner, Vector2 ptApplication){
            return this.applyDamage(damage, dirDamage, damageOwner);
        }
        
    }
    
    protected enum CannonState{
        STOP,
        MOVE,
        ATTACK
    }

    protected enum CannonInfluence{
        GO_RIGHT,
        GO_LEFT,
        ATTACK
    }
}
