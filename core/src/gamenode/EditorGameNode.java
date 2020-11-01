/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import characters.Grandma;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BackgroundScreen;
import com.mygdx.game.EditorScreen;
import com.mygdx.game.ForegroundScreen;
import com.mygdx.game.GUIScreen;
import com.mygdx.game.GameScreen;
import com.mygdx.game.HelpGame;
import com.mygdx.game.MenuScreen;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DEditorFactory;
import com.mygdx.game.WorldPlane;
import com.mygdx.game.scenery.Car;
import com.mygdx.game.scenery.GroundCity;
import guicomponents.GuiComponent;
import guicomponents.GuiEditorBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.SoundManager;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class EditorGameNode extends GameNode{
    
    private final String SAVEFILENAME = "savedLevel.txt";
    
    public EditorGameNode(HelpGame game, Batch batch){
        super("Editor");
        
        // --- init screen ---
        this.screensDisplayed.clear();
        this.screensDisplayed.add(new BackgroundScreen(batch, game.getGameWorld(), game.getMapBackgroundPlanes()));
        this.screensDisplayed.add(new EditorScreen(batch, game.getGameWorld()));
        this.screensDisplayed.add(new ForegroundScreen(batch, game.getGameWorld(), game.getMapForegroundPlanes()));
        //this.screensDisplayed.add(new GUIScreen(batch, game.getGameWorld()));
        
        this.screensDisplayed.add(new MenuScreen(batch, game.getGameMenuManager()));  
        
        this.screensDisplayed.add(new MenuScreen(batch, game.getEditorMenuManager()));  
    }
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime) {
        super.updateLogic(game, deltaTime);
        
        // Compute the next step of the background logic.
        for(Map.Entry<Float, WorldPlane> plane : game.getMapBackgroundPlanes().entrySet()){
            plane.getValue().step(deltaTime);
        }
        
        // Compute the next step of the foreground logic.
        for(Map.Entry<Float, WorldPlane> plane : game.getMapForegroundPlanes().entrySet()){
            plane.getValue().step(deltaTime);
        }
        
        // Compute the next step of the environment game logic.
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            game.getGameWorld().step(deltaTime);
        }
        
        // Compute the next step of the Menu manager Logic.
        game.getGameMenuManager().step(deltaTime);
        
        game.getEditorMenuManager().step(deltaTime);
        
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
                game.getGameWorld().saveObject2Ds(SAVEFILENAME);
                System.out.println("----- Level Saved -----");
            }
        }
    }

    
    protected void flushLevel(HelpGame game){
        game.getGameWorld().flushWorld();
        
        game.getGameMenuManager().dispose();
        
        game.getEditorMenuManager().dispose();
    }
    
    @Override
    public boolean onStartingNode(HelpGame game){
        super.onStartingNode(game);
        
        game.clearAllWorldPlanes();
        
        TextureManager.getInstance().resetLoadedResources();
        SoundManager.getInstance().resetLoadedResources();
        MusicManager.getInstance().resetLoadedResources();
        
        this.initializeLevel(game);
        
        TextureManager.getInstance().UpdateResources();
        SoundManager.getInstance().UpdateResources();
        MusicManager.getInstance().UpdateResources();
        
        return true;
    }
    
    protected void initializeLevel(HelpGame game){
        // Init Editor Menu Manager.
        GuiEditorBlock editorBlock = new GuiEditorBlock();
        game.getEditorMenuManager().setCanevas(editorBlock);
        
        // Init Editor Level
        GroundCity ground = new GroundCity(game.getGameWorld().getWorld(), 10000, -150f, 150);
        game.getGameWorld().addObject2DToWorld(ground);
        
        System.out.println(game.getEditorLevelPath());
        
        List<Object2DEditorFactory> factories = this.loadObject2DEditorFactories(game.getEditorLevelPath());
        
        for(Object2DEditorFactory factory : factories){
            factory.createTemplate(game.getGameWorld().getWorld());

            game.getEditorMenuManager().AddObject2DAsComponent(factory);
        }      
    }
    
    private List<Object2DEditorFactory> loadObject2DEditorFactories(String path){
        List<Object2DEditorFactory> listObject2DEditorFactories = new ArrayList<Object2DEditorFactory>();
        
        File file = new File(path);
        
        if(file.exists()){
            Scanner sc; 
            try {
                sc = new Scanner(file);
                
                while (sc.hasNextLine()){
                    String line = sc.nextLine(); 
                    
                    String[] tokens = line.split("\\(");
                    
                    Object2DEditorFactory factory = new Object2DEditorFactory(tokens[0]);
                    
                    tokens = tokens[1].split("\\)");
                    tokens = tokens[0].split(",");
                    
                    for(String token : tokens){
                        factory.addArgument(token);
                    }

                    listObject2DEditorFactories.add(factory);
                }               
            } catch (FileNotFoundException ex) {
                Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
            }
 
        }
        
        return listObject2DEditorFactories;
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
}
