/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.ShieldActionFixture;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class Grandma extends Character2D{
    
    public static final int LEFT_RIGHT_DIST = 18;
    
    private static final String GRANDMATEXT = "character/spritemapmeme.png";
    
    private static final String DENTIERTEXT = "spritemapdentierV3-01.png";
    TextureRegion[][] lifeSprites;
    
    private ShieldActionFixture damageZone;
    
    private Set<GrandmaInfluence> influences = new HashSet<GrandmaInfluence>();
    
    private StateNode previousStateNode;
    private StateNode currentStateNode;
    
    private SideCharacter previousSide; 
    
    private boolean gotPurse;
   
    
    public Grandma(World world, float posX, float posY){
        super(12);
        
        this.timeInvulnerabilitySec = 2.f;
        this.scaleDamageForce = 0.2f;
        
        this.isAnOpponent = false;
        
        this.side = SideCharacter.RIGHT;
        this.previousSide = SideCharacter.RIGHT;
        
        this.currentStateNode = new StateNode(GrandmaState.UNFOLDED_UMBRELLA_UP);
        this.previousStateNode = this.currentStateNode;
        
        this.gotPurse = false;
        
        // Part graphic
        this.assignTextures();
        
        // Part Physic
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        this.physicBody = body;
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        updateFixture();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(GRANDMATEXT, this);
        
        Texture dentier = TextureManager.getInstance().getTexture(DENTIERTEXT, this);
        
        if(this.texture != null
                && dentier != null){
            
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 76, 76);
            // walk folded
            this.listAnimations.add(new Animation(0.2f, tmp[0]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            this.listAnimations.add(new Animation(0.2f, tmp[1]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            // attack
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[2]);
            array.removeRange(3, 7);
            this.listAnimations.add(new Animation(0.1f, array));
            array = new Array<TextureRegion>(tmp[2]);
            array.removeRange(0, 4);
            this.listAnimations.add(new Animation(0.1f, array));
            // walk unfolded side
            this.listAnimations.add(new Animation(0.2f, tmp[3]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            this.listAnimations.add(new Animation(0.2f, tmp[4]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            // flying
            array = new Array<TextureRegion>(tmp[5]);
            array.removeRange(0, 0);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            array = new Array<TextureRegion>(tmp[6]);
            array.removeRange(0, 0);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            // walk unfolded up
            this.listAnimations.add(new Animation(0.2f, tmp[7]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            this.listAnimations.add(new Animation(0.2f, tmp[8]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            // jump
            array = new Array<TextureRegion>(tmp[9]);
            array.removeRange(3, 7);
            this.listAnimations.add(new Animation(0.1f, array));
            array = new Array<TextureRegion>(tmp[9]);
            array.removeRange(0, 4);
            this.listAnimations.add(new Animation(0.1f, array));
            // swing
            array = new Array<TextureRegion>(tmp[10]);
            array.removeRange(0, 5);
            array.removeRange(1, 1);
            this.listAnimations.add(new Animation(0.3f, array));
            array = new Array<TextureRegion>(tmp[10]);
            array.removeRange(0, 6);
            this.listAnimations.add(new Animation(0.3f, array));        
            // Jump unfolded
            array = new Array<TextureRegion>(tmp[11]);
            array.removeRange(3, 7);
            this.listAnimations.add(new Animation(0.1f, array));
            array = new Array<TextureRegion>(tmp[11]);
            array.removeRange(0, 4);
            this.listAnimations.add(new Animation(0.1f, array)); 
            // open umbrella
            array = new Array<TextureRegion>(tmp[10]);
            array.removeRange(3, 7);
            this.listAnimations.add(new Animation(0.3f, array));
            array = new Array<TextureRegion>(tmp[10]);
            array.removeRange(6, 7);
            array.removeRange(0, 2);
            this.listAnimations.add(new Animation(0.3f, array));
            //death
            array = new Array<TextureRegion>(tmp[12]);
            array.removeRange(4, 7);
            this.listAnimations.add(new Animation(0.3f, array));
            array = new Array<TextureRegion>(tmp[12]);
            array.removeRange(0, 3);
            this.listAnimations.add(new Animation(0.3f, array));
            
            //cinematic animations
            // back on feet
            array = new Array<TextureRegion>(tmp[17]);
            array.removeRange(4, 7);
            this.listAnimations.add(new Animation(0.3f, array));
            array = new Array<TextureRegion>(tmp[17]);
            array.removeRange(0, 3);
            this.listAnimations.add(new Animation(0.3f, array));
            // with purse
            this.listAnimations.add(new Animation(0.2f, tmp[18]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            this.listAnimations.add(new Animation(0.2f, tmp[19]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            
            this.changeAnimation(0, true);

            // Life sprite
            this.lifeSprites = TextureRegion.split(dentier, 76, 76);
        }
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        this.influences.clear();
        for(String influence : lInfluences){
            influence = influence.toLowerCase();
            if(influence.equals("right")){
                this.influences.add(GrandmaInfluence.GO_RIGHT);
            }else if(influence.equals("left")){
                this.influences.add(GrandmaInfluence.GO_LEFT);
            }else if(influence.equals("attack")){
                this.influences.add(GrandmaInfluence.ATTACK);
            }else if(influence.equals("jump")){
                this.influences.add(GrandmaInfluence.JUMP);
            }else if(influence.equals("up")){
                this.influences.add(GrandmaInfluence.UMB_UP);
            }else if(influence.equals("down")){
                this.influences.add(GrandmaInfluence.UMB_DOWN);
            }else if(influence.equals("switch")){
                this.influences.add(GrandmaInfluence.UMB_SWITCH);
            }else if(influence.equals("onground")){
                this.influences.add(GrandmaInfluence.CINE_ON_GROUND);
            }else if(influence.equals("onfeet")){
                this.influences.add(GrandmaInfluence.CINE_BACK_ON_FEET);
            }else if(influence.equals("walk")){
                this.influences.add(GrandmaInfluence.CINE_GO_WALK);
            }else if(influence.equals("gotpurse")){
                this.influences.add(GrandmaInfluence.CINE_GOT_PURSE);
            }
        }
    }
            
    
    @Override
    public void updateLogic(float deltaTime){
        
        super.updateLogic(deltaTime);
        
        createInfluences();
        
        influences2Actions(deltaTime);
        
        // apply damages OR bounce
        updateShield(deltaTime);
        
        if(this.side != this.previousSide){
            updateFixture();
            if(this.side == SideCharacter.LEFT){
                this.physicBody.setTransform(this.physicBody.getPosition().x - LEFT_RIGHT_DIST * 1.4f * P2M, this.physicBody.getPosition().y, 0);
            }else{
                this.physicBody.setTransform(this.physicBody.getPosition().x + LEFT_RIGHT_DIST * 1.4f * P2M, this.physicBody.getPosition().y, 0);
            }
            this.previousSide = this.side;
            
        }
        
        if(this.previousStateNode.stateNode != this.currentStateNode.stateNode 
                && (this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_UP || this.previousStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_UP)){
           updateFixture(); 
        }
        
        this.previousStateNode = this.currentStateNode;
    }
    
    private void updateShield(float deltaTime){
        
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_MIDDLE || this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_UP){
            this.damageZone.setApplyDamage(false);
            this.damageZone.applyAction(deltaTime, this);
        }else if(this.currentStateNode.stateNode == GrandmaState.FOLDED_UMBRELLA_ATTACK){
            this.damageZone.setApplyDamage(true);
            this.damageZone.applyAction(deltaTime, this, 0.15f);
        }
    }
    
    private void updateFixture(){
        
        if(this.feetFixture != null)
            this.physicBody.destroyFixture(this.feetFixture);
        
        float restitution = 0.1f;
        if(Grandma.this.collisionFixture.size() > 0){
            Fixture fix = Grandma.this.collisionFixture.get(0);
            restitution = fix.getRestitution();
        }
        
        for(Fixture fix : this.collisionFixture){
            this.physicBody.destroyFixture(fix);
        }
        this.collisionFixture.clear();
        
        if(this.damageZone != null){
            this.damageZone.dispose(this.physicBody);
        }
        
        float yOffset = 0f;
        boolean hasToRotate = false;
        if(this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_UP){
            yOffset = 30f;
            hasToRotate = true;
        }
        
        if(this.side == SideCharacter.RIGHT){
            CircleShape circleLeft = new CircleShape();
            circleLeft.setRadius(10 * P2M);
            circleLeft.setPosition(new Vector2(-LEFT_RIGHT_DIST * P2M, -20 * P2M));
            
            FixtureDef fixtureDef = new FixtureDef();
            
            this.setCollisionFilterMask(fixtureDef, false);
            
            fixtureDef.shape = circleLeft;
            fixtureDef.density = 1f; 
            fixtureDef.friction = 0.1f;
            fixtureDef.restitution = restitution; 
            
            Fixture fix = this.physicBody.createFixture(fixtureDef);
        
            this.setCollisionFilterMask(fixtureDef, true);
            
            this.collisionFixture = new ArrayList<Fixture>();
            this.collisionFixture.add(fix);
            fix.setUserData(this);
            
            PolygonShape feet = new PolygonShape();
            feet.setAsBox(4 * P2M, 3 * P2M, new Vector2(-LEFT_RIGHT_DIST * P2M, (72 - 70 - 36) * P2M), 0);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = feet;
            fixtureDef.density = 1f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            fix = this.physicBody.createFixture(fixtureDef);
            fix.setSensor(true);
            fix.setUserData(this);

            this.feetFixture = fix;
            
            // Part damage zone
            
            PolygonShape damageShape = new PolygonShape();
           
            if(hasToRotate){
                damageShape.setAsBox(30 * P2M, 17 * P2M, new Vector2(LEFT_RIGHT_DIST * 1.5f * P2M, yOffset * P2M), 0);
            }else{
                damageShape.setAsBox(15 * P2M, 35 * P2M, new Vector2(LEFT_RIGHT_DIST * 1.5f * P2M, yOffset * P2M), 0);
            }
            
            fixtureDef = new FixtureDef();
            fixtureDef.shape = damageShape;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            fix = this.physicBody.createFixture(fixtureDef);
            Set<Fixture> setDamage = new HashSet<Fixture>();
            setDamage.add(fix);
            this.damageZone = new ShieldActionFixture(setDamage, 35, 0.3f);
            
        }else{
            CircleShape circleRight = new CircleShape();
            circleRight.setRadius(10 * P2M);
            circleRight.setPosition(new Vector2(LEFT_RIGHT_DIST * P2M, -20 * P2M));
            
            FixtureDef fixtureDef = new FixtureDef();
            
            this.setCollisionFilterMask(fixtureDef, false);
            
            fixtureDef.shape = circleRight;
            fixtureDef.density = 1f; 
            fixtureDef.friction = 0.1f;
            fixtureDef.restitution = restitution; 
            
            Fixture fix = this.physicBody.createFixture(fixtureDef);
        
            this.setCollisionFilterMask(fixtureDef, true);
            
            this.collisionFixture = new ArrayList<Fixture>();
            this.collisionFixture.add(fix);
            fix.setUserData(this);
            
            PolygonShape feet = new PolygonShape();
            feet.setAsBox(4 * P2M , 3 * P2M, new Vector2(LEFT_RIGHT_DIST * P2M, (72 - 70 - 36) * P2M), 0);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = feet;
            fixtureDef.density = 1f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            fix = this.physicBody.createFixture(fixtureDef);
            fix.setSensor(true);
            fix.setUserData(this);

            this.feetFixture = fix;
            
            // Part damage zone
            
            PolygonShape damageShape = new PolygonShape();
            if(hasToRotate){
                damageShape.setAsBox(35 * P2M, 15 * P2M, new Vector2(-LEFT_RIGHT_DIST * 1.5f * P2M, yOffset * P2M), 0);
            }else{
                damageShape.setAsBox(15 * P2M, 35 * P2M, new Vector2(-LEFT_RIGHT_DIST * 1.5f * P2M, yOffset * P2M), 0);
            }
            fixtureDef = new FixtureDef();
            fixtureDef.shape = damageShape;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            fix = this.physicBody.createFixture(fixtureDef);
            Set<Fixture> setDamage = new HashSet<Fixture>();
            setDamage.add(fix);
            this.damageZone = new ShieldActionFixture(setDamage, 35, 0.3f);
            
        }
        
    }
    private void createInfluences(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.isCinematicEntity){
            return;
        }
        
        // Control hero
        /*if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            this.influences.add(GrandmaInfluence.GO_LEFT);
        }else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            this.influences.add(GrandmaInfluence.GO_RIGHT);    
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
           this.influences.add(GrandmaInfluence.UMB_UP);     
        }else if(Gdx.input.isKeyPressed(Input.Keys.S)){
           this.influences.add(GrandmaInfluence.UMB_DOWN);     
        }
        
        // Control umbrella
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            this.influences.add(GrandmaInfluence.ATTACK);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            this.influences.add(GrandmaInfluence.ATTACK);   
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            this.influences.add(GrandmaInfluence.UMB_SWITCH);  
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            this.influences.add(GrandmaInfluence.UMB_SWITCH);  
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            this.influences.add(GrandmaInfluence.JUMP);  
        }*/
        
        // main controls
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            this.influences.add(GrandmaInfluence.GO_LEFT);
        }else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            this.influences.add(GrandmaInfluence.GO_RIGHT);    
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
           this.influences.add(GrandmaInfluence.UMB_UP);     
        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
           this.influences.add(GrandmaInfluence.UMB_DOWN);     
        }
        
        // Control umbrella
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            this.influences.add(GrandmaInfluence.ATTACK);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            this.influences.add(GrandmaInfluence.ATTACK);   
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            this.influences.add(GrandmaInfluence.UMB_SWITCH);  
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            this.influences.add(GrandmaInfluence.UMB_SWITCH);  
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
            this.influences.add(GrandmaInfluence.JUMP);  
        }
    }
    
    private void influences2Actions(float deltaTime){            
        Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();
        boolean isWalking = false;
        this.gotPurse = false;
        while(it.hasNext()){
            GrandmaInfluence currentInfluence = it.next();
            switch(currentInfluence){
                case CINE_GO_WALK:
                    isWalking = true;
                break;
                case CINE_GOT_PURSE:
                    this.gotPurse = true;
                break;
            }
        } 
        
        if(isWalking){
            this.slowingDown = 0.5f;
        }else{
            this.slowingDown = 1;
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
        this.currentStateNode.updatePhysic(deltaTime);
        
        this.influences.clear();
    }
    
    protected enum GrandmaState{
        JUMP_FOLDED,
        JUMP_UNFOLDED_UP,
        JUMP_UNFOLDED_MIDDLE,
        FOLDED_UMBRELLA_NORMAL,
        FOLDED_UMBRELLA_ATTACK,
        UNFOLDED_UMBRELLA_UP,
        UNFOLDED_UMBRELLA_MIDDLE,
        UNFOLDED_UMBRELLA_DOWN,
        
        // Cinematic States
        CINE_ON_GROUND,
        CINE_BACK_ON_FEET,
    }
    
    public boolean isUmbrellaUnfolded(){
        return this.currentStateNode.stateNode == GrandmaState.JUMP_UNFOLDED_UP
            || this.currentStateNode.stateNode == GrandmaState.JUMP_UNFOLDED_MIDDLE
            || this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_UP
            || this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_MIDDLE
            || this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_DOWN;
    }
    
    public Vector2 GetUmbrellaDirection(){
        Vector2 umbrellaDirection = new Vector2();
        
        if(this.currentStateNode.stateNode == GrandmaState.JUMP_UNFOLDED_UP
            || this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_UP){
            umbrellaDirection.y = 1;
        }else if(this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_DOWN){
            umbrellaDirection.y = -1;
        }else{
            SideCharacter side = this.getSideCharacter();
            
            if(side == SideCharacter.RIGHT){
                umbrellaDirection.x = 1;
            }else{
                umbrellaDirection.x = -1;
            }
        }
        
        return umbrellaDirection;
    }
    
    protected enum GrandmaInfluence{
        GO_RIGHT,
        GO_LEFT,
        JUMP,
        UMB_UP,
        UMB_DOWN,
        UMB_SWITCH,
        ATTACK,
        
        // Cinematic influences
        CINE_ON_GROUND,
        CINE_BACK_ON_FEET,
        CINE_GO_WALK,
        CINE_GOT_PURSE
    }
    
    @Override
    public Sprite createLifeBar(){
        int lifePointsRemaining = this.getLifePointsMax() - this.getLifePoints();
        return new Sprite(this.lifeSprites[lifePointsRemaining/7][lifePointsRemaining%7]);

    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        // If the umbrella is unfolded-down, and the opponent is beneath the umbrella, the no damage is apply
        
        Vector2 dirOpponent = damageOwner.getPositionBody().sub(this.getPositionBody());
        dirOpponent = dirOpponent.nor();
        
        Vector2 dirDown = new Vector2(0, -1);           
        if(!dirOpponent.isZero(0.001f) && 
                this.currentStateNode.stateNode == GrandmaState.UNFOLDED_UMBRELLA_DOWN &&
                Math.abs(Math.acos(dirOpponent.dot(dirDown))) < Math.PI/4){
            
            Grandma.this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitProjectile", new Vector2(Grandma.this.getPositionBody()));
            
            return false;
        }
        
        boolean isDamageApplied = super.applyDamage(damage, dirDamage, damageOwner);
        
        if(isDamageApplied && this.getLifePoints() > 0){
            this.notifyObject2DStateListener(Object2DStateListener.Object2DState.DAMAGE_TOOK, 4);
        }
        
        return isDamageApplied;
    }
    
    @Override
    public void onDeath(){
        this.notifyGameEventListener(GameEventListener.EventType.GAMEOVER, "defeat", new Vector2(this.getPositionBody()));
    }
    
    // --- Class StateNode ---
    
    private class StateNode{
        
        public final GrandmaState stateNode;
        
        private int pauseAnimation;
        
        private boolean restartAnimation;
        
        private boolean jumped = false;
        private float currentJumpTime = -1f;
        
        public StateNode(GrandmaState state){
            this.stateNode = state;
            this.restartAnimation = true;
        }
        
        // Part next node
        public StateNode getNextStateNode(){
            
            if(Grandma.this.lifeState == LifeState.DEAD)
                return null;
            
            switch(this.stateNode){
                case JUMP_FOLDED :
                    return getJumpFoldedNextNode();
                case JUMP_UNFOLDED_UP :
                    return getJumpUnfoldedUpNextNode();
                case JUMP_UNFOLDED_MIDDLE :
                    return getJumpUnfoldedMiddleNextNode();
                case FOLDED_UMBRELLA_NORMAL :
                    return getUmbFoldedNormNextNode();
                case FOLDED_UMBRELLA_ATTACK :
                    return getUmbFoldedAttackNextNode();
                case UNFOLDED_UMBRELLA_UP :
                    return getUmbUnfoldedUpNextNode();
                case UNFOLDED_UMBRELLA_MIDDLE :
                    return getUmbUnfoldedMiddleNextNode();
                case UNFOLDED_UMBRELLA_DOWN :
                    return getUmbUnfoldedDownNextNode();
                case CINE_ON_GROUND:
                    return getOnGroundNextNode();
                case CINE_BACK_ON_FEET:
                    return getBackOnFeetNextNode();
            }         
            
            return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_UP);
        }
        
        private StateNode getOnGroundNextNode(){
            if(Grandma.this.isCurrentAnimationOver()){
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

                while(it.hasNext()){
                    GrandmaInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case CINE_BACK_ON_FEET :
                            Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "onFeet", new Vector2(Grandma.this.getPositionBody()));
                            return new StateNode(GrandmaState.CINE_BACK_ON_FEET);

                    }
                } 
            }
            return null;
        }
        
        private StateNode getBackOnFeetNextNode(){
            if(Grandma.this.isCurrentAnimationOver()){
                return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);
            }
            return null;
        }
        
        private StateNode getJumpFoldedNextNode(){
            if(Grandma.this.isCurrentAnimationOver()){
                // test if better
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

                while(it.hasNext()){
                    GrandmaInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case UMB_SWITCH :
                            Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaOpen", new Vector2(Grandma.this.getPositionBody()));
                            return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_MIDDLE);

                    }
                }
                //
                return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);
            }
            
            Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

            while(it.hasNext()){
                GrandmaInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case UMB_SWITCH :
                        switchAnimJump(GrandmaState.JUMP_UNFOLDED_MIDDLE);
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaOpen", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.JUMP_UNFOLDED_MIDDLE);

                }
            }
            
            return null;
        }
        
        private void switchAnimJump(GrandmaState influence){
            switch(influence){
                case JUMP_UNFOLDED_MIDDLE:
                    if(Grandma.this.side == SideCharacter.RIGHT){
                        Grandma.this.changeAnimation(14, false, Grandma.this.animationTime);
                    }else{
                        Grandma.this.changeAnimation(15, false, Grandma.this.animationTime);
                    }
                    break;
                case JUMP_FOLDED:
                    if(Grandma.this.side == SideCharacter.RIGHT){
                        Grandma.this.changeAnimation(10, false, Grandma.this.animationTime);
                    }else{
                        Grandma.this.changeAnimation(11, false, Grandma.this.animationTime);
                    }
                    break;

            }
        }
        
        private StateNode getJumpUnfoldedUpNextNode(){
            if(Grandma.this.isCurrentAnimationOver()){
                // test if better
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

                while(it.hasNext()){
                    GrandmaInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case UMB_SWITCH :
                            return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);

                    }
                }
                //
                return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_UP);
            }
            
            Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();
            boolean isStillUp = false;
            
            while(it.hasNext()){
                GrandmaInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case UMB_SWITCH :
                        switchAnimJump(GrandmaState.JUMP_FOLDED);
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaClose", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.JUMP_FOLDED);
                    case UMB_DOWN :
                        return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_DOWN);
                    case UMB_UP:
                        isStillUp = true;
                        break;
                }
            }
            
            if(!isStillUp){
                return new StateNode(GrandmaState.JUMP_UNFOLDED_MIDDLE);
            }
            
            return null;
            
        }
        
        
        private StateNode getJumpUnfoldedMiddleNextNode(){
            if(Grandma.this.isCurrentAnimationOver()){
                
                // test if better
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

                while(it.hasNext()){
                    GrandmaInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case UMB_SWITCH :
                            Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaClose", new Vector2(Grandma.this.getPositionBody()));
                            return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);

                    }
                }
                //
                
                return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_MIDDLE);
            }
            
            
            Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

            while(it.hasNext()){
                GrandmaInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case UMB_SWITCH :
                        switchAnimJump(GrandmaState.JUMP_FOLDED);
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaClose", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.JUMP_FOLDED);
                    case UMB_UP :
                        return new StateNode(GrandmaState.JUMP_UNFOLDED_UP);
                    case UMB_DOWN :
                        return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_DOWN);
                }
            }
            
            return null;
        }
        
        private StateNode getUmbFoldedNormNextNode(){
                
            Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

            while(it.hasNext()){
                GrandmaInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case JUMP :
                        if(!Grandma.this.isFlying()){
                            //applyPhysicJump(); ////////
                            return new StateNode(GrandmaState.JUMP_FOLDED);
                        }
                        break;
                    case UMB_SWITCH :
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaOpen", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_MIDDLE);
                    case ATTACK :
                        launchAttackAnimation();
                        return new StateNode(GrandmaState.FOLDED_UMBRELLA_ATTACK);
                    case CINE_ON_GROUND:
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "onGround", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.CINE_ON_GROUND);
                }
            }
            return null;
        }
        
        private void launchAttackAnimation(){
            if(Grandma.this.side == SideCharacter.RIGHT){
                Grandma.this.changeAnimation(2, false);
            }else{
                Grandma.this.changeAnimation(3, false);
            }
            Grandma.this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "umbrella", new Vector2(Grandma.this.getPositionBody()));
        }
        
        
        private StateNode getUmbFoldedAttackNextNode(){
            if(Grandma.this.isCurrentAnimationOver()){
                return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);
            }
            return null;
        }
        
        private StateNode getUmbUnfoldedUpNextNode(){
            Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

            boolean isStillUp = false;
            while(it.hasNext()){
                GrandmaInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case JUMP :
                        if(!Grandma.this.isFlying()){
                            //applyPhysicJump(); ////
                            return new StateNode(GrandmaState.JUMP_UNFOLDED_UP);
                        }
                        break;
                    case UMB_SWITCH :
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaClose", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);
                    case UMB_DOWN :
                        return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_DOWN);
                    case UMB_UP:
                        isStillUp = true;
                        break;
                }
            }
            
            if(!isStillUp){
                return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_MIDDLE);
            }
            
            return null;
        }
        
        private StateNode getUmbUnfoldedMiddleNextNode(){
            Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

            while(it.hasNext()){
                GrandmaInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case JUMP :
                        if(!Grandma.this.isFlying()){
                            //applyPhysicJump(); ///////////
                            return new StateNode(GrandmaState.JUMP_UNFOLDED_MIDDLE);
                        }
                        break;
                    case UMB_SWITCH :
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaClose", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);
                    case UMB_DOWN :
                        return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_DOWN);
                    case UMB_UP :
                        return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_UP);
                }
            }
            return null;
        }
        
        private StateNode getUmbUnfoldedDownNextNode(){
            Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

            boolean isStillUp = false;
            
            while(it.hasNext()){
                GrandmaInfluence currentInfluence = it.next();
                switch(currentInfluence){
                    case UMB_SWITCH :
                        Grandma.this.notifyGameEventListener(GameEventListener.EventType.ACTION, "umbrellaClose", new Vector2(Grandma.this.getPositionBody()));
                        return new StateNode(GrandmaState.FOLDED_UMBRELLA_NORMAL);
                    case UMB_UP :
                        return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_UP);
                    case UMB_DOWN:
                        isStillUp = true;
                        break;
                }
            }
            
            if(!isStillUp){
                return new StateNode(GrandmaState.UNFOLDED_UMBRELLA_MIDDLE);
            }
            
            return null;
        }
        
        
        // Part animation
        
        public int getCurrentAnimation(){
            
            if(Grandma.this.lifeState == LifeState.DEAD){
                this.pauseAnimation = 0;
                this.restartAnimation = true;
                if(Grandma.this.side == SideCharacter.RIGHT){
                    return 18;
                }else{
                    return 19;
                }
            }
            
            switch(this.stateNode){
                case JUMP_FOLDED :
                    return getJumpFoldedAnimation();
                case JUMP_UNFOLDED_UP :
                    return getJumpUnfoldedMiddleAnimation();
                case JUMP_UNFOLDED_MIDDLE :
                    return getJumpUnfoldedMiddleAnimation();
                case FOLDED_UMBRELLA_NORMAL :
                    return getUmbFoldedNormAnimation();
                case FOLDED_UMBRELLA_ATTACK :
                    //return getUmbFoldedAttackAnimation();
                    return -1;
                case UNFOLDED_UMBRELLA_UP :
                    return getUmbUnfoldedUpAnimation();
                case UNFOLDED_UMBRELLA_MIDDLE :
                    return getUmbUnfoldedMiddleAnimation();
                case UNFOLDED_UMBRELLA_DOWN :
                    return getUmbUnfoldedDownAnimation();
                case CINE_ON_GROUND :
                    return getOnGroundAnimation();
                case CINE_BACK_ON_FEET :
                    return getBackOnFeetAnimation();

            }
            return 0;
        }
        
        private int getOnGroundAnimation(){
            this.restartAnimation = false;
            this.pauseAnimation = 0;
            if(Grandma.this.side == SideCharacter.RIGHT){
                return 18;
            }else{
                return 19;
            }
        }
        
        private int getBackOnFeetAnimation(){
            this.restartAnimation = false;
            this.pauseAnimation = 0;
            if(Grandma.this.side == SideCharacter.RIGHT){
                return 20;
            }else{
                return 21;
            }
        }
        
        private int getJumpFoldedAnimation(){
            this.restartAnimation = false;
            this.pauseAnimation = 0;
            if(Grandma.this.side == SideCharacter.RIGHT){
                return 10;
            }else{
                return 11;
            }
        }
        
        private int getJumpUnfoldedMiddleAnimation(){
            this.restartAnimation = false;
            this.pauseAnimation = 0;
            if(Grandma.this.side == SideCharacter.RIGHT){
                return 14;
            }else{
                return 15;
            }
        }
        
        private int getUmbFoldedNormAnimation(){
            this.restartAnimation = true;
            if(Grandma.this.isFlying()){
                this.pauseAnimation = 1;
                if(Grandma.this.side == SideCharacter.RIGHT){
                    return 10;
                }else{
                    return 11;
                }
            }else{
                boolean isMove = false;
                
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();
                
                while(it.hasNext() && !isMove){
                    GrandmaInfluence influence = it.next();   
                    if(influence == GrandmaInfluence.GO_LEFT || influence == GrandmaInfluence.GO_RIGHT){
                        isMove = true;
                    }
                }
                if(Grandma.this.gotPurse){
                    if(isMove){
                        this.pauseAnimation = 0;
                        if(Grandma.this.side == SideCharacter.RIGHT){
                            return 22;
                        }else{
                            return 23;
                        }
                    }else{
                        this.pauseAnimation = -1;

                        if(Grandma.this.side == SideCharacter.RIGHT){
                            return 22;
                        }else{
                            return 23;
                        }
                    }
                }else{
                    if(isMove){
                        this.pauseAnimation = 0;
                        if(Grandma.this.side == SideCharacter.RIGHT){
                            return 0;
                        }else{
                            return 1;
                        }
                    }else{
                        this.pauseAnimation = -1;

                        if(Grandma.this.side == SideCharacter.RIGHT){
                            return 0;
                        }else{
                            return 1;
                        }
                    }
                }
            }
        }
        
        private int getUmbFoldedAttackAnimation(){
            this.restartAnimation = true;
            this.pauseAnimation = 0;
            if(Grandma.this.side == SideCharacter.RIGHT){
                return 2;
            }else{
                return 3;
            }
        }
        
        private int getUmbUnfoldedUpAnimation(){
            this.restartAnimation = true;
            if(Grandma.this.isFlying()){
                this.pauseAnimation = 0;
                if(Grandma.this.side == SideCharacter.RIGHT){
                    return 6;
                }else{
                    return 7;
                }
            }else{
                boolean isMove = false;
                
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();
                
                while(it.hasNext() && !isMove){
                    GrandmaInfluence influence = it.next();           
                    if(influence == GrandmaInfluence.GO_LEFT || influence == GrandmaInfluence.GO_RIGHT){
                        isMove = true;
                    }
                }
                if(isMove){
                    this.pauseAnimation = 0;
                    if(Grandma.this.side == SideCharacter.RIGHT){
                        return 8;
                    }else{
                        return 9;
                    }
                }else{
                    this.pauseAnimation = -1;
                    
                    if(Grandma.this.side == SideCharacter.RIGHT){
                        return 8;
                    }else{
                        return 9;
                    }
                }
            }
        }
        
        private int getUmbUnfoldedMiddleAnimation(){
            this.restartAnimation = true;
            if(Grandma.this.isFlying()){
                this.pauseAnimation = 1;
                if(Grandma.this.side == SideCharacter.RIGHT){
                    return 14;
                }else{
                    return 15;
                }
            }else{
                boolean isMove = false;
                
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();
                
                while(it.hasNext() && !isMove){
                    GrandmaInfluence influence = it.next(); 
                    if(influence == GrandmaInfluence.GO_LEFT || influence == GrandmaInfluence.GO_RIGHT){
                        isMove = true;
                    }
                }
                if(isMove){
                    this.pauseAnimation = 0;
                    if(Grandma.this.side == SideCharacter.RIGHT){
                        return 4;
                    }else{
                        return 5;
                    }
                }else{
                    this.pauseAnimation = -1;
                    
                    if(Grandma.this.side == SideCharacter.RIGHT){
                        return 4;
                    }else{
                        return 5;
                    }
                }
            }
        }
        
        private int getUmbUnfoldedDownAnimation(){
            this.restartAnimation = true;
            this.pauseAnimation = 0;
            if(Grandma.this.side == SideCharacter.RIGHT){
                return 12;
            }else{
                return 13;
            }
        }
        
        // Part physic
        
        public void updatePhysic(float deltaTime){
            Vector2 velocity = Grandma.this.physicBody.getLinearVelocity();
            
            if(Grandma.this.lifeState == LifeState.DEAD){
                
                // Clamp speed
                if(Math.abs(velocity.x) > 6.f){
                    velocity.x = 6.f * (float)Math.signum(velocity.x);
                }

                if(Math.abs(velocity.y) > 13.f){
                    velocity.y = 13.f * (float)Math.signum(velocity.y);
                }

                Grandma.this.physicBody.setLinearVelocity(velocity);
                return;
            }
            
            if((this.stateNode != GrandmaState.UNFOLDED_UMBRELLA_DOWN || Grandma.this.isFlying())
                    && this.stateNode != GrandmaState.CINE_ON_GROUND){
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();

                boolean isMove = false;
                
                while(it.hasNext()){
                    GrandmaInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                            velocity.x += 1.f;
                            Grandma.this.side = SideCharacter.RIGHT;
                            isMove = true;
                            break;
                        case GO_LEFT :
                            velocity.x -= 1.f;
                            Grandma.this.side = SideCharacter.LEFT;
                            isMove = true;
                            break;
                    }
                }
                
                if(!(isMove || Grandma.this.isFlying())){
                    if(Math.abs(velocity.x) > 1.f){
                        velocity.x -= 0.5f * (float)Math.signum(velocity.x);
                    }else{
                        velocity.x = 0.f;
                    }
                }
                
            }else{
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();             
                while(it.hasNext()){
                    GrandmaInfluence currentInfluence = it.next();
                    switch(currentInfluence){
                        case GO_RIGHT :
                            Grandma.this.side = SideCharacter.RIGHT;
                            break;
                        case GO_LEFT :
                            Grandma.this.side = SideCharacter.LEFT;
                            break;
                    }
                }
            }
            
            Fixture fix = Grandma.this.collisionFixture.get(0);
            if(this.stateNode != GrandmaState.UNFOLDED_UMBRELLA_DOWN && this.stateNode != GrandmaState.CINE_ON_GROUND && fix.getRestitution() > 0.1f){
                fix.setRestitution(0.1f);
            }else if(this.stateNode == GrandmaState.UNFOLDED_UMBRELLA_DOWN && fix.getRestitution() < 0.8f){
                fix.setRestitution(2.5f);
            }else if(this.stateNode == GrandmaState.CINE_ON_GROUND && fix.getRestitution() < 0.8f){
                fix.setRestitution(2.5f);
            }
          
            if((this.stateNode == GrandmaState.JUMP_UNFOLDED_UP  || this.stateNode == GrandmaState.UNFOLDED_UMBRELLA_UP)
                    && Grandma.this.isFlying() && velocity.y < 0){
                velocity.y /= 2;
            }
            
            
            if(/*!Grandma.this.isFlying() 
                    && */(this.stateNode == GrandmaState.JUMP_FOLDED
                    || this.stateNode == GrandmaState.JUMP_UNFOLDED_MIDDLE
                    || this.stateNode == GrandmaState.JUMP_UNFOLDED_UP)){
                
                boolean isMove = false;
                
                Iterator<GrandmaInfluence> it = Grandma.this.influences.iterator();
                
                while(it.hasNext() && !isMove){
                    GrandmaInfluence influence = it.next(); 
                    if(influence == GrandmaInfluence.JUMP){
                        isMove = true;
                    }
                }
                if(isMove){ // && this.jumped == false){
                    if(this.jumped == false){
                        velocity.y = 0;
                        this.jumped = true;
                        this.currentJumpTime = 0;
                    }
                    
                    if(this.currentJumpTime >= 0 && this.currentJumpTime < 0.10f){
                        velocity.y += 110 * deltaTime;
                        this.currentJumpTime += deltaTime;
                    }else if(this.currentJumpTime >= 0.10f){
                        velocity.y += 110 * (deltaTime - this.currentJumpTime + 0.10f);
                        
                        this.currentJumpTime = -1f;
                    }

                }
            }else{
                this.jumped = false;
            } 
            
            
            // Clamp speed
            if(Math.abs(velocity.x) > 6.f * Grandma.this.slowingDown){
                velocity.x = 6.f * Grandma.this.slowingDown * (float)Math.signum(velocity.x);
            }

            if(Math.abs(velocity.y) > 15.f){
                velocity.y = 15.f * (float)Math.signum(velocity.y);
            }
            
            Grandma.this.physicBody.setLinearVelocity(velocity);
        }
        
        
        public int isPauseAnimation(){
            return this.pauseAnimation;
        }
        
        public boolean isRestartAnimation(){
            return this.restartAnimation;
        }
        
    }
}
