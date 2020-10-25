/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import com.mygdx.game.ScreenTouchListener;
import java.util.HashMap;
import java.util.Map;
import ressourcesmanagers.ResourceManagerListener;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class GameNodeManager extends GameNode implements Disposable, ResourceManagerListener, GameEventListener{

    private Map<String, GameNode> gameNodes;
    
    private GameNode currentGameNode;
    
    private LoadingGameNode loadingGameNode;
    
    private boolean waiting4Resources;
    
    public GameNodeManager(Batch batch){
        super("gameNodeManager");
        
        this.gameNodes = new HashMap<String, GameNode>();
        
        this.currentGameNode = null;
        
        this.waiting4Resources = false;
        
        TextureManager.getInstance().addGameEventListener(this);
        
        // loading game node part.
        
        this.loadingGameNode = new LoadingGameNode(batch);
        
    }
    
    
    public void addGameNode(GameNode gameNode){
        this.gameNodes.put(gameNode.getId(), gameNode);
    }
    
    public boolean changeCurrentGameNode(HelpGame game, GameNode gameNode){
        if(gameNode != null && this.gameNodes.containsKey(gameNode.getId())){
            if(this.currentGameNode != null){
                this.currentGameNode.onEndingNode(game);
            }
            this.loadingGameNode.setDisplayLoadingGraphical(gameNode.hasLoadingScreen());
            
            this.waiting4Resources = gameNode.onStartingNode(game);

            this.currentGameNode = gameNode;
            
            return true;
        }
        return false;
    }
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime){
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
    public void touchDown(int screenX, int screenY, int pointer, int button){
        this.currentGameNode.touchDown(screenX, screenY, pointer, button);
    }
    
    @Override
    public void touchUp(int screenX, int screenY, int pointer, int button){
        this.currentGameNode.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer){
        this.currentGameNode.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public void mouseMoved(int screenX, int screenY){
        this.currentGameNode.mouseMoved(screenX, screenY);
    }
    
    @Override
    public void scrolled(int amount) {
        this.currentGameNode.scrolled(amount);
    }
    
    @Override
    public void dispose() {
        
    }

    @Override
    public void onResourcesLoaded() {
        this.waiting4Resources = false;
    }

    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location){
        
        if(this.currentGameNode != null){
            switch(type){
                case GAMENODECHANGE:
                    GameNode nextNode = this.currentGameNode.getGameNodeByKey(details);
                    if(nextNode != null){
                        this.changeCurrentGameNode(helpGame, nextNode);
                    }
                    break;
            }
        }   
    }
    
    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        // Nothing to do.
    }
    
    public String getCurrentGameNodeId(){
        return this.currentGameNode.getId();
    }
    
    public boolean isCurrentGameNodeLvl(){
        return this.currentGameNode instanceof LvlGameNode;
    }
}
