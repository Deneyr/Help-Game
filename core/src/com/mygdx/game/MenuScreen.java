/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import menu.MenuManager;


/**
 *
 * @author françois
 */
public class MenuScreen implements Screen{

    /*public static final String TITLE = "HELP";
    public static final String SUBTITLE = "PRESS        ENTER";*/
    
    private final MenuManager menuManager;
    
    private final Batch batch;
    
    private OrthographicCamera camera;
    
    private Vector2 cameraSize;
    
    // Part ressources
    
    BitmapFont titleFont;
    BitmapFont subTitleFont;
    
    Sprite umbrella;
    
    public MenuScreen(Batch batch, MenuManager menuManager){
        this.menuManager = menuManager;
       
        this.cameraSize = new Vector2(800, 400);
        this.camera = new OrthographicCamera(this.cameraSize.x, this.cameraSize.y);
        
        this.batch = batch;
        
        /*// Part ressources initialization
        
        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();

        fontParameters.size = 100;
        fontParameters.shadowColor = Color.CYAN;
        fontParameters.color = Color.BLACK;
    
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts" + File.separator + "helpFont.ttf"));
        this.titleFont = generator.generateFont(fontParameters);
        
        fontParameters = new FreeTypeFontParameter();

        fontParameters.size = 40;
        fontParameters.shadowColor = Color.CYAN;
        fontParameters.color = Color.BLACK;
    
        this.subTitleFont = generator.generateFont(fontParameters);
        
        this.umbrella = new Sprite(new Texture("parapluie.png"));
        this.umbrella.setSize(75, 75);
        this.umbrella.setPosition(-25, -100);*/
    }
   
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float f) {
        /*Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
        
        this.menuManager.updateCamera(this.camera, this.cameraSize.x, this.cameraSize.y);
        
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        
        this.menuManager.drawBatch(this.camera, this.batch);
        
        this.batch.end();
    }

    @Override
    public void resize(int i, int i1) {
        this.cameraSize.x = i;
        this.cameraSize.y = i1;
        
        this.camera.setToOrtho(false, this.cameraSize.x, this.cameraSize.y);
        
        this.camera.position.set(new Vector2(0, 0),0);
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
