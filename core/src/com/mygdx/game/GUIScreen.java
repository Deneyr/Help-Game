/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import guicomponents.GuiText;
import guicomponents.GuiText.ReferenceCorner;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author françois
 */
public class GUIScreen implements Screen{

    private final float P2M = 1.5f/64; 
    
    private final GameWorld gameWorld;
    
    private final Batch batch;
    
    private OrthographicCamera camera;
    
    private ShapeRenderer shapeRenderer;
    
    private GuiText scoreText;
    private int currentScore;
    
    public GUIScreen(Batch batch, GameWorld gameWorld){
        this.gameWorld = gameWorld;
        
        this.batch = batch;
        
        this.camera = new OrthographicCamera(800, 480);
        
        this.shapeRenderer = new ShapeRenderer();
        
        this.scoreText = new GuiText("0 $", 30, ReferenceCorner.RIGHT, ReferenceCorner.LEFT, 0.95f, 1, 2);
        this.currentScore = -1;
    }
   
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        this.camera.position.set(this.gameWorld.getHeroPosition().x / P2M, this.gameWorld.getHeroPosition().y / P2M, 0);
        this.camera.update();
        this.shapeRenderer.setProjectionMatrix(camera.combined);
        this.shapeRenderer.begin(ShapeType.Filled);
        
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        
        // Start render
        List<Sprite> listSprites = this.gameWorld.getLifeBarInRegion((this.camera.position.x - this.camera.viewportWidth / 2.f * 1.1f) * P2M,
                                                                           (this.camera.position.y - this.camera.viewportHeight / 2.f * 1.1f) * P2M,
                                                                           (this.camera.position.x + this.camera.viewportWidth / 2.f * 1.1f) * P2M,
                                                                           (this.camera.position.y + this.camera.viewportHeight / 2.f * 1.1f) * P2M);
        
        Iterator<Sprite> it = listSprites.iterator();
        while(it.hasNext()){
            Sprite sprite = it.next();
            
            this.shapeRenderer.setColor(0, 0, 0, 1);
            this.shapeRenderer.rect(sprite.getX() + sprite.getWidth() / 2 - 20 - 2, sprite.getY() + sprite.getHeight() - 2, 44, 9);
            this.shapeRenderer.setColor(1, 1, 1, 1);
            this.shapeRenderer.rect(sprite.getX() + sprite.getWidth() / 2 - 20, sprite.getY() + sprite.getHeight(), 40 * sprite.getScaleX(), 5);
        }
        
        // life bar hero
        Sprite lifeBarHero = this.gameWorld.getLifeBarHero();
        this.batch.setColor(lifeBarHero.getColor());
        this.batch.draw(lifeBarHero, 
                                        this.camera.position.x - this.camera.viewportWidth / 2.f, this.camera.position.y + this.camera.viewportHeight / 2.f - lifeBarHero.getHeight(),
                                        lifeBarHero.getOriginX(), lifeBarHero.getOriginY(),
                                        lifeBarHero.getWidth(),lifeBarHero.getHeight(),
                                        lifeBarHero.getScaleX(),lifeBarHero.getScaleY(),
                                        lifeBarHero.getRotation());
        
        int score = this.gameWorld.getCurrentMoney();
        if(this.currentScore < 0 || this.currentScore != score){
            this.scoreText.setText(String.valueOf(score) + " $");
            this.currentScore = score;
        }        
        this.scoreText.drawBatch(this.camera, this.batch);
        
        this.batch.end();
        this.shapeRenderer.end();
        
        // Start dialogue rendering    
        this.shapeRenderer.begin(ShapeType.Filled);        
        this.gameWorld.drawShapeRenderer(this.camera, this.shapeRenderer);
        this.shapeRenderer.end();
        
        this.batch.begin();
        this.gameWorld.drawBatch(this.camera, this.batch);
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
