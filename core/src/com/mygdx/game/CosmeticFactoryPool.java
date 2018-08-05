/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import cosmetics.CosmeticObject2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Deneyr
 */
public class CosmeticFactoryPool <T extends CosmeticObject2D> extends Pool<T>{

    private Class class2Generate;
    
    public CosmeticFactoryPool(Class class2Generate){
        super();
        
        this.class2Generate = class2Generate;
    }
    
    
    public T obtainTriggeredObject2D(World world, Object2D giver, Object2D receiver, Vector2 position, Vector2 speed, float strength) {
        
        T obj = super.obtain();
        
        obj.initialize(world, giver, receiver, position, speed, strength);
        
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

    public void freeTriggeredObject2D(T obj) {
        
        this.free(obj);
    }
    
    
}
