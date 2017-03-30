/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import java.util.List;
import java.util.Set;

/**
 *
 * @author françois
 */
public class KinematicActionFixtures extends ActionFixtures{

    public KinematicActionFixtures(Set<Fixture> fixtures){
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
        
        Vector2 velocity = owner.physicBody.getLinearVelocity();
        for(Object2D obj : this.setObject2DInside){
            if(obj instanceof Character2D){
                obj.physicBody.setTransform(obj.physicBody.getPosition().add(new Vector2(velocity.x/1.005f * deltaTime, 0)), 0);
            }
        }
    }
    
}
