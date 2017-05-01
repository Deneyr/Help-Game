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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author françois
 */
public class GameScreen implements Screen{

    private final float P2M = 1.5f/64; 
    
    private final HelpGame game;
    
    private OrthographicCamera camera;
    
    private ShapeRenderer shapeRenderer;
    
    
    public GameScreen(HelpGame game){
        this.game = game;
        
        this.camera = new OrthographicCamera(800, 480);
        
        this.shapeRenderer = new ShapeRenderer();
    }
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float f) {
        /*Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
        
        
        // Update camera (center on hero)
        this.getCamera().position.set(this.game.getGameWorld().getHeroPosition().x / P2M, this.game.getGameWorld().getHeroPosition().y / P2M, 0);
        this.getCamera().update();
        this.game.batch.setProjectionMatrix(this.getCamera().combined);
       
        //this.game.batch.enableBlending();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, 0.5f);
        shapeRenderer.rect(this.getCamera().position.x - this.getCamera().viewportWidth * 1.1f / 2, this.getCamera().position.y - this.getCamera().viewportHeight * 1.1f / 2, this.getCamera().viewportWidth * 1.1f, this.getCamera().viewportHeight * 1.1f);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        this.game.batch.begin();

        // Start render
        List<Sprite> listSprites = this.game.getGameWorld().getSpritesInRegion((this.getCamera().position.x - this.getCamera().viewportWidth / 2.f * 1.1f) * P2M,
                                                                           (this.getCamera().position.y - this.getCamera().viewportHeight / 2.f * 1.1f) * P2M,
                                                                           (this.getCamera().position.x + this.getCamera().viewportWidth / 2.f * 1.1f) * P2M,
                                                                           (this.getCamera().position.y + this.getCamera().viewportHeight / 2.f * 1.1f) * P2M);
        
        Iterator<Sprite> it = listSprites.iterator();
        while(it.hasNext()){
            Sprite sprite = it.next();
            if(sprite != null){
                this.game.batch.setColor(sprite.getColor());
                this.game.batch.draw(sprite, 
                                        sprite.getX(), sprite.getY(),
                                        sprite.getOriginX(), sprite.getOriginY(),
                                        sprite.getWidth(),sprite.getHeight(),
                                        sprite.getScaleX(),sprite.getScaleY(),
                                        sprite.getRotation());
            }
            //System.out.println("position : " + sprite.getX() + "-" + sprite.getY());
        }
        this.game.batch.end();
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
    
}
