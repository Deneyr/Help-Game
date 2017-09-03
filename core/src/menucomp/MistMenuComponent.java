/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menucomp;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import guicomponents.GuiComponent;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class MistMenuComponent extends GuiComponent{
    private static final String TEXT = "menu/mist.png";
    
    private static final float SCALE_X = 3f;
    private static final float SCALE_Y = 3f;
    
    public MistMenuComponent(){
        super();
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(TEXT, this);    
    }
    
    @Override
    public void drawBatch(Camera camera, Batch batch) {
        Sprite sprite = this.createCurrentSprite(camera);
        
        if(sprite != null){
            batch.setColor(sprite.getColor());
            
            batch.draw(sprite, 
                            sprite.getX(), sprite.getY(),
                            sprite.getOriginX(), sprite.getOriginY(),
                            sprite.getWidth(),sprite.getHeight(),
                            sprite.getScaleX() * SCALE_X,sprite.getScaleY() * SCALE_Y,
                            sprite.getRotation());
            
            // tild map.
            int nbRepX = (int) Math.ceil(((camera.viewportWidth / 2 + camera.position.x) - (sprite.getX() + sprite.getWidth())) / sprite.getWidth() * SCALE_X);
            
            for(int i = 0; i < nbRepX; i++){
                batch.draw(sprite, 
                            sprite.getX() + (i + 1) * sprite.getWidth() * SCALE_X, sprite.getY(),
                            sprite.getOriginX(), sprite.getOriginY(),
                            sprite.getWidth(),sprite.getHeight(),
                            sprite.getScaleX() * SCALE_X,sprite.getScaleY() * SCALE_Y,
                            sprite.getRotation());
            }
            
            nbRepX = (int) Math.ceil((sprite.getX() - (camera.position.x - camera.viewportWidth / 2)) / sprite.getWidth() * SCALE_X);
            
            for(int i = 0; i < nbRepX; i++){
                batch.draw(sprite, 
                            sprite.getX() - (i + 1) * sprite.getWidth() * SCALE_X, sprite.getY(),
                            sprite.getOriginX(), sprite.getOriginY(),
                            sprite.getWidth(),sprite.getHeight(),
                            sprite.getScaleX() * SCALE_X,sprite.getScaleY() * SCALE_Y,
                            sprite.getRotation());
            }
        }
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        
    }
}
