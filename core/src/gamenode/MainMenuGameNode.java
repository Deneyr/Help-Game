/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import guicomponents.GuiMenuText;

/**
 *
 * @author Deneyr
 */
public class MainMenuGameNode extends MenuGameNode{

    public MainMenuGameNode(HelpGame game, Batch batch) {
        super("MainMenuGameNode", game, batch);
    }

    @Override
    protected void initializeMenu(HelpGame game) {
        GuiMenuText guiMenuText = new GuiMenuText("Start", 0f, 0f);
        guiMenuText.setEventDetails(GameEventListener.EventType.GAMENODECHANGE, "Start");
        game.getMenuManager().addGuiMenuText(guiMenuText);
        
        guiMenuText = new GuiMenuText("Credits", 0f, -0.3f);
        guiMenuText.setEventDetails(GameEventListener.EventType.GAMENODECHANGE, "Credits");
        game.getMenuManager().addGuiMenuText(guiMenuText);
        
    }
    
}
