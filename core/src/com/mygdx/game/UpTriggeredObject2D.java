/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author françois
 */
public class UpTriggeredObject2D extends TriggeredObject2D{

    private static final Texture UPTEXTURE = new Texture("Collectible_Piece_spritemap.png");
    
    private boolean isTriggered;
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        super.initialize(world, position, speed, 20);
        
        this.isTriggered = false;
    }
    
    public static TriggeredObject2D createNewTriggeredObject2D(){
        return new UpTriggeredObject2D();
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && objThatTriggered instanceof Grandma){
            this.isTriggered = true;
            
            Grandma grandma = (Grandma) objThatTriggered;
            
            grandma.setLifePoints(grandma.getLifePoints() + 1);
        }
        
        super.trigger(objThatTriggered);
    }
    
    @Override
    public boolean IsDynamicObject(){
        return true;
    }
}
