/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.OpponentCAC1;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author françois
 * @param <T>
 */
public interface OpponentPool<T extends OpponentCAC1>{
    
    public T obtainTriggeredObject2D(World world, Object2D target, float posX, float posY);
    
    public void freeTriggeredObject2D(T obj);
    
}