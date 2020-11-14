/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class EditorScreen implements Screen, ScreenTouchListener{

    private final float P2M = 1.5f/64; 
    
    private final GameWorld gameWorld;
    
    private final Batch batch;
    
    private OrthographicCamera camera;
    
    private ShapeRenderer shapeRenderer;
    
    private Vector3 cameraPosition;
    
    private boolean isGameRunning;
    
    
    public EditorScreen(Batch batch, GameWorld gameWorld){
        this.gameWorld = gameWorld;
        
        this.batch = batch;
        
        this.camera = new OrthographicCamera(800, 480);
        
        this.shapeRenderer = new ShapeRenderer();
        
        this.cameraPosition = new Vector3(0, 0, 0);
       
        this.isGameRunning = false;
    }
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float f) {

        if(this.isGameRunning == false || this.gameWorld.getHero() == null){
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
            this.getCamera().position.set(this.cameraPosition);
        }else{
            Vector2 worldCameraPosition = this.gameWorld.getCameraPosition();
            this.getCamera().position.set(worldCameraPosition.x / P2M, worldCameraPosition.y / P2M, 0);
        }
        
        // Update camera (center on hero)
        //this.getCamera().position.set(this.gameWorld.getCameraPosition().x / P2M, this.gameWorld.getCameraPosition().y / P2M, 0);
        this.getCamera().update();
        this.batch.setProjectionMatrix(this.getCamera().combined);
       
        //this.batch.enableBlending();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        this.shapeRenderer.setProjectionMatrix(camera.combined);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(1f, 1f, 1f, 0.5f);
        this.shapeRenderer.rect(this.getCamera().position.x - this.getCamera().viewportWidth * 1.1f / 2, this.getCamera().position.y - this.getCamera().viewportHeight * 1.1f / 2, this.getCamera().viewportWidth * 1.1f, this.getCamera().viewportHeight * 1.1f);
        this.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        this.batch.begin();

        // Start render
        List<Sprite> listSprites = this.gameWorld.getSpritesInRegion((this.getCamera().position.x - this.getCamera().viewportWidth / 2.f * 1.1f) * P2M,
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
        this.batch.end();
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

    /**
     * @param isGameRunning the isGameRunning to set
     */
    public void setIsGameRunning(boolean isGameRunning) {
        this.isGameRunning = isGameRunning;
    }
    
    private Vector2 getWorldCoordinate(float screenX, float screenY){
        float visualX = this.camera.position.x - this.getCamera().viewportWidth / 2 + screenX;
        float visualY = this.camera.position.y + this.getCamera().viewportHeight / 2 - screenY;
        
        return new Vector2(visualX * P2M, visualY * P2M);
    }
    
    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {        
        
        Vector2 position = this.getWorldCoordinate(screenX, screenY);

        System.out.println("x : " + position.x);
        System.out.println("y : " + position.y);
        System.out.println("----------------");
        
        this.gameWorld.onTouchDown(position.x, position.y, pointer, button);
    }
    
    @Override
    public void touchUp(int screenX, int screenY, int pointer, int button){
        Vector2 position = this.getWorldCoordinate(screenX, screenY);
        
        this.gameWorld.onTouchUp(position.x, position.y, pointer, button);
    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer){
        Vector2 position = this.getWorldCoordinate(screenX, screenY);
        
        this.gameWorld.onTouchDragged(position.x, position.y, pointer);
    }

    @Override
    public void mouseMoved(int screenX, int screenY){
        Vector2 position = this.getWorldCoordinate(screenX, screenY);
        
        this.gameWorld.onMouseMoved(position.x, position.y);
    }
    
    @Override
    public void scrolled(int amount) {
        // nothing to do for now.
    }
}
