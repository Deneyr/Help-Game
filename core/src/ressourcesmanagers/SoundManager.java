/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressourcesmanagers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Deneyr
 */
public class SoundManager extends ResourceManager implements AssetErrorListener{

    
    private static SoundManager instance;
       
    private SoundManager(){
        super();
        
        ResourceManager.addGameEventListener(this);
        
        this.assetManager.setErrorListener(this);
    }
    
    
    public static SoundManager getInstance(){
        if(SoundManager.instance != null){
            return SoundManager.instance;
        }
        return SoundManager.instance = new SoundManager();
    }

    @Override
    public void resetLoadedResources(){
        super.resetLoadedResources();
    }
    
    @Override
    public void LoadNewResources() {
        for(String resourcePath : this.ressources2Load){
            ResourceManager.assetManager.load(resourcePath, Sound.class);
            this.mapLoadRessourceAgain.put(resourcePath, true);
        }
    }
    
    public Sound getSound(String path){
        synchronized(this){
            this.registerResource(path);
            if(ResourceManager.assetManager.isLoaded(path)){
                return ResourceManager.assetManager.get(path);
            }
        }
        return null;
    }
   
    @Override
    public void onResourcesLoaded(){
        // nothing to do.
    }
    
    @Override
    public void error(AssetDescriptor asset, java.lang.Throwable throwable){
        System.out.println("error : " + asset.toString());
    }
    
}
