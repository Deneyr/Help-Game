/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
    
    public abstract void draw(Batch batch, Camera camera, ShapeRenderer shapeRenderer);
    
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
