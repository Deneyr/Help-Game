/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import java.util.Set;

/**
 *
 * @author françois
 */
public class DamageActionFixture extends ActionFixtures{
    
    private int damageInflicted;
    
    private boolean encounterSomething;
    
    public DamageActionFixture(Set<Fixture> fixtures, int damageInflicted){
        super(fixtures);
        
        this.damageInflicted = damageInflicted;
        
        this.encounterSomething = false;
    }
    
    @Override
    public void applyAction(float deltaTime, final Object2D owner) {
        super.applyAction(deltaTime, owner);

        for(Object2D obj : DamageActionFixture.this.setObject2DInside){
            if(obj != owner){
                Vector2 physicBody = new Vector2(obj.physicBody.getPosition());
                Vector2 dirDamage = physicBody.sub(owner.physicBody.getPosition());
                dirDamage = dirDamage.nor();

                boolean isEffective = obj.applyDamage(DamageActionFixture.this.damageInflicted, dirDamage, owner);

                DamageActionFixture.this.encounterSomething |= isEffective;
            }
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
    public boolean isEncounterSomething() {
        return encounterSomething;
    }
    
}
