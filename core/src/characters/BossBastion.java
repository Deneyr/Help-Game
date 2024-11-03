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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Character2D;
import com.mygdx.game.DamageActionFixture;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.scenery.CannonCorpus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import ressourcesmanagers.TextureManager;
import triggered.CannonBallTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class BossBastion extends ABoss2D{
   
    private static final String BOSSBASTIONTEXT = "character/Help_BastionKaira_5000x5440_BastionKaira.png";
    
    private static final String CANNONTEXT = "Cannon.png";
    
    protected final String id = UUID.randomUUID().toString();
    
    protected StateNode currentStateNode;
    
    protected Set<BastionInfluence> influences = new HashSet<BastionInfluence>();
    
    protected Object2D target;
    
    protected DamageActionFixture damageActionFixture;
    
    protected boolean canAttack;
    
    protected SideCharacter previousSide;
    
    protected boolean isReseting;
    
    protected Vector2 offsetPosition;
    protected Vector2 targetedOffsetPosition;
    protected float offsetTime;
    
    private float timerBeforeTurning;
    private final float maxTimerBeforeTurning;
    
    private SideCharacter bounceSide;
    private float bounceTimer;
    private final float maxBounceTimer;
    
    private List<Cannon> cannons;
    
    private float defenderSpawnRatio;
    
    public BossBastion(){
       this(40 * 35, null);    
    }
    
    public BossBastion(int lifePoint, Object2D target){
        super(lifePoint);
        
        this.timerBeforeTurning = 0;
        this.maxTimerBeforeTurning = 1;
        
        this.bounceSide = Character2D.SideCharacter.LEFT;
        this.bounceTimer = 0;
        this.maxBounceTimer = 1;
        
        this.scaleDamageForce = 0.1f;
        
        this.side = SideCharacter.LEFT;
        
        this.currentStateNode = new BossBastion.StateNode(BossBastion.BastionState.STARTING);
        
        this.canAttack = true;
        
        this.target = target;
        
        this.isReseting = false;
        
        this.defenderSpawnRatio = 0.2f;
    }
    
    public BossBastion(World world, Object2D target, float posX, float posY) {
        super(40 * 35);
        
        this.maxTimerBeforeTurning = 1;
        this.maxBounceTimer = 1;
        
        this.defenderSpawnRatio = 0.25f;
        
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
        
        this.currentStateNode = new BossBastion.StateNode(BossBastion.BastionState.STARTING);
        
        this.canAttack = true;
        
        this.isReseting = false;
        
        // Part Physic
        this.initializePhysicBossBastion(world, target, posX, posY);
        
        // Part graphic
        this.assignTextures();
        
        this.previousSide = this.side;
    }
    
    protected void updateFixture(){
        /*
        if(this.damageActionFixture != null){
            damageActionFixture.dispose(this.physicBody);
        }
        
        
        if(this.side == SideCharacter.RIGHT){        
            // Part damage zone
            
            PolygonShape damageShape = new PolygonShape();

            damageShape.setAsBox(20 * P2M, 35 * P2M, new Vector2(20 * P2M, 0), 0);
            
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = damageShape;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            Fixture fix = this.physicBody.createFixture(fixtureDef);
            Set<Fixture> setDamage = new HashSet<Fixture>();
            setDamage.add(fix);
            this.damageActionFixture = new DamageActionFixture(setDamage, 1);
            
        }else{
            // Part damage zone
            
            PolygonShape damageShape = new PolygonShape();

            damageShape.setAsBox(20 * P2M, 35 * P2M, new Vector2(-20 * P2M, 0), 0);
            
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = damageShape;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            Fixture fix = this.physicBody.createFixture(fixtureDef);
            Set<Fixture> setDamage = new HashSet<Fixture>();
            setDamage.add(fix);
            this.damageActionFixture = new DamageActionFixture(setDamage, 1);
        }*/
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        this.influences.clear();
        for(String influence : lInfluences){
            influence = influence.toLowerCase();
            if(influence.equals("construct")){
                this.influences.add(BastionInfluence.CONSTRUCT);
            }
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        this.updateTimer(deltaTime);
        
        this.updateOffsetPosition(deltaTime);
        
        createInfluencesBossBastion();
        
        influences2Actions(deltaTime);
        
        if(this.side != this.previousSide){
            this.updateFixture();
            this.previousSide = this.side;
        }
        
        if(this.lifeState == LifeState.ALIVE){
            this.damageActionFixture.applyAction(deltaTime, this);
            
            if(this.damageActionFixture.hasEncounteredSomething()){
                this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "barbed", new Vector2(this.getPositionBody()));
            }
            
            if(this.currentStateNode.stateNode == BastionState.CONSTRUCTING){
                this.notifyGameEventListener(GameEventListener.EventType.LOOP, "steamEngine" + ":" + this.id, this.getPositionBody());
            }
        }
        
        for(Cannon cannonObj : this.cannons){
            cannonObj.updateLogic(deltaTime);
        }
    }
     
    protected void updateOffsetPosition(float deltaTime){    
        if(BossBastion.this.lifeState == LifeState.DEAD){
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
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 500, 680);
        
        // construction
        Array<TextureRegion> array = new Array<TextureRegion>();
        array.addAll(tmp[0]);
        array.addAll(tmp[1]);
        array.addAll(tmp[2]);
        Array<TextureRegion> rowArray = new Array<TextureRegion>(tmp[3]);
        rowArray.removeRange(7, 9);
        array.addAll(rowArray);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
        
        array = new Array<TextureRegion>();
        array.addAll(tmp[4]);
        array.addAll(tmp[5]);
        array.addAll(tmp[6]);
        array.addAll(tmp[7]);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void assignTextures(){
        
        this.texture = TextureManager.getInstance().getTexture(BOSSBASTIONTEXT, this);      
        Texture cannon = TextureManager.getInstance().getTexture(CANNONTEXT, this);
        
        if(this.texture != null
            && cannon != null){
            this.initializeGraphic();
            
            for(Cannon cannonObj : this.cannons){
                cannonObj.assignTextures(cannon);
            }
        }
    }
    
    private void SpawnDefender(Object2D damageOwner, int previousLifePoints, int currentLifePoints){
        float previousLPRatio = previousLifePoints / (float) (this.getLifePointsMax());
        float currentLPRatio = currentLifePoints / (float) (this.getLifePointsMax());
        
        int previousNbWave = (int) (previousLPRatio / this.defenderSpawnRatio);
        int currentNbWave = (int) (currentLPRatio / this.defenderSpawnRatio);
        
        int nbWavesToGenerate = previousNbWave - currentNbWave;
        Vector2 diffDamageOwnerThis = damageOwner.getPositionBody().sub(this.getPositionBody());
        for(int i = 0; i < nbWavesToGenerate; i++){
            this.notifyObject2D2CreateListener(OpponentCAC1.class, this.getPositionBody().add(Math.signum(diffDamageOwnerThis.x) * 200 * P2M, 50 * P2M).scl(1 / P2M), new Vector2(0, 0));
        }
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        if(this.currentStateNode.stateNode == BastionState.NORMAL
            && damageOwner instanceof CannonBallTriggeredObject2D == false){
            
            int previousLifePoints = this.getLifePoints();
            boolean result = super.applyDamage(damage, dirDamage, damageOwner);

            if(result){
                int indexAnimation = this.listAnimations.size() - 1;
                int nbMaxFrame = this.listAnimations.get(this.listAnimations.size()-1).getKeyFrames().length;
                int indexFrame = (int) (((float) (this.getLifePointsMax() - this.getLifePoints()) / this.getLifePointsMax()) * nbMaxFrame);
                this.changeAnimation(indexAnimation, true, 0.2f * indexFrame);

                if(damageOwner instanceof Character2D
                    && this.bounceTimer <= 0){
                    this.bounceSide = ((Character2D) damageOwner).getSideCharacter();
                    this.bounceTimer = this.maxBounceTimer;         
                }
                
                this.SpawnDefender(damageOwner, previousLifePoints, this.getLifePoints());
            }

            return result;
        }
        return false;
    }
    
    /*
    @Override
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        
        if(reset){
            super.setCollisionFilterMask(fixtureDef, true);
            return;
        }
        
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = 0x0002;
    }*/
    
    protected final void initializePhysicBossBastion(World world, Object2D target, float posX, float posY){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);
        //body.setFixedRotation(true);
        
        // Collision fixture
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(200 * P2M, 125 * P2M, new Vector2( 0, -208 * P2M), 0);
        
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
        collisionBox.setAsBox(163 * P2M, 75 * P2M, new Vector2( 0, -24 * P2M), 0);
        
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
        
                
        collisionBox = new PolygonShape();
        collisionBox.setAsBox(113 * P2M, 68 * P2M, new Vector2( 0, 126 * P2M), 0);
        
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
        feet.setAsBox(180 * P2M, 5 * P2M, new Vector2(0, -208 * P2M), 0);
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
        Set<Fixture> setDamage = new HashSet<Fixture>();
        PolygonShape damageBox = new PolygonShape();
        damageBox.setAsBox(202 * P2M, 100 * P2M, new Vector2( 0, -180 * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = damageBox;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        fix = this.physicBody.createFixture(fixtureDef);
        setDamage.add(fix);
        
        damageBox = new PolygonShape();
        damageBox.setAsBox(165 * P2M, 77 * P2M, new Vector2( 0, -24 * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = damageBox;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 
        
        fix = this.physicBody.createFixture(fixtureDef);
        setDamage.add(fix);
        
        damageBox = new PolygonShape();
        damageBox.setAsBox(116 * P2M, 130 * P2M, new Vector2( 0, 50 * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = damageBox;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 
        
        fix = this.physicBody.createFixture(fixtureDef);
        setDamage.add(fix);
        
        this.damageActionFixture = new DamageActionFixture(setDamage, 1);
        
        // Cannons
        this.cannons = new ArrayList<Cannon>();
        this.cannons.add(new Cannon(this.physicBody, target, world, posX, posY, -150, 30, 0, SideCharacter.LEFT, 2f));
        this.cannons.add(new Cannon(this.physicBody, target, world, posX, posY, 150, 30, 0, SideCharacter.RIGHT, 2f));
        
        this.cannons.add(new Cannon(this.physicBody, target, world, posX, posY, -100, 160, 1, SideCharacter.LEFT, 2f));
        this.cannons.add(new Cannon(this.physicBody, target, world, posX, posY, 100, 160, 1, SideCharacter.RIGHT, 2f));
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
        
        this.offsetPosition = new Vector2(0, 0);
        
        Vector2 dirVelocity = this.getBodyVelocity().nor();
        Vector2 upVector = new Vector2(0, 1);
        float angle = dirVelocity.angle(upVector) / 2f;
        dirVelocity = dirVelocity.rotate(angle);
        
        this.spawnLoot(dirVelocity);
        
        this.notifyGameEventListener(GameEventListener.EventType.DEATH, this.name, this.getPositionBody());
        
        //this.notifyGameEventListener(GameEventListener.EventType.LOOP_STOP, "hummer" + ":" + this.id, this.getPositionBody());
    }
    
    // listener
    @Override
    public void addGameEventListener(GameEventListener listener){
        super.addGameEventListener(listener);
        
        if(listener != null){
            for(Cannon cannonObj : this.cannons){
                cannonObj.addGameEventListener(listener);
            }
        }
    }
    
    @Override
    public void ReinitPlatform(World world){
        for(Cannon cannonObj : this.cannons){
            cannonObj.ReinitPlatform(world);
        }
    }
    
    @Override
    public void addObject2DStateListener(Object2DStateListener object2DStateListener){
        super.addObject2DStateListener(object2DStateListener);
        
        for(Cannon cannonObj : this.cannons){
            cannonObj.addObject2DStateListener(object2DStateListener);
        }
    }
    
    private void setDeathCollisionBox(){
        for(Fixture fix : this.collisionFixture){
            this.physicBody.destroyFixture(fix);
        }
        this.collisionFixture.clear();
        /*
        // Collision fixture
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(90 * P2M, 25 * P2M, new Vector2( 0, -50 * P2M), 0);
        
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
        */
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
        // Cannons
        for(Cannon cannonObj : this.cannons){
            cannonObj.removeBody(world);
        }
        this.cannons.clear();
        
        super.removeBody(world);
    }
    
    protected void createInfluencesBossBastion(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        
        if(this.target != null){
            if(this.currentStateNode.stateNode == BastionState.STARTING){
                if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < 2000){
                    this.influences.add(BastionInfluence.CONSTRUCT);
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
        
        this.currentStateNode.updatePhysic();
                
        this.influences.clear();
    }   

    private void updateTimer(float deltaTime) {
        if(this.timerBeforeTurning > 0){
            this.timerBeforeTurning -= deltaTime;
        }
        
        if(this.bounceTimer > 0){
            this.bounceTimer -= deltaTime;
        }
    }
    
    // dispose function
    @Override
    public void dispose(){
        
        if(this.damageActionFixture != null){
            this.damageActionFixture.dispose(this.physicBody);
            this.damageActionFixture = null;
        }
        
        super.dispose();
    }
    
    protected enum BastionState{
        STARTING,
        CONSTRUCTING,
        NORMAL
    }
    
    protected enum BastionInfluence{
        CONSTRUCT
    }
    
    protected class StateNode{
        private final BastionState stateNode;
        
        private int pauseAnimation;
        
        private boolean restartAnimation;
        
        public StateNode(BastionState state){
            this.stateNode = state;
            
            this.pauseAnimation = 0;
            
            this.restartAnimation = true;
        }
        
        // Part nextNode
        public StateNode getNextStateNode(){
            if(BossBastion.this.lifeState == LifeState.DEAD)
                return null;
            
            switch(this.getStateNode()){
                case STARTING:
                    return getNextNodeStarting();
                case CONSTRUCTING:
                    return getNextNodeConstructing();
                case NORMAL:
                    return getNextNodeNormal();
            } 
            return null;
        }
        
        private StateNode getNextNodeNormal(){
            Iterator<BastionInfluence> it = BossBastion.this.influences.iterator();
            
            return null;
        }
        
        private StateNode getNextNodeStarting(){
            Iterator<BastionInfluence> it = BossBastion.this.influences.iterator();
            
            while(it.hasNext()){
                BastionInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case CONSTRUCT :
                        BossBastion.this.changeAnimation(0, false);                    
                        
                        return new StateNode(BastionState.CONSTRUCTING);
                }
            }
            return null;
        }
                
        private StateNode getNextNodeConstructing(){
            if(BossBastion.this.isCurrentAnimationOver()){
                
                List<String> influences = new ArrayList<String>();
                influences.add("construct");
                for(Cannon cannonObj: BossBastion.this.cannons){
                    cannonObj.setInfluenceList(influences);
                }
                
                BossBastion.this.notifyGameEventListener(GameEventListener.EventType.LOOP_STOP, "steamEngine" + ":" + BossBastion.this.id, BossBastion.this.getPositionBody());
                
                return new StateNode(BastionState.NORMAL);
            }
            
            return null;
        }
        
        // Part animation      
        
        // Part physic
        public void updatePhysic(){
            if(BossBastion.this.lifeState == LifeState.DEAD){
                return;
            }
            /*
            Vector2 velocity = BossBastion.this.physicBody.getLinearVelocity();
            //System.out.println("Speed pre : " + velocity.x);
            
            boolean isMove = false;         
            if(!BossBastion.this.isFlying()){
                Iterator<BossBastion.BastionInfluence> it = BossBastion.this.influences.iterator();
                
                if((isMove = this.ApplyBounce(velocity)) == false){           
                    while(it.hasNext()){
                        BossBastion.BastionInfluence currentInfluence = it.next();
                        switch(currentInfluence){
                            case GO_RIGHT :
                                if(this.CanTurn(currentInfluence)){
                                    velocity.x += 0.3f;
                                    BossBastion.this.side = SideCharacter.RIGHT;
                                    isMove = true;
                                }
                                break;
                            case GO_LEFT :
                                if(this.CanTurn(currentInfluence)){
                                    velocity.x -= 0.3f;
                                    BossBastion.this.side = SideCharacter.LEFT;
                                    isMove = true;
                                }
                                break;
                        }
                    }
                }
            }
            
            // Balance physic
            float angularVelocity = BossBastion.this.physicBody.getAngularVelocity();
            Iterator<BossBastion.BastionInfluence> it = BossBastion.this.influences.iterator();
            while(it.hasNext()){
                BossBastion.BastionInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case TURN_LEFT :
                        angularVelocity -= 0.2;
                        break;
                    case TURN_RIGHT :
                        angularVelocity += 0.2;
                        break;
                }
            }
            
            if(!(isMove || BossBastion.this.isFlying())){
                if(Math.abs(velocity.x) > 1.f){
                    velocity.x -= 0.5f * (float)Math.signum(velocity.x);
                }else{
                    velocity.x = 0.f;
                }
            }
            
            // Clamp speed
            if(Math.abs(velocity.y) > 15.f){
                velocity.y = 15.f * (float)Math.signum(velocity.y);
            }
            //System.out.println("Angle : " + angle);
            BossBastion.this.physicBody.setLinearVelocity(velocity);
            BossBastion.this.physicBody.setAngularVelocity(angularVelocity);
            */
        }
        /*
        public boolean CanTurn(BossBastion.BastionInfluence currentInfluence){
            if(BossBastion.this.side == SideCharacter.LEFT && currentInfluence == BossBastion.BastionInfluence.GO_LEFT
                || BossBastion.this.side == SideCharacter.RIGHT && currentInfluence == BossBastion.BastionInfluence.GO_RIGHT){
                return true;
            }
            if(BossBastion.this.timerBeforeTurning <= 0){
                
                BossBastion.this.timerBeforeTurning = BossBastion.this.maxTimerBeforeTurning;
                
                return true;
            }
            return false;
        }*/
        
        public int isPauseAnimation(){
           return this.pauseAnimation;
        }
        
        public boolean isRestartAnimation(){
            return this.restartAnimation;
        }

        /**
         * @return the stateNode
         */
        public BastionState getStateNode() {
            return stateNode;
        }
        
        private boolean ApplyBounce(Vector2 velocity) {
            /*if(BossBastion.this.bounceTimer > 0){
                SideCharacter bounceOwnerSide = BossBastion.this.bounceSide;

                if(bounceOwnerSide == SideCharacter.LEFT){
                    if(velocity.x > 0){
                        velocity.x = 0;
                    }
                    velocity.x -= 0.3f;
                    BossBastion.this.side = SideCharacter.LEFT;
                }else{
                    if(velocity.x < 0){
                        velocity.x = 0;
                    }
                    velocity.x += 0.3f;
                    BossBastion.this.side = SideCharacter.RIGHT;
                }    
                
                return true;
            }*/
            return false;
        }
    }
    
    public class Cannon extends Character2D{
        protected final String id = UUID.randomUUID().toString();
        
        private Joint joint;
        
        protected Object2D target;

        private StateNode currentStateNode;      
        protected Set<BossBastion.CannonInfluence> influences = new HashSet<BossBastion.CannonInfluence>();
        
        private float offsetX;
        private float offsetY;
        
        private boolean canAttack;
        
        protected float attackCooldown;
        
        private SideCharacter startSide;
        
        private float constructOffsetX;
        
        public Cannon(Body ownerBody, Object2D target, World world, float posX, float posY, float offsetX, float offsetY, int high, SideCharacter side, float attackCooldown) {
            super(100);
            
            this.target = target;
            
            this.isInvulnerable = true;
            this.hasLifeBar = false;
            
            this.currentStateNode = new StateNode(BossBastion.CannonState.START);
            
            this.canAttack = true;
            
            this.attackCooldown = attackCooldown;
            
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            
            // Part physic
            this.collisionFixture = new ArrayList<Fixture>();
            BodyDef groundBodyDef = new BodyDef();    
            groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.DynamicBody);
       
            this.priority = 0;
            
            PolygonShape ground = new PolygonShape();
            ground.setAsBox(55 * P2M, 25 * P2M, new Vector2(0, 0), 0);
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
            fix.setSensor(true);
            this.collisionFixture.add(fix);
            
            this.physicBody = groundBody;
            
            // joint creation
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = ownerBody;
            jointDef.bodyB = this.physicBody;
            jointDef.localAnchorA.set(new Vector2(this.offsetX * P2M,  this.offsetY * P2M));
            jointDef.localAnchorB.set(new Vector2(60 * P2M, 0));
            jointDef.collideConnected = false;
                       
            this.startSide = side;
            
            jointDef.enableLimit = true; 
            if(this.startSide == SideCharacter.LEFT){
                if(high == 0){
                    jointDef.lowerAngle = (float) (-(Math.PI / 2 - 0.2));
                }else{
                    jointDef.lowerAngle = (float) (-Math.PI);
                }
                jointDef.upperAngle = (float) (Math.PI / 2 - 0.2);
            }else{
                jointDef.lowerAngle = (float) (Math.PI / 2 + 0.2);
                if(high == 0){
                    jointDef.upperAngle = (float) (3*Math.PI / 2 - 0.2);
                }else{
                    jointDef.upperAngle = (float) (2*Math.PI);
                }
            }
            
            float startAngle;
            if(this.startSide == SideCharacter.LEFT){
                startAngle = 0;
            }else{
                startAngle = (float) (Math.PI);
            }
            this.physicBody.setTransform(this.getPositionBody(), startAngle);
            
            this.joint = world.createJoint(jointDef);
            
            this.constructOffsetX = 0;
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
            if(this.physicBody != null){
                world.destroyJoint(this.joint);

                super.removeBody(world);
            }
        }
        
        @Override
        public void setInfluenceList(List<String> lInfluences){
            this.influences.clear();
            for(String influence : lInfluences){
                influence = influence.toLowerCase();
                if(influence.equals("right")){
                    this.influences.add(BossBastion.CannonInfluence.GO_RIGHT);
                }else if(influence.equals("left")){
                    this.influences.add(BossBastion.CannonInfluence.GO_LEFT);
                }else if(influence.equals("attack")){
                    this.influences.add(BossBastion.CannonInfluence.ATTACK);
                }else if(influence.equals("construct")){
                    this.influences.add(BossBastion.CannonInfluence.CONSTRUCT);
                }
            }
        }

        @Override
        public Sprite createCurrentSprite(){
            
            if(this.currentStateNode.stateNode != CannonState.START){
                Sprite sprite = super.createCurrentSprite();
                
                sprite.setX(sprite.getX() + this.constructOffsetX);
                
                return sprite;
            }
            
            return null;
        }
        
        @Override
        public void updateLogic(float deltaTime){
            
            if(this.physicBody == null){
                return;
            }
            
            this.currentStateNode.updateLogic(deltaTime);
            
            super.updateLogic(deltaTime);
        
            this.createInfluences();
            this.influences2Actions(deltaTime);
            
            this.influences.clear();
        }
        
        protected void createInfluences(){
            
            if(this.target == null){
                return;
            }
            
            if(this.target.getPositionBody().sub(this.getPositionBody()).len() < 800 * P2M){
                Vector2 dirCannon = new Vector2(-1, 0).rotate((float) (this.physicBody.getAngle() * 180 / Math.PI));
                Vector2 dirTarget = new Vector2(Cannon.this.target.getPositionBody().sub(Cannon.this.getPositionBody())).nor();
                if(dirTarget.crs(dirCannon) > 0){
                    this.influences.add(BossBastion.CannonInfluence.GO_RIGHT);
                }else{
                    this.influences.add(BossBastion.CannonInfluence.GO_LEFT);
                }
                
                if(Math.abs(dirTarget.crs(dirCannon)) < 0.1f && dirTarget.dot(dirCannon) > 0){
                    this.influences.add(BossBastion.CannonInfluence.ATTACK);
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
                    && prevNode.getStateNode() != BossBastion.CannonState.ATTACK 
                    && (nextNode != null && nextNode.getStateNode() == BossBastion.CannonState.ATTACK)){
                this.canAttack = false;
                Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            if(Cannon.this.physicBody != null){
                                Vector2 dirBall = new Vector2(-1, 0).rotate((float) (Cannon.this.physicBody.getAngle() * 180 / Math.PI));

                                Cannon.this.notifyObject2D2CreateListener(CannonBallTriggeredObject2D.class, Cannon.this.getPositionBody().add(dirBall.scl(Cannon.this.texture.getWidth() / 10.5f * P2M)).scl(1 / P2M), dirBall.scl(160 * P2M));

                                Cannon.this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "cannon", Cannon.this.getPositionBody());
                                Cannon.this.notifyGameEventListener(GameEventListener.EventType.SHAKESCREEN, "0.2:0.2", Cannon.this.getPositionBody());                               
                            }
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
            private BossBastion.CannonState stateNode;

            public StateNode(BossBastion.CannonState state){
                this.stateNode = state;
            }

            public void updateLogic(float deltaTime){           
                if(Cannon.this.currentStateNode.stateNode == CannonState.CONSTRUCT){
                    
                    float constuctSpeed = 100;
                    if(Cannon.this.startSide == SideCharacter.LEFT){
                        Cannon.this.constructOffsetX -= deltaTime * constuctSpeed; 
                    }else{
                        Cannon.this.constructOffsetX += deltaTime * constuctSpeed;
                    }
                    
                    Cannon.this.notifyGameEventListener(GameEventListener.EventType.LOOP, "mechanismTranslate" + ":" + Cannon.this.id, Cannon.this.getPositionBody());
                }
            }
            
            // Part nextNode
            public StateNode getNextStateNode(){

                switch(this.getStateNode()){
                    case START:
                        return getNextNodeStart();
                    case CONSTRUCT:
                        return getNextNodeConstruct();
                    case STOP:
                        return getNextNodeStop();
                    case MOVE:
                        return getNextNodeMove();
                    case ATTACK:
                        return getNextNodeAttack();

                } 
                return null;
            }
            
            private StateNode getNextNodeConstruct(){
                boolean canMove = false;
                if(Cannon.this.startSide == SideCharacter.LEFT){
                    canMove = Cannon.this.constructOffsetX <= 0;
                }else{
                    canMove = Cannon.this.constructOffsetX >= 0;
                }
                          
                if(canMove){
                    Cannon.this.constructOffsetX = 0;
                    Cannon.this.physicBody.setFixedRotation(false);
                    Cannon.this.physicBody.setAwake(true);
                    
                    Cannon.this.notifyGameEventListener(GameEventListener.EventType.LOOP_STOP, "mechanismTranslate" + ":" + Cannon.this.id, Cannon.this.getPositionBody());
                    Cannon.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "steamPressure", Cannon.this.getPositionBody());
                    
                    return new StateNode(BossBastion.CannonState.MOVE); 
                }
                
                return null;
            }

            private StateNode getNextNodeStart(){
                
                Iterator<BossBastion.CannonInfluence> it = Cannon.this.influences.iterator();
                
                while(it.hasNext()){
                    BossBastion.CannonInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case CONSTRUCT :
                            
                            if(Cannon.this.startSide == SideCharacter.LEFT){
                                Cannon.this.physicBody.setTransform(Cannon.this.getPositionBody(), 0);
                            }else{
                                Cannon.this.physicBody.setTransform(Cannon.this.getPositionBody(), (float) (Math.PI));
                            }
                            Cannon.this.physicBody.setFixedRotation(true);
                            
                            if(Cannon.this.startSide == SideCharacter.LEFT){
                                Cannon.this.constructOffsetX = 200;
                            }else{
                                Cannon.this.constructOffsetX = -200;
                            }
                            return new StateNode(BossBastion.CannonState.CONSTRUCT);
                            
                    }
                }
                
                return null;
            }
            
            private StateNode getNextNodeStop(){
                
                Iterator<BossBastion.CannonInfluence> it = Cannon.this.influences.iterator();
                
                while(it.hasNext()){
                    BossBastion.CannonInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                        case GO_LEFT :
                            return new StateNode(BossBastion.CannonState.MOVE);
                        case ATTACK :
                            if(Cannon.this.canAttack){
                                return new StateNode(BossBastion.CannonState.ATTACK);
                            }
                            
                    }
                }
                
                return null;
            }
            
            private StateNode getNextNodeAttack(){
                if(Cannon.this.isCurrentAnimationOver()){
                    return new StateNode(BossBastion.CannonState.STOP);
                }
                return null;
            }

            private StateNode getNextNodeMove(){                
                Iterator<BossBastion.CannonInfluence> it = Cannon.this.influences.iterator();
                
                boolean isStillMoving = false;
                
                while(it.hasNext()){
                    BossBastion.CannonInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                        case GO_LEFT :                           
                            isStillMoving = true;
                            break;
                        case ATTACK :
                            if(Cannon.this.canAttack){
                                return new StateNode(BossBastion.CannonState.ATTACK);
                            }
                            
                    }
                }
                
                if(isStillMoving){
                    return null;
                }
                return new StateNode(BossBastion.CannonState.STOP);
            }
            
            private double getTrueAngle(double angle){
                angle %= (Math.PI * 2);
                
                if(angle > Math.PI){
                    angle = angle - (Math.PI * 2);
                }
                if(angle < -180){
                    angle = angle + (Math.PI * 2);
                }
                return angle;
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
                
                if(Cannon.this.currentStateNode.stateNode == CannonState.START
                    || Cannon.this.currentStateNode.stateNode == CannonState.CONSTRUCT){
                    return;
                }
                
                Iterator<BossBastion.CannonInfluence> it = Cannon.this.influences.iterator();
            
                while(it.hasNext()){
                    BossBastion.CannonInfluence currentInfluence = it.next();
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
                float angle = (float) this.getTrueAngle(Cannon.this.physicBody.getAngle());
                
                if(Cannon.this.side == SideCharacter.LEFT){
                    if(angle < -(Math.PI / 2 - 0.2)
                       || angle > Math.PI / 2){
                        Cannon.this.physicBody.applyAngularImpulse((float) (20000 * deltaTime * Math.PI / 180), true);
                    }

                    /*if(Cannon.this.physicBody.getAngle() > 0){
                        Cannon.this.angularDir = -1;
                    }*/
                }else{
                    if(angle > -(Math.PI / 2 + 0.2)
                        && angle < Math.PI / 2){
                        Cannon.this.physicBody.applyAngularImpulse((float) (-20000 * deltaTime * Math.PI / 180), true);
                    }

                    /*if(Cannon.this.physicBody.getAngle() < -Math.PI){
                        Cannon.this.angularDir = 1;
                    }*/
                }
            }

            /**
             * @return the stateNode
             */
            public BossBastion.CannonState getStateNode() {
                return stateNode;
            }
        }    
    }
    
    protected enum CannonState{
        START,
        CONSTRUCT,
        STOP,
        MOVE,
        ATTACK
    }

    protected enum CannonInfluence{
        CONSTRUCT,
        GO_RIGHT,
        GO_LEFT,
        ATTACK
    }
}
