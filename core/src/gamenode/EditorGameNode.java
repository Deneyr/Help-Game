/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import characters.Grandma;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.mygdx.game.ScreenTouchListener;
import com.mygdx.game.WorldPlane;
import com.mygdx.game.scenery.Car;
import com.mygdx.game.scenery.GroundLowerCity;
import com.mygdx.game.scenery.GroundUpperCity;
import cosmetics.HitCosmeticObject2D;
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
import triggered.BulletTriggeredObject2D;
import triggered.CannonBallTriggeredObject2D;
import triggered.ChimneySmokeTriggeredObject2D;
import triggered.TeethTriggeredObject2D;
import triggered.UpTriggeredObject2D;

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
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){       
            game.getGameWorld().getGameEditorManager().setGameRunning(!game.getGameWorld().getGameEditorManager().isGameRunning());
        }
            
        if(game.getGameWorld().getGameEditorManager().isGameRunning()){
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
        // import dynamic resources (created at runtime).
        TextureManager.getInstance().getTexture(BulletTriggeredObject2D.BULLETTEXTURE, null);
        
        TextureManager.getInstance().getTexture(CannonBallTriggeredObject2D.CANNONBALLTEXTURE, null);
        
        TextureManager.getInstance().getTexture(ChimneySmokeTriggeredObject2D.CHIMNEYSMOKETEXTURE, null);
        
        TextureManager.getInstance().getTexture(TeethTriggeredObject2D.TEETHTEXTURE, null);
        
        TextureManager.getInstance().getTexture(UpTriggeredObject2D.UPTEXTURE, null);
        
        TextureManager.getInstance().getTexture(HitCosmeticObject2D.HIT_TEXTURE, null);
        
        // Init Editor Menu Manager.
        GuiEditorBlock editorBlock = new GuiEditorBlock();
        game.getEditorMenuManager().setCanevas(editorBlock);
        
        // Init Editor Level
        GroundUpperCity ground = new GroundUpperCity(game.getGameWorld().getWorld(), 10000, -150f, 150);
        game.getGameWorld().addObject2DToWorld(ground);
        
        System.out.println(game.getEditorLevelPath());
        
        List<Object2DEditorFactory> factories = this.loadObject2DEditorFactories(game.getEditorLevelPath());
        
        for(Object2DEditorFactory factory : factories){
            factory.createTemplate(game.getGameWorld().getWorld());

            game.getEditorMenuManager().AddObject2DAsComponent(factory);
        }      
        
        // Music & Sounds.          
        this.initSoundsLvl();
        
        // Load level
        this.loadObject2Ds(game, SAVEFILENAME);
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
        SoundManager.getInstance().getSound("sounds/action/checkPointTaken.ogg");
        SoundManager.getInstance().getSound("sounds/action/StrongChestOpen.ogg");
        SoundManager.getInstance().getSound("sounds/action/PhoneBoxOpen.ogg");
                
        // Damages taken.
        SoundManager.getInstance().getSound("sounds/damagesTaken/crash_box.ogg");
        SoundManager.getInstance().getSound("sounds/damagesTaken/metalHitDamage.ogg");
        
        // Environment sounds.
        SoundManager.getInstance().getSound("sounds/environment/Ventilo_Wind_Loop.ogg");
    }
    
    private void loadObject2Ds(HelpGame game, String path){
        File file = new File(path);
        if(file.exists()){
            Scanner sc;
            try {
                sc = new Scanner(file);
                
                while (sc.hasNextLine()){
                    String line = sc.nextLine();
                    if(line.contains("//")){
                        String[] tokens = line.split("//");
                        
                        String classID = tokens[tokens.length - 1];
                        
                        Object2DEditorFactory factory = game.getEditorMenuManager().getFactoryFromID(classID);
                        
                        if(factory != null){
                            
                            int startIndex = line.indexOf("(");
                            String subString = line.substring(startIndex + 1);

                            int endIndex = subString.lastIndexOf(")");
                            subString = subString.substring(0, endIndex);
                            
                            tokens = subString.split(",");
                            
                            Vector2 position = new Vector2(Float.parseFloat(tokens[factory.getIndexPosX()])
                                                        , Float.parseFloat(tokens[factory.getIndexPosY()]));
                            
                            float angle = 0;
                            if(factory.getIndexPosA() > 0){
                                angle = Float.parseFloat(tokens[factory.getIndexPosA()]);
                            }
                            
                            game.getGameWorld().createObject(factory, position, angle);
                        }                        
                    }                
                }
            }catch (FileNotFoundException ex) {
                Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
            }
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
