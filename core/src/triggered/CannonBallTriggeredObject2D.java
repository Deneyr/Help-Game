/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class CannonBallTriggeredObject2D extends TriggeredObject2D{

    public static final String CANNONBALLTEXTURE = "Canon_Boulet.png";
    
    private int damageInflicted;
    
    public CannonBallTriggeredObject2D(){
        super();
        
        this.damageInflicted = 2;
        
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(CANNONBALLTEXTURE, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 50, 50);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, 3);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 0);
            this.listAnimations.add(new Animation(0.2f, array));
        }
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        float radius = 20;

        super.initialize(world, position, speed, radius * 1.1f);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(radius * P2M);
        circle.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.8f; 
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        fix.setSensor(true);
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered){
            Vector2 targetPhysicBody = new Vector2(objThatTriggered.getPositionBody());
            Vector2 dirDamage = targetPhysicBody.sub(this.getPositionBody());
            dirDamage = dirDamage.nor();
            
            if(objThatTriggered instanceof Grandma){
                Grandma grandma = (Grandma) objThatTriggered;
                
                grandma.applyDamage(this.damageInflicted, dirDamage, this);
                
                this.changeAnimation(1, false);

                super.trigger(objThatTriggered);
            }else if(objThatTriggered instanceof Character2D){
                Character2D chara = (Character2D) objThatTriggered;
                
                chara.applyDamage(50, dirDamage, this);
            }else{
                this.physicBody.setLinearVelocity(Vector2.Zero);
                this.changeAnimation(1, false);
                super.trigger(objThatTriggered);
            }
        }
    }
    
    @Override
    protected void SetBody(BodyDef bodyDef){
        bodyDef.gravityScale = 0;
    }
    
    @Override
    protected void SetCollisionMask(FixtureDef fixtureDef){
        fixtureDef.filter.categoryBits = 0x0004;
        fixtureDef.filter.maskBits = 0x0003;
    }
    
    @Override
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        fixtureDef.filter.categoryBits = 0x0004;
        fixtureDef.filter.maskBits = 0x0003;
    }
    
    @Override
    public void onOutOfScreen(double dist){
        if(this.object2DStateListener.get() != null){
            this.object2DStateListener.get().onStateChanged(this, Object2DStateListener.Object2DState.DEATH, 0);
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        /*if(sprite != null){
            sprite.setColor(0.5f, 0.5f, 0.5f, 1f);
        }*/
        return sprite;
    }
    
    @Override
    public void reflectBullet(Object2D reflecter){
        if(!this.isTriggered){
            this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "hitProjectile", new Vector2(this.getPositionBody()));
            
            this.physicBody.setLinearVelocity(Vector2.Zero);
            this.changeAnimation(1, false);
            super.trigger(reflecter);
        }
    }
    
    @Override
    public boolean IsDynamicObject(){
        return true;
    }
}
