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
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import java.util.ArrayList;
import java.util.List;
import ressourcesmanagers.TextureManager;
import triggered.UpTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class BreakableCar extends Character2D {
    
    private static final String[] OBJECT_ARRAY = {
        "car/Anim_Voiture-01.png"};
    
    private int indexObject;
    
    public BreakableCar(World world, float posX, float posY, int indexTrash, int side){
        super(9);
        
        this.indexObject = indexTrash;
        this.scaleDamageForce = 0.1f;
        
        this.Initialize(world, posX, posY, side, 10f);
        
        this.priority = 3;
    }
    
    public final void Initialize(World world, float posX, float posY, int side, float density) {
        
        if(side > 0){
            this.side = SideCharacter.LEFT;
        }else{
            this.side = SideCharacter.RIGHT;
        }
        
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
        
        switch(this.indexObject){
            case 0:
                this.createCollisions0(density, body);
                break;
        }      
        
        this.hasLifeBar = false;      
    }
    
    protected void createCollisions0(float density, Body groundBody) {
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef = new FixtureDef();
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.density = density; 
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body 
        
        ground.setAsBox(95 * P2M * this.scale, 20 * P2M * this.scale, new Vector2(0, -25 * P2M * this.scale), 0);       
        Fixture fix = groundBody.createFixture(fixtureDef);
        this.collisionFixture.add(fix);
        fix.setUserData(this);
                
        ground.setAsBox(60 * P2M * this.scale, 18 * P2M * this.scale, new Vector2(24 * P2M * this.scale * (this.side == SideCharacter.LEFT ? 1 : -1), 12 * P2M * this.scale), 0);            
        fix = groundBody.createFixture(fixtureDef); 
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox(100 * P2M * this.scale, 20 * P2M * this.scale, new Vector2(0, -50 * P2M * this.scale), 0);   
        fixtureDef = new FixtureDef();
        fixtureDef.shape = feet;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f; 

        fix = groundBody.createFixture(fixtureDef);
        fix.setSensor(true);
        fix.setUserData(this);
        
        // damage & bounce scale
        //this.scaleDamageForce = 0.01f;
        
        this.feetFixture = fix;
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        // nothing to do. (it's a car!)
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(OBJECT_ARRAY[this.indexObject], this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 200, 100);
        
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.NORMAL));
            
            this.changeAnimation(0, true);
        }
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        
        if(damageOwner != this){
            damage = 1;
        }
        
        float sign = -Math.signum((new Vector2(1f, 0f).dot(dirDamage)));
        
        Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);
        ptApplication.add(this.physicBody.getPosition());

        int oldLifePoints = this.lifePoints;
        boolean result = this.applyDamage(damage, dirDamage, damageOwner, ptApplication);
        
        if(result){
            int indexAnimation = (int) (((float) (this.getLifePointsMax() - this.getLifePoints()) / this.getLifePointsMax()) * (this.listAnimations.get(0).getKeyFrames().length - 1));

            this.changeAnimation(0, true, 0.2f * indexAnimation);
        }
        
        if(result){
            float angle = dirDamage.angle(new Vector2(0, 1)) / 2f;
            dirDamage = dirDamage.rotate(angle);
            float ratioDamage = (oldLifePoints - this.lifePoints) / (float) this.getLifePointsMax();
            Vector2 linearImpulseDamage = dirDamage.scl(ratioDamage * 100.f * this.scaleDamageForce);
            if(linearImpulseDamage.x < 0){
                linearImpulseDamage.y *= - 1;
            }
            this.physicBody.applyLinearImpulse(linearImpulseDamage, Vector2.Zero, true);             
            
            if(this.getLifePoints() <= 0){
                this.notifyGameEventListener(GameEventListener.EventType.DAMAGE, "boxCrash", new Vector2(this.getPositionBody()));

                Vector2 upVector = new Vector2(0, 1);
                angle = dirDamage.nor().angle(upVector) / 2f;
                dirDamage = dirDamage.rotate(angle);

                this.notifyObject2DStateListener(Object2DStateListener.Object2DState.DEATH, 10, false);

                this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), dirDamage.scl(2f));

                this.spawnLoot(new Vector2(dirDamage.scl(2f)), damageOwner);
            }else{
                this.notifyGameEventListener(GameEventListener.EventType.DAMAGE, "metalHitDamage", new Vector2(this.getPositionBody()));
            }
        }
        
        return result;
    }
    
    protected void spawnLoot(Vector2 dirDamage, Object2D damageOwner)
    {
        dirDamage.scl(2f);
        
        if(Math.random() < 0.7){
            this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), (new Vector2(dirDamage)).rotate((float) Math.random()*360));
        }
        
        if(Math.random() < 0.5){
            this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), (new Vector2(dirDamage)).rotate((float) Math.random()*360));
        }
        
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
        // nothing to do
    }
}
