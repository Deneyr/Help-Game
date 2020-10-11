/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Object2D;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class GuiEditorItem extends GuiComponent{

    private Object2D object2D;
    
    GuiComponent parent;
    
    private Sprite sprite;
    
    public GuiEditorItem(Object2D obj){
        super();
        
        this.object2D = obj;
        
        this.sprite = null;
        
        this.parent = parent;
    }
    

    @Override
    public void drawBatch(Camera camera, Batch batch) {

        Sprite sprite = this.createCurrentSprite(camera);
     
        if(sprite != null){
            batch.setColor(sprite.getColor());
            batch.draw(sprite , 
                sprite.getX(), sprite.getY(),
                sprite.getOriginX(), sprite.getOriginY(),
                sprite.getWidth(), sprite.getHeight(),
                sprite.getScaleX(), sprite.getScaleY(),
                sprite.getRotation());
        }
    }

    @Override
    public Sprite createCurrentSprite(Camera camera){
        this.getSprite();
                
        if(this.spriteColor.a > 0){   
            if(this.sprite != null){

                if(!this.spriteColor.equals(this.sprite.getColor())){
                    this.sprite.setColor(this.spriteColor);
                }

                if(this.spriteRotation != this.sprite.getRotation()){
                    this.sprite.setColor(this.spriteColor);
                }

                this.sprite.setPosition(this.location.x - this.sprite.getWidth() / 2, this.location.y - this.sprite.getHeight() / 2);
            }
        }
        
        return this.sprite;
    }
    
    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        
    }
    
        /**
     * @return the object2D
     */
    public Object2D getObject2D() {
        return object2D;
    }

    /**
     * @return the sprite
     */
    public Sprite getSprite() {
        if(this.sprite == null){
            this.sprite = this.getObject2D().createCurrentSprite();
        }
        
        return this.sprite;
    }

}