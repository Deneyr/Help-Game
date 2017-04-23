/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author françois
 * @param <T>
 */
public class Object2DFactoryPool<T extends TriggeredObject2D> extends Pool<T> implements FactoryPool<T>{

    private Class class2Generate;
    
    public Object2DFactoryPool(Class class2Generate){
        super();
        
        this.class2Generate = class2Generate;
    }
    
    
    @Override
    public T obtainTriggeredObject2D(World world, Vector2 position, Vector2 speed) {
        
        T obj = super.obtain();
        
        obj.initialize(world, position, speed);
        
        return obj;
    }

    @Override
    protected T newObject() {
        try {
            return (T) this.class2Generate.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(Object2DFactoryPool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Object2DFactoryPool.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;
    }

    @Override
    public void freeTriggeredObject2D(T obj) {
        
        this.free(obj);
    }
    
    
}
