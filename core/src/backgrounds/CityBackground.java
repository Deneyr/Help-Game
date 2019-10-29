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
public class CityBackground extends BackgroundWorld{

    public static final String BUILDING = "background/Decors_Fond1.png";
    
    public CityBackground(int seed){
        super(seed);
        
        this.ratioDist = 0.6f;
        
        this.assignTextures();
    }
    
    
    @Override
    public void step(float delta) {
        // nothing to do for now.
    }
    
    @Override
    public void assignTextures(){
        
        Texture building = TextureManager.getInstance().getTexture(BUILDING, this);
        
        if(building != null){
            BackgroundPart part = new BackgroundPart(111 * P2M, new Vector2(-1000 * P2M, -40 * P2M),  new Vector2(50000 * P2M, -40 * P2M), 1f);
        
            part.addObject2D2Scenary(building, 70);
            part.createSpriteList(this.seed);

            this.backgroundPartList.put(part.getStartPart().x, part);
        }
    } 
    
}
