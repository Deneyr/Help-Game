/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressourcesmanagers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Deneyr
 */
public class TextureManager extends ResourceManager implements AssetErrorListener{

    
    private static TextureManager instance;
    
    private Set<GraphicalComponent> waitingObject2D;
       
    private TextureManager(){
        super();
        
        ResourceManager.addGameEventListener(this);
        
        this.waitingObject2D = new HashSet<GraphicalComponent>();
        
        ResourceManager.assetManager.setErrorListener(this);
    }
    
    
    public static TextureManager getInstance(){
        if(TextureManager.instance != null){
            return TextureManager.instance;
        }
        return TextureManager.instance = new TextureManager();
    }

    @Override
    public void resetLoadedResources(){
        super.resetLoadedResources();
        
        this.waitingObject2D.clear();
    }
    
    @Override
    public void LoadNewResources() {
        for(String resourcePath : this.ressources2Load){
            ResourceManager.assetManager.load(resourcePath, Texture.class);
            this.mapLoadRessourceAgain.put(resourcePath, true);
        }
    }
    
    public Texture getTexture(String path, GraphicalComponent graphicalComponent){
        synchronized(this){
            this.registerResource(path);
            if(ResourceManager.assetManager.isLoaded(path)){
                return ResourceManager.assetManager.get(path);
            }else if(graphicalComponent != null){               
                this.waitingObject2D.add(graphicalComponent);
            }
        }
        return null;
    }
   
    @Override
    public void onResourcesLoaded(){
        synchronized(this){
            for(GraphicalComponent graphicalComponent : this.waitingObject2D){
                graphicalComponent.assignTextures();
            }
        }
    }
    
    @Override
    public void error(AssetDescriptor asset, java.lang.Throwable throwable){
        System.out.println("error : " + asset.toString());
    }
    
}
