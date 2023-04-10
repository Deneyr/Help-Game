/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class GameEditorScreen extends EditorScreen implements ScreenTouchListener{

    private ShapeRenderer shapeRenderer;
    
    public GameEditorScreen(Batch batch, GameWorld gameWorld){
        super(batch, gameWorld);  
        
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float f) {
        
        super.render(f);
        
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
        this.drawSelectionRectangle();
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
    
    private void drawSelectionRectangle(){
        
        Vector2 firstPositionTouched = this.gameWorld.getGameEditorManager().getFirstPositionTouched();
        Vector2 positionTouched = this.gameWorld.getGameEditorManager().getPositionTouched();
        
        if(firstPositionTouched != null && firstPositionTouched.equals(positionTouched) == false){
            float lowerX = Math.min(firstPositionTouched.x, positionTouched.x) / P2M;
            float upperX = Math.max(firstPositionTouched.x, positionTouched.x) / P2M;

            float lowerY = Math.min(firstPositionTouched.y, positionTouched.y) / P2M;
            float upperY = Math.max(firstPositionTouched.y, positionTouched.y) / P2M;
            
            this.shapeRenderer.setColor(1f, 0f, 0f, 0.5f);
            this.shapeRenderer.rect(lowerX, lowerY, upperX - lowerX, upperY - lowerY);
        }
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