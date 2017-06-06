/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.HelpGame;
import java.util.ArrayList;
import java.util.List;
import ressourcesmanagers.ResourceManager;
import ressourcesmanagers.ResourceManagerListener;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class GameNodeManager extends GameNode implements Disposable, ResourceManagerListener{

    private List<GameNode> gameNodes;
    
    private GameNode currentGameNode;
    
    private LoadingGameNode loadingGameNode;
    
    private boolean waiting4Resources;
    
    public GameNodeManager(Batch batch){
        super();
        
        this.gameNodes = new ArrayList<GameNode>();
        
        this.currentGameNode = null;
        
        this.waiting4Resources = false;
        
        TextureManager.getInstance().addGameEventListener(this);
        
        // loading game node part.
        
        this.loadingGameNode = new LoadingGameNode(batch);
        
    }
    
    
    public void addGameNode(GameNode gameNode){
        this.gameNodes.add(gameNode);
    }
    
    public boolean changeCurrentGameNode(HelpGame game, GameNode gameNode){
        if(gameNode != null && this.gameNodes.contains(gameNode)){
            if(this.currentGameNode != null){
                this.currentGameNode.onEndingNode(game);
            }
            this.waiting4Resources = gameNode.onStartingNode(game);

            this.currentGameNode = gameNode;
            
            return true;
        }
        return false;
    }
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime){
        // Update current node.
        if(!this.waiting4Resources){
            GameNode nextNode = this.currentGameNode.getNextGameNode();   
            this.changeCurrentGameNode(game, nextNode);
        }
        
        // Update logic current game node.
        if(this.waiting4Resources){
            this.loadingGameNode.updateLogic(game, deltaTime);
        }else{
            this.currentGameNode.updateLogic(game, deltaTime);
        }
    }
    
    @Override
    public void renderScreens(HelpGame game, float deltaTime){
        if(this.waiting4Resources){
            this.loadingGameNode.renderScreens(game, deltaTime);
        }else{
            this.currentGameNode.renderScreens(game, deltaTime);
        }
    }
    
    @Override
    public void dispose() {
        
    }

    @Override
    public void onResourcesLoaded() {
        this.waiting4Resources = false;
    }
    
}
