/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Object2D;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class OpponentThief extends OpponentCAC1{
    private static final String OPPTHIEFTEXT = "character/spritemapkairaVoleur-01.png";
    
    public OpponentThief(World world, Object2D target){
        super(100, target);
    }
    
    public OpponentThief(World world, Object2D target, float posX, float posY){
        super(100, target);
        
        this.maxSpeed = 6f;
        
        // Part graphic
        this.assignTextures();
        
        this.initializePhysicCAC1(world, posX, posY);
        
        this.updateFixture();
        
        this.previousSide = this.side;
        
        this.hasLifeBar = false;
    }
    
    @Override
    public void assignTextures(){
        
        this.texture = TextureManager.getInstance().getTexture(OPPTHIEFTEXT, this);
        
        if(this.texture != null){
            this.initializeGraphic();
            
            // Part graphic
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 76, 76);
            this.listAnimations.add(new Animation(0.2f, tmp[4]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);

            this.listAnimations.add(new Animation(0.2f, tmp[5]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            
            this.listAnimations.get(2).setFrameDuration(0.15f);
            this.listAnimations.get(3).setFrameDuration(0.15f);
        }
    }
}
