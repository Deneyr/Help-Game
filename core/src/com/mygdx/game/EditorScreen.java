/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Deneyr
 */
public abstract class EditorScreen implements Screen{

    protected final float P2M = 1.5f/64; 
    
    protected OrthographicCamera camera;
      
    protected final GameWorld gameWorld;
    
    protected final Batch batch;
    
    protected Vector3 cameraPosition;   
    
    private boolean previousIsGameRunning;
    
    public EditorScreen(Batch batch, GameWorld gameWorld){
        this.gameWorld = gameWorld;
        
        this.batch = batch;
        
        this.camera = new OrthographicCamera(800, 480);
             
        this.previousIsGameRunning = false;
        
        this.cameraPosition = new Vector3(0, 0, 0);
    }
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float f) {

        boolean isGameRunning = this.gameWorld.getGameEditorManager().isGameRunning();
        if(this.previousIsGameRunning != isGameRunning){
            
            if(isGameRunning){
                this.gameWorld.setCameraPosition(new Vector2(this.cameraPosition.x * P2M, this.cameraPosition.y * P2M));
            }
            
            this.previousIsGameRunning = isGameRunning;
        }
        
        if(isGameRunning == false || this.gameWorld.getHero() == null){
            if(Gdx.input.isKeyPressed(Input.Keys.Q)){
                this.cameraPosition.x -= f * 1000; 
            }else if(Gdx.input.isKeyPressed(Input.Keys.D)){
                this.cameraPosition.x += f * 1000;  
            }

            if(Gdx.input.isKeyPressed(Input.Keys.Z)){
                this.cameraPosition.y += f * 1000; 
            }else if(Gdx.input.isKeyPressed(Input.Keys.S)){
                this.cameraPosition.y -= f * 1000; 
            }
            //this.getCamera().position.set(this.cameraPosition);
            
            //this.gameWorld.getGameEditorManager().setCameraPosition(new Vector2(this.cameraPosition.x * P2M, this.cameraPosition.y * P2M));
        }else{
            Vector2 worldCameraPosition = this.gameWorld.getCameraPosition();
            this.cameraPosition.set(worldCameraPosition.x / P2M, worldCameraPosition.y / P2M, 0);
        }
        
        this.camera.position.set(this.cameraPosition);        
    }

    @Override
    public void resize(int i, int i1) {
        this.getCamera().setToOrtho(false, i, i1);
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        
    }

    /**
     * @return the camera
     */
    public OrthographicCamera getCamera() {
        return camera;
    }
    
    public void InitCameraPosition(Vector2 cameraPosition){
        this.cameraPosition = new Vector3(cameraPosition.x / P2M, cameraPosition.y / P2M, 0);
    }
}
