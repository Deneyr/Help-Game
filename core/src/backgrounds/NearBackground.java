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
public class NearBackground extends BackgroundWorld{

    public static final Texture BUILDING1 = new Texture("building" + File.separator + "Obstacle_Maison.png");
    public static final Texture BUILDING2 = new Texture("building" + File.separator + "Obstacle_Maison10.png");
    public static final Texture BUILDING3 = new Texture("building" + File.separator + "Obstacle_Maison3.png");
    
    
    public NearBackground(int seed){
        super();
        
        this.ratioDist = 1f;
        
        BackgroundPart part = new BackgroundPart(111 * P2M, new Vector2(-2000 * P2M, -200 * P2M),  new Vector2(2000 * P2M, -200 * P2M), 1f);
        
        part.addObject2D2Scenary(BUILDING1, 30);
        part.addObject2D2Scenary(BUILDING2, 30);
        part.addObject2D2Scenary(BUILDING3, 10);
        part.createSpriteList(seed);
        
        this.backgroundPartList.put(part.getStartPart().x, part);
    }
    
    @Override
    public void step(float delta) {
        // nothing to do for now.
    }
}
