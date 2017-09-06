/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressourcesmanagers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Deneyr
 */
public class MusicManager extends ResourceManager implements AssetErrorListener{

    private static MusicManager instance;
       
    private MusicManager(){
        super();
        
        ResourceManager.addGameEventListener(this);
        
        this.assetManager.setErrorListener(this);
    }
    
    
    public static MusicManager getInstance(){
        if(MusicManager.instance != null){
            return MusicManager.instance;
        }
        return MusicManager.instance = new MusicManager();
    }

    @Override
    public void resetLoadedResources(){
        super.resetLoadedResources();
    }
    
    @Override
    public void LoadNewResources() {
        for(String resourcePath : this.ressources2Load){
            ResourceManager.assetManager.load(resourcePath, Music.class);
            this.mapLoadRessourceAgain.put(resourcePath, true);
        }
    }
    
    public Music getMusic(String path){
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