/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import java.io.File;


/**
 *
 * @author françois
 */
public class MenuScreen implements Screen{

    public static final String TITLE = "HELP";
    public static final String SUBTITLE = "PRESS        ENTER";
    
    private final HelpGame game;
    
    OrthographicCamera camera;
    
    // Part ressources
    
    BitmapFont titleFont;
    BitmapFont subTitleFont;
    
    Sprite umbrella;
    
    public MenuScreen(HelpGame game){
        this.game = game;
       
        this.camera = new OrthographicCamera(800, 480);
        
        // Part ressources initialization
        
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
        this.umbrella.setPosition(-25, -100);
    }
   
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.camera.update();
        this.game.batch.setProjectionMatrix(this.camera.combined);
        
        this.game.batch.begin();
        
        this.titleFont.draw(this.game.batch, TITLE, -100, 100);
        this.subTitleFont.draw(this.game.batch, SUBTITLE, -160, -50);
        this.game.batch.draw(this.umbrella, 
                                    this.umbrella.getX(), this.umbrella.getY(),
                                    this.umbrella.getOriginX(), this.umbrella.getOriginY(),
                                    this.umbrella.getWidth(),this.umbrella.getHeight(),
                                    this.umbrella.getScaleX(),this.umbrella.getScaleY(),
                                    this.umbrella.getRotation());
        
        this.game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {
        this.camera.setToOrtho(false, i, i1);
        
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
