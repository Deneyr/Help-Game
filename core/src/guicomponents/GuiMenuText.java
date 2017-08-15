/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.GameEventListener;

/**
 *
 * @author Deneyr
 */
public class GuiMenuText extends GuiText{
    
    private GameEventListener.EventType eventType;
    
    private String details;
    
    public GuiMenuText(String str, float locX, float locY) {
        super(str, 40, ReferenceCorner.MIDDLE, ReferenceCorner.MIDDLE, locX, locY, 2, Color.LIGHT_GRAY, Color.GRAY);
    }
    
    public void setEventDetails(GameEventListener.EventType eventType, String details){
        this.eventType = eventType;
        this.details = details;
    }
    
    public void onSelected(){
        this.notifyGameEventListener(this.eventType, this.details, this.location);
    }
    
}
