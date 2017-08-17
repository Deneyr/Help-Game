/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import guicomponents.GuiComponent;
import guicomponents.GuiMenuText;
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
public class MenuManager implements Disposable, GameEventListener{

    private float epochTime;
    
    private Vector2 cameraPosition;
    private Vector2 cameraScale;
    private float cameraRotation;
    
    private List<Animation> cameraListAnimations;
    
    private TreeMap<Integer, GuiComponent> layoutsGuiComponent; 
    private Map<GuiComponent, List<Animation>> mapListAnimations;
    
    private List<GuiMenuText> listGuiMenuTexts;
    private int indexCurrentText;
    private int indexPreviousText;
    private Animation hoverAnimation;
    private Animation unHoverAnimation;
    
    // Listener part.
    protected List<WeakReference<GameEventListener>> listEventGameListeners;
    
    public MenuManager(){
        this.epochTime = -1;
        
        this.mapListAnimations = new HashMap<GuiComponent,List<Animation>>();
        
        this.layoutsGuiComponent = new TreeMap<Integer, GuiComponent>();
        
        this.cameraListAnimations = new ArrayList<Animation>();
        
        this.listGuiMenuTexts = new ArrayList<GuiMenuText>();
        
        this.listEventGameListeners = new ArrayList<WeakReference<GameEventListener>>();
        
        // Part camera
        this.cameraPosition = new Vector2(0, 0);
        
        this.cameraScale = new Vector2(1f, 1f);
        
        this.cameraRotation = 0;
        
        // Part Hover.
        this.indexCurrentText = -1;
        this.indexPreviousText = -1;
        
        this.hoverAnimation = new Animation(null, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0, 0, 0.5f);
        this.hoverAnimation.setColorAnimation(Color.LIGHT_GRAY, Color.WHITE);
        this.hoverAnimation.setRelativePositionAnimation(new Vector2(0.1f, 0));
    
        this.unHoverAnimation = new Animation(null, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_DEC, 0, 0, 0.5f);
        this.unHoverAnimation.setColorAnimation(Color.WHITE, Color.LIGHT_GRAY);
        this.unHoverAnimation.setRelativePositionAnimation(new Vector2(-0.1f, 0));
    }
    
    public void setIndexCurrentText(int currentIndexText){
        if(this.indexCurrentText != currentIndexText){
            
            this.indexPreviousText = this.indexCurrentText;
            this.indexCurrentText = currentIndexText;
            
            if(this.indexCurrentText < 0){
                this.indexCurrentText = 0;
            }
            if(this.indexCurrentText >= this.listGuiMenuTexts.size()){
                this.indexCurrentText = this.listGuiMenuTexts.size() - 1;
            }
            
            if(this.indexPreviousText != this.indexCurrentText){
                if(this.indexCurrentText >= 0 && this.indexCurrentText < this.listGuiMenuTexts.size()){
                    GuiMenuText guiMenuText = this.listGuiMenuTexts.get(this.indexCurrentText);
                    this.hoverAnimation.setGuiComponent(this, guiMenuText, this.epochTime);
                }

                if(this.indexPreviousText >= 0 && this.indexPreviousText < this.listGuiMenuTexts.size()){
                    GuiMenuText guiMenuText = this.listGuiMenuTexts.get(this.indexPreviousText);
                    this.unHoverAnimation.setGuiComponent(this, guiMenuText, this.epochTime);
                }
            }
        }
    }
    
    public void addGuiComponent(GuiComponent guiComponent, int layoutIndex){
        this.layoutsGuiComponent.put(layoutIndex, guiComponent);
        this.mapListAnimations.put(guiComponent, new ArrayList<Animation>());
    }
    
