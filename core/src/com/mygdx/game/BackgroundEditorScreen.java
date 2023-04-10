/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Deneyr
 */
public class BackgroundEditorScreen extends EditorScreen implements Screen{

    private final TreeMap<Float, WorldPlane> mapBackgroundPlanes;
       
    public BackgroundEditorScreen(Batch batch, GameWorld gameWorld, TreeMap<Float, WorldPlane> mapBackgroundPlanes){
        super(batch, gameWorld);
        
        this.mapBackgroundPlanes = mapBackgroundPlanes;
    }
    
    @Override
    public void render(float f) {
        
        super.render(f);
        
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(Map.Entry<Float, WorldPlane> plane : this.mapBackgroundPlanes.entrySet()){

            // Update camera (center on hero)
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
}