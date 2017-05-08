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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.SolidObject2D;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;

/**
 *
 * @author françois
 */
public class CannonBallTriggeredObject2D extends TriggeredObject2D{

    private static final Texture CANNONBALLTEXTURE = new Texture("Canon_Boulet.png");
    
    private int damageInflicted;
    
    public CannonBallTriggeredObject2D(){
        super();
        
        this.damageInflicted = 3;
        
        // Part graphic
        this.texture = CANNONBALLTEXTURE;
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 50, 50);
        // walk folded
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(1, 3);
        this.listAnimations.add(new Animation(0.2f, array));
        
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 0);
        this.listAnimations.add(new Animation(0.2f, array));
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        float radius = 25;

        super.initialize(world, position, speed, radius * 1.1f);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(radius * P2M);
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
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && 
                (objThatTriggered instanceof Grandma || objThatTriggered instanceof SolidObject2D)){
            
            if(objThatTriggered instanceof Grandma){
                Grandma grandma = (Grandma) objThatTriggered;
                
                Vector2 targetPhysicBody = new Vector2(grandma.getPositionBody());
                Vector2 dirDamage = targetPhysicBody.sub(this.getPositionBody());
                dirDamage = dirDamage.nor();
                
                grandma.applyDamage(3, dirDamage, this);
            }
            
            this.changeAnimation(1, false);

            super.trigger(objThatTriggered);
        }
    }
    
    @Override
    public boolean IsDynamicObject(){
        return false;
    }
    
    @Override
    public void onOutOfScreen(double dist){
        if(this.object2DStateListener.get() != null){
            this.object2DStateListener.get().notifyStateChanged(this, Object2DStateListener.Object2DState.DEATH, 0);
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        if(sprite != null){
            sprite.setColor(0.5f, 0.5f, 0.5f, 1f);
        }
        return sprite;
    }
    
    @Override
    public void reflectBullet(Object2D reflecter){
        if(!this.isTriggered){
            this.physicBody.setLinearVelocity(Vector2.Zero);
            this.changeAnimation(1, false);
            super.trigger(reflecter);
        }
    }
}
