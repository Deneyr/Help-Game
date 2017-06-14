/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.util.ArrayList;
import java.util.List;
import menu.Animation.AnimationState;

/**
 *
 * @author Deneyr
 */
public class MenuManager implements Disposable{
    private float epochTime;
    
    private Vector2 cameraPosition;
    private Vector2 cameraScale;
    private float cameraRotation;
    
    List<Animation> listAnimation;
    
    public MenuManager(){
        this.epochTime = -1;
        
        this.listAnimation = new ArrayList<Animation>();
    }
    
    public void addAnimation(Animation animation){
        this.listAnimation.add(animation);
    }
    
    public void step(float deltaTime){
        if(this.epochTime < 0){
            this.epochTime = 0;
        }
        
        this.epochTime += deltaTime;
        
        for(Animation animation : this.listAnimation){
            animation.updateAnimation(this.epochTime, this);
        }
    }
    
    public void drawBatch(Camera camera, Batch batch){
        for(Animation animation : this.listAnimation){
            if(animation.getAnimationState() == AnimationState.RUN){
                animation.getGuiComponent().drawBatch(camera, batch);
            }
        }
    }
    
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer){
        for(Animation animation : this.listAnimation){
            if(animation.getAnimationState() == AnimationState.RUN){
                animation.getGuiComponent().drawShapeRenderer(camera, shapeRenderer);
            }
        }
    }

    @Override
    public void dispose() {
        this.epochTime = -1;
        
        for(Animation animation : this.listAnimation){
            animation.dispose();
        }
        this.listAnimation.clear();
    }

}
