/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import gamenode.EditorGameNode;
import java.util.TreeMap;
import gamenode.GameNode;
import gamenode.GameNodeManager;
import gamenode.Lvl1GameNode;
import gamenode.Lvl1UpperCity;
import gamenode.MainMenuGameNode;
import java.util.ArrayList;
import java.util.List;
import menu.EditorMenuManager;
import menu.GameMenuManager;
import menu.MenuManager;



/**
 *
 * @author françois
 */
public class HelpGame extends Game implements GameEventListener{

    public static float P2M = 1.5f/64; 
    
    /// Game logic Worlds.
    private final GameWorld gameWorld = new GameWorld();
    
    private final TreeMap<Float, WorldPlane> mapBackgroundPlanes = new TreeMap<Float, WorldPlane>();
    
    private final TreeMap<Float, WorldPlane> mapForegroundPlanes = new TreeMap<Float, WorldPlane>();
    
    private final MenuManager menuManager = new MenuManager();
    
    private final GameMenuManager gameMenuManager = new GameMenuManager();
    
    private final EditorMenuManager editorMenuManager = new EditorMenuManager();
    
    private final SoundMusicManager soundMusicManager = new SoundMusicManager();
   
    // Batch
    public SpriteBatch batch;
    
    // Game nodes.
    GameNodeManager gameNodeManager;
    
    // Game event
    private final List<GameEventContainer> listGameEvents;
    
    // PlayerData
    private PlayerData playerData;
    
    // Editor
    private String editorLevelPath;
    
    public HelpGame(){
        super();
        
        this.listGameEvents = new ArrayList<GameEventContainer>();
        
        this.playerData = new PlayerData();
        
        this.editorLevelPath = null;
    }
    
    public HelpGame(String editorLevelPath){
        this();
        
        this.editorLevelPath = editorLevelPath;
    }
    
    public boolean LoadSaveFile(String filePath){
        PlayerData dataLoaded = PlayerDataManager.getInstance().deserializePlayerData(filePath);
        
        if(dataLoaded != null){
            
            dataLoaded.setCurrentCheckpointIndex(-1);
            
            this.playerData = dataLoaded;
            
            if(this.playerData.getCurrentLevel() != null){
                GameNode menuNode = this.gameNodeManager.getGameNodeByKey("Menu");
                GameNode startNode = this.gameNodeManager.getGameNodeByKey(this.playerData.getCurrentLevel());
                if(startNode != null){
                    menuNode.addNextNode("Start", startNode);
                }
            }
            
            return true;
        }
        return false;
    }
    
    public boolean SerializeSaveFile(String filePath){
        boolean isSuccess = PlayerDataManager.getInstance().serializePlayerData(filePath, this.playerData);
        
        if(isSuccess && this.playerData.getCurrentLevel() != null){
            GameNode menuNode = this.gameNodeManager.getGameNodeByKey("Menu");
            GameNode startNode = this.gameNodeManager.getGameNodeByKey(this.playerData.getCurrentLevel());
            if(menuNode != null){
                menuNode.addNextNode("Start", startNode);
            }
        }
        
        return isSuccess;
    }
    
