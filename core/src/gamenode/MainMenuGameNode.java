/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import menucomp.Tree1MenuComponent;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import guicomponents.GuiMenuText;
import menu.Animation;
import menu.Interpolation;
import menucomp.GroundMenuComponent;
import menucomp.OrphenageMenuComponent;
import menucomp.Tree2MenuComponent;

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
        
        // Add Options.
        GuiMenuText guiMenuText = new GuiMenuText("Start", 0f, 0f);
        guiMenuText.setEventDetails(GameEventListener.EventType.GAMENODECHANGE, "Start");
        game.getMenuManager().addGuiMenuText(guiMenuText);
        
        guiMenuText = new GuiMenuText("Credits", 0f, -0.3f);
        guiMenuText.setEventDetails(GameEventListener.EventType.GAMENODECHANGE, "Credits");
        game.getMenuManager().addGuiMenuText(guiMenuText);
        
        // Add scenery.
        
        OrphenageMenuComponent orph = new OrphenageMenuComponent();
        orph.setLocation(-0.9f, -0.8f);
        game.getMenuManager().addGuiComponent(orph, 0f);
        
        Tree1MenuComponent tree = new Tree1MenuComponent();
        tree.setLocation(-2f, -2f);
        game.getMenuManager().addGuiComponent(tree, 1f);
        
        Tree2MenuComponent tree2 = new Tree2MenuComponent();
        tree2.setLocation(-2f, -2f);
        game.getMenuManager().addGuiComponent(tree2, 1.1f);
        
        GroundMenuComponent ground = new GroundMenuComponent();
        ground.setLocation(0, -1.6f);
        game.getMenuManager().addGuiComponent(ground, 3);
        
        // Add animations.
        Animation animation = new Animation(tree, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 0f, 0f, 30f);
        animation.setPositionAnimation(new Vector2(-2f, -0.35f), new Vector2(2f, -0.35f));      
        game.getMenuManager().addAnimation(animation);
        
        animation = new Animation(tree2, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 1f, 0f, 30f);
        animation.setPositionAnimation(new Vector2(-2f, -0.35f), new Vector2(2f, -0.35f));      
        game.getMenuManager().addAnimation(animation);
    }
    
}
