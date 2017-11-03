/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.HelpGame;
import com.mygdx.game.MenuScreen;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.SoundManager;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public abstract class MenuGameNode extends GameNode{
    
    public MenuGameNode(String id, HelpGame game, Batch batch) {
        super(id);
        
        // --- init screen ---
        this.screensDisplayed.clear();
        this.screensDisplayed.add(new MenuScreen(batch, game.getMenuManager()));
    }
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime) { 
        super.updateLogic(game, deltaTime);
        // Compute the next step of the environment game logic.
        game.getMenuManager().step(deltaTime);
    }
    
    protected abstract void initializeMenu(HelpGame game);
    
    protected void flushMenu(HelpGame game){
        game.getMenuManager().dispose();
    }
    
    @Override
    public boolean onStartingNode(HelpGame game){
        super.onStartingNode(game);
        
        game.clearAllWorldPlanes();
        
        TextureManager.getInstance().resetLoadedResources();
        SoundManager.getInstance().resetLoadedResources();
        MusicManager.getInstance().resetLoadedResources();
        
        this.initializeMenu(game);
        
        TextureManager.getInstance().UpdateResources();
        SoundManager.getInstance().UpdateResources();
        MusicManager.getInstance().UpdateResources();
        
        return true;
    }
    
    @Override
    public void onEndingNode(HelpGame game){
        super.onEndingNode(game);
        
        this.flushMenu(game);
    }
    
    @Override
    public boolean hasLoadingScreen(){
        return true;
    }  
    
}
