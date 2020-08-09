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
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author françois
 */
public class BackgroundScreen implements Screen{

    private final float P2M = 1.5f/64; 
    
    private final Batch batch;
    
    private final TreeMap<Float, WorldPlane> mapBackgroundPlanes;
    
    private final GameWorld gameWorld;
    
    private final OrthographicCamera camera;
    
    
    public BackgroundScreen(Batch batch, GameWorld gameWorld, TreeMap<Float, WorldPlane> mapBackgroundPlanes){
        this.batch = batch;
        
        this.gameWorld = gameWorld;
        
        this.mapBackgroundPlanes = mapBackgroundPlanes;
        
        this.camera = new OrthographicCamera(800, 480);
    }
    
    @Override
    public void show() {
        
    }

    
    
    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(Entry<Float, WorldPlane> plane : this.mapBackgroundPlanes.entrySet()){

            // Update camera (center on hero)
            this.getCamera().position.set(this.gameWorld.getCameraPosition().x / P2M * plane.getKey(), this.gameWorld.getCameraPosition().y / P2M /** plane.getKey() WIP */, 0);
            this.getCamera().update();
            this.batch.setProjectionMatrix(this.getCamera().combined);
            
            this.batch.begin();    

            // Start render
            List<Sprite> listSprites = plane.getValue().getSpritesInRegion((this.getCamera().position.x - this.getCamera().viewportWidth / 2.f * 1.1f) * P2M,
                                                                               (this.getCamera().position.y - this.getCamera().viewportHeight / 2.f * 1.1f) * P2M,
                                                                               (this.getCamera().position.x + this.getCamera().viewportWidth / 2.f * 1.1f) * P2M,
                                                                               (this.getCamera().position.y + this.getCamera().viewportHeight / 2.f * 1.1f) * P2M);

            Iterator<Sprite> it = listSprites.iterator();
            while(it.hasNext()){
                Sprite sprite = it.next();
                if(sprite != null){
                    this.batch.setColor(sprite.getColor());
                    this.batch.draw(sprite, 
                                            sprite.getX(), sprite.getY(),
                                            sprite.getOriginX(), sprite.getOriginY(),
                                            sprite.getWidth(),sprite.getHeight(),
                                            sprite.getScaleX(),sprite.getScaleY(),
                                            sprite.getRotation());
                }
            }
            
            this.batch.setColor(1f, 1f, 1f, 1f);
            this.batch.end();
        }
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
