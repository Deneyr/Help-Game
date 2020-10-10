/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.g2d.Batch;
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
    
    public EditorGameNode(HelpGame game, Batch batch){
        super("Editor");
        
        // --- init screen ---
        this.screensDisplayed.clear();
        this.screensDisplayed.add(new BackgroundScreen(batch, game.getGameWorld(), game.getMapBackgroundPlanes()));
        this.screensDisplayed.add(new EditorScreen(batch, game.getGameWorld()));
        this.screensDisplayed.add(new ForegroundScreen(batch, game.getGameWorld(), game.getMapForegroundPlanes()));
        //this.screensDisplayed.add(new GUIScreen(batch, game.getGameWorld()));
        
        this.screensDisplayed.add(new MenuScreen(batch, game.getGameMenuManager()));       
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
        // game.getGameWorld().step(deltaTime);
        
        // Compute the next step of the Menu manager Logic.
        game.getGameMenuManager().step(deltaTime);
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
        
        TextureManager.getInstance().UpdateResources();
        SoundManager.getInstance().UpdateResources();
        MusicManager.getInstance().UpdateResources();
        
        return true;
    }
    
    protected void initializeLevel(HelpGame game){
        // Init Game Menu Manager.
        
        GroundCity ground = new GroundCity(game.getGameWorld().getWorld(), 10000, -150f, 150);
        game.getGameWorld().addObject2DToWorld(ground);
        
        System.out.println(game.getEditorLevelPath());
        
        List<Object2DEditorFactory> factories = this.loadObject2DEditorFactories(game.getEditorLevelPath());
        
        for(Object2DEditorFactory factory : factories){
            factory.createTemplate(game.getGameWorld().getWorld());
            System.out.println(factory.getTemplate());
            System.out.println(factory);
            System.out.println("-------");
        }

        /*try {
            Class<?> act = Class.forName("com.mygdx.game.scenery.Abribus");
            
            Constructor<?> constructor = act.getConstructor(World.class, float.class, float.class);
            
            Object2D obj = (Object2D)constructor.newInstance(game.getGameWorld().getWorld(), 0f, 50f);
            game.getGameWorld().addObject2DToWorld(obj);
            
            obj = (Object2D)constructor.newInstance(game.getGameWorld().getWorld(), 200f, 70f);
            game.getGameWorld().addObject2DToWorld(obj);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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
