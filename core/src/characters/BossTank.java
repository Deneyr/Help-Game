/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Character2D;
import com.mygdx.game.DamageActionFixture;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.scenery.TrackObject2D;
import com.mygdx.game.scenery.WheelObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import ressourcesmanagers.TextureManager;
import triggered.CannonBallTriggeredObject2D;
import triggered.GravityCannonBallTriggeredObject2D;
import triggered.TeethTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class BossTank extends ABoss2D{
   
    private static final String BOSSTANKTEXT = "character/Anim_Tank V2.png";
    
    protected final String id = UUID.randomUUID().toString();
    
    private static final float MOVE_DIST = P2M * 1000;
    private static final float ATTACK_DIST = P2M * 400;
    
    protected StateNode currentStateNode;
    
    protected Set<TankInfluence> influences;
    
    protected Object2D target;
    
    protected DamageActionFixture damageActionFixture;
    
    protected boolean canAttack;
    
    protected float maxSpeed;
    
    protected SideCharacter previousSide;
    
    protected Vector2 spawnPoint;
    protected float maxDistanceFromSpawn;
    protected boolean isReseting;
    
    protected Vector2 offsetPosition;
    protected Vector2 targetedOffsetPosition;
    protected float offsetTime;
    
    private float timerBeforeTurning;
    private final float maxTimerBeforeTurning;
    
    private SideCharacter bounceSide;
    private float bounceTimer;
    private final float maxBounceTimer;
    
    protected TrackObject2D wheels;
    
    protected float attackCooldown;
    
    public BossTank(){
       this(6 * 35, null);
    }
    
    public BossTank(int lifePoint, Object2D target){
        super(lifePoint);
        this.influences = new HashSet<TankInfluence>();
        
        this.attackCooldown = 2f;
        
        this.timerBeforeTurning = 0;
        this.maxTimerBeforeTurning = 1;
        
        this.bounceSide = Character2D.SideCharacter.LEFT;
        this.bounceTimer = 0;
        this.maxBounceTimer = 1;
        
        this.scaleDamageForce = 0.1f;
        
        this.side = SideCharacter.LEFT;
        
        this.currentStateNode = new BossTank.StateNode(TankState.NORMAL);
        
        this.canAttack = true;
        
        this.target = target;
        
        this.maxSpeed = 15f;
        
        this.spawnPoint = new Vector2(0, 0);
        this.maxDistanceFromSpawn = -1f;
        this.isReseting = false;
        
        this.previousSide = this.side;
    }
    
    public BossTank(World world, Object2D target, float posX, float posY) {
        super(6 * 35);
        this.influences = new HashSet<TankInfluence>();
        
        this.attackCooldown = 2f;
        
        this.maxTimerBeforeTurning = 1;
        this.maxBounceTimer = 1;
        
        this.initialize(world, target, posX, posY);
    }
    
    public final void initialize(World world, Object2D target, float posX, float posY){
        
        this.timerBeforeTurning = 0;
        this.bounceSide = Character2D.SideCharacter.LEFT;
        this.bounceTimer = 0;
        
        this.offsetPosition = new Vector2();
        this.targetedOffsetPosition = new Vector2();
        this.offsetTime = -1;
        
        this.lifePoints = this.getLifePointsMax();
        
        this.priority = 3;
        
        this.target = target;
        
        this.side = SideCharacter.LEFT;
        
        this.currentStateNode = new BossTank.StateNode(TankState.NORMAL);
        
        this.canAttack = true;
        
        this.maxSpeed = 5f;
        
        this.spawnPoint = new Vector2(posX * P2M, posY * P2M);
        this.maxDistanceFromSpawn = -1;
        this.isReseting = false;
        
        // Part Physic
        this.initializePhysicBossTank(world, posX, posY);
        
        // Part graphic
        this.assignTextures();
        
        this.previousSide = this.side;
    }
    
    protected void updateFixture(){
        
        for(Fixture fix : this.collisionFixture){
            this.physicBody.destroyFixture(fix);
        }
        this.collisionFixture.clear();
        
        if(this.side == SideCharacter.RIGHT){        
            // Collision fixture
            PolygonShape collisionBox = new PolygonShape();
            collisionBox.setAsBox(100 * P2M, 75 * P2M, new Vector2( 15 * P2M, 20 * P2M), 0);

            FixtureDef fixtureDef = new FixtureDef();

            this.setCollisionFilterMask(fixtureDef, false);

            fixtureDef.shape = collisionBox;
            fixtureDef.density = 5f; 
            fixtureDef.friction = 0.5f;
            fixtureDef.restitution = 0.1f; 
            Fixture fix = this.physicBody.createFixture(fixtureDef);

            this.setCollisionFilterMask(fixtureDef, true);

            this.collisionFixture = new ArrayList<Fixture>();
            this.collisionFixture.add(fix);
            fix.setUserData(this);


            collisionBox = new PolygonShape();
            collisionBox.setAsBox(50 * P2M, 35 * P2M, new Vector2( -116 * P2M, -10 * P2M), (float) Math.toRadians(20));

            fixtureDef = new FixtureDef();

            this.setCollisionFilterMask(fixtureDef, false);

            fixtureDef.shape = collisionBox;
            fixtureDef.density = 5f; 
            fixtureDef.friction = 0.5f;
            fixtureDef.restitution = 0.1f; 
            fix = this.physicBody.createFixture(fixtureDef);

            this.setCollisionFilterMask(fixtureDef, true);

            this.collisionFixture.add(fix);
            fix.setUserData(this);
            
        }else{
            // Collision fixture
            PolygonShape collisionBox = new PolygonShape();
            collisionBox.setAsBox(100 * P2M, 75 * P2M, new Vector2( -15 * P2M, 20 * P2M), 0);

            FixtureDef fixtureDef = new FixtureDef();

            this.setCollisionFilterMask(fixtureDef, false);

            fixtureDef.shape = collisionBox;
            fixtureDef.density = 5f; 
            fixtureDef.friction = 0.5f;
            fixtureDef.restitution = 0.1f; 
            Fixture fix = this.physicBody.createFixture(fixtureDef);

            this.setCollisionFilterMask(fixtureDef, true);

            this.collisionFixture = new ArrayList<Fixture>();
            this.collisionFixture.add(fix);
            fix.setUserData(this);


            collisionBox = new PolygonShape();
            collisionBox.setAsBox(50 * P2M, 35 * P2M, new Vector2( 116 * P2M, -10 * P2M), (float) Math.toRadians(-20));

            fixtureDef = new FixtureDef();

            this.setCollisionFilterMask(fixtureDef, false);

            fixtureDef.shape = collisionBox;
            fixtureDef.density = 5f; 
            fixtureDef.friction = 0.5f;
            fixtureDef.restitution = 0.1f; 
            fix = this.physicBody.createFixture(fixtureDef);

            this.setCollisionFilterMask(fixtureDef, true);

            this.collisionFixture.add(fix);
            fix.setUserData(this);
        }
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        this.influences.clear();
        for(String influence : lInfluences){
            influence = influence.toLowerCase();
            if(influence.equals("right")){
                this.influences.add(TankInfluence.GO_RIGHT);
            }else if(influence.equals("left")){
                this.influences.add(TankInfluence.GO_LEFT);
            }
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        this.updateTimer(deltaTime);
        
        this.updateOffsetPosition(deltaTime);
        
        createInfluencesBossHummer();
        
        influences2Actions(deltaTime);
        
        if(this.side != this.previousSide){
            this.updateFixture();
            this.previousSide = this.side;
        }
        
        Vector2 linearVelocity = this.getBodyVelocity();
        this.wheels.setParentSpeedX(linearVelocity.x);     
        this.wheels.updateLogic(deltaTime);
        
        if(this.lifeState == LifeState.ALIVE){
            this.damageActionFixture.applyAction(deltaTime, this);
            
            this.notifyGameEventListener(GameEventListener.EventType.LOOP, "tank" + ":" + this.id, this.getPositionBody());
        }
    }
    
    protected void updateOffsetPosition(float deltaTime){    
        if(BossTank.this.lifeState == LifeState.DEAD){
            return;
        }
        
        float maxTime = 0.05f;
        
        if(this.offsetTime < 0
                || this.offsetTime > maxTime){
            
            if(this.targetedOffsetPosition.y > 0){
                this.targetedOffsetPosition = new Vector2(0, -0.02f);
            }else{
                this.targetedOffsetPosition = new Vector2(0, 0.02f);
            }
            
            this.offsetTime = 0;
        }
        
        this.offsetTime += deltaTime;
        
        float newOffsetX = this.targetedOffsetPosition.x;
        float newOffsetY = this.targetedOffsetPosition.y;   
        if(this.offsetTime < maxTime){
            newOffsetX = this.offsetPosition.x + (this.targetedOffsetPosition.x - this.offsetPosition.x) * deltaTime * 0.8f;
            newOffsetY = this.offsetPosition.y + (this.targetedOffsetPosition.y - this.offsetPosition.y) * deltaTime * 0.8f;
        }      
        this.offsetPosition.set(newOffsetX, newOffsetY);
    }
    
    protected final void initializeGraphic()
    {
        // Part graphic
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 400, 250);
        // walk
        for (int i =0; i < tmp.length - 1; i++) {
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[i]);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
        }
        
        Array<TextureRegion> array = new Array<TextureRegion>();
        array.add(tmp[tmp.length - 1][0]);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void assignTextures(){
        
        this.texture = TextureManager.getInstance().getTexture(BOSSTANKTEXT, this);
        
        if(this.texture != null){            
            this.initializeGraphic();
            
            this.wheels.assignTextures(this.texture);
        }
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        boolean result = super.applyDamage(damage, dirDamage, damageOwner);
        
        if(result){
            int indexAnimation = (int) (((float) (this.getLifePointsMax() - this.getLifePoints()) / this.getLifePointsMax()) * (this.listAnimations.size() - 1));
            this.changeAnimation(indexAnimation, this.pause, this.animationTime);
            
            if(damageOwner instanceof Character2D
                && this.bounceTimer <= 0){
                this.bounceSide = ((Character2D) damageOwner).getSideCharacter();
                this.bounceTimer = this.maxBounceTimer;         
            }
        }
        
        return result;
    }
    
    protected final void initializePhysicBossTank(World world, float posX, float posY){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);
        //body.setFixedRotation(true);
        
        // Collision fixture
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(100 * P2M, 75 * P2M, new Vector2( -15 * P2M, 20 * P2M), 0);
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = collisionBox;
        fixtureDef.density = 5f; 
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f; 
        Fixture fix = body.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        
        
        collisionBox = new PolygonShape();
        collisionBox.setAsBox(50 * P2M, 35 * P2M, new Vector2( 116 * P2M, -10 * P2M), (float) Math.toRadians(-20));
        
        fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = collisionBox;
        fixtureDef.density = 5f; 
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f; 
        fix = body.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);

        this.collisionFixture.add(fix);
        fix.setUserData(this);
        
        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox(170 * P2M, 40 * P2M, new Vector2( -5 * P2M, -85 * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = feet;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        fix = body.createFixture(fixtureDef);
        fix.setSensor(true);
        fix.setUserData(this);
        
        this.feetFixture = fix;
        this.physicBody = body;
                
        // Part damage zone    
        PolygonShape damageBox = new PolygonShape();
        damageBox.setAsBox(180 * P2M, 45 * P2M, new Vector2( -5 * P2M, -75 * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = damageBox;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        fix = this.physicBody.createFixture(fixtureDef);
        Set<Fixture> setDamage = new HashSet<Fixture>();
        setDamage.add(fix);
        
        this.damageActionFixture = new DamageActionFixture(setDamage, 1);
        
        // Wheels
        this.wheels = new TrackObject2D(this.physicBody, world, posX, posY, 0, 0, this.priority + 1);
    }
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(bounceOwner instanceof Character2D
            && this.bounceTimer <= 0){
            this.bounceSide = ((Character2D) bounceOwner).getSideCharacter();
            this.bounceTimer = this.maxBounceTimer;
            
            this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitProjectile", new Vector2(this.getPositionBody()));             
        }
    }
    
    @Override
    protected void onDeath(){
        this.setDeathCollisionBox();
        
        this.priority = 1;
        this.wheels.setIsDestroyed(true);
        
        this.offsetPosition = new Vector2(0, 0);
        
        Vector2 dirVelocity = this.getBodyVelocity().nor();
        Vector2 upVector = new Vector2(0, 1);
        float angle = dirVelocity.angle(upVector) / 2f;
        dirVelocity = dirVelocity.rotate(angle);
        
        this.spawnLoot(dirVelocity);
        
        this.notifyGameEventListener(GameEventListener.EventType.DEATH, this.name, this.getPositionBody());
        
        this.notifyGameEventListener(GameEventListener.EventType.LOOP_STOP, "tank" + ":" + this.id, this.getPositionBody());
    }
    
    private void setDeathCollisionBox(){
        for(Fixture fix : this.collisionFixture){
            this.physicBody.destroyFixture(fix);
        }
        this.collisionFixture.clear();
        
        // Collision fixture
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(180 * P2M, 25 * P2M, new Vector2( -5 * P2M, -95 * P2M), 0);
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = collisionBox;
        fixtureDef.density = 5f; 
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f; 
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture.add(fix);
        fix.setUserData(this);
    }
    
    protected void spawnLoot(Vector2 dirVelocity)
    {
        //this.notifyGameEventListener(GameEventListener.EventType.CINEMATIC, "dialogueKaira4", new Vector2(this.getPositionBody()));
            
        this.notifyObject2D2CreateListener(OpponentCAC1.class, this.getPositionBody().add(5 * P2M, 50 * P2M).scl(1 / P2M), new Vector2(0, 0));
            
        this.notifyObject2D2CreateListener(OpponentCAC2.class, this.getPositionBody().add(-5 * P2M, 50 * P2M).scl(1 / P2M), new Vector2(0, 0));
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        int side = this.side == SideCharacter.LEFT ? 1 : -1;     
        sprite.setScale(sprite.getScaleX() * side, sprite.getScaleY());
        sprite.setPosition(sprite.getX() + this.offsetPosition.x / P2M, sprite.getY() + this.offsetPosition.y / P2M);
        
        return sprite;
    }
    
    @Override
    public void removeBody(World world){      
        if(this.wheels != null){
            this.wheels.removeBody(world);
        }
        
        super.removeBody(world);
    }
    
    protected void createInfluencesBossHummer(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        // Balance influences
        double reworkedAngle = getReworkedAngle(Math.toDegrees(this.physicBody.getAngle()));
        if(reworkedAngle > 20){
            this.influences.add(TankInfluence.TURN_LEFT);
        }else if(reworkedAngle < -20){
            this.influences.add(TankInfluence.TURN_RIGHT);
        }
        
        // Attack influences
        
        if(this.target != null){   
            if(this.maxDistanceFromSpawn > 0 
                    && (Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn
                        || (this.isReseting && Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn / 2))){
                if(!this.isReseting){
                    this.isReseting = true;
                }
                if(this.spawnPoint.x - this.physicBody.getPosition().x > 0){
                    this.influences.add(TankInfluence.GO_RIGHT);
                }else{
                    this.influences.add(TankInfluence.GO_LEFT);
                }
            }else{
                if(this.isReseting){
                    this.isReseting = false;
                }
                if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
                    if(this.target.getPositionBody().dst(this.physicBody.getPosition()) > ATTACK_DIST){
                        this.influences.add(TankInfluence.ATTACK);
                    }
                    
                    if(this.target.getPositionBody().x - this.physicBody.getPosition().x > 0){
                        this.influences.add(TankInfluence.GO_RIGHT);
                    }else{
                        this.influences.add(TankInfluence.GO_LEFT);
                    }
                }else{
                    double rand = Math.random()*100;
                    if(rand > 50){
                        if(this.side == SideCharacter.RIGHT){
                            this.influences.add(TankInfluence.GO_RIGHT);
                        }else{
                            this.influences.add(TankInfluence.GO_LEFT);
                        }
                    }else if(rand > 40){
                        if(this.side == SideCharacter.RIGHT){
                            this.influences.add(TankInfluence.GO_LEFT);
                        }else{
                            this.influences.add(TankInfluence.GO_RIGHT);
                        }
                    }
                }
            }
        }
    }
    
    private static double getReworkedAngle(double angle){
        double circleAngle = angle % 360;
        if(Math.abs(circleAngle) > 360){
            circleAngle = -(Math.signum(angle) * 360 - circleAngle);
        }
        return circleAngle;
    }
    
    protected void influences2Actions(float deltaTime){
        
        StateNode prevNode = this.currentStateNode;
        StateNode nextNode = this.currentStateNode.getNextStateNode();
        if(nextNode != null){
            this.currentStateNode = nextNode;
        }
        
        this.updateAttack(prevNode, nextNode);
        
        this.currentStateNode.updatePhysic();
                
        this.influences.clear();
    }
    
    protected void updateAttack(StateNode prevNode, StateNode nextNode){
        if(this.lifeState == LifeState.DEAD){
            return;
        }

        if(this.canAttack
                && prevNode.getStateNode() != TankState.ATTACK 
                && (nextNode != null && nextNode.getStateNode() == TankState.ATTACK)){
            this.canAttack = false;
            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        if(BossTank.this.lifeState == LifeState.ALIVE){
                            int dirScale = BossTank.this.side == SideCharacter.LEFT ? 1 : -1;

                            float angle = (BossTank.this.side == SideCharacter.RIGHT ? 0 : (float) Math.PI) + BossTank.this.physicBody.getAngle();
                            
                            //Vector2 dirBall = new Vector2(-1 * dirScale, 0).rotate((float) (Math.toDegrees(BossTank.this.physicBody.getAngle())));
                            Vector2 cannonPosition = BossTank.this.getPositionBody().add(new Vector2(-140 * P2M * dirScale, 65 * P2M).rotate((float) (Math.toDegrees(BossTank.this.physicBody.getAngle()))));
                            Vector2 velocityBall = GravityCannonBallTriggeredObject2D.getSpeedToTarget(angle, BossTank.this.target.getPositionBody().sub(cannonPosition));
                            
                            Vector2 dirBall = (new Vector2(1, 0)).rotateRad(angle);
                            float normVelocityBall = velocityBall.len();
                            if(normVelocityBall < 8){
                                normVelocityBall = 8;
                            }else if(normVelocityBall > 20){
                                normVelocityBall = 20;
                            }
                            dirBall = dirBall.setLength(normVelocityBall);
                            
                            BossTank.this.notifyObject2D2CreateListener(GravityCannonBallTriggeredObject2D.class, cannonPosition.scl(1 / P2M), dirBall);

                            BossTank.this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "cannon", BossTank.this.getPositionBody());
                            BossTank.this.notifyGameEventListener(GameEventListener.EventType.SHAKESCREEN, "0.2:0.2", BossTank.this.getPositionBody()); 
                        }
                    }

            }, 1f);

            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        BossTank.this.canAttack = true;
                    }

            }, this.attackCooldown);
        }
    }
    
    /**
     * @param maxDistance the maxDistanceFromSpawn to set
     */
    public void setMaxDistance(float maxDistance) {
        this.maxDistanceFromSpawn = maxDistance * P2M;
    }

    private void updateTimer(float deltaTime) {
        if(this.timerBeforeTurning > 0){
            this.timerBeforeTurning -= deltaTime;
        }
        
        if(this.bounceTimer > 0){
            this.bounceTimer -= deltaTime;
        }
    }
    
    protected enum TankState{
        NORMAL,
        ATTACK
    }
    
    protected enum TankInfluence{
        GO_RIGHT,
        GO_LEFT,
        TURN_RIGHT,
        TURN_LEFT,
        ATTACK
    }
    
    protected class StateNode{
        private final TankState stateNode;
        
        private int pauseAnimation;
        
        private boolean restartAnimation;
        
        public StateNode(TankState state){
            this.stateNode = state;
            
            this.pauseAnimation = 0;
            
            this.restartAnimation = true;
        }
        
        // Part nextNode
        public StateNode getNextStateNode(){
            if(BossTank.this.lifeState == LifeState.DEAD)
                return null;
            
            switch(this.getStateNode()){
                case NORMAL:
                    return getNextNodeNormal();
                case ATTACK:
                    return getNextNodeAttack();
            } 
            return null;
        }
        
        private StateNode getNextNodeNormal(){
            Iterator<TankInfluence> it = BossTank.this.influences.iterator();
            
            while(it.hasNext()){
                TankInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case ATTACK :
                        if(BossTank.this.canAttack){
                            BossTank.this.changeAnimation(BossTank.this.currentAnimation, false);
                            
                            return new StateNode(TankState.ATTACK);
                        }
                }
            }
            return null;
        }
        
        private StateNode getNextNodeAttack(){
            if(BossTank.this.isCurrentAnimationOver()){
                BossTank.this.changeAnimation(BossTank.this.currentAnimation, true);
                
                return new StateNode(TankState.NORMAL);
            }
            return null;
        }
        
        // Part animation      
        public int getCurrentAnimation(){
            if(BossTank.this.lifeState == LifeState.DEAD){
                this.pauseAnimation = -1;
                this.restartAnimation = true;
                return BossTank.this.currentAnimation;
            }

            
            /*if(BossTank.this.isInvulnerable){
                this.pauseAnimation = -1;
                this.restartAnimation = false;
                return BossTank.this.currentAnimation;
            }*/
            
            switch(this.getStateNode()){
                case NORMAL:
                    return getAnimationNormal();
                case ATTACK:
                    return getAnimationAttack();

            } 
            return -1;
        }
        
        private int getAnimationNormal(){
            this.pauseAnimation = -1;
            this.restartAnimation = true;
            return BossTank.this.currentAnimation;
        }
        
        private int getAnimationAttack(){
            this.pauseAnimation = 0;
            this.restartAnimation = true;
            return BossTank.this.currentAnimation;
        }
        
        // Part physic
        public void updatePhysic(){
            if(BossTank.this.lifeState == LifeState.DEAD){
                return;
            }
            
            Vector2 velocity = BossTank.this.physicBody.getLinearVelocity();
            //System.out.println("Speed pre : " + velocity.x);
            
            boolean isMove = false;         
            if(!BossTank.this.isFlying()){
                Iterator<BossTank.TankInfluence> it = BossTank.this.influences.iterator();
                
                if(this.getStateNode() != BossTank.TankState.ATTACK 
                        && (isMove = this.ApplyBounce(velocity)) == false){           
                    while(it.hasNext()){
                        BossTank.TankInfluence currentInfluence = it.next();
                        switch(currentInfluence){
                            case GO_RIGHT :
                                if(this.CanTurn(currentInfluence)){
                                    velocity.x += 0.3f;
                                    BossTank.this.side = SideCharacter.RIGHT;
                                    isMove = true;
                                }
                                break;
                            case GO_LEFT :
                                if(this.CanTurn(currentInfluence)){
                                    velocity.x -= 0.3f;
                                    BossTank.this.side = SideCharacter.LEFT;
                                    isMove = true;
                                }
                                break;
                        }
                    }
                }
            }
            
            // Balance physic
            float angularVelocity = BossTank.this.physicBody.getAngularVelocity();
            Iterator<TankInfluence> it = BossTank.this.influences.iterator();
            while(it.hasNext()){
                TankInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case TURN_LEFT :
                        angularVelocity -= 0.2;
                        break;
                    case TURN_RIGHT :
                        angularVelocity += 0.2;
                        break;
                }
            }
            
            if(!(isMove || BossTank.this.isFlying())){
                if(Math.abs(velocity.x) > 1.f){
                    velocity.x -= 0.5f * (float)Math.signum(velocity.x);
                }else{
                    velocity.x = 0.f;
                }
            }
            
            if(Math.abs(velocity.x) > BossTank.this.maxSpeed){
                velocity.x = BossTank.this.maxSpeed * (float)Math.signum(velocity.x);
            }
            
            // Clamp speed
            if(Math.abs(velocity.y) > 15.f){
                velocity.y = 15.f * (float)Math.signum(velocity.y);
            }
            //System.out.println("Angle : " + angle);
            BossTank.this.physicBody.setLinearVelocity(velocity);
            BossTank.this.physicBody.setAngularVelocity(angularVelocity);
        }
         
        public boolean CanTurn(TankInfluence currentInfluence){
            if(BossTank.this.side == SideCharacter.LEFT && currentInfluence == TankInfluence.GO_LEFT
                || BossTank.this.side == SideCharacter.RIGHT && currentInfluence == TankInfluence.GO_RIGHT){
                return true;
            }
            if(BossTank.this.timerBeforeTurning <= 0){
                
                BossTank.this.timerBeforeTurning = BossTank.this.maxTimerBeforeTurning;
                
                return true;
            }
            return false;
        }
        
        public int isPauseAnimation(){
           return this.pauseAnimation;
        }
        
        public boolean isRestartAnimation(){
            return this.restartAnimation;
        }

        /**
         * @return the stateNode
         */
        public TankState getStateNode() {
            return stateNode;
        }

        private boolean ApplyBounce(Vector2 velocity) {
            if(BossTank.this.bounceTimer > 0){
                SideCharacter bounceOwnerSide = BossTank.this.bounceSide;

                if(bounceOwnerSide == SideCharacter.LEFT){
                    if(velocity.x > 0){
                        velocity.x = 0;
                    }
                    velocity.x -= 0.3f;
                    BossTank.this.side = SideCharacter.LEFT;
                }else{
                    if(velocity.x < 0){
                        velocity.x = 0;
                    }
                    velocity.x += 0.3f;
                    BossTank.this.side = SideCharacter.RIGHT;
                }    
                
                return true;
            }
            return false;
        }
    }
    
}