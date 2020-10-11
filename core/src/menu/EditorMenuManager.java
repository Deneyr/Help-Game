/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.HelpGame;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DEditorFactory;
import guicomponents.GuiComponent;
import guicomponents.GuiEditorBlock;
import guicomponents.GuiEditorItem;
import guicomponents.GuiText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deneyr
 */
public class EditorMenuManager extends MenuManager{
    
    private GuiEditorBlock canevas;
    
    public EditorMenuManager(){
        super();
    }
    
    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location){      
        switch(type){
            case LVLSTART :
                this.onStart();
                break;
        }
        
    }
    
    private void onStart(){
        super.dispose();
        
        this.canevas.setLocation(-1f, -2f);
        this.addGuiComponent(this.canevas, 0);
        
        Animation animation = new Animation(this.canevas, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0f, 0f, 1f);
        animation.setColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1));      
        this.addAnimation(animation);
        
        animation = new Animation(this.canevas, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0.5f, 0f, 0.5f);
        animation.setPositionAnimation(new Vector2(-1f, -2f), new Vector2(-1f, -1f));      
        this.addAnimation(animation);
    }
    
    @Override
    public void dispose(){
        super.dispose();
    }

    public void AddObject2DAsComponent(Object2DEditorFactory factory){
        this.canevas.AddObject2DAsComponent(factory);
    }
    
    /**
     * @param canevas the canevas to set
     */
    public void setCanevas(GuiEditorBlock canevas) {
        this.canevas = canevas;
    }
}
