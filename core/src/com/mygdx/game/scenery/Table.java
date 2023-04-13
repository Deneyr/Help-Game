/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import java.util.ArrayList;
import java.util.List;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class Table extends Character2D{
    
    private static final String TABLETEXT = "urbanObj/Help_Props_84x64_TabouretCafe.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private boolean canBeHitAgain;
    
    private boolean canSendEventBounce;
    
    public Table(World world, float posX, float posY) {
        super(1);
        
        this.Initialize(world, posX, posY, 8f);
    }
    
    public final void Initialize(World world, float posX, float posY, float density) {
        
        this.side = SideCharacter.RIGHT;   
        
        this.canSendEventBounce = true;
        this.canBeHitAgain = true;
        
        // Part graphic
        this.assignTextures();
        
        // Part Physic
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);
        this.physicBody = body;
        
        // Collision fixture
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(32 * P2M, 8 * P2M, new Vector2( 0, 10 * P2M), 0);
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
        
        
        collisionBox = new PolygonShape();
        collisionBox.setAsBox(8 * P2M, 15 * P2M, new Vector2( 0, -5 * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = collisionBox;
        fixtureDef.density = density; 
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0f; 

        fix = this.physicBody.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);

        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox(36 * P2M, 30 * P2M, new Vector2( 0, 0), 0);
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
        this.texture = TextureManager.getInstance().getTexture(TABLETEXT, this);
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
                    Table.this.canSendEventBounce = true;
                }
            }, 0.3f);
        }
        
        super.applyBounce(bounceVector, bounceOwner, ptApplication.add(this.physicBody.getPosition()));
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        
        if(this.canBeHitAgain){
            this.notifyGameEventListener(GameEventListener.EventType.ACTION, "metalHit", new Vector2(this.getPositionBody())); 

            this.canBeHitAgain = false;
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    Table.this.canBeHitAgain = true;
                }
            }, 0.5f);
        }
        return false;
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){
        // nothing to do. (it's a box!)
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }    
}