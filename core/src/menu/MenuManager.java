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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameEventListener;
import guicomponents.GuiComponent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Deneyr
 */
public class MenuManager implements Disposable{

    private float epochTime;
    
    private Vector2 cameraPosition;
    private Vector2 cameraScale;
    private float cameraRotation;
    
    Animation cameraAnimation;
    
    TreeMap<Integer, GuiComponent> layoutsGuiComponent; 
    Map<GuiComponent, List<Animation>> mapListAnimations;
    
    protected List<WeakReference<GameEventListener>> listEventGameListeners;
    
    public MenuManager(){
        this.epochTime = -1;
        
        this.mapListAnimations = new HashMap<GuiComponent,List<Animation>>();
        
        this.layoutsGuiComponent = new TreeMap<Integer, GuiComponent>();
        
        this.cameraAnimation = null;
        
        this.listEventGameListeners = new ArrayList<WeakReference<GameEventListener>>();
        
        // Part camera
        this.cameraPosition = new Vector2();
        
        this.cameraScale = new Vector2();
        
        this.cameraRotation = 0;
    }
    
    public void addGuiComponent(GuiComponent guiComponent, int layoutIndex){
        this.layoutsGuiComponent.put(layoutIndex, guiComponent);
        this.mapListAnimations.put(guiComponent, new ArrayList<Animation>());
    }
    
    public void addCameraAnimation(Animation cameraAnimation){
        
    }
    
    public void addAnimation(Animation animation){
        GuiComponent guiComponent = animation.getGuiComponent();
        if(this.mapListAnimations.containsKey(guiComponent)){
            List<Animation> listAnimations = this.mapListAnimations.get(guiComponent);
            listAnimations.add(animation);
        }
    }
    
    public void step(float deltaTime){
        if(this.epochTime < 0){
            this.epochTime = 0;
        }
        
        this.epochTime += deltaTime;
        
        for(List<Animation> listAnimations : this.mapListAnimations.values()){
            for(Animation animation : listAnimations){
                animation.updateAnimation(this.epochTime, this, false);
            }
        }
        
        if(this.cameraAnimation != null){
            this.cameraAnimation.updateAnimation(this.epochTime, this, true);
        }
    }
    
    public void drawBatch(Camera camera, Batch batch){
        for(GuiComponent guiComponent : this.layoutsGuiComponent.values()){
            guiComponent.drawBatch(camera, batch);
        }
    }
    
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer){
        for(GuiComponent guiComponent : this.layoutsGuiComponent.values()){
            guiComponent.drawShapeRenderer(camera, shapeRenderer);
        }
    }

    @Override
    public void dispose() {
        this.epochTime = -1;
        
        for(List<Animation> listAnimations : this.mapListAnimations.values()){
            for(Animation animation : listAnimations){
                animation.dispose();
            }
        }
        if(this.cameraAnimation != null){
            this.cameraAnimation.dispose();
            this.cameraAnimation = null;
        }
        
        this.layoutsGuiComponent.clear();
        this.mapListAnimations.clear();
        
        // Part camera
        this.cameraPosition = new Vector2();
        
        this.cameraScale = new Vector2();
        
        this.cameraRotation = 0;
    }
    
    public void addGameEventListener(GameEventListener listener){
        if(listener != null){
            this.listEventGameListeners.add(new WeakReference(listener));
        }
    }

    /**
     * @param cameraScale the cameraScale to set
     */
    public void setCameraScale(float cameraScaleX, float cameraScaleY) {
        this.cameraScale = cameraScale;
    }

    /**
     * @param cameraRotation the cameraRotation to set
     */
    public void setCameraRotation(float cameraRotation) {
        this.cameraRotation = cameraRotation;
    }
    
    /**
     * @param cameraPosition the cameraPosition to set
     */
    public void setCameraPosition(float cameraScaleX, float cameraScaleY) {
        this.cameraPosition = cameraPosition;
    }
    
    public void updateCamera(Camera camera, float originalWidth, float originalHeight){
        
        camera.position.set(this.cameraPosition.x, this.cameraPosition.y, 0);
        
        camera.rotate(new Vector3(0, 0, 1), this.cameraRotation);
        
        if(this.cameraScale.x != 1 || this.cameraScale.y != 1){
            camera.viewportWidth = originalWidth * this.cameraScale.x;
            camera.viewportHeight = originalHeight * this.cameraScale.y;
        }
        
        camera.update();
    }

}
