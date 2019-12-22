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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import ressourcesmanagers.TextureManager;
import triggered.CannonBallTriggeredObject2D;
import triggered.UpTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class MetalBox extends SmallBox{
    
    private static final String METALBOXTEXT = "destroyable/Spritemap-MetalCaisse.png";
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    private boolean canBeHitAgain;
    
    public MetalBox(World world, float posX, float posY) {
        super(world, posX, posY, 3);
        
        this.Initialize(world, posX, posY, 76f * SCALE_X, 76f * SCALE_Y, 20f);
        
        this.canBeHitAgain = true;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(METALBOXTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 152, 152);
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
    protected void spawnLoot(Vector2 dirDamage, Object2D damageOwner)
    {
        dirDamage.scl(2f);
        
        for(int i=0; i < 20; i++){
        
            if(Math.random() < 0.5){
                this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), (new Vector2(dirDamage)).rotate((float) Math.random()*360));
            }

        }
    }
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        // Nothing to do
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        
        if(damageOwner instanceof CannonBallTriggeredObject2D){
            damage = 1;

            float sign = -Math.signum((new Vector2(1f, 0f).dot(dirDamage)));

            Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);
            ptApplication.add(this.physicBody.getPosition());

            boolean result = this.applyDamage(damage, dirDamage, damageOwner, ptApplication);

            int animationUpdated = this.getLifePointsMax() - this.getLifePoints();

            if(this.currentAnimation != animationUpdated){
                this.changeAnimation(animationUpdated, true);
            }

            if(result){
                this.notifyGameEventListener(GameEventListener.EventType.DAMAGE, "metalHitDamage", new Vector2(this.getPositionBody()));
                if(this.getLifePoints() <= 0){
                    this.notifyGameEventListener(GameEventListener.EventType.DAMAGE, "boxCrash", new Vector2(this.getPositionBody()));

                    Vector2 upVector = new Vector2(0, 1);
                    float angle = dirDamage.nor().angle(upVector) / 2f;
                    dirDamage = dirDamage.rotate(angle);

                    this.notifyObject2DStateListener(Object2DStateListener.Object2DState.DEATH, 6, false);

                    this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), dirDamage.scl(2f));

                    this.spawnLoot(new Vector2(dirDamage.scl(2f)), damageOwner);
                }
            }

            return result;
        }else{ 
            if(this.canBeHitAgain){
                this.notifyGameEventListener(GameEventListener.EventType.ACTION, "metalHit", new Vector2(this.getPositionBody())); 
                
                this.canBeHitAgain = false;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        MetalBox.this.canBeHitAgain = true;
                    }
                }, 0.5f);
            }
        }
        return false;
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
}
