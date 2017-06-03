/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressourcesmanagers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.WorldPlane;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Deneyr
 */
public abstract class ResourceManager implements WorldPlane, Disposable, ResourceManagerListener{

    private static Texture loadingScreenTexture;
    private static Sprite loadingScreenSprite;
    
    protected static AssetManager assetManager;
    
    protected Map<String, Boolean> mapLoadRessourceAgain;
    
    protected List<String> ressources2Load;
    
    protected static boolean isLoading;
    
    protected static List<WeakReference<ResourceManagerListener>> resourceManagerListeners; 
    
    public ResourceManager(){
        if(ResourceManager.assetManager == null){
            ResourceManager.assetManager = new AssetManager();
        }
        
        if(ResourceManager.loadingScreenTexture == null){
            ResourceManager.loadingScreenTexture = new Texture("parapluieLoadScreen.png");
            ResourceManager.loadingScreenSprite = new Sprite(ResourceManager.loadingScreenTexture);
        }
        
        if(ResourceManager.resourceManagerListeners == null){
           ResourceManager.resourceManagerListeners = new ArrayList<WeakReference<ResourceManagerListener>>();
        }
        
        this.mapLoadRessourceAgain = new HashMap<String, Boolean>();
        
        this.ressources2Load = new ArrayList<String>();

        this.isLoading = false;
    }
    
    // listener
    public static void addGameEventListener(ResourceManagerListener listener){
        if(listener != null){
            ResourceManager.resourceManagerListeners.add(new WeakReference(listener));
        }
    }
    
    public static void notifyResourcesLoaded(){
        for(WeakReference<ResourceManagerListener> listener : ResourceManager.resourceManagerListeners){
            if(listener.get() != null){
                listener.get().onResourcesLoaded();
            }
        }
    }
    
    @Override
    public void dispose() {
        if(ResourceManager.assetManager != null){
            ResourceManager.assetManager.dispose();
            ResourceManager.assetManager = null;
        }
        
        if(ResourceManager.loadingScreenSprite != null){
            ResourceManager.loadingScreenSprite = null;
            ResourceManager.loadingScreenTexture.dispose();
            ResourceManager.loadingScreenTexture = null;
        }
        
        if(ResourceManager.resourceManagerListeners != null){
            ResourceManager.resourceManagerListeners.clear();
            ResourceManager.resourceManagerListeners = null;
        }
    }
    
    public void resetLoadedResources(){
        
        for(Entry<String, Boolean> entryLoadRessourceAgain : this.mapLoadRessourceAgain.entrySet()){
            entryLoadRessourceAgain.setValue(false);
        }
        this.ressources2Load.clear();
    }
    
    public boolean registerResource(String path){
        if(this.mapLoadRessourceAgain.containsKey(path)){
            this.mapLoadRessourceAgain.put(path, true);
            return true;
        }
        this.ressources2Load.add(path);
        return false;
    }
    
    public void UpdateResources(){

        ResourceManager.isLoading = true;
        
        HashSet<Entry<String, Boolean>> entryLoadRessourcesAgain = new HashSet<Entry<String, Boolean>>(this.mapLoadRessourceAgain.entrySet());
        for(Entry<String, Boolean> entryLoadRessourceAgain : entryLoadRessourcesAgain){
            if(!entryLoadRessourceAgain.getValue()){
                ResourceManager.assetManager.unload(entryLoadRessourceAgain.getKey());
                this.mapLoadRessourceAgain.remove(entryLoadRessourceAgain.getKey());
            }
        }
        
        this.LoadNewResources();
        
        this.ressources2Load.clear();
    } 
    
    protected static boolean hasFinishedLoading(){
        return ResourceManager.assetManager.update();
    }
    
    protected abstract void LoadNewResources();
    
    
    @Override
    public void onResourcesLoaded(){
        // Nothing to do.
    }
    
    // Part loading screen
    
    public void drawBatch(Camera camera, Batch batch){
        float posX = camera.position.x + camera.viewportWidth * 0.6f / 2f;
        float posY = camera.position.x + camera.viewportHeight * 0.6f / 2f;
        
        
        Sprite sprite = ResourceManager.loadingScreenSprite;
        sprite.setPosition(posX, posY);
        
        batch.draw(sprite, 
                sprite.getX(), sprite.getY(),
                sprite.getOriginX(), sprite.getOriginY(),
                sprite.getWidth(),sprite.getHeight(),
                sprite.getScaleX(),sprite.getScaleY(),
                sprite.getRotation());
    }
    
    public void updateLogic(float deltaTime){
        ResourceManager.loadingScreenSprite.setRotation(deltaTime * 10 * ResourceManager.loadingScreenSprite.getRotation());
        
        
        if(ResourceManager.hasFinishedLoading()){
            if(ResourceManager.isLoading){
                ResourceManager.isLoading = false;
                
                this.onResourcesLoaded();
                
                ResourceManager.notifyResourcesLoaded();
            }
        }
        
    }
    
    
    @Override
    public List<Sprite> getSpritesInRegion(float lowerX, float lowerY, float upperX, float upperY){
        ArrayList<Sprite> listSprites = new ArrayList<Sprite>();
                
        ResourceManager.loadingScreenSprite.setPosition(lowerX + (upperX - lowerX) * 4/5, lowerY + (upperY - lowerY) * 4/5);
        
        listSprites.add(loadingScreenSprite);
        
        return listSprites;
    }
    
    @Override
    public void step(float delta){
        this.updateLogic(delta);
    }
    
    @Override
    public void flushWorld(){
        this.dispose();
    }
   
    
}
