/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.box2d.Fixture;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Deneyr
 */
public class ActivableActionFixture extends TriggerActionFixture{
    
    private final int activableKey;
    
    public ActivableActionFixture(Set<Fixture> fixtures, int activableKey) {
        super(fixtures);
        
        this.activableKey = activableKey;
    }
    
    @Override
    public void applyAction(float deltaTime, final Object2D owner) {
        HashSet<Object2D> prevSetObject2DInside = new HashSet<Object2D>(this.setObject2DInside);

        this.applyDefaultAction();
        
        HashSet<Object2D> afterSetObject2DInside = new HashSet<Object2D>(this.setObject2DInside);
        
        if(owner != null && owner instanceof TriggeredObject2D){
            TriggeredObject2D triggerObj = (TriggeredObject2D) owner;

            afterSetObject2DInside.removeAll(prevSetObject2DInside);
            prevSetObject2DInside.removeAll(this.setObject2DInside);
            
            for(Object2D obj : afterSetObject2DInside){
                triggerObj.onObj2DEnteredArea(obj);
            }
            
            for(Object2D obj : prevSetObject2DInside){
                triggerObj.onObj2DExitedArea(obj);
            }
            
            if(Gdx.input.isKeyJustPressed(this.activableKey)){
                for(Object2D obj : this.setObject2DInside){
                    if(obj != owner && obj instanceof Grandma){
                        triggerObj.trigger(obj);
                    }
                }
            }
        }
    }
    
}
