/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.SolidObject2D;
import com.mygdx.game.TriggeredObject2D;

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
        float radius = 33;
        
        super.initialize(world, position, speed, radius * 1.1f);
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && 
                (objThatTriggered instanceof Grandma || objThatTriggered instanceof SolidObject2D)){
            
            if(objThatTriggered instanceof Grandma){
                Grandma grandma = (Grandma) objThatTriggered;
                
                grandma.setLifePoints(grandma.getLifePoints() - this.damageInflicted);
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
        if(dist > 10 * P2M && this.object2DStateListener.get() != null){
            this.object2DStateListener.get().notifyStateChanged(this, Object2DStateListener.Object2DState.DEATH, 0);
        }
    }
}
