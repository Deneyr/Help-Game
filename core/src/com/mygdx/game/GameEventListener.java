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
    
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location);
    
    public enum EventType{
        SCORE,
        CINEMATIC,
        ENDCINEMATIC,
        DAMAGE,
        ATTACK,
        LIFE,
        ACTION,
        LOOP,
        LOOP_STOP,
        TAKE,
        GAMEOVER, // defeat, success
        ENTERSTRUCT, // Enter a residence
        QUITSTRUCT,// Quit a residence
        LVLSTART, // The beginning of the level (before the GAMESTART event).
        GAMESTART, // Used to display the start level banner.
        CHECKPOINT, // reached
        MENUOPTION, // move, select
        GAMENODECHANGE // restart, [id gamenode]
    }
}
