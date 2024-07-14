/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import characters.BossHummer;
import characters.BossTank;
import characters.OpponentCAC1;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import ressourcesmanagers.TextureManager;
import triggered.UpTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class StrongChestBoss extends SmallBox{
    
    private static final String STRONGCHESTBOXTEXT = "destroyable/Help_Props_2052x128_CoffreBoss.png";
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    protected boolean canBeHitAgain;
    
    public StrongChestBoss(World world, float posX, float posY) {
        super(world, posX, posY, 2);
        
        this.Initialize(world, posX, posY, 58f * SCALE_X, 62f * SCALE_Y, -50f * SCALE_X, 0, 30f);
        
        this.canBeHitAgain = true;
    }
    
    public StrongChestBoss(World world, float posX, float posY, int lifePoint) {
        super(world, posX, posY, lifePoint);
        
        this.canBeHitAgain = true;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(STRONGCHESTBOXTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 228, 129);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, 8);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            //array.removeRange(0, 0);
            this.listAnimations.add(new Animation(0.2f, array));

            this.changeAnimation(0, true);
        }
    }
    
    @Override
    protected void spawnLoot(Vector2 dirDamage, Object2D damageOwner)
    {
        if(damageOwner == this){
            return;
        }
        
        for(Fixture fixture : this.collisionFixture){
            fixture.setSensor(true);
        }
        
        //if(Math.random() > 1){
        
            dirDamage.scl(10);
            
            this.notifyObject2D2CreateListener(BossTank.class, this.getPositionBody().scl(1 / P2M).add(new Vector2(0, 75)), new Vector2(0, 0));
        //}
    }
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        // Nothing to do
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        
        if(damageOwner != this){
            damage = 1;
        }
        
        float sign = -Math.signum((new Vector2(1f, 0f).dot(dirDamage)));
        
        Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);
        ptApplication.add(this.physicBody.getPosition());

        boolean result = false;
        
        if(this.getLifePoints() > 1){
            result = this.applyDamage(damage, dirDamage, damageOwner, ptApplication);

            int animationUpdated = this.getLifePointsMax() - this.getLifePoints();

            if(this.currentAnimation != animationUpdated){
                this.changeAnimation(animationUpdated, true);
            }

            if(result && this.getLifePoints() <= 1){

                this.notifyGameEventListener(GameEventListener.EventType.ACTION, "strongChestOpen", new Vector2(this.getPositionBody()));

                Vector2 upVector = new Vector2(0, 1);
                float angle = dirDamage.nor().angle(upVector) / 2f;
                dirDamage = dirDamage.rotate(angle);
                
                
                final Vector2 finalDirDamage = new Vector2(dirDamage);
                final Object2D finalDamageOwner = damageOwner;
                
                this.changeAnimation(1, false);
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        StrongChestBoss.this.spawnLoot(new Vector2(finalDirDamage.scl(2f)), finalDamageOwner);
                    }
                }, 1.5f);
            }
        }else{
            if(this.canBeHitAgain){
                this.notifyGameEventListener(GameEventListener.EventType.ACTION, "metalHit", new Vector2(this.getPositionBody())); 
                
                this.canBeHitAgain = false;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        StrongChestBoss.this.canBeHitAgain = true;
                    }
                }, 0.5f);
            }
        }
        
        return result;
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
}

