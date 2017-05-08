/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import static com.mygdx.game.HelpGame.P2M;
import java.io.File;
import java.util.ArrayList;
import triggered.BulletTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class OpponentDIST1 extends OpponentCAC1{
    
    private static final Texture OPPDIST1TEXT = new Texture("character" + File.separator + "spritemapkairaTir-01.png");
    
    public OpponentDIST1(World world, Body target, float posX, float posY){
        super(100, target);
        
        this.texture = OPPDIST1TEXT;
        
        this.initializeGraphic();
        
        this.initializePhysicDIST1(world, posX, posY);
    }
    
    @Override
    public void updateLogic(float deltaTime){
        if(this.currentAnimation >=0 && !this.pause){
            this.animationTime += deltaTime;
        }
        
        createInfluencesDIST1();
        
        influences2Actions();
    }
    
    @Override
    protected void influences2Actions(){
        
        OpponentCAC1.StateNode prevNode = this.currentStateNode;
        OpponentCAC1.StateNode nextNode = this.currentStateNode.getNextStateNode();
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
        
        this.updateAttack(prevNode, nextNode);
        
        this.currentStateNode.updatePhysic();
          
        
        this.influences.clear();
    }
    
    protected void updateAttack(OpponentCAC1.StateNode prevNode, OpponentCAC1.StateNode nextNode){
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
                            if(!OpponentDIST1.this.isInvulnerable){
                            Vector2 dirBall = new Vector2(OpponentDIST1.this.side == SideCharacter.RIGHT? 1: -1, 0);
                            Vector2 posBall = new Vector2(OpponentDIST1.this.getPositionBody()).scl(1 / P2M);
                            posBall = posBall.add(dirBall.scl(10)).add(new Vector2(0, 4));
                            OpponentDIST1.this.notifyObject2D2CreateListener(BulletTriggeredObject2D.class, posBall, dirBall.scl(50 * P2M));
                        }
                    }
            }, 0.6f);
            
            
            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        OpponentDIST1.this.canAttack = true;
                    }         
            }, 4f);
        }
    }
    
    protected final void initializePhysicDIST1(World world, float posX, float posY){
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
    }
    
}
