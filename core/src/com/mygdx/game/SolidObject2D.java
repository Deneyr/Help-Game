/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 *
 * @author françois
 */
public abstract class SolidObject2D extends Object2D{
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        return true;
    } 
    
    @Override
    protected void setCollisionFilterMask(FixtureDef fixtureDef, boolean reset){
        
        if(reset){
            super.setCollisionFilterMask(fixtureDef, true);
            return;
        }
        
        fixtureDef.filter.categoryBits = 0x0002;
    }
}
