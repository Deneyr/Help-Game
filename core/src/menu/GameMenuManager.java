/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import guicomponents.GuiComponent;
import guicomponents.GuiMenuText;
import guicomponents.GuiText;
import guicomponents.GuiText.ReferenceCorner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deneyr
 */
public class GameMenuManager extends MenuManager{
    
    private Map<String, GuiComponent> mapModelGuiComponents = new HashMap<String, GuiComponent>();
    
    public GameMenuManager(){
        super();
    }
    
    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location){
        
        switch(type){
            case GAMESTART :
                String[] parts = details.split(":");
                if(parts != null && parts.length > 1){
                    this.onStart(parts[0], parts[1]);
                }
                break;
            case GAMEOVER :
                if(details.equals("defeat")){
                    this.onDefeat();
                }else{
                    this.onSuccess(details);
                }
                break;
        }
        
        //super.notifyGameEventListeners(type, details, location);
    }
    
    private void onStart(String indexLevel, String titleLevel){
        super.dispose();
        
        GuiComponent gameStart = this.mapModelGuiComponents.get("gameStart");
        gameStart.setLocation(-1.6f, -2f);
        this.addGuiComponent(gameStart, 0);
        
        Animation animation = new Animation(gameStart, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0f, 0f, 1f);
        animation.setColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1));      
        this.addAnimation(animation);
        
        animation = new Animation(gameStart, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 3f, 0f, 1f);
        animation.setColorAnimation(new Color(1, 1, 1, 1), new Color(1, 1, 1, 0));      
        this.addAnimation(animation);
        
        // Part GUI
        GuiText guiText = new GuiText("Niveau " + indexLevel, 20, ReferenceCorner.RIGHT, ReferenceCorner.MIDDLE, 0f, 0f, 1.5f, Color.WHITE, Color.DARK_GRAY);
        guiText.setLocation(-2f, 0f);
        this.addGuiComponent(guiText, 1);
        animation = new Animation(guiText, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0.5f, 0f, 0.5f);
        animation.setPositionAnimation(new Vector2(-2f, -0.15f), new Vector2(-0.1f, -0.15f));      
        this.addAnimation(animation);
        
        animation = new Animation(guiText, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 3f, 0f, 0.5f);
        animation.setPositionAnimation(new Vector2(-0.1f, -0.1f), new Vector2(-0.1f, 2f));      
        this.addAnimation(animation);
        
        guiText = new GuiText(titleLevel, 20, ReferenceCorner.LEFT, ReferenceCorner.MIDDLE, 0f, 0f, 1.5f, Color.WHITE, Color.DARK_GRAY);
        guiText.setLocation(2f, 0f);
        this.addGuiComponent(guiText, 2);
        animation = new Animation(guiText, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0.5f, 0f, 0.5f);
        animation.setPositionAnimation(new Vector2(2f, -0.1f), new Vector2(0.1f, -0.1f));      
        this.addAnimation(animation);
        
        animation = new Animation(guiText, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 3f, 0f, 0.5f);
        animation.setPositionAnimation(new Vector2(0.1f, -0.1f), new Vector2(0.1f, -2f));      
        this.addAnimation(animation);
    }
    
    private void onSuccess(String details){
        super.dispose();
        
        // Part animation
        GuiComponent halo = this.mapModelGuiComponents.get("halo");
        
        halo.setLocation(-1.6f, -2.2f);
        this.addGuiComponent(halo, 0);
        
        Animation animation = new Animation(halo, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0f, 0f, 1f);
        animation.setScaleAnimation(new Vector2(20f, 20f), new Vector2(1f, 1f));      
        this.addAnimation(animation);
        
        animation = new Animation(halo, Animation.RunType.RESTART, Interpolation.InterpolationType.QUADRA_INC, 1f, 0f, 1f);
        animation.setScaleAnimation(new Vector2(1f, 1f), new Vector2(1.5f, 1.5f));      
        this.addAnimation(animation);
        
        animation = new Animation(halo, Animation.RunType.RESTART, Interpolation.InterpolationType.QUADRA_INC, 1f, 1f, 1f);
        animation.setScaleAnimation(new Vector2(1.5f, 1.5f), new Vector2(1f, 1f));      
        this.addAnimation(animation);
        
        // Part GUI
        GuiMenuText guiMenuText = new GuiMenuText("Victoire !", 0f, 0.2f);
        guiMenuText.setEventDetails(GameEventListener.EventType.GAMENODECHANGE, "Menu");
        this.addGuiMenuText(guiMenuText);
        
        this.setIndexCurrentText(0);
    }
    
    private void onDefeat(){
        super.dispose();
        
        // Part animation
        GuiComponent halo = this.mapModelGuiComponents.get("halo");
        
        halo.setLocation(-1.6f, -2.2f);
        this.addGuiComponent(halo, 0);
        
        Animation animation = new Animation(halo, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0f, 0f, 1f);
        animation.setScaleAnimation(new Vector2(20f, 20f), new Vector2(1f, 1f));      
        this.addAnimation(animation);
        
        animation = new Animation(halo, Animation.RunType.RESTART, Interpolation.InterpolationType.QUADRA_INC, 1f, 0f, 1f);
        animation.setScaleAnimation(new Vector2(1f, 1f), new Vector2(1.5f, 1.5f));      
        this.addAnimation(animation);
        
        animation = new Animation(halo, Animation.RunType.RESTART, Interpolation.InterpolationType.QUADRA_INC, 1f, 1f, 1f);
        animation.setScaleAnimation(new Vector2(1.5f, 1.5f), new Vector2(1f, 1f));      
        this.addAnimation(animation);
        
        
        GuiComponent gameOverMenuComponent = this.mapModelGuiComponents.get("gameOver");
        
        gameOverMenuComponent.setLocation(-1.6f, -2.1f);
        gameOverMenuComponent.setSpriteColor(new Color(1, 1, 1, 0));
        this.addGuiComponent(gameOverMenuComponent, 1);
        
        animation = new Animation(gameOverMenuComponent, Animation.RunType.NORMAL, Interpolation.InterpolationType.LINEAR, 2f, 0f, 1f);
        animation.setColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1));      
        this.addAnimation(animation);
        
        // Part GUI
        GuiMenuText guiMenuText = new GuiMenuText("Continue", 0f, 0.2f);
        guiMenuText.setEventDetails(GameEventListener.EventType.GAMENODECHANGE, "restart");
        this.addGuiMenuText(guiMenuText);
        
        animation = new Animation(guiMenuText, Animation.RunType.NORMAL, Interpolation.InterpolationType.LINEAR, 2f, 0f, 0f);
        guiMenuText.setSpriteColor(new Color(1, 1, 1, 0));
        animation.setColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1));      
        this.addAnimation(animation);
        
        guiMenuText = new GuiMenuText("Quit", 0f, -0.1f);
        guiMenuText.setEventDetails(GameEventListener.EventType.GAMENODECHANGE, "Menu");
        this.addGuiMenuText(guiMenuText);
        
        animation = new Animation(guiMenuText, Animation.RunType.NORMAL, Interpolation.InterpolationType.LINEAR, 2f, 0f, 0f);
        guiMenuText.setSpriteColor(new Color(1, 1, 1, 0));
        animation.setColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1));      
        this.addAnimation(animation);
        
        this.setIndexCurrentText(0);
    }
    
    @Override
    public void addAnimation(Animation animation){
        GuiComponent guiComponent = animation.getGuiComponent();
        if(!this.mapListAnimations.containsKey(guiComponent)){
            this.mapListAnimations.put(guiComponent, new ArrayList<Animation>());
        }
        
        List<Animation> listAnimations = this.mapListAnimations.get(guiComponent);
        listAnimations.add(animation);
    }
    
    public void addModelGuiComponent(String nameComponent, GuiComponent guiComponent){
        this.mapModelGuiComponents.put(nameComponent, guiComponent);
    }
    
    @Override
    public void dispose(){
        this.mapModelGuiComponents.clear();
        
        super.dispose();
    }
}
