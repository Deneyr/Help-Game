/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Deneyr
 */
public interface GameEventListener {
    
    public void onGameEvent(EventType type, String details, Vector2 location);
    
    public enum EventType{
        SCORE,
        CINEMATIC,
        DAMAGE,
        ATTACK,
        LIFE,
        TAKE,
        GAMEOVER,
        GAMESTART
    }
}
