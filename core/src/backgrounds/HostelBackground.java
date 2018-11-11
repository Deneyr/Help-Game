/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BackgroundWorld;
import static com.mygdx.game.HelpGame.P2M;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class HostelBackground extends BackgroundWorld{

    public static final String HOSTEL = "background/House_Hostel.png";
    
    private Vector2 startPointPart;
    
    public HostelBackground(int seed, float startPointX, float startPointY){
        super(seed);
        
        this.ratioDist = 1f;
        
        this.startPointPart = new Vector2(startPointX * P2M, startPointY * P2M);
        
        this.assignTextures();
    }
    
    @Override
    public void step(float delta) {
        // nothing to do for now.
    }
    
    @Override
    public void assignTextures(){

        Texture house = TextureManager.getInstance().getTexture(HOSTEL, this);
        
        if(house != null){
            
            String[][] residenceMap = {
                {"0:1", "3:0", "5:2", "1:2"},
                {"0:1", "1:0", "1:1", "1:2"},
                {"1:1", "1:1", "5:0", "5:1"}
            };
            
            
            ResidencePart part = new ResidencePart(this.startPointPart, residenceMap, 200, 200);
        
            part.addObject2D2Scenary(house);
            part.createSpriteList(this.seed);

            this.backgroundPartList.put(part.getStartPart().x, part);
            
        }
    }
    
    @Override
    public void createSoldidObj(World world){
        String[][] residenceMap = {
            {"0:1", "3:0", "5:2", "1:2"},
            {"0:1", "1:0", "1:1", "1:2"},
            {"1:1", "1:1", "5:0", "5:1"}
        };
    }
}
