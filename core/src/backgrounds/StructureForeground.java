/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BackgroundWorld;
import com.mygdx.game.HelpGame;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ressourcesmanagers.TextureManager;


/**
 *
 * @author Deneyr
 */
public abstract class StructureForeground extends BackgroundWorld{

    protected ArrayList<String> texturePathList;
    private ArrayList<Texture> textureList;
    
    private Map<String, StructurePart> structurePartMap;
    
    public StructureForeground() {
        super(0);
        
        this.texturePathList = new ArrayList<String>();
        this.textureList = new ArrayList<Texture>();
        
        this.structurePartMap = new HashMap<String, StructurePart>();
    }
    
    @Override
    public void assignTextures(){

        boolean allTextureLoaded = true;
        for(String texturePath: this.texturePathList){
            Texture texture = TextureManager.getInstance().getTexture(texturePath, this);
            
            if(texture != null){
                this.textureList.add(texture);
            }else{
                allTextureLoaded = false;
            }
        }
        
        if(allTextureLoaded){
            
            for(StructurePart structurePart: this.structurePartMap.values()){
                
                structurePart.addObject2D2Scenary(this.textureList.get(structurePart.getTextureIndex()), 0);

                structurePart.createSpriteList(this.seed);
                
                this.backgroundPartList.put(structurePart.getStartPart().x, structurePart);
            }
            
        }
    }
    
    @Override
    public void step(float delta) {
        
        for(StructurePart structurePart: this.structurePartMap.values()){
            structurePart.step(delta);
        }
    }
    
    public void addStructurePart(String partID, int textureIndex, Vector2 positionStartPart, Vector2 positionEndPart){
        this.structurePartMap.put(partID, new StructurePart(textureIndex, new Vector2(positionStartPart), new Vector2(positionEndPart)));
    }
    
    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
        if(this.structurePartMap.containsKey(details)){
            StructurePart structurePart = this.structurePartMap.get(details);
            
            switch(type){
                case ENTERSTRUCT:
                    structurePart.setMustAppear(false);
                    break;
                case QUITSTRUCT:
                    structurePart.setMustAppear(true);
                    break;
            }
        }
    }
    
    @Override
    public void dispose() {
        this.structurePartMap.clear();
        
        super.dispose();
    }
    
    public class StructurePart extends BackgroundPart{
        
        private boolean mustAppear;
        private float appearanceRatio;
        private int textureIndex;
        
        public StructurePart(int textureIndex, Vector2 positionStartPart, Vector2 positionEndPart) {
            super(0, positionStartPart, positionEndPart, 1f);
            
            this.mustAppear = true;
            this.appearanceRatio = 1f;
            this.textureIndex = textureIndex;
        }
        
        public void step(float delta) {
            if(this.mustAppear && this.appearanceRatio < 1f){
                this.appearanceRatio += delta;
                
                if(this.appearanceRatio > 1f){
                    this.appearanceRatio = 1f;
                }
                
                this.updateSpriteAlpha();
            }else if(!this.mustAppear && this.appearanceRatio > 0f){
                this.appearanceRatio -= delta;
                
                if(this.appearanceRatio < 0f){
                    this.appearanceRatio = 0f;
                }
                
                this.updateSpriteAlpha();
            }    
        }
 
        private void updateSpriteAlpha(){
            for(Sprite sprite: this.spriteListInBackground){
                sprite.setAlpha(this.appearanceRatio);
            }
        }
        
        /**
         * @param mustAppear the mustAppear to set
         */
        public void setMustAppear(boolean mustAppear) {
            this.mustAppear = mustAppear;
        }

        @Override
        public void createSpriteList(int seed){
                        
            List<Sprite> spriteList = new ArrayList<Sprite>();         
            
            if(this.mustAppear || this.ratioSprite > 0.001f){
                for(Texture text : this.sceneryElemList){

                    Sprite sprite = new Sprite(text);
                    
                    sprite.setAlpha(this.appearanceRatio);
                    sprite.setPosition((this.startPart.x + this.endPart.x)/(2 * P2M) - sprite.getWidth() / 2, (this.startPart.y + this.endPart.y)/(2 * P2M) - sprite.getHeight() / 2);
                    
                    float scaleX = (this.endPart.x - this.startPart.x) / (P2M * sprite.getWidth());
                    float scaleY = (this.endPart.y - this.startPart.y) / (P2M * sprite.getHeight());
                                  
                    sprite.setScale(scaleX, scaleY);
                    
                    this.ratioSprite = Math.max(scaleX, scaleY);
                    
                    spriteList.add(sprite);
                }
            }
            
            this.spriteListInBackground = spriteList;          
        }

        /**
         * @return the textureIndex
         */
        public int getTextureIndex() {
            return textureIndex;
        }
        
    }
    
}
