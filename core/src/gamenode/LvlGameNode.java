/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.BackgroundScreen;
import com.mygdx.game.GUIScreen;
import com.mygdx.game.GameScreen;
import com.mygdx.game.HelpGame;
import com.mygdx.game.WorldPlane;
import java.util.Map;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.SoundManager;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public abstract class LvlGameNode extends GameNode{
    
    public LvlGameNode(String id, HelpGame game, Batch batch){
        super(id);
        
        // --- init screen ---
        this.screensDisplayed.clear();
        this.screensDisplayed.add(new BackgroundScreen(batch, game.getGameWorld(), game.getMapBackgroundPlanes()));
        this.screensDisplayed.add(new GameScreen(batch, game.getGameWorld()));
        this.screensDisplayed.add(new GUIScreen(batch, game.getGameWorld()));
        
        this.outputGameNode.put("restart", this);
    }
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime) {
        super.updateLogic(game, deltaTime);
        
        // Compute the next step of the background logic.
        for(Map.Entry<Float, WorldPlane> plane : game.getMapBackgroundPlanes().entrySet()){
            plane.getValue().step(deltaTime);
        }
        
        // Compute the next step of the environment game logic.
        game.getGameWorld().step(deltaTime);
    }
    
    /*@Override
    public void renderScreens(HelpGame game, float deltaTime){
        
        for(Screen screenDisplayed : this.screensDisplayed){
            if(screenDisplayed instanceof BackgroundScreen){
                BackgroundScreen backgroundScreen = (BackgroundScreen) screenDisplayed;
                
                backgroundScreen.setTargetCameraPosition(game.getGameWorld().getHeroPosition().x / P2M, game.getGameWorld().getHeroPosition().y / P2M);
            }
        }
        
        super.renderScreens(game, deltaTime);
    }*/
    
    protected abstract void initializeLevel(HelpGame game);
    
    protected void flushLevel(HelpGame game){
        game.getGameWorld().flushWorld();
    }
    
    @Override
    public boolean onStartingNode(HelpGame game){
        super.onStartingNode(game);
        
        game.clearAllWorldPlanes();
        
        TextureManager.getInstance().resetLoadedResources();
        SoundManager.getInstance().resetLoadedResources();
        MusicManager.getInstance().resetLoadedResources();
        
        this.initializeLevel(game);
        game.getGameWorld().setCurrentMoney(game.getPlayerData().getCurrentMoney());
        
        TextureManager.getInstance().UpdateResources();
        SoundManager.getInstance().UpdateResources();
        MusicManager.getInstance().UpdateResources();
        
        return true;
    }
    
    @Override
    public void onEndingNode(HelpGame game){
        super.onEndingNode(game);
        
        game.getPlayerData().setCurrentLevel(this.getId());
        game.getPlayerData().setCurrentMoney(game.getGameWorld().getCurrentMoney());
        
        this.flushLevel(game);
    }
    
    @Override
    public boolean hasLoadingScreen(){
        return true;
    }  
    
    protected void initSoundsLvl(){
        
        // Swing.
        SoundManager.getInstance().getSound("sounds/attacks/swingUmbrella.ogg");    
        SoundManager.getInstance().getSound("sounds/attacks/swingBat.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/swingPunch.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/swingBigPunch.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/shot.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/reloadGun.ogg");
        
        // Hit.
        SoundManager.getInstance().getSound("sounds/attacks/hitPunch.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/hitPunch2.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/Cannon_Explosion_1.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/Cannon_Explosion_2.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/bounce_1.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/bounce_2.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/bounce_3.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/bounce_4.ogg");
        SoundManager.getInstance().getSound("sounds/attacks/bounce_5.ogg");
        
        //Action.
        SoundManager.getInstance().getSound("sounds/action/umbrellaOpen.ogg");
        SoundManager.getInstance().getSound("sounds/action/umbrellaClose.ogg");
        SoundManager.getInstance().getSound("sounds/action/music_note.ogg");
        SoundManager.getInstance().getSound("sounds/action/Button_Click.ogg");
        
        // Damages taken.
        SoundManager.getInstance().getSound("sounds/damagesTaken/crash_box.ogg");
        
        // Environment sounbs.
        SoundManager.getInstance().getSound("sounds/environment/Ventilo_Wind_Loop.ogg");
    }
}
