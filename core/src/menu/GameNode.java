/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.Screen;
import com.mygdx.game.HelpGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Deneyr
 */
public abstract class GameNode {
    
    protected List<Screen> screensDisplayed;
    
    protected Map<Integer, GameNode> outputGameNode;
    
    public GameNode(){
        this.outputGameNode = new HashMap<Integer, GameNode>();
        
        this.screensDisplayed = new ArrayList<Screen>();
    }
    
    public abstract void updateLogic(HelpGame game, float deltaTime);
    
    public void renderScreens(HelpGame game, float deltaTime){
        for(Screen screenDisplayed : this.screensDisplayed){
            game.setScreen(screenDisplayed);
            game.render();
        }
    }
    
    public void onStartingNode(HelpGame game){
        // Nothing to do.
    }
    
    public void onEndingNode(HelpGame game){
        // Nothing to do.
    }
    
}
