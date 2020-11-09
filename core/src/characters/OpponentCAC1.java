/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.graphics.g2d.Animation;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ressourcesmanagers.TextureManager;
import triggered.TeethTriggeredObject2D;

/**
 *
 * @author françois
 */
public class OpponentCAC1 extends Character2D{
   
    //private static final String OPPCAC1TEXT = "character" + File.separator + "opponentCAC1.png";
    private static final String OPPCAC1TEXT = "character/opponentCAC1.png";
    
    
    private static final float ATT_DIST = P2M * 70;
    private static final float MOVE_DIST = P2M * 200;
    
    protected StateNode currentStateNode;
    
    protected Set<OppInfluence> influences = new HashSet<OppInfluence>();
    
    protected Object2D target;
    
    protected DamageActionFixture damageActionFixture;
    
    protected boolean canAttack;
    
    protected float maxSpeed;
    
    protected SideCharacter previousSide;
    
    protected boolean noPurse;
    
    protected Vector2 spawnPoint;
    protected float maxDistanceFromSpawn;
    protected boolean isReseting;
    
    public OpponentCAC1(){
       this(100, null);    
    }
    
    public OpponentCAC1(int lifePoint, Object2D target){
        super(lifePoint);
        
        this.side = SideCharacter.RIGHT;
        
        this.currentStateNode = new OpponentCAC1.StateNode(OpponentCAC1.OppState.NORMAL);
        
        this.canAttack = true;
        
        this.target = target;
        
        this.maxSpeed = 2f;
        
        this.noPurse = false;
        
        this.spawnPoint = new Vector2();
        this.maxDistanceFromSpawn = -1f;
        this.isReseting = false;
    }
    
    public OpponentCAC1(World world, Object2D target, float posX, float posY) {
        super(100);
        this.target = target;
        
        this.side = SideCharacter.RIGHT;
        
        this.currentStateNode = new OpponentCAC1.StateNode(OpponentCAC1.OppState.NORMAL);
        
        this.canAttack = true;
        
        this.maxSpeed = 2f;
        
        this.noPurse = false;
        
        this.spawnPoint = new Vector2(posX * P2M, posY * P2M);
        this.maxDistanceFromSpawn = -1f;
        this.isReseting = false;
        
        // Part graphic
        this.assignTextures();
        
        // Part Physic
        this.initializePhysicCAC1(world, posX, posY);
        
        this.previousSide = this.side;
    }
    
    public void initialize(World world, Object2D target, float posX, float posY){
        this.lifePoints = 100;
        
        this.target = target;
        
        this.side = SideCharacter.RIGHT;
        
        this.currentStateNode = new OpponentCAC1.StateNode(OpponentCAC1.OppState.NORMAL);
        
        this.canAttack = true;
        
        this.maxSpeed = 2f;
        
        this.noPurse = false;
        
        this.spawnPoint = new Vector2(posX * P2M, posY * P2M);
        this.maxDistanceFromSpawn = -1f;
        this.isReseting = false;
        
        // Part graphic
        this.assignTextures();
        
        // Part Physic
        this.initializePhysicCAC1(world, posX, posY);
        
        this.previousSide = this.side;
    }
    
