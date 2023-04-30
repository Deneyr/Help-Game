/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import guicomponents.GuiText;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class GUIEditorScreen implements Screen{

    private final float P2M = 1.5f/64; 
    
    private final GameWorld gameWorld;
    
    private final Batch batch;
    
    private OrthographicCamera camera;
    
    private GuiText priorityText;
    private int currentPriority;
    private Object2D currentSelectedObject2D;
    
    public GUIEditorScreen(Batch batch, GameWorld gameWorld){
        this.gameWorld = gameWorld;
        
        this.batch = batch;
        
        this.camera = new OrthographicCamera(800, 480);
        
        this.priorityText = new GuiText("Priority : -", 30, GuiText.ReferenceCorner.LEFT, GuiText.ReferenceCorner.LEFT, -0.9f, 1, 2, Color.GRAY, Color.LIGHT_GRAY);
        this.currentPriority = -1;
        this.currentSelectedObject2D = null;
    }
   
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        this.camera.position.set(this.gameWorld.getCameraPosition().x / P2M, this.gameWorld.getCameraPosition().y / P2M, 0);
        this.camera.update();

        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        
        Object2D selectedObject2D = this.gameWorld.getEditorFirstSelectedObject();
        if(this.currentSelectedObject2D != selectedObject2D
                || (selectedObject2D != null && selectedObject2D.getPriority() != this.currentPriority))
        {
            this.currentSelectedObject2D = selectedObject2D;
            if(this.currentSelectedObject2D != null){
                this.currentPriority = selectedObject2D.getPriority();

                this.priorityText.setText("Priority : " + String.valueOf(this.currentPriority)); 
            }else{
                this.priorityText.setText("Priority : -"); 
            }
        }
        
        this.priorityText.drawBatch(this.camera, this.batch);
        
        this.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.camera.setToOrtho(false, width, height);
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
    
}
