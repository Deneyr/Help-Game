/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.Color;
import menucomp.Tree1MenuComponent;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import guicomponents.GuiMenuText;
import menu.Animation;
import menu.Interpolation;
import menucomp.BackgroundMenuComponent;
import menucomp.CarMenuComponent;
import menucomp.GroundMenuComponent;
import menucomp.HaloMenuComponent;
import menucomp.MistMenuComponent;
import menucomp.OrphenageMenuComponent;
import menucomp.RainMenuComponent;
import menucomp.StreetLampMenuComponent;
import menucomp.Tree2MenuComponent;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.SoundManager;

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
        // The first layer.
        
        BackgroundMenuComponent background = new BackgroundMenuComponent();
        background.setLocation(0f, 0f);
        game.getMenuManager().addGuiComponent(background, 0f);
        
        OrphenageMenuComponent orph = new OrphenageMenuComponent();
        orph.setLocation(-0.9f, -0.8f);
        game.getMenuManager().addGuiComponent(orph, 0.05f);
        
        // The second layer.
        StreetLampMenuComponent lamp = new StreetLampMenuComponent();
        lamp.setLocation(-0.9f, -0.65f);
        game.getMenuManager().addGuiComponent(lamp, 0.1f);
        
        lamp = new StreetLampMenuComponent();
        lamp.setLocation(0.7f, -0.65f);
        game.getMenuManager().addGuiComponent(lamp, 0.1001f);
        
        HaloMenuComponent halo = new HaloMenuComponent();
        halo.setLocation(-0.91f, -0.2f);
        game.getMenuManager().addGuiComponent(halo, 0.1002f);
        
        HaloMenuComponent halo2 = new HaloMenuComponent();
        halo2.setLocation(0.7f, -0.2f);
        game.getMenuManager().addGuiComponent(halo2, 0.1003f);
        
        
        CarMenuComponent car = new CarMenuComponent();
        car.setLocation(-2f, -0.41f);
        game.getMenuManager().addGuiComponent(car, 0.1005f);
        
        // The third layer.
        MistMenuComponent mist = new MistMenuComponent();
        mist.setLocation(0f, 0f);
        game.getMenuManager().addGuiComponent(mist, 0.001f);
        
        MistMenuComponent mist2 = new MistMenuComponent();
        mist2.setLocation(0.1f, 0.2f);
        game.getMenuManager().addGuiComponent(mist2, 0.101f);
        
        
        // the fourth layer.
        Tree1MenuComponent tree = new Tree1MenuComponent();
        tree.setLocation(-0.8f, -0.35f);
        game.getMenuManager().addGuiComponent(tree, 1f);
        
        Tree2MenuComponent tree2 = new Tree2MenuComponent();
        tree2.setLocation(0.5f, -0.35f);
        game.getMenuManager().addGuiComponent(tree2, 1.1f);
        
        // The fifth layer.
        GroundMenuComponent ground = new GroundMenuComponent();
        ground.setLocation(0, -1.6f);
        game.getMenuManager().addGuiComponent(ground, 3);
        
        // rain 
        
        int nbHeight = 6;
        int nbWidth = 6;
        RainMenuComponent[][] arrayRain = new RainMenuComponent[nbHeight][nbWidth];
        for(int i = 0; i < nbHeight; i++){
            for(int i2 = 0; i2 < nbWidth; i2++){
                arrayRain[i][i2] = new RainMenuComponent();
                arrayRain[i][i2].setLocation(3f, 3f);
                game.getMenuManager().addGuiComponent(arrayRain[i][i2], 0.55f + 0.001f*i + 0.0001f*i2);
            }
        }
        
        // Add animations.
        Animation animation;
        // start rain animations.
        
        for(int i = 0; i < nbHeight; i++){
            for(int i2 = 0; i2 < nbWidth; i2++){
                animation = new Animation(arrayRain[i][i2], Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 1f / nbWidth * i2, 0f, 1f);
                animation.setPositionAnimation(new Vector2(-1 + 3f / nbHeight * i, 0.8f), new Vector2(-1.5f + 3f / nbHeight * i, -1f));      
                game.getMenuManager().addAnimation(animation);
            }
        }
        
        // end rain animations.
        
        animation = new Animation(mist, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 0f, 0f, 1500f);
        animation.setPositionAnimation(new Vector2(0f, 0f), new Vector2(200f, 0f));      
        game.getMenuManager().addAnimation(animation);
        
        animation = new Animation(mist2, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 0f, 0f, 1500f);
        animation.setPositionAnimation(new Vector2(0f, 0.2f), new Vector2(100f, 0.2f));      
        game.getMenuManager().addAnimation(animation);
        
        animation = new Animation(halo, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, -2f, 2f, 2f);
        animation.setColorAnimation(new Color(1, 1, 1, 0.8f), new Color(1, 1, 1, 0.2f));
        animation.setScaleAnimation(new Vector2(1, 1), new Vector2(0.5f, 0.5f));
        game.getMenuManager().addAnimation(animation);
        animation = new Animation(halo, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 0f, 2f, 2f);
        animation.setColorAnimation(new Color(1, 1, 1, 0.2f), new Color(1, 1, 1, 0.8f));
        animation.setScaleAnimation(new Vector2(0.5f, 0.5f), new Vector2(1f, 1f));
        game.getMenuManager().addAnimation(animation);
        
        animation = new Animation(halo2, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, -3.5f, 2f, 2f);
        animation.setColorAnimation(new Color(1, 1, 1, 0.8f), new Color(1, 1, 1, 0.2f));
        animation.setScaleAnimation(new Vector2(1, 1), new Vector2(0.5f, 0.5f));
        game.getMenuManager().addAnimation(animation);
        animation = new Animation(halo2, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, -1.5f, 2f, 2f);
        animation.setColorAnimation(new Color(1, 1, 1, 0.2f), new Color(1, 1, 1, 0.8f));
        animation.setScaleAnimation(new Vector2(0.5f, 0.5f), new Vector2(1f, 1f));
        game.getMenuManager().addAnimation(animation);
        
        
        animation = new Animation(car, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, -5f, 10f, 5f);
        animation.setPositionAnimation(new Vector2(-1.5f, -0.41f), new Vector2(1.5f, -0.41f));
        game.getMenuManager().addAnimation(animation);
        
        /*animation = new Animation(tree, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 0f, 0f, 60f);
        animation.setPositionAnimation(new Vector2(-2f, -0.35f), new Vector2(2f, -0.35f));      
        game.getMenuManager().addAnimation(animation);
        
        Animation animation = new Animation(tree2, Animation.RunType.RESTART, Interpolation.InterpolationType.LINEAR, 1f, 0f, 60f);
        animation.setPositionAnimation(new Vector2(-2f, -0.35f), new Vector2(2f, -0.35f));      
        game.getMenuManager().addAnimation(animation);*/
        
        // Music & Sounds.
        MusicManager.getInstance().registerResource("sounds/Help_MainTitle.ogg");
        
    }
    
}
