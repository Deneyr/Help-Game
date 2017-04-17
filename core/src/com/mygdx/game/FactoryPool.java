/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author françois
 * @param <T>
 */
public interface FactoryPool<T extends TriggeredObject2D>{
    
    public T obtainTriggeredObject2D(World world, Vector2 position, Vector2 speed);
    
    public void freeTriggeredObject2D(T obj);
    
}
