/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import java.util.List;

/**
 *
 * @author françois
 */
public interface WorldPlane extends Disposable, GameEventListener{
    List<Sprite> getSpritesInRegion(float lowerX, float lowerY, float upperX, float upperY);
    
    void step(float delta);
    
    void flushWorld();
}
