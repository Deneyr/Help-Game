/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import ressourcesmanagers.TextureManager;
import triggered.UpTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class ConveyorBelt extends TriggeredObject2D{
    
    protected final String id = UUID.randomUUID().toString();   
    
    private static final String[] OBJECT_ARRAY = {
        "factory/Help_Props_TapisRoulant_1520x200.png"};  
    
    private Vector2 velocity;
    
    private float angle;
    private int side;
    
    protected Set<Object2D> setObject2DInside;  
    
    public ConveyorBelt(World world, float posX, float posY, float angle, float scale, int side, Vector2 velocity){
        this.velocity = new Vector2(velocity);
        this.angle = angle;
        this.side = side;
        
        this.setObject2DInside = new HashSet<Object2D>();
        
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
    }  
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        PolygonShape vectorShape = new PolygonShape();
        vectorShape.setAsBox(175 * P2M * this.scale, 30 * P2M * this.scale, new Vector2(0, 10 * P2M * this.scale), 0);
        
        super.initialize(world, position, speed, vectorShape);

        PolygonShape ground = new PolygonShape();
        ground.setAsBox(180 * P2M * this.scale, 30 * P2M * this.scale);
        FixtureDef fixtureDef = new FixtureDef();
        this.setCollisionFilterMask(fixtureDef, false);
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
              
        Fixture fix = this.physicBody.createFixture(fixtureDef);       
        fix.setUserData(this);
        
        this.physicBody.setTransform(this.getPositionBody(), this.angle);

        this.collisionFixture = new ArrayList<Fixture>();       
        this.collisionFixture.add(fix);
    }
    
    @Override
    protected void SetCollisionMask(FixtureDef fixtureDef){
        this.setCollisionFilterMask(fixtureDef, false);
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(OBJECT_ARRAY[0], this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 380, 100);
        
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.LOOP));

            this.changeAnimation(0, false);
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        this.notifyGameEventListener(GameEventListener.EventType.LOOP, "conveyorBelt" + ":" + this.id, this.getPositionBody());
        
        Vector2 newVelocity = new Vector2(this.velocity);
        newVelocity.rotate((float) (Math.toDegrees(this.physicBody.getAngle())));

        for(Object2D obj : this.setObject2DInside){
            Vector2 newPosition = obj.getPositionBody().add((new Vector2(newVelocity)).scl(deltaTime));
            obj.setTransform(newPosition.x, newPosition.y, obj.getAngleBody());
        }
    }
    
    @Override
    public void onObj2DEnteredArea(Object2D obj){
        if(this.IsObject2DAffected(obj)){
            this.setObject2DInside.add(obj);
        }
    }
    
    @Override
    public void onObj2DExitedArea(Object2D obj){
        if(this.IsObject2DAffected(obj)){
            this.setObject2DInside.remove(obj);
            
            Vector2 newVelocity = new Vector2(this.velocity);
            newVelocity.rotate((float) (Math.toDegrees(this.physicBody.getAngle())));
            newVelocity.add(obj.getBodyVelocity());
            obj.setVelocity(newVelocity.x, newVelocity.y);
        }
    }
    
    private boolean IsObject2DAffected(Object2D obj){
        return obj instanceof Character2D 
            || (obj instanceof TriggeredObject2D && obj.getBodyType() == BodyType.DynamicBody);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();

        sprite.setScale(sprite.getScaleX() * this.side, sprite.getScaleY());

        return sprite;
    }
    
    @Override
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        
        if(reset){
            super.setCollisionFilterMask(fixtureDef, true);
            return;
        }
        
        fixtureDef.filter.categoryBits = 0x0002;
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        // Nothing to do
    }
}