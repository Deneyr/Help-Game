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
public class FarBackground extends BackgroundWorld{

    public static final Texture FARBACKGROUND = new Texture("background" + File.separator + "cityBackground.jpg");
    
    
    public FarBackground(int seed){
        super();
        
        this.ratioDist = 0.001f;
        
        BackgroundPart part = new BackgroundPart(1000 * P2M, new Vector2(0 * P2M, 100 * P2M),  new Vector2(1000 * P2M, 100 * P2M), 3f);
        
        part.addObject2D2Scenary(FARBACKGROUND, 100);
        part.createSpriteList(seed);
        
        this.backgroundPartList.put(part.getStartPart().x, part);
    }
    
    @Override
    public void step(float delta) {
        // nothing to do for now.
    }
}