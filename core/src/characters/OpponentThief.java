/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Object2D;
import java.io.File;

/**
 *
 * @author Deneyr
 */
public class OpponentThief extends OpponentCAC1{
    private static final Texture OPPTHIEFTEXT = new Texture("character" + File.separator + "spritemapkairaVoleur-01.png");
    
    public OpponentThief(World world, Object2D target){
        super(100, target);
    }
    
    public OpponentThief(World world, Object2D target, float posX, float posY){
        super(100, target);
        
        this.texture = OPPTHIEFTEXT;
        
        this.maxSpeed = 6f;
        
        this.initializeGraphic();
        this.listAnimations.get(2).setFrameDuration(0.15f);
        this.listAnimations.get(3).setFrameDuration(0.15f);
        
        this.initializePhysicCAC1(world, posX, posY);
        
        this.updateFixture();
        
        this.previousSide = this.side;
    }
}
