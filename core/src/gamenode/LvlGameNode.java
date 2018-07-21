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
import com.mygdx.game.MenuScreen;
import com.mygdx.game.WorldPlane;
import guicomponents.GuiComponent;
import java.util.Map;
import menucomp.CartoonHaloMenuComponent;
import menucomp.GameOverMenuComponent;
import menucomp.GameStartMenuComponent;
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
        
        this.screensDisplayed.add(new MenuScreen(batch, game.getGameMenuManager()));
        
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
        
        // Compute the next step of the Menu manager Logic.
        game.getGameMenuManager().step(deltaTime);
    }
    
    protected void initializeLevel(HelpGame game){
        // Init Game Menu Manager.
        GuiComponent guiComponent = new CartoonHaloMenuComponent();
        game.getGameMenuManager().addModelGuiComponent("halo", guiComponent);
        
        guiComponent = new GameOverMenuComponent(); 
        game.getGameMenuManager().addModelGuiComponent("gameOver", guiComponent);
        
        guiComponent = new GameStartMenuComponent(); 
        game.getGameMenuManager().addModelGuiComponent("gameStart", guiComponent);
    }
    
    protected void initializeLevelState(HelpGame game){
        game.initWorldState();
    }
    
    protected void flushLevel(HelpGame game){
        game.getGameWorld().flushWorld();
        
        game.getGameMenuManager().dispose();
    }
    
    @Override
    public boolean onStartingNode(HelpGame game){
        super.onStartingNode(game);
        
        game.clearAllWorldPlanes();
        
        TextureManager.getInstance().resetLoadedResources();
        SoundManager.getInstance().resetLoadedResources();
        MusicManager.getInstance().resetLoadedResources();
        
        this.initializeLevel(game);
        
        this.initializeLevelState(game);
        
        TextureManager.getInstance().UpdateResources();
        SoundManager.getInstance().UpdateResources();
        MusicManager.getInstance().UpdateResources();
        
        return true;
    }
    
    @Override
    public void onEndingNode(HelpGame game){
        super.onEndingNode(game);
        
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
        SoundManager.getInstance().getSound("sounds/attacks/Metal_Wire.ogg");
        
        //Action.
        SoundManager.getInstance().getSound("sounds/action/umbrellaOpen.ogg");
        SoundManager.getInstance().getSound("sounds/action/umbrellaClose.ogg");
        SoundManager.getInstance().getSound("sounds/action/music_note.ogg");
        SoundManager.getInstance().getSound("sounds/action/Button_Click.ogg");
        SoundManager.getInstance().getSound("sounds/action/metalHit1.ogg");
        SoundManager.getInstance().getSound("sounds/action/metalHit2.ogg");
        
        // Damages taken.
        SoundManager.getInstance().getSound("sounds/damagesTaken/crash_box.ogg");
        SoundManager.getInstance().getSound("sounds/damagesTaken/metalHitDamage.ogg");
        
        // Environment sounds.
        SoundManager.getInstance().getSound("sounds/environment/Ventilo_Wind_Loop.ogg");
    }
}