    @Override
    public void create() {  
        // GameWorld
        this.gameWorld.addGameEventListener(this);
        
        // Menu manager
        this.menuManager.addGameEventListener(this);
        
        // Game Menu Manager
        this.gameMenuManager.addGameEventListener(this);
        
        // Create the sprite batch
        this.batch = new SpriteBatch();
        
        // Init GameNodeManager
        this.gameNodeManager = new GameNodeManager(this.batch);
        
        // Add Node Part
        GameNode menuNode;
        if(this.editorLevelPath == null){
            GameNode gameNode = new Lvl1GameNode(this, this.batch);    
            this.gameNodeManager.addGameNode(gameNode);

            gameNode = new Lvl1UpperCity(this, this.batch);    
            this.gameNodeManager.addGameNode(gameNode);

            menuNode = new MainMenuGameNode(this, this.batch);    
            this.gameNodeManager.addGameNode(menuNode);

            // Link Nodes Part
            gameNode.addNextNode("Menu", menuNode);
            menuNode.addNextNode("Start", gameNode);

            // Load save file
            this.LoadSaveFile("profil.save");
        }else{
            // Game Menu Manager
            this.editorMenuManager.addGameEventListener(this);
            
            menuNode = new EditorGameNode(this, this.batch);
            this.gameNodeManager.addGameNode(menuNode);
            
            Gdx.input.setInputProcessor(new InputAdapter(){

                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    HelpGame.this.gameNodeManager.touchDown(screenX, screenY, pointer, button);                  
                    return true;
                }
                
                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    HelpGame.this.gameNodeManager.touchUp(screenX, screenY, pointer, button);
                    return true;   
                }

                @Override
                public boolean touchDragged(int screenX, int screenY, int pointer) {
                    HelpGame.this.gameNodeManager.touchDragged(screenX, screenY, pointer);
                    return true;
                }

                @Override
                public boolean mouseMoved(int screenX, int screenY) {
                    HelpGame.this.gameNodeManager.mouseMoved(screenX, screenY);
                    return true;
                }
            });
                       
        }
        
        // Initialize first node
        this.gameNodeManager.changeCurrentGameNode(this, menuNode);  
    }
    
    @Override
    public void render() {
        
        // Update logic
        this.gameNodeManager.updateLogic(this, Gdx.graphics.getDeltaTime());
        
        // Render screens
        this.gameNodeManager.renderScreens(this, Gdx.graphics.getDeltaTime());
        
        // Manage sounds & Musics
        this.soundMusicManager.step(Gdx.graphics.getDeltaTime());
        
        // Forward game events
        synchronized(this.listGameEvents){
            this.computeGameEvents();
        }
    }
    
    public void renderScreen(){
        super.render();
    }

    @Override
    public void dispose() {
        this.clearAllWorldPlanes();
        
        this.batch.dispose();
    }

    public void clearAllWorldPlanes(){
        for(WorldPlane plane : this.mapBackgroundPlanes.values()){
            plane.dispose();
        }
        this.mapBackgroundPlanes.clear();
        
        for(WorldPlane plane : this.mapForegroundPlanes.values()){
            plane.dispose();
        }
        this.mapForegroundPlanes.clear();
        
        this.gameWorld.flushWorld();
        
        this.menuManager.dispose();
        
        this.gameMenuManager.dispose();
        
        this.editorMenuManager.dispose();
        
        this.soundMusicManager.dispose();

        this.batch.flush();
    }
    
    /**
     * @return the gameWorld
     */
    public GameWorld getGameWorld() {
        return this.gameWorld;
    }
    
    /**
     * @return the menuManager
     */
    public MenuManager getMenuManager() {
        return this.menuManager;
    }
    
    /**
     * @return the gameMenuManager
     */
    public GameMenuManager getGameMenuManager() {
        return gameMenuManager;
    }
    
     /**
     * @return the editorMenuManager
     */
    public EditorMenuManager getEditorMenuManager() {
        return editorMenuManager;
    }
    
    /**
     * @return the mapBackgroundPlanes
     */
    public TreeMap<Float, WorldPlane> getMapBackgroundPlanes() {
        return this.mapBackgroundPlanes;
    }
    
    /**
     * @return the mapBackgroundPlanes
     */
    public String getEditorLevelPath() {
        return this.editorLevelPath;
    }
    
    /**
     * @return the mapForegroundPlanes
     */
    public TreeMap<Float, WorldPlane> getMapForegroundPlanes() {
        return mapForegroundPlanes;
    }

    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        synchronized(this.listGameEvents){
            this.listGameEvents.add(new GameEventContainer(type, details, location));
        }
    }

    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
        // Nothing to do.
    }
    
    public void computeGameEvents(){
        
        List<GameEventContainer> endGameEventContainerList = new ArrayList<GameEventContainer>();
                
        for(GameEventContainer gameEvent : this.listGameEvents){

            this.updatePlayerData(gameEvent);
            
            switch(gameEvent.eventType){
                case CHECKPOINT:                   
                    this.soundMusicManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
                    break;
                // End level event : must be forward after the others events.
                case GAMEOVER:
                case GAMENODECHANGE:
                    endGameEventContainerList.add(gameEvent);
                    break;
                case ENTERSTRUCT:
                case QUITSTRUCT:
                    for(WorldPlane worldPlane : this.mapForegroundPlanes.values()){
                        worldPlane.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
                    }
                    break;
                default:
                    this.soundMusicManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);

                    this.gameNodeManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
                    
                    this.gameMenuManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
                    
                    if(this.editorLevelPath != null){
                        this.editorMenuManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
                    }
            }
        }
        
        // Play the end level events.
        for(GameEventContainer gameEvent : endGameEventContainerList){
            this.soundMusicManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);

            this.gameNodeManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
            
            this.gameMenuManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
            
            if(this.editorLevelPath != null){
                this.editorMenuManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
            }
        }
        this.listGameEvents.clear();
    }
    
    private void updatePlayerData(GameEventContainer gameEventContainer){
        
        if(this.gameNodeManager.isCurrentGameNodeLvl()){
            switch(gameEventContainer.eventType){
                case CHECKPOINT:
                    this.getPlayerData().setCurrentMoney(this.gameWorld.getCurrentMoney());
                    this.getPlayerData().setCurrentCheckpointIndex(Integer.parseInt(gameEventContainer.details));
                    
                    this.SerializeSaveFile("profil.save");
                break;
                case GAMEOVER:
                    if(gameEventContainer.details.equals("success")){
                        this.getPlayerData().resetCheckpoint();
                        this.getPlayerData().setCurrentMoney(this.gameWorld.getCurrentMoney());
                    }
                break;
                case LVLSTART:
                    this.getPlayerData().setCurrentLevel(this.gameNodeManager.getCurrentGameNodeId());
                    this.gameWorld.setCurrentMoney(this.getPlayerData().getCurrentMoney());
                    
                    this.SerializeSaveFile("profil.save");
                break;
            }
        }
                    
    }
    
    /**
     * @return the playerData
     */
    public PlayerData getPlayerData() {
        return playerData;
    }
    
    public class GameEventContainer{
        public GameEventContainer(EventType eventType, String details, Vector2 location){
            this.eventType = eventType;
            this.details = details;
            this.location = location;
        }
        
        public EventType eventType;
        public String details;
        public Vector2 location;
    }
   
}
