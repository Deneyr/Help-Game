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
import com.mygdx.game.DamageActionFixture;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.ShieldActionFixture;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Deneyr
 */
public class OpponentCACElite extends OpponentCAC2{
    
    private static final Texture OPPCACELITETEXT = new Texture("character" + File.separator + "spritemapkairaBrute-01.png");
    
    protected ShieldActionFixture shieldActionFixture;
    
    public OpponentCACElite(World world, Object2D target, float posX, float posY) {
        super(world, target);
        
        this.texture = OPPCACELITETEXT;
        
        this.maxSpeed = 6f;
        
        this.initializeGraphic();
        this.listAnimations.get(2).setFrameDuration(0.2f);
        this.listAnimations.get(3).setFrameDuration(0.2f);
        
        this.initializePhysicCACElite(world, posX, posY);
        
        this.updateFixture();
        
        this.previousSide = this.side;
    }
    
    protected final void initializePhysicCACElite(World world, float posX, float posY){
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
        fixtureDef.density = 10f; 
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
        
        // shield fixture
        circle.setRadius(22 * P2M);
        
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        fix = this.physicBody.createFixture(fixtureDef);
        Set<Fixture> setDamage = new HashSet<Fixture>();
        setDamage.add(fix);
        this.shieldActionFixture = new ShieldActionFixture(setDamage, 3, 0.2f);
        this.shieldActionFixture.setApplyDamage(false);
    }
    
    @Override
    public void updateLogic(float deltaTime){
        if(this.currentAnimation >=0 && !this.pause){
            this.animationTime += deltaTime;
        }
        
        this.createInfluencesCACElite();
        
        this.influences2Actions(deltaTime);
        
        if(this.side != this.previousSide){
            this.updateFixture();
            this.previousSide = this.side;
        }
        
        /*if(this.lifeState == LifeState.ALIVE){
            this.shieldActionFixture.applyAction(deltaTime, this);
        }*/
    }
    
    protected void updateFixture(){
        
        if(this.damageActionFixture != null){
            damageActionFixture.dispose(this.physicBody);
        }
        
        
        if(this.side == SideCharacter.RIGHT){        
            // Part damage zone
            
            PolygonShape damageShape = new PolygonShape();

            damageShape.setAsBox(20 * P2M, 35 * P2M, new Vector2(25 * P2M, 0), 0);
            
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = damageShape;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            Fixture fix = this.physicBody.createFixture(fixtureDef);
            Set<Fixture> setDamage = new HashSet<Fixture>();
            setDamage.add(fix);
            this.damageActionFixture = new DamageActionFixture(setDamage, 3);
            
        }else{
            // Part damage zone
            
            PolygonShape damageShape = new PolygonShape();

            damageShape.setAsBox(20 * P2M, 35 * P2M, new Vector2(-25 * P2M, 0), 0);
            
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = damageShape;
            fixtureDef.density = 0f; 
            fixtureDef.friction = 0.05f;
            fixtureDef.restitution = 0.1f; 

            Fixture fix = this.physicBody.createFixture(fixtureDef);
            Set<Fixture> setDamage = new HashSet<Fixture>();
            setDamage.add(fix);
            this.damageActionFixture = new DamageActionFixture(setDamage, 3);
        }
        
    }
    
    @Override
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
                    if(!OpponentCACElite.this.isInvulnerable){
                        OpponentCACElite.this.shieldActionFixture.applyAction(deltaTime, OpponentCACElite.this);
                        OpponentCACElite.this.damageActionFixture.applyAction(deltaTime, OpponentCACElite.this);
                    }
                }
            }, 0.4f);
            
            
            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        OpponentCACElite.this.canAttack = true;
                    }         
            }, 2f);
        }
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        
        damage -= 10;
        
        return super.applyDamage(damage, dirDamage, damageOwner);
    }
    
}
