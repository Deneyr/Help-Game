/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

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
    
    private GameNode nextGameNode;
    
    public GameNode(){
        this.outputGameNode = new HashMap<Integer, GameNode>();
        
        this.screensDisplayed = new ArrayList<Screen>();
        
        this.nextGameNode = null;
    }
    
    public abstract void updateLogic(HelpGame game, float deltaTime);
    
    public void renderScreens(HelpGame game, float deltaTime){
        for(Screen screenDisplayed : this.screensDisplayed){
            game.setScreen(screenDisplayed);
            game.renderScreen();
        }
    }
    
    public void addNextNode(int index, GameNode gameNode){
        this.outputGameNode.put(index, gameNode);
    }
    
    public boolean onStartingNode(HelpGame game){
        return false;
    }
    
    public void onEndingNode(HelpGame game){
        // Nothing to do.
    }
    
     /**
     * @return the nextGameNode
     */
    public GameNode getNextGameNode() {
        return this.nextGameNode;
    }
    
}
