/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.scenery.Ventilo;
import java.util.Set;

/**
 *
 * @author françois
 */
public class TriggerActionFixture extends ActionFixtures{

    public TriggerActionFixture(Set<Fixture> fixtures) {
        super(fixtures);
    }
    
    
    @Override
    public void applyAction(float deltaTime, final Object2D owner) {
        super.applyAction(deltaTime, owner);
        if(owner != null && owner instanceof TriggeredObject2D){
            TriggeredObject2D triggerObj = (TriggeredObject2D) owner;

            for(Object2D obj : this.setObject2DInside){
                if(obj != owner){
                    triggerObj.trigger(obj);
                }
            }
        }
    }
    
}