    protected void updateFixture(){
        
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
        }
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        this.influences.clear();
        for(String influence : lInfluences){
            influence = influence.toLowerCase();
            if(influence.equals("right")){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else if(influence.equals("left")){
                this.influences.add(OppInfluence.GO_LEFT);
            }else if(influence.equals("attack")){
                this.influences.add(OppInfluence.ATTACK);
            }else if(influence.equals("jump")){
                this.influences.add(OppInfluence.JUMP);
            }else if(influence.equals("nopurse")){
                this.influences.add(OppInfluence.CINE_NO_PURSE);
            }
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        createInfluencesCAC1();
        
        if(this.isInvulnerable){
            this.influences.remove(OppInfluence.ATTACK);
        }
        
        influences2Actions(deltaTime);
        
        if(this.side != this.previousSide){
            this.updateFixture();
            this.previousSide = this.side;
        }
    }
    
    protected final void initializeGraphic()
    {
        // Part graphic
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 76, 76);
        // walk
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(9, 9);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
        array = new Array<TextureRegion>(tmp[1]);
        array.removeRange(0, 0);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
        // attack
        array = new Array<TextureRegion>(tmp[2]);
        array.removeRange(3, 9);
        this.listAnimations.add(new Animation(0.3f, array));
        array = new Array<TextureRegion>(tmp[2]);
        array.removeRange(0, 6);
        this.listAnimations.add(new Animation(0.3f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.REVERSED);
        // death
        array = new Array<TextureRegion>(tmp[3]);
        array.removeRange(3, 9);
        this.listAnimations.add(new Animation(0.3f, array));
        array = new Array<TextureRegion>(tmp[3]);
        array.removeRange(0, 6);
        this.listAnimations.add(new Animation(0.3f, array));
        // flying
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 8);
        this.listAnimations.add(new Animation(10, array));
        array = new Array<TextureRegion>(tmp[1]);
        array.removeRange(1, 9);
        this.listAnimations.add(new Animation(10, array));
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void assignTextures(){
        
        this.texture = TextureManager.getInstance().getTexture(OPPCAC1TEXT, this);
        
        if(this.texture != null){
            this.initializeGraphic();
            
            this.listAnimations.get(2).setFrameDuration(0.15f);
            this.listAnimations.get(3).setFrameDuration(0.15f);
        }
    } 
    
    public final void initializeSimpleGraphic(){
        // Part graphic
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 76, 76);
        // walk
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[1]);
        array.removeRange(4, 4);
        array.add(tmp[1][2]);
        array.add(tmp[1][1]);
        this.listAnimations.add(new Animation(0.15f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(4, 4);
        array.add(tmp[0][2]);
        array.add(tmp[0][1]);
        this.listAnimations.add(new Animation(0.15f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
        // attack
        array = new Array<TextureRegion>(tmp[2]);
        this.listAnimations.add(new Animation(0.3f, array));
        array = new Array<TextureRegion>(tmp[3]);
        this.listAnimations.add(new Animation(0.3f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.REVERSED);
        // death
        array = new Array<TextureRegion>(tmp[3]);
        this.listAnimations.add(new Animation(0.3f, array));
        array = new Array<TextureRegion>(tmp[3]);
        this.listAnimations.add(new Animation(0.3f, array));
        // flying
        array = new Array<TextureRegion>(tmp[3]);
        array.removeRange(0, 2);
        this.listAnimations.add(new Animation(0.2f, array));
        array = new Array<TextureRegion>(tmp[2]);
        array.removeRange(0, 2);
        this.listAnimations.add(new Animation(0.2f, array));
        
        this.changeAnimation(0, true);
    }
    
    protected final void initializePhysicCAC1(World world, float posX, float posY){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);

        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(19 * P2M);
        circle.setPosition(new Vector2(0, 0));
        //circle.setPosition(new Vector2(38 * P2M, 14 * P2M));
        /*PolygonShape granMa = new PolygonShape();
        granMa.setAsBox(25 * P2M, 18 * P2M, new Vector2(0, -13 * P2M), 0);*/  
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 5f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f; 
        body.setFixedRotation(true);
        Fixture fix = body.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);

        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox(8 * P2M, 3 * P2M, new Vector2(0, -20 * P2M), 0);
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
        circle = new CircleShape();
        circle.setRadius(30 * P2M);
        circle.setPosition(new Vector2(0, 0));
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        fix = this.physicBody.createFixture(fixtureDef);
        Set<Fixture> setDamage = new HashSet<Fixture>();
        setDamage.add(fix);
        this.damageActionFixture = new DamageActionFixture(setDamage, 1);
    }
    
    @Override
    protected void onDeath(){
        for(Fixture fixture : this.collisionFixture){
           this.physicBody.destroyFixture(fixture);
        }
        this.collisionFixture.clear();
        PolygonShape feet = new PolygonShape();
        feet.setAsBox(18 * P2M, 8 * P2M, new Vector2(0, -10 * P2M), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = feet;
        fixtureDef.density = 10f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        Fixture fix = this.physicBody.createFixture(fixtureDef);
        fix.setUserData(this);
        this.collisionFixture.add(fix);
        
        Vector2 dirVelocity = this.getBodyVelocity().nor();
        Vector2 upVector = new Vector2(0, 1);
        float angle = dirVelocity.angle(upVector) / 2f;
        dirVelocity = dirVelocity.rotate(angle);
        
        if(Math.random() < 0.3){
            this.notifyObject2D2CreateListener(TeethTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), dirVelocity.scl(-3f));
        }
        if(Math.random() < 0.1){
            this.notifyObject2D2CreateListener(TeethTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), dirVelocity.scl(-4f));
        }
        
        this.notifyGameEventListener(GameEventListener.EventType.DEATH, this.name, this.getPositionBody());
    }
    
    protected void createInfluencesDIST1(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        
        if(this.target == null){
            return;
        }
        
        if(this.maxDistanceFromSpawn > 0 
                && (this.physicBody.getPosition().dst(this.spawnPoint) > this.maxDistanceFromSpawn
                    || (this.isReseting && this.physicBody.getPosition().dst(this.spawnPoint) > this.maxDistanceFromSpawn / 2))){
            if(!this.isReseting){
                this.isReseting = true;
            }
            if(this.spawnPoint.x - this.physicBody.getPosition().x > 0){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else{
                this.influences.add(OppInfluence.GO_LEFT);
            }      
        }else{
            if(this.isReseting){
                this.isReseting = false;
            }
            if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
                if(this.target.getPositionBody().x - this.physicBody.getPosition().x > 0){
                    this.influences.add(OppInfluence.GO_RIGHT);
                }else{
                    this.influences.add(OppInfluence.GO_LEFT);
                }
            }else{
                double rand = Math.random()*100;
                if(rand > 10){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }else{
                        this.influences.add(OppInfluence.GO_LEFT);
                    }
                }else if(rand > 8){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_LEFT);
                    }else{
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }
                }
            }
        }
        
        if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
            if(this.canAttack
                    && this.target.getPositionBody().y - this.physicBody.getPosition().y > 75 * P2M 
                    && this.target.getPositionBody().y - this.physicBody.getPosition().y < 150 * P2M){
                this.influences.add(OppInfluence.JUMP);
            }

            if(Math.abs(this.target.getPositionBody().sub(this.physicBody.getPosition()).len()) <  300 * P2M
                    && Math.abs(this.target.getPositionBody().y - this.physicBody.getPosition().y) < 50 * P2M){
                if(this.isReseting){
                    this.isReseting = false;
                }
                this.influences.add(OppInfluence.ATTACK);
            }
        }
    }
    
    protected void createInfluencesCAC1(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        
        if(this.target == null){
            return;
        }
        
        if(this.maxDistanceFromSpawn > 0 
                && (Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn
                    || (this.isReseting && Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn / 2))){
            if(!this.isReseting){
                this.isReseting = true;
            }
            if(this.spawnPoint.x - this.physicBody.getPosition().x > 0){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else{
                this.influences.add(OppInfluence.GO_LEFT);
            }      
        }else{
            if(this.isReseting){
                this.isReseting = false;
            }
            if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
                if(this.target.getPositionBody().x - this.physicBody.getPosition().x > 0){
                    this.influences.add(OppInfluence.GO_RIGHT);
                }else{
                    this.influences.add(OppInfluence.GO_LEFT);
                }
            }else{
                double rand = Math.random()*100;
                if(rand > 10){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }else{
                        this.influences.add(OppInfluence.GO_LEFT);
                    }
                }else if(rand > 8){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_LEFT);
                    }else{
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }
                }
            }
        }
        
        if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
            if(Math.abs(this.target.getPositionBody().sub(this.physicBody.getPosition()).len()) <  50 * P2M
                    && Math.abs(this.target.getPositionBody().y - this.physicBody.getPosition().y) < 50 * P2M){
                if(this.isReseting){
                    this.isReseting = false;
                }    
                this.influences.add(OppInfluence.ATTACK);
            }  
        }
    }
    
    protected void createInfluencesTemeri(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        
        if(this.target == null){
            return;
        }
        
        if(this.maxDistanceFromSpawn > 0 
                && (Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn
                    || (this.isReseting && Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn / 2))){
            if(!this.isReseting){
                this.isReseting = true;
            }
            if(this.spawnPoint.x - this.physicBody.getPosition().x > 0){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else{
                this.influences.add(OppInfluence.GO_LEFT);
            }      
        }else{
            if(this.isReseting){
                this.isReseting = false;
            }
            
            if(this.side == SideCharacter.LEFT
                    && this.target.getPositionBody().x - this.physicBody.getPosition().x > 0){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else if(this.side == SideCharacter.RIGHT
                    && this.target.getPositionBody().x - this.physicBody.getPosition().x < 0){
                this.influences.add(OppInfluence.GO_LEFT);
            }
                       
            /*
            if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
                if(this.target.getPositionBody().x - this.physicBody.getPosition().x > 0){
                    if(this.side == SideCharacter.LEFT){
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }
                }else{
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_LEFT);
                    }
                }
            }else{
                double rand = Math.random()*100;
                if(rand > 10){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }else{
                        this.influences.add(OppInfluence.GO_LEFT);
                    }
                }else if(rand > 8){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_LEFT);
                    }else{
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }
                }
            }*/
        }
    }
    
    protected void createInfluencesCAC2(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        
        if(this.target == null){
            return;
        }       
        
        if(this.maxDistanceFromSpawn > 0 
                && (Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn
                    || (this.isReseting && Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn / 2))){
            if(!this.isReseting){
                this.isReseting = true;
            }
            
            if(this.spawnPoint.x - this.physicBody.getPosition().x > 0){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else{
                this.influences.add(OppInfluence.GO_LEFT);
            }      
        }else{
            if(this.isReseting){
                this.isReseting = false;
            }
            if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
                if(this.target.getPositionBody().x - this.physicBody.getPosition().x > 0){
                    this.influences.add(OppInfluence.GO_RIGHT);
                }else{
                    this.influences.add(OppInfluence.GO_LEFT);
                }
            }else{
                double rand = Math.random()*100;
                if(rand > 10){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }else{
                        this.influences.add(OppInfluence.GO_LEFT);
                    }
                }else if(rand > 8){
                    if(this.side == SideCharacter.RIGHT){
                        this.influences.add(OppInfluence.GO_LEFT);
                    }else{
                        this.influences.add(OppInfluence.GO_RIGHT);
                    }
                }
            }
        }
         
        if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST){
            if(this.canAttack
                    && this.target.getPositionBody().y - this.physicBody.getPosition().y > 40 * P2M 
                    && this.target.getPositionBody().y - this.physicBody.getPosition().y < 150 * P2M){
                this.influences.add(OppInfluence.JUMP);
            }

            if(Math.abs(this.target.getPositionBody().sub(this.physicBody.getPosition()).len()) <  60 * P2M
                    && Math.abs(this.target.getPositionBody().y - this.physicBody.getPosition().y) < 50 * P2M){
                if(this.isReseting){
                    this.isReseting = false;
                }
                this.influences.add(OppInfluence.ATTACK);
            }
        }  
    }
    
    protected void createInfluencesCACElite(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        
        if(this.target == null){
            return;
        }
        
        
        if(this.maxDistanceFromSpawn > 0 
                && (Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn
                    || (this.isReseting && Math.abs(this.physicBody.getPosition().x - this.spawnPoint.x) > this.maxDistanceFromSpawn / 2))){
            if(!this.isReseting){
                this.isReseting = true;
            }
            if(this.spawnPoint.x - this.physicBody.getPosition().x > 0){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else{
                this.influences.add(OppInfluence.GO_LEFT);
            }      
        }else{
            if(this.isReseting){
                this.isReseting = false;
            }
            if(this.target.getPositionBody().dst(this.physicBody.getPosition()) < MOVE_DIST * 1.5){
                if(this.target.getPositionBody().x - this.physicBody.getPosition().x > 0){
                    this.influences.add(OppInfluence.GO_RIGHT);
                }else{
                    this.influences.add(OppInfluence.GO_LEFT);
                }
            }
        }
        
        if(this.canAttack
                && this.target.getPositionBody().y - this.physicBody.getPosition().y > 50 * P2M 
                && this.target.getPositionBody().y - this.physicBody.getPosition().y < 150 * P2M){
            this.influences.add(OppInfluence.JUMP);
        }

        if(Math.abs(this.target.getPositionBody().sub(this.physicBody.getPosition()).len()) <  60 * P2M
                && Math.abs(this.target.getPositionBody().y - this.physicBody.getPosition().y) < 50 * P2M){
            if(this.isReseting){
                this.isReseting = false;
            }
            this.influences.add(OppInfluence.ATTACK);
        } 
    }
    
    protected void influences2Actions(float deltaTime){
        Iterator<OppInfluence> it = OpponentCAC1.this.influences.iterator();
        this.noPurse = false;
        while(it.hasNext()){
            OppInfluence currentInfluence = it.next();
            switch(currentInfluence){
                case CINE_NO_PURSE:
                    this.noPurse = true;
                break;
            }
        } 
        
        StateNode prevNode = this.currentStateNode;
        StateNode nextNode = this.currentStateNode.getNextStateNode();
        if(nextNode != null){
            this.currentStateNode = nextNode;
        }
        
        int animIndex = this.currentStateNode.getCurrentAnimation();
        boolean restartAnimation = this.currentStateNode.isRestartAnimation();
        
        int pauseAnim = this.currentStateNode.isPauseAnimation();
        if(animIndex >= 0 && (this.currentAnimation != animIndex || (pauseAnim == 0) == this.pause)){
            if(prevNode != this.currentStateNode || restartAnimation){
                switch(pauseAnim){
                    case 0:
                        this.changeAnimation(animIndex, false);
                        break;
                    case 1 :
                        this.changeAnimation(animIndex, true, 10f);
                        break;
                    case -1 :
                        this.changeAnimation(animIndex, true);
                        break;
                }
            }else{
                switch(pauseAnim){
                    case 0:
                        this.changeAnimation(animIndex, false, this.animationTime);
                        break;
                    case 1 :
                        this.changeAnimation(animIndex, true, this.animationTime);
                        break;
                    case -1 :
                        this.changeAnimation(animIndex, true, this.animationTime);
                        break;
                }
            }
        }
        
        this.updateAttack(prevNode, nextNode, deltaTime);
        
        this.currentStateNode.updatePhysic();
                
        this.influences.clear();
    }
    
    protected void updateAttack(OpponentCAC1.StateNode prevNode, OpponentCAC1.StateNode nextNode, final float deltaTime){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.canAttack
                && prevNode.getStateNode() != OppState.ATTACK 
                && (nextNode != null && nextNode.getStateNode() == OppState.ATTACK)){
            this.canAttack = false;
            
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    if(!OpponentCAC1.this.isInvulnerable){
                        OpponentCAC1.this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "punch", new Vector2(OpponentCAC1.this.getPositionBody()));
                        
                        OpponentCAC1.this.damageActionFixture.applyAction(deltaTime, OpponentCAC1.this);
                    }
                }
            }, 0.3f);
            
            
            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        OpponentCAC1.this.canAttack = true;
                    }         
            }, 2f);
        }
    }
    
    /**
     * @param maxDistance the maxDistanceFromSpawn to set
     */
    public void setMaxDistance(float maxDistance) {
        this.maxDistanceFromSpawn = maxDistance * P2M;
    }
    
    protected enum OppState{
        NORMAL,
        ATTACK
    }
    
    protected enum OppInfluence{
        JUMP,
        GO_RIGHT,
        GO_LEFT,
        ATTACK,
        CINE_NO_PURSE
    }
    
    protected class StateNode{
        private OppState stateNode;
        
        private int pauseAnimation;
        
        private boolean restartAnimation;
        
        public StateNode(OppState state){
            this.stateNode = state;
            
            this.pauseAnimation = 0;
            
            this.restartAnimation = true;
        }
        
        // Part nextNode
        public StateNode getNextStateNode(){
            if(OpponentCAC1.this.lifeState == LifeState.DEAD)
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
            Iterator<OppInfluence> it = OpponentCAC1.this.influences.iterator();
            
            while(it.hasNext()){
                OppInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case ATTACK :
                        if(OpponentCAC1.this.canAttack){
                            return new StateNode(OppState.ATTACK);
                        }
                }
            }
            return null;
        }
        
        private StateNode getNextNodeAttack(){
            if(OpponentCAC1.this.isCurrentAnimationOver()){
                return new StateNode(OppState.NORMAL);
            }
            return null;
        }
         
        // Part animation 
        public int getCurrentAnimation(){
            if(OpponentCAC1.this.lifeState == LifeState.DEAD){
                this.pauseAnimation = 0;
                this.restartAnimation = true;
                if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                    return 4;
                }else{
                    return 5;
                }
            }

            
            if(OpponentCAC1.this.isInvulnerable){
                this.pauseAnimation = -1;
                if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                    return 4;
                }else{
                    return 5;
                }
            }
            
            switch(this.getStateNode()){
                case NORMAL:
                    return getAnimationNormal();
                case ATTACK:
                    return getAnimationAttack();

            } 
            return -1;
        }
        
        private int getAnimationNormal(){
            this.restartAnimation = true;
            if(OpponentCAC1.this.isFlying()){
                if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                    this.pauseAnimation = -1;
                    return 6;
                }else{
                    this.pauseAnimation = -1;
                    return 7;
                }
            }else{
                boolean isMove = false;
                
                Iterator<OppInfluence> it = OpponentCAC1.this.influences.iterator();
                
                while(it.hasNext() && !isMove){
                    OppInfluence influence = it.next(); 
                    if(influence == OppInfluence.GO_LEFT || influence == OppInfluence.GO_RIGHT){
                        isMove = true;
                    }
                }
                
                if(OpponentCAC1.this.noPurse){
                    if(isMove){
                        this.pauseAnimation = 0;
                        if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                            return 8;
                        }else{
                            return 9;
                        }
                    }else{
                        if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                            this.pauseAnimation = -1;
                            return 8;
                        }else{
                            this.pauseAnimation = 1;
                            return 9;
                        }
                    }
                }else{
                    if(isMove){
                        this.pauseAnimation = 0;
                        if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                            return 0;
                        }else{
                            return 1;
                        }
                    }else{
                        if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                            this.pauseAnimation = -1;
                            return 0;
                        }else{
                            this.pauseAnimation = 1;
                            return 1;
                        }
                    }
                }
            }
        }
        
        private int getAnimationAttack(){
            this.pauseAnimation = 0;
            this.restartAnimation = false;
            if(OpponentCAC1.this.side == SideCharacter.RIGHT){
                return 2;
            }else{
                return 3;
            }
        }
        
        // Part physic
        public void updatePhysic(){
            if(OpponentCAC1.this.lifeState == LifeState.DEAD){
                return;
            }
            
            Vector2 velocity = OpponentCAC1.this.physicBody.getLinearVelocity();
            
            boolean isMove = false;
            if(!OpponentCAC1.this.isFlying()){
                Iterator<OpponentCAC1.OppInfluence> it = OpponentCAC1.this.influences.iterator();
                
                while(it.hasNext()){
                    OpponentCAC1.OppInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case JUMP :
                            velocity.y += 4f;
                            isMove = true;
                            break;

                    }
                }
            }
            
            if(this.getStateNode() != OpponentCAC1.OppState.ATTACK){
                Iterator<OpponentCAC1.OppInfluence> it = OpponentCAC1.this.influences.iterator();

                isMove = false;
                
                while(it.hasNext()){
                    OpponentCAC1.OppInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                            velocity.x += 0.1f;
                            OpponentCAC1.this.side = SideCharacter.RIGHT;
                            isMove = true;
                            break;
                        case GO_LEFT :
                            velocity.x -= 0.1f;
                            OpponentCAC1.this.side = SideCharacter.LEFT;
                            isMove = true;
                            break;
                    }
                }              
            }
            
            if(!(isMove || OpponentCAC1.this.isFlying())){
                if(Math.abs(velocity.x) > 1.f){
                    velocity.x -= 0.5f * (float)Math.signum(velocity.x);
                }else{
                    velocity.x = 0.f;
                }
            }
            
            if(Math.abs(velocity.x) > OpponentCAC1.this.maxSpeed){
                velocity.x = OpponentCAC1.this.maxSpeed * (float)Math.signum(velocity.x);
            }
            
            // Clamp speed
            if(Math.abs(velocity.y) > 15.f){
                velocity.y = 15.f * (float)Math.signum(velocity.y);
            }
            
            OpponentCAC1.this.physicBody.setLinearVelocity(velocity);
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
        public OppState getStateNode() {
            return stateNode;
        }
    }
    
}
