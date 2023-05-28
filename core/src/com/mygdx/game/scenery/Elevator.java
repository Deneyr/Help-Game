/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.KinematicActionFixture;
import com.mygdx.game.Object2D;
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
 * @author Deneyr
 */
public class Elevator extends Character2D{
    private static final String ELEVATORTEXT = "platform/Help_Props_Ascenseur_450x430.png";
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    private KinematicActionFixture kinematicActionFixture;
    
    private Vector2 velocityVector;
    
    private Vector2 startPosition;
    private Vector2 endPosition;
    
    private Elevator.StateNode currentStateNode;      
    protected Set<Elevator.ElevatorInfluence> influences = new HashSet<Elevator.ElevatorInfluence>();
    
    public Elevator(World world, float posX, float posY, float dirX, float dirY, float speed){
        super(100);
                        
        this.startPosition = new Vector2(posX * P2M, posY * P2M);
        this.endPosition =(new Vector2(this.startPosition)).add(new Vector2(dirX * P2M, dirY * P2M));
        
        this.velocityVector = (new Vector2(this.endPosition).sub(this.startPosition)).setLength(speed * P2M);
        
        // Part graphic
        this.assignTextures();
        
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
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(165 * P2M * SCALE_X * this.scale, 21 * P2M * SCALE_Y * this.scale, new Vector2(0, -175 * P2M * SCALE_Y * this.scale), 0);
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
        
        ground.setAsBox(165 * P2M * SCALE_X * this.scale, 21 * P2M * SCALE_Y * this.scale, new Vector2(0, 75 * P2M * SCALE_Y * this.scale), 0);
        fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.physicBody = groundBody;
        
        
        this.currentStateNode = new Elevator.StateNode(Elevator.ElevatorState.STOP);
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(ELEVATORTEXT, this);
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
        
        this.createInfluences();
        this.influences2Actions(deltaTime);
            
        this.influences.clear();

        
        /*if(this.speed > 0){
            
            Vector2 displacementVector = new Vector2(this.direction);

            Vector2 distVector = this.getPositionBody().sub(this.startPosition);
            if(distVector.len2() > this.maxRadius * this.maxRadius){
                
                float scalarSign = 1;
                if(distVector.dot(displacementVector) > 0){
                    scalarSign = -1;
                }

                this.physicBody.setLinearVelocity(displacementVector.scl(this.speed * scalarSign));
            }

            this.kinematicActionFixture.applyAction(deltaTime, this);
        }*/
    }
    
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        this.influences.clear();
        for(String influence : lInfluences){
            influence = influence.toLowerCase();
            if(influence.equals("goTop")){
                this.influences.add(Elevator.ElevatorInfluence.GO_TOP);
            }else if(influence.equals("goBot")){
                this.influences.add(Elevator.ElevatorInfluence.GO_BOT);
            }else if(influence.equals("stop")){
                this.influences.add(Elevator.ElevatorInfluence.STOP);
            }
        }
    }

        
    protected void createInfluences(){

        float distStart = this.getPositionBody().dst(this.startPosition);
        float distEnd = this.getPositionBody().dst(this.endPosition);
        
        if(distStart < 1){
            this.influences.add(ElevatorInfluence.GO_TOP);
        }else if(distEnd < 1){
            this.influences.add(ElevatorInfluence.GO_BOT);
        }else if(this.currentStateNode.stateNode == Elevator.ElevatorState.STOP){
            this.influences.add(ElevatorInfluence.GO_TOP);
        }
    }
        
    protected void influences2Actions(float deltaTime){

        StateNode prevNode = this.currentStateNode;
        StateNode nextNode = this.currentStateNode.getNextStateNode();
        if(nextNode != null){
            this.currentStateNode = nextNode;
        }

        this.currentStateNode.updatePhysic(deltaTime);
    }
        
        
    protected class StateNode{
        private Elevator.ElevatorState stateNode;

        public StateNode(Elevator.ElevatorState state){
            this.stateNode = state;
        }

        // Part nextNode
        public StateNode getNextStateNode(){

            switch(this.getStateNode()){
                case MOVING_TOP:
                    return getNextNodeMoveTop();
                case MOVING_BOT:
                    return getNextNodeMoveBot();
                case STOP:
                    return getNextNodeStop();

            } 
            return null;
        }

        private StateNode getNextNodeStop(){

            float distStart = Elevator.this.getPositionBody().dst(Elevator.this.startPosition);
            float distEnd = Elevator.this.getPositionBody().dst(Elevator.this.endPosition);
            
            Iterator<Elevator.ElevatorInfluence> it = Elevator.this.influences.iterator();

            while(it.hasNext()){
                Elevator.ElevatorInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case GO_TOP :
                        if(distEnd >= 1){
                            return new StateNode(Elevator.ElevatorState.MOVING_TOP);
                        }
                    case GO_BOT :
                        if(distStart >= 1){
                            return new StateNode(Elevator.ElevatorState.MOVING_BOT);        
                        }
                }
            }

            return null;
        }

        private StateNode getNextNodeMoveTop(){

            float distEnd = Elevator.this.getPositionBody().dst(Elevator.this.endPosition);
            
            if(distEnd < 1){
                return new StateNode(Elevator.ElevatorState.STOP);  
            }
            
            Iterator<Elevator.ElevatorInfluence> it = Elevator.this.influences.iterator();
            
            while(it.hasNext()){
                Elevator.ElevatorInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case GO_BOT :
                        return new StateNode(Elevator.ElevatorState.STOP);
                    case STOP :
                        return new StateNode(Elevator.ElevatorState.STOP);                       
                }
            }

            return null;
        }
        
        private StateNode getNextNodeMoveBot(){

            float distStart = Elevator.this.getPositionBody().dst(Elevator.this.startPosition);
            
            if(distStart < 1){
                return new StateNode(Elevator.ElevatorState.STOP);  
            }
            
            Iterator<Elevator.ElevatorInfluence> it = Elevator.this.influences.iterator();

            while(it.hasNext()){
                Elevator.ElevatorInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case GO_TOP :
                        return new StateNode(Elevator.ElevatorState.STOP);
                    case STOP :
                        return new StateNode(Elevator.ElevatorState.STOP);                       
                }
            }

            return null;
        }

        // Part physic
        public void updatePhysic(float deltaTime){

            Vector2 velocity = new Vector2(Elevator.this.velocityVector);
            Vector2 oppVelocity = (new Vector2(Elevator.this.velocityVector)).scl(-1);
            
            switch(this.getStateNode()){

                case MOVING_TOP:
                    
                    Vector2 diffVector = new Vector2(velocity).sub(Elevator.this.physicBody.getLinearVelocity());
                    Vector2 accVector = new Vector2(diffVector).setLength(Elevator.this.velocityVector.len() * 0.01f);

                    if(diffVector.dot(accVector) <= 0){
                        Elevator.this.physicBody.setLinearVelocity(velocity);
                    }else{
                        Elevator.this.physicBody.setLinearVelocity(new Vector2(Elevator.this.physicBody.getLinearVelocity()).add(accVector));
                    }

                    break;
                case MOVING_BOT:
                    
                    diffVector = new Vector2(oppVelocity).sub(Elevator.this.physicBody.getLinearVelocity());
                    accVector = new Vector2(diffVector).setLength(0.1f);

                    if(diffVector.dot(accVector) <= 0){
                        Elevator.this.physicBody.setLinearVelocity(oppVelocity);
                    }else{
                        Elevator.this.physicBody.setLinearVelocity(new Vector2(Elevator.this.physicBody.getLinearVelocity()).add(accVector));
                    }
                    break;
                case STOP:
                    
                    diffVector = new Vector2(0, 0).sub(Elevator.this.physicBody.getLinearVelocity());
                    accVector = new Vector2(diffVector).setLength(0.1f);

                    if(diffVector.dot(accVector) <= 0){
                        Elevator.this.physicBody.setLinearVelocity(new Vector2(0, 0));
                    }else{
                        Elevator.this.physicBody.setLinearVelocity(new Vector2(Elevator.this.physicBody.getLinearVelocity()).add(accVector));
                    }
                    break;
            }
        }

        /**
         * @return the stateNode
         */
        public Elevator.ElevatorState getStateNode() {
            return stateNode;
        }
    }
    
    @Override
    public void dispose(){

        if(this.kinematicActionFixture != null){
            this.kinematicActionFixture.dispose(this.physicBody);
        }

        super.dispose();
    }

    protected enum ElevatorState{
        STOP,
        MOVING_TOP,
        MOVING_BOT
    }

    protected enum ElevatorInfluence{
        GO_TOP,
        GO_BOT,
        STOP
    }
}
