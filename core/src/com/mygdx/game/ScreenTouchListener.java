/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author Deneyr
 */
public interface ScreenTouchListener {
    
    void touchDown(int screenX, int screenY, int pointer, int button);
    
    void touchUp(int screenX, int screenY, int pointer, int button);

    void touchDragged(int screenX, int screenY, int pointer);

    void mouseMoved(int screenX, int screenY);
    
    void scrolled(int amount);
}
