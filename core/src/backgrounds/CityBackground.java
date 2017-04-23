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
import java.io.File;

/**
 *
 * @author françois
 */
public class CityBackground extends BackgroundWorld{

    public static final Texture BUILDING = new Texture("background" + File.separator + "Decors_Fond1.png");
    
    
    public CityBackground(int seed){
        super();
        
        this.ratioDist = 0.6f;
        
        BackgroundPart part = new BackgroundPart(111 * P2M, new Vector2(-2000 * P2M, -225 * P2M),  new Vector2(2000 * P2M, -225 * P2M), 1f);
        
        part.addObject2D2Scenary(BUILDING, 70);
        part.createSpriteList(seed);
        
        this.backgroundPartList.put(part.getStartPart().x, part);
    }
    
    
    @Override
    public void step(float delta) {
        // nothing to do for now.
    }
    
}
