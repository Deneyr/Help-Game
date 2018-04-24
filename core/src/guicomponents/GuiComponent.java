/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Object2D;

/**
 *
 * @author Deneyr
 */
public abstract class GuiComponent extends Object2D{
    
    protected Vector2 location;
    
    // Part Sprite attributes.
    
    protected Color spriteColor;
    
    protected float spriteRotation;
    
    protected Vector2 spriteScale;
    
    public GuiComponent(){
        super();
        this.location = new Vector2();
        
        // Part Sprite attributes.
        
        this.spriteColor = Color.WHITE;
        
        this.spriteRotation = 0;
        
        this.spriteScale = new Vector2(1, 1);
    }
    
    public abstract void drawBatch(Camera camera, Batch batch);
    public abstract void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer);
    
    public Sprite createCurrentSprite(Camera camera){
        Sprite sprite = null;
        
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        if(this.spriteColor.a > 0){
        
            if(this.currentAnimation < 0){
                if(this.texture != null){
                    sprite = new Sprite(this.texture);
                }else{
                    sprite = null;
                }
            }else{
                TextureRegion region = this.listAnimations.get(this.currentAnimation).getKeyFrame(this.animationTime);
                sprite = new Sprite(region);  
            }


            if(sprite != null){

                if(!this.spriteColor.equals(sprite.getColor())){
                    sprite.setColor(this.spriteColor);
                }

                if(this.spriteRotation != sprite.getRotation()){
                    sprite.setColor(this.spriteColor);
                }

                if(this.spriteScale.x != sprite.getScaleX() || this.spriteScale.y != sprite.getScaleX()){
                    sprite.setScale(this.spriteScale.x, this.spriteScale.y);
                }

                sprite.setPosition(posX, posY);
            }
        }
        
        return sprite;
    }
    
    
    /**
     * @return the location
     */
    public Vector2 getLocation() {
        return this.location;
    }

    public void setLocation(float locX, float locY) {
        this.location.set(locX, locY);
    }
    
    // Part lerp sprite attributes.

    /**
     * @return the spriteColor
     */
    public Color getSpriteColor() {
        return spriteColor;
    }

    /**
     * @param spriteColor the spriteColor to set
     */
    public void setSpriteColor(Color spriteColor) {
        this.spriteColor = spriteColor;
    }

    /**
     * @return the spriteRotation
     */
    public float getSpriteRotation() {
        return spriteRotation;
    }

    /**
     * @param spriteRotation the spriteRotation to set
     */
    public void setSpriteRotation(float spriteRotation) {
        this.spriteRotation = spriteRotation;
    }

    /**
     * @return the spriteScale
     */
    public Vector2 getSpriteScale() {
        return spriteScale;
    }

    /**
     * @param spriteScale the spriteScale to set
     */
    public void setSpriteScale(Vector2 spriteScale) {
        this.spriteScale = spriteScale;
    }
    
   
    
}
