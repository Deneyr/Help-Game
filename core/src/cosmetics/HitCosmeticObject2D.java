/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmetics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class HitCosmeticObject2D  extends CosmeticObject2D{
    public static final String HIT_TEXTURE = "ImpactHit.png";
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    public HitCosmeticObject2D(){
        super();
        
        this.radius = 10;
        
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(HIT_TEXTURE, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 152, 152);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>();
            array.add(tmp[0][0]);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>();
            array.add(tmp[0][1]);
            this.listAnimations.add(new Animation(0.2f, array));
            
            array = new Array<TextureRegion>();
            array.add(tmp[0][2]);
            this.listAnimations.add(new Animation(0.2f, array));
            
            array = new Array<TextureRegion>();
            array.add(tmp[0][3]);
            this.listAnimations.add(new Animation(0.2f, array));
        }
    }
    
    @Override
    public void initialize(World world, Object2D giver, Object2D receiver, Vector2 position, Vector2 speed, float strength ) {
        
        super.initialize(world, giver, receiver, position, speed, strength);
        
        this.changeAnimation((int) (Math.random() * 3), true);
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        switch(this.cosmeticState){
            case ALIVE:
                this.cosmeticState = CosmeticState.ONDEATH;             
                break;
            case ONDEATH:
                    this.cosmeticState = CosmeticState.DEAD; 
                    this.notifyObject2DStateListener(Object2DStateListener.Object2DState.TOOK_BY_PLAYER, 1);
                break;
            case DEAD:
                break;
        }
    }
    
    @Override
    public boolean IsDynamicObject(){
        return false;
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}
