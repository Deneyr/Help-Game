/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.BossHummer;
import characters.OpponentCAC1;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Deneyr
 */
public class BossOpponentFactoryPool <T extends BossHummer> extends Pool<T> implements BossOpponentPool<T>{

    private Class class2Generate;
    
    public BossOpponentFactoryPool(Class class2Generate){
        super();
        
        this.class2Generate = class2Generate;
    }
    
    
    @Override
    public T obtainTriggeredObject2D(World world, Object2D target, float posX, float posY) {
        
        T obj = super.obtain();
        
        obj.initialize(world, target, posX, posY);
        
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