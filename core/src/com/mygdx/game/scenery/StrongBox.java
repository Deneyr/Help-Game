/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import java.util.List;
import ressourcesmanagers.TextureManager;
import triggered.UpTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class StrongBox extends SmallBox{
    
    private static final String STRONGBOXTEXT = "destroyable/Spritemap-GrosseCaisse.png";
    
    public StrongBox(World world, float posX, float posY) {
        super(world, posX, posY, 6);
        
        this.Initialize(world, posX, posY, 40f, 36f, 20f);
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(STRONGBOXTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 79, 73);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(1, 6);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 0);
            array.removeRange(1, 5);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 1);
            array.removeRange(1, 4);
            this.listAnimations.add(new Animation(0.2f, array));
            
            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 2);
            array.removeRange(1, 3);
            this.listAnimations.add(new Animation(0.2f, array));
            
            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 3);
            array.removeRange(1, 2);
            this.listAnimations.add(new Animation(0.2f, array));
            
            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 4);
            array.removeRange(1, 1);
            this.listAnimations.add(new Animation(0.2f, array));

            array = new Array<TextureRegion>(tmp[0]);
            array.removeRange(0, 5);
            this.listAnimations.add(new Animation(0.2f, array));

            this.changeAnimation(0, true);
        }
    }
    
    @Override
    protected void spawnLoot(Vector2 dirDamage, Object2D damageOwner)
    {
        if(damageOwner == this){
            return;
        }
        
        dirDamage.scl(2f);
        
        for(int i=0; i < 20; i++){
        
            if(Math.random() < 0.5){
                this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), (new Vector2(dirDamage)).rotate((float) Math.random()*360));
            }

        }
    }
    
    @Override
    public void setInfluenceList(List<String> lInfluences){      
        for(String influence : lInfluences){
            influence = influence.toLowerCase();
            if(lInfluences.contains("suicide")){
                this.applyDamage(Integer.MAX_VALUE, Vector2.Zero, this);
            }
        }
    }
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
        // Nothing to do
    }
}
