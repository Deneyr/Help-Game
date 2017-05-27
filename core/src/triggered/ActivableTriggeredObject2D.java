/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ActivableActionFixture;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Deneyr
 */
public class ActivableTriggeredObject2D extends TriggeredObject2D{

    private static final Texture ACTIVABLETEXTURE = new Texture("gui" + File.separator + "bulletexte_V1_400x50.png");
    
    private int activableKey;
    
    private float radius;
    
    private Grandma target;
    
    private GameEventListener.EventType eventType;
    private String details;
    
    public ActivableTriggeredObject2D(World world, float posX, float posY, int activableKey, GameEventListener.EventType eventType, String details, float radius){
        super();
        
        this.radius = radius;
        
        this.activableKey = activableKey;
        
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
        this.eventType = eventType;
        this.details = details;
        
        this.target = null;
        
        // Part graphic
        this.texture = ACTIVABLETEXTURE;
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 50, 37);
        // walk folded
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        this.listAnimations.add(new Animation(0.5f, array, Animation.PlayMode.LOOP));
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        this.initialize(world, position, speed, this.radius);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(this.radius * P2M);
        circle.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.8f; 
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        fix.setSensor(true);
        
        this.changeAnimation(0, false);
    }
    
    public void initialize(World world, Vector2 position, Vector2 speed, float radius){
        if(this.physicBody == null){
            // Part Physic
            BodyDef bodyDef = new BodyDef();
            
            if(this.IsDynamicObject()){
                bodyDef.type = BodyDef.BodyType.DynamicBody;         
            }else{
                bodyDef.type = BodyDef.BodyType.KinematicBody;
            }
            bodyDef.position.set(position.x * P2M, position.y * P2M); 

            Body body = world.createBody(bodyDef);

            // Collision fixture
            CircleShape circle = new CircleShape();
            circle.setRadius(radius * P2M);
            circle.setPosition(new Vector2(0, 0));

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;

            body.setFixedRotation(true);
            Fixture fix = body.createFixture(fixtureDef);

            Set<Fixture> setTrigger = new HashSet<Fixture>();
            setTrigger.add(fix);
            this.triggerActionFixture = new ActivableActionFixture(setTrigger, this.activableKey);

            this.physicBody = body;
            
            // Set physic
            if(this.IsDynamicObject()){
                this.physicBody.applyLinearImpulse(speed, Vector2.Zero, true);   
            }else{
                this.physicBody.setLinearVelocity(speed);
            }
            
            
            // Reset alpha & scaling
            this.setAlpha(1f);
            this.setScale(1f);
        }
        
        this.isTriggered = false;
    }
    
    @Override
    public void onObj2DEnteredArea(Object2D obj){
        if(obj instanceof Grandma){
            this.target = (Grandma) obj;
        }
    }
    
    @Override
    public void onObj2DExitedArea(Object2D obj){
        if(obj instanceof Grandma){
            this.target = null;
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        if(sprite != null && this.target != null){
            Sprite spriteTarget = this.target.createCurrentSprite();
            
            if(spriteTarget != null){
                sprite.setPosition(spriteTarget.getX() + spriteTarget.getWidth() / 2 - 20 - 2, spriteTarget.getY() + spriteTarget.getHeight() - 2);
            }else{
                sprite.setPosition(this.target.getPositionBody().x / P2M, this.target.getPositionBody().y / P2M);
            }
        }else{
            return null;
        }
        
        return sprite;
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        if(objThatTriggered instanceof Grandma){
            this.notifyGameEventListener(this.eventType, this.details, Vector2.Zero);
        }
    }
    
}
