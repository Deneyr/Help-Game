/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.TreeMap;
import gamenode.GameNode;
import gamenode.GameNodeManager;
import gamenode.Lvl1GameNode;
import gamenode.MainMenuGameNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    
    private final MenuManager menuManager = new MenuManager();
    
    private final GameMenuManager gameMenuManager = new GameMenuManager();
    
    private final SoundMusicManager soundMusicManager = new SoundMusicManager();
   
    // Batch
    public SpriteBatch batch;
    
    // Game nodes.
    GameNodeManager gameNodeManager;
    
    // Game event
    private final List<GameEventContainer> listGameEvents;
    
    // PlayerData
    private PlayerData playerData;
    
    public HelpGame(){
        super();
        
        this.listGameEvents = new ArrayList<GameEventContainer>();
        
        this.playerData = new PlayerData();
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
        GameNode gameNode = new Lvl1GameNode(this, this.batch);    
        this.gameNodeManager.addGameNode(gameNode);
        
        GameNode menuNode = new MainMenuGameNode(this, this.batch);    
        this.gameNodeManager.addGameNode(menuNode);
        
        // Link Nodes Part
        gameNode.addNextNode("Menu", menuNode);
        menuNode.addNextNode("Start", gameNode);
        
        // Initialize first node
        this.gameNodeManager.changeCurrentGameNode(this, menuNode);
        
    }
    
    @Override
    public void render() {
        
        // Update logic
        this.gameNodeManager.updateLogic(this, Gdx.graphics.getDeltaTime());
        
        // Render screens
        this.gameNodeManager.renderScreens(this, Gdx.graphics.getDeltaTime());
        
        // Forward game events.
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
        
        this.gameWorld.flushWorld();
        
        this.menuManager.dispose();
        
        this.gameMenuManager.dispose();
        
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
     * @return the mapBackgroundPlanes
     */
    public TreeMap<Float, WorldPlane> getMapBackgroundPlanes() {
        return this.mapBackgroundPlanes;
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

            switch(gameEvent.eventType){
                case CHECKPOINT:
                    this.getPlayerData().setCurrentMoney(this.gameWorld.getCurrentMoney());
                    
                    this.soundMusicManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
                    break;
                // End level event : must be forward after the others events.
                case GAMEOVER:
                case GAMENODECHANGE:
                    endGameEventContainerList.add(gameEvent);
                    break;
                default:
                    this.soundMusicManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);

                    this.gameNodeManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
                    
                    this.gameMenuManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
            }
        }
        
        // Play the end level events.
        for(GameEventContainer gameEvent : endGameEventContainerList){
            this.soundMusicManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);

            this.gameNodeManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
            
            this.gameMenuManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
        }
        this.listGameEvents.clear();
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
    
    public class PlayerData implements Serializable{
        private  static  final  long serialVersionUID =  1350092881346723535L;
        
        private int currentMoney;
        
        private String idCurrentLevel;
        
        PlayerData(){
            this.currentMoney = 0;
            
            this.idCurrentLevel = "";
        }

        /**
         * @return the currentMoney
         */
        public int getCurrentMoney() {
            return currentMoney;
        }

        /**
         * @param currentMoney the currentMoney to set
         */
        public void setCurrentMoney(int currentMoney) {
            this.currentMoney = currentMoney;
        }

        /**
         * @return the currentLevel
         */
        public String getCurrentLevel() {
            return idCurrentLevel;
        }

        /**
         * @param currentLevel the currentLevel to set
         */
        public void setCurrentLevel(String currentLevel) {
            this.idCurrentLevel = currentLevel;
        }
    }
}
