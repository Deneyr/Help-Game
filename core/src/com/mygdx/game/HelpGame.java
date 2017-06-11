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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author françois
 */
public class HelpGame extends Game implements GameEventListener{

    public static float P2M = 1.5f/64; 
    
    /// Game logic Worlds.
    private final GameWorld gameWorld = new GameWorld();
    
    private final TreeMap<Float, WorldPlane> mapBackgroundPlanes = new TreeMap<Float, WorldPlane>();
   
    // Batch
    public SpriteBatch batch;
    
    // Game Nodes.
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
        
        // Create the sprite batch
        this.batch = new SpriteBatch();
        
        // Init GameNodeManager
        this.gameNodeManager = new GameNodeManager(this.batch);
        
        GameNode gameNode = new Lvl1GameNode(this, this.batch);
        
        this.gameNodeManager.addGameNode(gameNode);
        
        this.gameNodeManager.changeCurrentGameNode(this, gameNode);
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
        this.batch.dispose();
    }

    public void clearAllWorldPlanes(){
        for(WorldPlane plane : this.mapBackgroundPlanes.values()){
            plane.dispose();
        }
        
        this.gameWorld.flushWorld();
    }
    
    /**
     * @return the gameWorld
     */
    public GameWorld getGameWorld() {
        return gameWorld;
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
        for(GameEventContainer gameEvent : this.listGameEvents){
            switch(gameEvent.eventType){
                case CHECKPOINT:
                    this.getPlayerData().setCurrentMoney(this.gameWorld.getCurrentMoney());
                    break;
            }
            
            this.gameNodeManager.onHelpGameEvent(this, gameEvent.eventType, gameEvent.details, gameEvent.location);
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
