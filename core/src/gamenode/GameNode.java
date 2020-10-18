/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import com.mygdx.game.ScreenTouchListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Deneyr
 */
public abstract class GameNode implements ScreenTouchListener {
   
    private String id;
    
    protected List<Screen> screensDisplayed;
    
    protected Map<String, GameNode> outputGameNode;
    
    private boolean isFirstUpdate;
    
    protected boolean isRestarted;
    
    public GameNode(String id){
        this.outputGameNode = new HashMap<String, GameNode>();
        
        this.screensDisplayed = new ArrayList<Screen>();
        
        this.id = id;
        
        this.isFirstUpdate = true;
        
        this.isRestarted = false;
    }
    
    public void updateLogic(HelpGame game, float deltaTime){
        if(this.isFirstUpdate){
            this.isFirstUpdate = false;
            
            this.onStartingGame(game);
        }
    }
    
    protected void onStartingGame(HelpGame game)
    {
        if(this.isRestarted){
            game.onGameEvent(GameEventListener.EventType.GAMESTART, this.getId(), Vector2.Zero);
        }else{
            game.onGameEvent(GameEventListener.EventType.LVLSTART, this.getId(), Vector2.Zero);
        }
    }
    
    public void renderScreens(HelpGame game, float deltaTime){
        for(Screen screenDisplayed : this.screensDisplayed){
            game.setScreen(screenDisplayed);
            game.renderScreen();
        }
    }
    
    public void addNextNode(String key, GameNode gameNode){
        this.outputGameNode.put(key, gameNode);
    }
    
    public boolean onStartingNode(HelpGame game){
        this.isFirstUpdate = true;
        
        return true;
    }
    
    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button){
        for(Screen screen : this.screensDisplayed){
            if(screen instanceof ScreenTouchListener){
                ((ScreenTouchListener) screen).touchDown(screenX, screenY, pointer, button);
            }
        }
    }
    
    @Override
    public void touchUp(int screenX, int screenY, int pointer, int button){
        for(Screen screen : this.screensDisplayed){
            if(screen instanceof ScreenTouchListener){
                ((ScreenTouchListener) screen).touchUp(screenX, screenY, pointer, button);
            }
        }
    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer){
        for(Screen screen : this.screensDisplayed){
            if(screen instanceof ScreenTouchListener){
                ((ScreenTouchListener) screen).touchDragged(screenX, screenY, pointer);
            }
        }
    }

    @Override
    public void mouseMoved(int screenX, int screenY){
        for(Screen screen : this.screensDisplayed){
            if(screen instanceof ScreenTouchListener){
                ((ScreenTouchListener) screen).mouseMoved(screenX, screenY);
            }
        }
    }
    
    @Override
    public void scrolled(int amount) {
        for(Screen screen : this.screensDisplayed){
            if(screen instanceof ScreenTouchListener){
                ((ScreenTouchListener) screen).scrolled(amount);
            }
        }
    }
    
    public void onEndingNode(HelpGame game){
        // Nothing to do.
    }
    
    
    public boolean hasLoadingScreen(){
        return false;
    }   
    
     /**
     * @return the nextGameNode
     */
    public GameNode getGameNodeByKey(String key) {
        return this.outputGameNode.get(key);
    }
    
     /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }
    
}
