/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import java.util.Set;

/**
 *
 * @author françois
 */
public class ShieldActionFixture extends DamageActionFixture{

    private float ratioBounce;
    
    private boolean applyDamage;
    
    private boolean canBeApplied;
    
    public ShieldActionFixture(Set<Fixture> fixtures, int damageInflicted, float ratioBounce) {
        super(fixtures, damageInflicted);
        
        this.ratioBounce = ratioBounce;
        this.applyDamage = true;
        
        this.canBeApplied = true;
    }

    @Override
    public void applyAction(final float deltaTime, final Object2D owner) {
        
        if(this.applyDamage){
            if(this.canBeApplied){
                this.canBeApplied = false;
                Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            ShieldActionFixture.super.applyAction(deltaTime, owner);
                            ShieldActionFixture.this.canBeApplied = true;
                        }
                    }, 0.15f);
             }
        }else{
            applyDefaultAction();

            for(Object2D obj : this.setObject2DInside){
                if(obj != owner && obj instanceof Character2D){
                    Character2D chara = (Character2D) obj;
                    
                    Vector2 dirChara = chara.physicBody.getPosition().sub(owner.physicBody.getPosition());
                    
                    Vector2 dirDamage = new Vector2(1, 0);
                    dirDamage = dirDamage.scl(dirDamage.dot(dirChara));
                    
                    if(dirDamage.isZero(0.001f)){
                        dirDamage = new Vector2(1, 0);
                    }
                    
                    dirDamage = dirDamage.nor();
                    dirDamage = dirDamage.scl(this.ratioBounce * 100);

                    chara.applyBounce(dirDamage, owner);
                }
            }
        }

    }
    
    /**
     * @return the applyDamage
     */
    public boolean isApplyDamage() {
        return applyDamage;
    }

    /**
     * @param applyDamage the applyDamage to set
     */
    public void setApplyDamage(boolean applyDamage) {
        this.applyDamage = applyDamage;
    }
    
}
