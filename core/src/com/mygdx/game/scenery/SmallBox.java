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
import com.badlogic.gdx.physics.box2d.Filter;
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
import triggered.UpTriggeredObject2D;
import java.util.ArrayList;
import java.util.List;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class SmallBox extends Character2D{

    private static final String SMALLBOXTEXT = "destroyable/SpritemapCaisse.png";
    
    private boolean canSendEventBounce;
    
    public SmallBox(World world, float posX, float posY) {
        super(3);
        
        this.Initialize(world, posX, posY, 18f, 16f, 8f);
    }
    
    public SmallBox(World world, float posX, float posY, int lifePoint) {
        super(lifePoint);
    }
    
    public final void Initialize(World world, float posX, float posY, float radiusCollisionX, float radiusCollisionY, float centerX, float centerY, float density) {
        
        this.side = SideCharacter.RIGHT;   
        
        this.canSendEventBounce = true;
        
        // Part graphic
        this.assignTextures();
        
        // Part Physic
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);
        this.physicBody = body;

        this.physicBody.setSleepingAllowed(true);
        this.physicBody.setAwake(false);
        
        // Collision fixture
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(radiusCollisionX * P2M, radiusCollisionY * P2M, new Vector2( centerX * P2M, centerY * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = collisionBox;
        fixtureDef.density = density; 
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0f; 

        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);

        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox((radiusCollisionX + 1) * P2M, (radiusCollisionY + 1) * P2M, new Vector2( centerX * P2M, centerY * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = feet;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f; 

        fix = body.createFixture(fixtureDef);
        fix.setSensor(true);
        fix.setUserData(this);
        
        // damage & bounce scale
        //this.scaleDamageForce = 0.01f;
        
        this.feetFixture = fix;
        
        this.hasLifeBar = false;
        
    }
    
    public final void Initialize(World world, float posX, float posY, float radiusCollisionX, float radiusCollisionY, float density) {
        
        this.side = SideCharacter.RIGHT;   
        
        this.canSendEventBounce = true;
        
        // Part graphic
        this.assignTextures();
        
        // Part Physic
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);
        this.physicBody = body;

        this.physicBody.setSleepingAllowed(true);
        this.physicBody.setAwake(false);
        
        // Collision fixture
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(radiusCollisionX * P2M, radiusCollisionY * P2M, new Vector2( 0, 0), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = collisionBox;
        fixtureDef.density = density; 
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0f; 

        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);

        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox((radiusCollisionX + 1) * P2M, (radiusCollisionY + 1) * P2M, new Vector2( 0, 0), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = feet;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f; 

        fix = body.createFixture(fixtureDef);
        fix.setSensor(true);
        fix.setUserData(this);
        
        // damage & bounce scale
        //this.scaleDamageForce = 0.01f;
        
        this.feetFixture = fix;
        
        this.hasLifeBar = false;
        
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(SMALLBOXTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 40, 37);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, 3);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 0);
            array.removeRange(1, 2);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 1);
            array.removeRange(1, 1);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 2);
            this.listAnimations.add(new Animation(0.2f, array));

            this.changeAnimation(0, true);
        }
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        // nothing to do. (it's a box!)
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        if(damageOwner != this){
            damage = 1;
        }
        
        float sign = -Math.signum((new Vector2(1f, 0f).dot(dirDamage)));
        
        Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);
        ptApplication.add(this.physicBody.getPosition());

        boolean result = this.applyDamage(damage, dirDamage, damageOwner, ptApplication);
        
        int animationUpdated = this.getLifePointsMax() - this.getLifePoints();
        
        if(this.currentAnimation != animationUpdated){
            this.changeAnimation(animationUpdated, true);
        }
        
        if(result && this.getLifePoints() <= 0){
            
            this.notifyGameEventListener(GameEventListener.EventType.DAMAGE, "boxCrash", new Vector2(this.getPositionBody()));
            
            Vector2 upVector = new Vector2(0, 1);
            float angle = dirDamage.nor().angle(upVector) / 2f;
            dirDamage = dirDamage.rotate(angle);
            
            this.notifyObject2DStateListener(Object2DStateListener.Object2DState.DEATH, 6, false);
            
            this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), dirDamage.scl(2f));
            
            this.spawnLoot(new Vector2(dirDamage.scl(2f)), damageOwner);
        }
        
        return result;
    }
    
    protected void spawnLoot(Vector2 dirDamage, Object2D damageOwner)
    {
        dirDamage.scl(2f);
        
        if(Math.random() < 0.3){
            this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), (new Vector2(dirDamage)).rotate((float) Math.random()*360));
        }

        if(Math.random() < 0.1){
            this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), (new Vector2(dirDamage)).rotate((float) Math.random()*360));
        }
    }
    
    @Override
    protected void onDeath(){
        Filter filter = new Filter();
        filter.categoryBits = 0x0004;
        filter.maskBits = 0x0002;
        
        for(Fixture fixture : this.collisionFixture){   
            fixture.setFilterData(filter);
        }
    }
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
            
        float sign = -Math.signum((new Vector2(1f, 0f).dot(bounceVector)));

        Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);
        
        if(this.canSendEventBounce){
            this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitBounce", new Vector2(this.getPositionBody()));
            
            this.canSendEventBounce = false;
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    SmallBox.this.canSendEventBounce = true;
                }
            }, 0.3f);
        }
        
        super.applyBounce(bounceVector, bounceOwner, ptApplication.add(this.physicBody.getPosition()));
    }
}
