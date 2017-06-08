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
    
    public HelpGame(){
        super();
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
        this.gameNodeManager.onHelpGameEvent(this, type, details, location);
    }

    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
        // Nothing to do.
    }
}
