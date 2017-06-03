/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.HelpGame;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class MenuManager extends GameNode implements Disposable {

    private List<GameNode> gameNodes;
    
    private GameNode currentGameNode;
    
    private LoadingGameNode loadingGameNode;
    
    public MenuManager(Batch batch){
        super();
        
        this.gameNodes = new ArrayList<GameNode>();
        
        // loading game node part.
        
        this.loadingGameNode = new LoadingGameNode(batch);
        
    }
    
    
    public void addGameNode(GameNode gameNode, boolean isFirstNode){
        this.gameNodes.add(gameNode);
        if(isFirstNode){
            this.currentGameNode = gameNode;
        }
    }
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime){
        
        // Update current node.
        
        this.currentGameNode.updateLogic(game, deltaTime);
    }
    
    @Override
    public void renderScreens(HelpGame game, float deltaTime){
        this.currentGameNode.renderScreens(game, deltaTime);
    }
    
    @Override
    public void dispose() {
        
    }
    
}
