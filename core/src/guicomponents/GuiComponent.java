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
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;

/**
 *
 * @author Deneyr
 */
public abstract class GuiComponent extends Object2D{
    
    protected Vector2 location;
    
    public GuiComponent(){
        super();
        this.location = new Vector2();
    }
    
    public abstract void drawBatch(Camera camera, Batch batch);
    public abstract void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer);
    
    public Sprite createCurrentSprite(Camera camera){
        Sprite sprite;
        
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        if(this.currentAnimation < 0){
            sprite = null;

        }else{
            TextureRegion region = this.listAnimations.get(this.currentAnimation).getKeyFrame(this.animationTime);
            sprite = new Sprite(region);  
        }
        if(sprite != null){
            sprite.setPosition(posX, posY);
        }
        
        return sprite;
    }
    
    /**
     * @return the location
     */
    public Vector2 getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(float locX, float locY) {
        this.location.set(locX, locY);
    }
}
