/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.Character2D;
import com.mygdx.game.DamageActionFixture;
import static com.mygdx.game.HelpGame.P2M;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import triggered.TeethTriggeredObject2D;

/**
 *
 * @author françois
 */
public class OpponentCAC1 extends Character2D{

    private static final Texture OPPCAC1TEXT = new Texture("character" + File.separator + "opponentCAC1.png");
    private static final float ATT_DIST = P2M * 70;
    private static final float MOVE_DIST = P2M * 200;
    
    protected StateNode currentStateNode;
    
    protected Set<OppInfluence> influences = new HashSet<OppInfluence>();
    
    protected final Body target;
    
    protected DamageActionFixture damageActionFixture;
    
    protected boolean canAttack;
    
    public OpponentCAC1(int lifePoint, Body target){
        super(lifePoint);
        
        this.side = SideCharacter.RIGHT;
        
        this.currentStateNode = new OpponentCAC1.StateNode(OpponentCAC1.OppState.NORMAL);
        
        this.canAttack = true;
        
        this.target = target;
    }
    
    public OpponentCAC1(World world, Body target, float posX, float posY) {
        super(100);
        this.target = target;
        
        this.side = SideCharacter.RIGHT;
        
        this.currentStateNode = new OpponentCAC1.StateNode(OpponentCAC1.OppState.NORMAL);
        
        this.canAttack = true;
        
        // Part graphic
        this.texture = OPPCAC1TEXT;
        
        this.initializeGraphic();
        
        // Part Physic
        this.initializePhysicCAC1(world, posX, posY);
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        createInfluencesCAC1();
        
        if(this.isInvulnerable){
            this.influences.remove(OppInfluence.ATTACK);
        }
        
        influences2Actions();
        
        if(this.lifeState != LifeState.DEAD){
            this.damageActionFixture.applyAction(deltaTime, this);
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
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.REVERSED);
        // flying
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 8);
        this.listAnimations.add(new Animation(10, array));
        array = new Array<TextureRegion>(tmp[1]);
        array.removeRange(1, 9);
        this.listAnimations.add(new Animation(10, array));
        
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
        circle.setRadius(25 * P2M);
        circle.setPosition(new Vector2(0, 0));
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        fix = this.physicBody.createFixture(fixtureDef);
        Set<Fixture> setDamage = new HashSet<Fixture>();
        setDamage.add(fix);
        this.damageActionFixture = new DamageActionFixture(setDamage, 2);
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
    }
    
    protected void createInfluencesDIST1(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.target.getPosition().dst(this.physicBody.getPosition()) < MOVE_DIST){
            if(this.target.getPosition().x - this.physicBody.getPosition().x > 0){
                this.influences.add(OppInfluence.GO_RIGHT);
            }else{
                this.influences.add(OppInfluence.GO_LEFT);
            }
            
            if(this.canAttack
                    && this.target.getPosition().y - this.physicBody.getPosition().y > 75 * P2M 
                    && this.target.getPosition().y - this.physicBody.getPosition().y < 150 * P2M){
                this.influences.add(OppInfluence.JUMP);
            }
            
            if(Math.abs(this.target.getPosition().sub(this.physicBody.getPosition()).len()) <  300 * P2M
                    && Math.abs(this.target.getPosition().y - this.physicBody.getPosition().y) < 50 * P2M){
                this.influences.add(OppInfluence.ATTACK);
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
    
    protected void createInfluencesCAC1(){
        if(this.lifeState == LifeState.DEAD){
            return;
        }
        
        if(this.target.getPosition().dst(this.physicBody.getPosition()) < MOVE_DIST){
            if(this.target.getPosition().x - this.physicBody.getPosition().x > 0){
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
    
    protected void influences2Actions(){
        
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
        this.currentStateNode.updatePhysic();
          
        
        this.influences.clear();
    }
    
    protected enum OppState{
        NORMAL,
        ATTACK
    }
    
    protected enum OppInfluence{
        JUMP,
        GO_RIGHT,
        GO_LEFT,
        ATTACK
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
            
            if(Math.abs(velocity.x) > 2.f){
                velocity.x = 2.f * (float)Math.signum(velocity.x);
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
