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
public class DamageActionFixture extends ActionFixtures{
    
    private int damageInflicted;
    
    private boolean encounterSomething;
    
    private boolean canBeApplied;
    
    public DamageActionFixture(Set<Fixture> fixtures, int damageInflicted){
        super(fixtures);
        
        this.damageInflicted = damageInflicted;
        
        this.encounterSomething = false;
        
        this.canBeApplied = true;
    }
    
    @Override
    public void applyAction(final float deltaTime, final Object2D owner) {
        super.applyAction(deltaTime, owner);

        this.encounterSomething = false;
        for(Object2D obj : DamageActionFixture.this.setObject2DInside){
            if(obj != owner){
                Vector2 physicBody = new Vector2(obj.physicBody.getPosition());
                Vector2 dirDamage = physicBody.sub(owner.physicBody.getPosition());
                dirDamage = dirDamage.nor();

                boolean isEffective = obj.applyDamage(DamageActionFixture.this.damageInflicted, dirDamage, owner);

                this.encounterSomething |= isEffective;
            }
        }
    }
    
    public void applyAction(final float deltaTime, final Object2D owner, float delay) {
        if(this.canBeApplied){
            this.canBeApplied = false;
            Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        DamageActionFixture.this.canBeApplied = true;
                        DamageActionFixture.this.applyAction(deltaTime, owner);
                    }
            }, delay);
        }
    }

    /**
     * @param damageInflicted the damageInflicted to set
     */
    public void setDamageInflicted(int damageInflicted) {
        this.damageInflicted = damageInflicted;
    }

    /**
     * @return the encounterSomething
     */
    public boolean hasEncounteredSomething() {
        return encounterSomething;
    }
    
}
