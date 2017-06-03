/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressourcesmanagers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.HelpGame;
import guicomponents.GuiText;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class LoadingScreen implements Screen{

    private final float P2M = 1.5f/64; 
    
    private Batch batch;
    
    private OrthographicCamera camera;
    
    private ShapeRenderer shapeRenderer;
    
    private GuiText loadingText;
    
    public LoadingScreen(Batch batch){    
        this.camera = new OrthographicCamera(800, 480);
        
        this.batch = batch;
        
        this.shapeRenderer = new ShapeRenderer();
        
        this.loadingText = new GuiText("Chargement ...", 40, GuiText.ReferenceCorner.RIGHT, GuiText.ReferenceCorner.LEFT, 0.95f, 1);
    }
   
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {

        this.camera.update();
        this.shapeRenderer.setProjectionMatrix(camera.combined);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
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
