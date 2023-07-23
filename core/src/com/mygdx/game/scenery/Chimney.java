/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ressourcesmanagers.TextureManager;
import triggered.ChimneySmokeTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class Chimney extends Character2D{
    
    private static final String CHIMNEYTEXT = "Chimney[76-76].png";
    
    protected static final float SCALE_X = 1f;
    protected static final float SCALE_Y = 1f;
    
    private Chimney.StateNode currentStateNode;      
    protected Set<Chimney.ChimneyInfluence> influences = new HashSet<Chimney.ChimneyInfluence>();
        
    private boolean canAttack;
        
    protected float attackCooldown;
    
    protected float emissionPeriod;
    protected float emissionLength;
    protected float idleLength;
    
    protected float currentEmissionPeriod;
    protected float currentEmissionLength;
    protected float currentIdleLength;

    public Chimney(World world, float posX, float posY, float angle, float emissionPeriod, float emissionLength, float idleLength){
        super(100);
                        
        this.canAttack = true;

        this.attackCooldown = 1;
        
        this.emissionPeriod = emissionPeriod;
        this.emissionLength = emissionLength;
        this.idleLength = idleLength;
        
        this.currentEmissionPeriod = 0;
        this.currentEmissionLength = 0;
        this.currentIdleLength = 0;
        
        this.currentStateNode = new StateNode(ChimneyState.STOP);
        
        // Part physic   
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        
        ground.setAsBox(12 * P2M * this.scale * SCALE_X, 20 * P2M * this.scale * SCALE_Y, new Vector2(0, 0), 0);
        
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
        
        // Transform
        if(angle != 0){
            this.physicBody.setTransform(this.physicBody.getPosition(), angle);
        }  
        
        this.assignTextures();
    }   
         
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(CHIMNEYTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 76, 76);
        
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, 2);
            this.listAnimations.add(new Animation(0, array, Animation.PlayMode.NORMAL));
            
            array = new Array<TextureRegion>(tmp[0]);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.NORMAL));

            this.changeAnimation(0, false);           
        }
    }
    
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        this.influences.clear();
        for(String influence : lInfluences){
            influence = influence.toLowerCase();
            if(influence.equals("attack")){
                this.influences.add(Chimney.ChimneyInfluence.ATTACK);
            }
        }
    }

    @Override
    public void updateLogic(float deltaTime){       
        super.updateLogic(deltaTime);

        this.createInfluences(deltaTime);
        this.influences2Actions(deltaTime);

        this.influences.clear();
    }

    protected void createInfluences(float delta){
        
        if(this.currentEmissionLength < this.emissionLength){
            this.currentIdleLength = 0;
            
            this.currentEmissionLength += delta;
            
            if(this.currentEmissionPeriod < this.emissionPeriod){
                this.currentEmissionPeriod += delta;
            }else{
                this.currentEmissionPeriod = 0;
                
                this.influences.add(Chimney.ChimneyInfluence.ATTACK);
            }
        }else if(this.currentIdleLength < this.idleLength){
            this.currentIdleLength += delta;
        }else{
            this.currentEmissionLength = 0;
            this.currentEmissionPeriod = 0;
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
    }

    protected void updateAttack(StateNode prevNode, StateNode nextNode){
        if(this.lifeState == Character2D.LifeState.DEAD){
            return;
        }

        if(this.canAttack
                && prevNode.getStateNode() != Chimney.ChimneyState.ATTACK 
                && (nextNode != null && nextNode.getStateNode() == Chimney.ChimneyState.ATTACK)){
            this.canAttack = false;
            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        Vector2 dirBall = new Vector2(0, 1).rotate((float) (Chimney.this.physicBody.getAngle() * 180 / Math.PI));

                        Chimney.this.notifyObject2D2CreateListener(ChimneySmokeTriggeredObject2D.class, Chimney.this.getPositionBody().add(dirBall.scl(Chimney.this.texture.getHeight() / 3 * P2M)).scl(1 / P2M), dirBall.scl(80 * P2M));
                        
                        Chimney.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "smoke", Chimney.this.getPositionBody());
                    }

            }, 0.6f);

            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        Chimney.this.canAttack = true;
                    }

            }, this.attackCooldown);
        }
    }


    protected class StateNode{
        private Chimney.ChimneyState stateNode;

        public StateNode(Chimney.ChimneyState state){
            this.stateNode = state;
        }

        // Part nextNode
        public StateNode getNextStateNode(){

            if(Chimney.this.getLifePoints() <= 0){
                return new StateNode(Chimney.ChimneyState.DEAD);
            }
            
            switch(this.getStateNode()){
                case STOP:
                    return getNextNodeStop();
                case ATTACK:
                    return getNextNodeAttack();

            } 
            return null;
        }

        private StateNode getNextNodeStop(){

            Iterator<Chimney.ChimneyInfluence> it = Chimney.this.influences.iterator();

            while(it.hasNext()){
                Chimney.ChimneyInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case ATTACK :
                        if(Chimney.this.canAttack){
                            return new StateNode(Chimney.ChimneyState.ATTACK);
                        }
                    break;

                }
            }

            return null;
        }

        private StateNode getNextNodeAttack(){
            if(Chimney.this.isCurrentAnimationOver()){
                return new StateNode(Chimney.ChimneyState.STOP);
            }
            return null;
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
       

        /**
         * @return the stateNode
         */
        public Chimney.ChimneyState getStateNode() {
            return stateNode;
        }
    }
    
    protected enum ChimneyState{
        STOP,
        ATTACK,
        DEAD
    }

    protected enum ChimneyInfluence{
        ATTACK
    }
}
