/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author françois
 */
public interface Object2DStateListener {
    
    void notifyStateChanged(Object2D notifier, Object2DState state, int animCounter, boolean canReplace);
    
    void notifyStateChanged(Object2D notifier, Object2DState state, int animCounter);
            
    public enum Object2DState{
        DAMAGE_TOOK,
        DEATH
    }
}
