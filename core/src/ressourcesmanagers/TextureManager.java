/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressourcesmanagers;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Object2D;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Deneyr
 */
public class TextureManager extends ResourceManager{

    
    private static TextureManager instance;
    
    private List<GraphicalComponent> waitingObject2D;
       
    private TextureManager(){
        super();
        
        this.waitingObject2D = new ArrayList<GraphicalComponent>();
    }
    
    
    public static TextureManager getInstance(){
        if(TextureManager.instance != null){
            return TextureManager.instance;
        }
        return TextureManager.instance = new TextureManager();
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
            if(ResourceManager.hasFinishedLoading()){
                return ResourceManager.assetManager.get(path);
            }else{
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
    
}
