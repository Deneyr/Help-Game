/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import backgrounds.Lvl1_1_Residence;
import backgrounds.Lvl1_2_Residence;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BackgroundEditorScreen;
import com.mygdx.game.EditorScreen;
import com.mygdx.game.ForegroundScreen;
import com.mygdx.game.GUIEditorScreen;
import com.mygdx.game.GameEditorScreen;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.GameWorld;
import com.mygdx.game.HelpGame;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.MenuScreen;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DEditorFactory;
import com.mygdx.game.WorldPlane;
import com.mygdx.game.scenery.GroundUpperCity;
import cosmetics.HitCosmeticObject2D;
import guicomponents.GuiEditorBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
    
    private Vector2 initCameraPosition;
    
    public EditorGameNode(HelpGame game, Batch batch){
        super("Editor");
        
        this.initCameraPosition = Vector2.Zero;
        
        // --- init screen ---
        this.screensDisplayed.clear();
        this.screensDisplayed.add(new BackgroundEditorScreen(batch, game.getGameWorld(), game.getMapBackgroundPlanes()));
        this.screensDisplayed.add(new GameEditorScreen(batch, game.getGameWorld()));
        this.screensDisplayed.add(new ForegroundScreen(batch, game.getGameWorld(), game.getMapForegroundPlanes()));
        this.screensDisplayed.add(new GUIEditorScreen(batch, game.getGameWorld()));
        
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
            GameWorld gameWorld = game.getGameWorld();
            game.getGameWorld().getGameEditorManager().setGameRunning(gameWorld, !gameWorld.getGameEditorManager().isGameRunning());
            
            if(game.getGameWorld().getGameEditorManager().isGameRunning()){
                game.onGameEvent(GameEventListener.EventType.EDITORRUNNING, this.getId(), null);
            }else{
                game.onGameEvent(GameEventListener.EventType.EDITORSTOPPED, this.getId(), null);
            }
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
        
        // Background houses
        int seed = 80;
        Lvl1_1_Residence lvl1_1_Residence = new Lvl1_1_Residence(seed, 5500f, -25, 200, 200);
        game.getMapBackgroundPlanes().put(lvl1_1_Residence.getRatioDist(), lvl1_1_Residence);
        
        Lvl1_2_Residence lvl1_2_Residence = new Lvl1_2_Residence(seed, 7300f, -25, 200, 200);
        game.getMapBackgroundPlanes().put(lvl1_2_Residence.getRatioDist(), lvl1_2_Residence);
        
        /*Lvl1_3_Residence lvl1_3_Residence = new Lvl1_3_Residence(seed, 10200f, -25, 200, 200);
        game.getMapBackgroundPlanes().put(lvl1_3_Residence.getRatioDist(), lvl1_3_Residence);*/
        
        // init background solid objects
        
        lvl1_1_Residence.createSolidObj(game.getGameWorld());
        
        lvl1_2_Residence.createSolidObj(game.getGameWorld());
        
        // lvl1_3_Residence.createSolidObj(game.getGameWorld());
        
        // Init Editor Menu Manager.
        GuiEditorBlock editorBlock = new GuiEditorBlock();
        game.getEditorMenuManager().setCanevas(editorBlock);
        
        // Init Editor Level
        GroundUpperCity ground = new GroundUpperCity(game.getGameWorld().getWorld(), 10000, -150f, 1000);
        game.getGameWorld().addObject2DToWorld(ground);
        
        /*Sign sign = new Sign(game.getGameWorld().getWorld(), 100f, 0, 0.02f, 2);
        game.getGameWorld().addObject2DToWorld(sign);
        
        sign = new Sign(game.getGameWorld().getWorld(), 200f, 0, -0.05f, 1);
        game.getGameWorld().addObject2DToWorld(sign);
        
        sign = new Sign(game.getGameWorld().getWorld(), 700f, 0, 0, 6);
        game.getGameWorld().addObject2DToWorld(sign);
        
        Pipe pipe = new Pipe(game.getGameWorld().getWorld(), 900f, 20f, 1, false);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        Abribus abribus = new Abribus(game.getGameWorld().getWorld(), 1400f, 15f);
        game.getGameWorld().addObject2DToWorld(abribus);
        
        Bench banc = new Bench(game.getGameWorld().getWorld(), 1400f, -15f);
        game.getGameWorld().addObject2DToWorld(banc);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 1700f, 150f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        Car car = new Car(game.getGameWorld().getWorld(), 2000f, 10f, 0, 1);
        game.getGameWorld().addObject2DToWorld(car);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 2200f, 150f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 2500f, 220f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        TreeWithoutLeaf tree = new TreeWithoutLeaf(game.getGameWorld().getWorld(), 2800f, 70f);
        game.getGameWorld().addObject2DToWorld(tree);
        
        Trashcan trashcan = new Trashcan(game.getGameWorld().getWorld(), 3000f, 0, 1, 1);
        game.getGameWorld().addObject2DToWorld(trashcan);
        
        trashcan = new Trashcan(game.getGameWorld().getWorld(), 3150f, 0, 0, 1);
        game.getGameWorld().addObject2DToWorld(trashcan);
        
        Bus bus = new Bus(game.getGameWorld().getWorld(), 3500f, 60f, 0, -1);
        game.getGameWorld().addObject2DToWorld(bus);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 3000f, 420f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 3400f, 320f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 3800f, 220f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        sign = new Sign(game.getGameWorld().getWorld(), 4200f, 0, -0.05f, 4);
        game.getGameWorld().addObject2DToWorld(sign);
        
        PoutrelleObstacle poutrelleObst = new PoutrelleObstacle(game.getGameWorld().getWorld(), 4360f, 210f, (float)Math.PI/2, 0, 1);
        game.getGameWorld().addObject2DToWorld(poutrelleObst);
        
        Crane crane = new Crane(game.getGameWorld().getWorld(), 5000f, 270, 0.8f, 0.1f, 0.9f, 0.2f, true);
        game.getGameWorld().addObject2DToWorld(crane);
        
        CityString cityString = new CityString(game.getGameWorld().getWorld(), 7000f, 600f);
        game.getGameWorld().addObject2DToWorld(cityString);
            
        Speaker speaker = new Speaker(game.getGameWorld().getWorld(), 9000f, 15f);
        game.getGameWorld().addObject2DToWorld(speaker);
        
        Scaffolding scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 9300f, 70f, 2, false); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 9600f, 110f, 1, false); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 11450f, 70f, 3, false); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 12000f, 110f, 1, false); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        Poutrelle poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 12300f, 100f, (float) (Math.PI / 2), new Vector2(1, 0), 5f, 5, 1);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        BarbedTriggeredObject2D barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 12300f, 0, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        crane = new Crane(game.getGameWorld().getWorld(), 13000f, 270f, 0.8f, 0.1f, 0.55f, 0.8f, false);
        game.getGameWorld().addObject2DToWorld(crane);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 13100, 40f, 2, false); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 13550f, 130f, 0f, null, 0f, 0f, 0.5f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        ObstacleHouse obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 14100f, 70f, 2); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 15000, 10f, 2, false); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 15300, 70f, 3, false); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 15500f, 300f, 4); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 14550f, 450f, (float)-Math.PI/4, null, 0f, 0f, 0.5f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 14800, 300f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 15100, 500f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        crane = new Crane(game.getGameWorld().getWorld(), 15550f, 885f, 0.8f, 0.1f, 0.55f, 0.9f, false);
        game.getGameWorld().addObject2DToWorld(crane);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 16200f, 700, 0f, null, 0f, 0f, 1f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 17000, 250f, 3); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 15800f, 250, 0f, new Vector2(0, 1), 3f, 5, 0.25f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 17500, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 18000, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 18400, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 19000, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        
        trashcan = new Trashcan(game.getGameWorld().getWorld(), 18000, 0, 1, 1);
        game.getGameWorld().addObject2DToWorld(trashcan);
        
        trashcan = new Trashcan(game.getGameWorld().getWorld(), 18100, 0, 0, 1);
        game.getGameWorld().addObject2DToWorld(trashcan);
        
        bus = new Bus(game.getGameWorld().getWorld(), 18400, 60f, 0, 1);
        game.getGameWorld().addObject2DToWorld(bus);
        
        // Balloons
        Balloon balloon = new Balloon(game.getGameWorld().getWorld(), 16000f, 150);
        game.getGameWorld().addObject2DToWorld(balloon);
        
        balloon = new Balloon(game.getGameWorld().getWorld(), 16200f, 200);
        game.getGameWorld().addObject2DToWorld(balloon);
        
        balloon = new Balloon(game.getGameWorld().getWorld(), 16400f, 200);
        game.getGameWorld().addObject2DToWorld(balloon);*/
        
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
        this.InitScreensCamera();
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
        SoundManager.getInstance().getSound("sounds/environment/conveyorBelt_Loop.ogg");
    }
    
    private void loadObject2Ds(HelpGame game, String path){
        File file = new File(path);
        if(file.exists()){
            Scanner sc;
            try {
                sc = new Scanner(file);
                
                if(sc.hasNextLine()){
                    String line = sc.nextLine();
                    if(line.contains("//") && line.contains(";")){
                        line = line.replaceAll("//", "");
                        
                        String[] tokens = line.split(";");
                        
                        this.initCameraPosition = new Vector2(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                    }
                }
                
                Object2D createdObj = null;
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
                            
                            createdObj = game.getGameWorld().createObject(factory, position, angle);
                        }                        
                    }
                    else if(createdObj != null){
                        if(line.contains("setTransform")){
                            int startIndex = line.indexOf("(");
                            String subString = line.substring(startIndex + 1);

                            int endIndex = subString.lastIndexOf(")");
                            subString = subString.substring(0, endIndex);
                            
                            String[] tokens = subString.split(",");
                            
                            createdObj.setTransform(Float.parseFloat(tokens[0]) * P2M, Float.parseFloat(tokens[1]) * P2M, Float.parseFloat(tokens[2]));
                        }else if(line.contains("setPriority")){
                            int startIndex = line.indexOf("(");
                            String subString = line.substring(startIndex + 1);

                            int endIndex = subString.lastIndexOf(")");
                            subString = subString.substring(0, endIndex);
                            
                            createdObj.setPriority(Integer.parseInt(subString));
                        }else if(line.contains("addObject2DToWorld")){
                                               
                        }else{
                            createdObj = null;
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
    
    private void InitScreensCamera(){
        for(Screen screen : this.screensDisplayed){
            if(screen instanceof EditorScreen){
                ((EditorScreen) screen).InitCameraPosition(this.initCameraPosition);
            }
        }
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
