/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author françois
 * @param <T>
 */
public class Object2DFactoryPool<T extends TriggeredObject2D> extends Pool<T> implements FactoryPool<T>{

    @Override
    public T obtainTriggeredObject2D(World world, Vector2 position, Vector2 speed) {
        T obj = super.obtain();
        
        obj.initialize(world, position, speed);
        
        return obj;
    }

    @Override
    protected T newObject() {
        TriggeredObject2D newTriggeredObject2D = T.createNewTriggeredObject2D();
        
        if(newTriggeredObject2D != null){
            return (T) newTriggeredObject2D;
        }
        return null;
    }

    @Override
    public void freeTriggeredObject2D(T obj) {
        this.free(obj);
    }
    
    
}
