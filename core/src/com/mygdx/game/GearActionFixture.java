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
public class GearActionFixture extends ActionFixtures{

    public GearActionFixture(Set<Fixture> fixtures){
        super(fixtures);
    }
    
    @Override
    public void addObject2dInside(Object2D obj){
        if(obj instanceof Character2D){
            super.addObject2dInside(obj);
        }
    }
    
    @Override
    public void applyAction(float deltaTime, Object2D owner) {
        super.applyAction(deltaTime, owner);
        
        float angularOffset = owner.physicBody.getAngularVelocity() * deltaTime;
        
        if(this.setObject2DInside.size() > 0){
            for(Object2D obj : this.setObject2DInside){
                Vector2 positionOffset = obj.getPositionBody().sub(owner.getPositionBody());
                positionOffset.rotateRad(angularOffset);
                          
                if(obj instanceof Character2D){              
                    obj.physicBody.setTransform(owner.getPositionBody().add(positionOffset), 0);
                }
            }
        }
    }
    
}
