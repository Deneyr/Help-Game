/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Object2D;

/**
 *
 * @author Deneyr
 */
public class AutoCannonCorpus extends CannonCorpus{
   
    public AutoCannonCorpus(World world, Object2D target, float posX, float posY, float angle, float attackCooldown, boolean isMovable, int startDirTargeted) {
        super(world, target, posX, posY, angle);
        
        // Child
        this.cannon = new AutoCannon(this.physicBody, target, world, posX , posY, attackCooldown, isMovable, CannonPosition.fromInteger(startDirTargeted));
        
        // Part graphic
        this.assignTextures();
    }
    
    /*
    public AutoCannonCorpus(World world, Object2D target, float posX, float posY, float angle) {
        super(world, target, posX, posY, angle);
        
        // Child
        this.cannon = new AutoCannon(this.physicBody, target, world, posX , posY);
        
        // Part graphic
        this.assignTextures();
    }*/
   
    
}
