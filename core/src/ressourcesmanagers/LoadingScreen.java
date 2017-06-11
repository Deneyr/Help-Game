/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressourcesmanagers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import guicomponents.GuiText;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class LoadingScreen implements Screen{

    private final float P2M = 1.5f/64; 
    
    private Batch batch;
    
    private OrthographicCamera camera; 
    
    private GuiText loadingText;
    
    private boolean displayLoadingGraphical;
    
    public LoadingScreen(Batch batch){    
        this.camera = new OrthographicCamera(800, 480);
        
        this.batch = batch;
        
        this.loadingText = new GuiText("Chargement ...", 30, GuiText.ReferenceCorner.LEFT, GuiText.ReferenceCorner.MIDDLE, -0.5f, -0.5f, 2);
        
        this.displayLoadingGraphical = true;
    }
   
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(this.displayLoadingGraphical){
            this.camera.update();

            this.batch.setProjectionMatrix(this.camera.combined);
            this.batch.begin();

            // life bar hero

            List<Sprite> listSprite = TextureManager.getInstance().getSpritesInRegion((this.camera.position.x - this.camera.viewportWidth / 2.f),
                                                                               (this.camera.position.y - this.camera.viewportHeight / 2.f),
                                                                               (this.camera.position.x + this.camera.viewportWidth / 2.f),
                                                                               (this.camera.position.y + this.camera.viewportHeight / 2.f));
            for(Sprite sprite : listSprite){
                this.batch.draw(sprite, 
                            sprite.getX(), sprite.getY(),
                            sprite.getOriginX(), sprite.getOriginY(),
                            sprite.getWidth(),sprite.getHeight(),
                            sprite.getScaleX(),sprite.getScaleY(),
                            sprite.getRotation());
            }


            this.loadingText.drawBatch(this.camera, this.batch);

            this.batch.end();
        }
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

    /**
     * @param displayLoadingGraphical the displayLoadingGraphical to set
     */
    public void setDisplayLoadingGraphical(boolean displayLoadingGraphical) {
        this.displayLoadingGraphical = displayLoadingGraphical;
    }
}
