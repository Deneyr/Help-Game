/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BackgroundWorld;
import static com.mygdx.game.HelpGame.P2M;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class NearBackground extends BackgroundWorld{

    public static final String BUILDING1 = "building/Background_Maison.png";
    public static final String BUILDING2 = "building/Background_Maison10.png";
    public static final String BUILDING3 = "building/Background_Maison3.png";
    
    
    public NearBackground(int seed){
        super(seed);
        
        this.ratioDist = 0.9f;
        
        this.assignTextures();
    }
    
    @Override
    public void step(float delta) {
        // nothing to do for now.
    }
    
    @Override
    public void assignTextures(){

        Texture build1 = TextureManager.getInstance().getTexture(BUILDING1, this);
        Texture build2 = TextureManager.getInstance().getTexture(BUILDING2, this);
        Texture build3 = TextureManager.getInstance().getTexture(BUILDING3, this);
        
        if(build1 != null
                && build2 != null
                && build3 != null){
            
            BackgroundPart part = new BackgroundPart(111 * P2M, new Vector2(-1000 * P2M, -25 * P2M),  new Vector2(2000 * P2M, -25 * P2M), 1f);
        
            part.addObject2D2Scenary(build1, 30);
            part.addObject2D2Scenary(build2, 30);
            part.addObject2D2Scenary(build3, 10);
            part.createSpriteList(this.seed);

            this.backgroundPartList.put(part.getStartPart().x, part);
            
        }
    }
}
