/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author françois
 */
public interface Object2DStateListener {
    
    void onStateChanged(Object2D notifier, Object2DState state, int animCounter, boolean canReplace);
    
    void onStateChanged(Object2D notifier, Object2DState state, int animCounter);
    
    void onObject2D2Create(Object2D notifier, Class obj2DClass, Vector2 position, Vector2 speed);
    
    public enum Object2DState{
        DAMAGE_TOOK,
        DEATH,
        TOOK_BY_PLAYER
    }
}
