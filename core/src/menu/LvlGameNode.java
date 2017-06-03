/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BackgroundScreen;
import com.mygdx.game.GUIScreen;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.GameScreen;
import com.mygdx.game.GameWorld;
import com.mygdx.game.HelpGame;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.WorldPlane;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Deneyr
 */
public abstract class LvlGameNode extends GameNode implements GameEventListener{

    /*
    protected static final GameWorld gameWorld = new GameWorld();
    
    protected static final TreeMap<Float, WorldPlane> mapBackgroundPlanes = new TreeMap<Float, WorldPlane>();
    */
    public LvlGameNode(HelpGame game, Batch batch){
        super();
        
        // --- init screen ---
        this.screensDisplayed.clear();
        this.screensDisplayed.add(new BackgroundScreen(batch, game.getMapBackgroundPlanes()));
        this.screensDisplayed.add(new GameScreen());
        this.screensDisplayed.add(new GUIScreen());
        
    }
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime) {
        // Compute the next step of the background logic.
        for(Map.Entry<Float, WorldPlane> plane : game.getMapBackgroundPlanes().entrySet()){
            plane.getValue().step(deltaTime);
        }
        
        // Compute the next step of the environment game logic.
        game.getGameWorld().step(deltaTime);
    }
    
    @Override
    public void renderScreens(HelpGame game, float deltaTime){
        
        for(Screen screenDisplayed : this.screensDisplayed){
            if(screenDisplayed instanceof BackgroundScreen){
                BackgroundScreen backgroundScreen = (BackgroundScreen) screenDisplayed;
                
                backgroundScreen.setTargetCameraPosition(game.getGameWorld().getHeroPosition().x / P2M, game.getGameWorld().getHeroPosition().y / P2M);
            }
        }
        
        super.renderScreens(game, deltaTime);
    }
    
    protected abstract void initializeLevel(HelpGame game);
    
    protected void flushLevel(){
        
    }
    
    @Override
    public void onStartingNode(HelpGame game){
        this.initializeLevel(game);
    }
    
    @Override
    public void onEndingNode(HelpGame game){
        this.flushLevel();
    }

    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {

    }
    
    // List init levels.
    
}
