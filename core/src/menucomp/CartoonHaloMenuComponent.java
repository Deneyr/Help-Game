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
public class CartoonHaloMenuComponent extends GuiComponent{
    private static final String TEXT = "CartoonHalo.png";
    
    public CartoonHaloMenuComponent(){
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
                sprite.getScaleX(),sprite.getScaleY(),
                sprite.getRotation());
        }
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        
    }
}