    public void addGuiMenuText(GuiMenuText guiMenuText){
        if(guiMenuText != null){
            guiMenuText.addGameEventListener(this);
            this.listGuiMenuTexts.add(guiMenuText);
        }
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
        
        this.UpdateInfluences();
        
        for(List<Animation> listAnimations : this.mapListAnimations.values()){
            for(Animation animation : listAnimations){
                animation.updateAnimation(this.epochTime, this, false);
            }
        }
        
        for(Animation cameraAnimation : this.cameraListAnimations){
            cameraAnimation.updateAnimation(this.epochTime, this, true);
        }
        
        // Update hover & unhover animations.
        this.hoverAnimation.updateAnimation(this.epochTime, this, false);
        this.unHoverAnimation.updateAnimation(this.epochTime, this, false);
    }
    
    private void UpdateInfluences(){
        // Control Menu.
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            this.setIndexCurrentText(this.indexCurrentText + 1);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            this.setIndexCurrentText(this.indexCurrentText - 1); 
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            if(this.indexCurrentText >= 0 && this.indexCurrentText < this.listGuiMenuTexts.size()){
                GuiMenuText guiMenuText = this.listGuiMenuTexts.get(this.indexCurrentText);
                guiMenuText.onSelected();
            }
        }
    }
    
    public void drawBatch(Camera camera, Batch batch){
        for(GuiComponent guiComponent : this.layoutsGuiComponent.values()){
            guiComponent.drawBatch(camera, batch);
        }
        
        for(GuiMenuText guiMenuText : this.listGuiMenuTexts){
            guiMenuText.drawBatch(camera, batch);
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
        
        this.indexCurrentText = -1;
        
        for(List<Animation> listAnimations : this.mapListAnimations.values()){
            for(Animation animation : listAnimations){
                animation.dispose();
            }
        }
        
        for(GuiMenuText lGuiMenuText : this.listGuiMenuTexts){
            lGuiMenuText.dispose();
        }
        
        for(Animation cameraAnimation : this.cameraListAnimations){
            cameraAnimation.dispose();
        }
        this.cameraListAnimations.clear();
        
        this.listGuiMenuTexts.clear();
        
        this.layoutsGuiComponent.clear();
        this.mapListAnimations.clear();
        
        // Part camera
        this.cameraPosition = new Vector2(0, 0);
        
        this.cameraScale = new Vector2(1f, 1f);
        
        this.cameraRotation = 0;
    }
    
    public void addGameEventListener(GameEventListener listener){
        if(listener != null){
            this.listEventGameListeners.add(new WeakReference(listener));
        }
    }

    public void notifyGameEventListeners(EventType event, String details, Vector2 location){
        for(WeakReference<GameEventListener> refEventGameListener : this.listEventGameListeners){
            if(refEventGameListener.get() != null){
                refEventGameListener.get().onGameEvent(event, details, location);
            }
        }
    }
    
    /**
     * @param cameraScale the cameraScale to set
     */
    public void setCameraScale(float cameraScaleX, float cameraScaleY) {
        this.cameraScale.set(cameraScaleX, cameraScaleY);
    }

    /**
     * @param cameraRotation the cameraRotation to set
     */
    public void setCameraRotation(float cameraRotation) {
        this.cameraRotation = cameraRotation;
    }
    
    public void setCameraPosition(float cameraScaleX, float cameraScaleY) {
        this.cameraPosition.set(cameraScaleX, cameraScaleY);
    }
    
    public void updateCamera(Camera camera, float originalWidth, float originalHeight){
        
        camera.rotate(new Vector3(0, 0, 1), this.cameraRotation);
        
        if(camera instanceof OrthographicCamera){
            if(this.cameraScale.x < 1 || this.cameraScale.y < 1){
                OrthographicCamera ortoCamera = (OrthographicCamera) camera;

                ortoCamera.setToOrtho(false, originalWidth * this.cameraScale.x, originalHeight * this.cameraScale.y);   
            }
        }

        camera.position.set(this.cameraPosition.x, this.cameraPosition.y, 0);
        
        camera.update();
    }

    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        this.notifyGameEventListeners(type, details, location);
    }

    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
        // Nothing to do.
    }

}
