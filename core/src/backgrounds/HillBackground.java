/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BackgroundWorld;
import static com.mygdx.game.HelpGame.P2M;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class HillBackground extends BackgroundWorld{
    
    public static final String HILL = "background/Decors_FondFond_Colline.png";
    
    public static final String CLOUD1 = "background/Help_Nuages_01.png";
    public static final String CLOUD2 = "background/Help_Nuages_04.png";
    public static final String CLOUD3 = "background/Help_Nuages_03.png";
    public static final String BIRD = "background/SpritemapOiseau.png";
    
    
    private DynamicElemLogic cloudsLogic;
    private DynamicElemLogic cloudsLogic2;
    private DynamicElemLogic cloudsLogic3;
    
    private BirdsDynamicElemLogic birdsLogic;
    
    private float lowerX;
    private float lowerY;
    private float upperX;
    private float upperY;
    
    public HillBackground(int seed){
        super(seed);
        
        this.ratioDist = 0.8f;
        
        this.assignTextures();
        
        this.lowerX = 0;
        this.lowerY = 0;
        this.upperX = 0;
        this.upperY = 0;
    }
    
    @Override
    public void assignTextures(){
        
        Texture building = TextureManager.getInstance().getTexture(CityBackground.BUILDING, this);
        Texture hill = TextureManager.getInstance().getTexture(HILL, this);
        Texture cloud = TextureManager.getInstance().getTexture(CLOUD1, this);
        Texture cloud2 = TextureManager.getInstance().getTexture(CLOUD2, this);
        Texture bird = TextureManager.getInstance().getTexture(BIRD, this);
        
        if(building != null
                && hill != null
                && cloud != null
                && cloud2 != null
                && bird != null){
            
            BackgroundPart part = new BackgroundPart(200 * P2M, new Vector2(-1000 * P2M, -75 * P2M),  new Vector2(2000 * P2M, -75 * P2M), 1f);
        
            part.addObject2D2Scenary(hill, 30);
            part.addObject2D2Scenary(building, 30);
            part.createSpriteList(seed);

            this.backgroundPartList.put(part.getStartPart().x, part);

            // Dynamic elem

            this.cloudsLogic = new CloudDynamicElemLogic(cloud, 1, -2000 * P2M, 2000 * P2M, 10);
            this.cloudsLogic2 = new CloudDynamicElemLogic(cloud2, 1, -2000 * P2M, 2000 * P2M, 10);
            this.cloudsLogic3 = new CloudDynamicElemLogic(cloud2, 1, -2000 * P2M, 2000 * P2M, 10);
            this.birdsLogic = new BirdsDynamicElemLogic(bird, 2, -2000 * P2M, 2000 * P2M, 4);
        }
        
    } 
    
    @Override
    public List<Sprite> getSpritesInRegion(float lowerX, float lowerY, float upperX, float upperY) {
        
        this.lowerX = lowerX;
        this.lowerY = lowerY;
        this.upperX = upperX;
        this.upperY = upperY;
        
        
        List<Sprite> spriteList = super.getSpritesInRegion(lowerX, lowerY, upperX, upperY);
        
        // Part birds
        for(Sprite sprite : this.birdsLogic.getSpriteMap().keySet()){
            if(sprite.getX() + sprite.getWidth() > lowerX / P2M && sprite.getX() < upperX / P2M){
                
                spriteList.add(sprite);
            }
        }
        
        // Part clouds
        for(Sprite sprite : this.cloudsLogic.getSpriteMap().keySet()){
            if(sprite.getX() + sprite.getWidth() > lowerX / P2M && sprite.getX() < upperX / P2M){
                
                spriteList.add(sprite);
            }
        }
        
        for(Sprite sprite : this.cloudsLogic2.getSpriteMap().keySet()){
            if(sprite.getX() + sprite.getWidth() > lowerX / P2M && sprite.getX() < upperX / P2M){
                
                spriteList.add(sprite);
            }
        }
        
        for(Sprite sprite : this.cloudsLogic3.getSpriteMap().keySet()){
            if(sprite.getX() + sprite.getWidth() > lowerX / P2M && sprite.getX() < upperX / P2M){
                
                spriteList.add(sprite);
            }
        }
       
        
        return spriteList;
    }
    
    @Override
    public void step(float delta) {
        
        
        this.cloudsLogic.step(delta, this.lowerX, this.lowerY, this.upperX, this.upperY);
        this.cloudsLogic2.step(delta, this.lowerX, this.lowerY, this.upperX, this.upperY);
        this.cloudsLogic3.step(delta, this.lowerX, this.lowerY, this.upperX, this.upperY);
        
        
        this.birdsLogic.step(delta, this.lowerX, this.lowerY, this.upperX, this.upperY);
    }
    
    
    protected class CloudDynamicElemLogic extends DynamicElemLogic{
        
        private final float levelArea;
        
        private final float spaceLevelArea;
        
        private Texture texture;
        
        public CloudDynamicElemLogic(Texture texture, int indexBehaviour, float startArea, float endArea, int nbSpriteIntoBox) {
            super(indexBehaviour, startArea, endArea, nbSpriteIntoBox);
            
            this.texture = texture;
            
            this.levelArea = 7f;
            this.spaceLevelArea = 2f;
        }
    
        
        @Override
        protected void addSprite(boolean isRight, float startArea, float endArea){
            Sprite newSprite = new Sprite(this.texture);
            
            
            float scale = (float) (0.5 * Math.random() + 1);
            newSprite.setScale(scale, scale);
            
            if(isRight){
                newSprite.setPosition((float) (Math.random() * (endArea - startArea) + startArea) / P2M, (this.levelArea + (float) (this.spaceLevelArea * Math.random())) / P2M);
            }else{
                newSprite.setPosition((float) (Math.random() * (endArea - startArea) + startArea) / P2M - newSprite.getWidth(), (this.levelArea + (float) (this.spaceLevelArea * Math.random())) / P2M);
            }
            
            
            
            this.spriteMap.put(newSprite, (float) ((2 * Math.random() + 1) * Math.signum(Math.random() - 0.5)));
        }
    }
    
    public class BirdsDynamicElemLogic extends AnimDynamicElemLogic{

        private final float levelArea;
        
        private final float spaceLevelArea;
        
        private List<Animation> animList; 
        
        public BirdsDynamicElemLogic(Texture bird, int indexBehaviour, float startArea, float endArea, int nbSpriteIntoBox) {
            super(indexBehaviour, startArea, endArea, nbSpriteIntoBox);
            
            TextureRegion[][] tmp = TextureRegion.split(bird, 76, 76);
            
            this.animList = new ArrayList<Animation>();
            
            this.animList.add(new Animation(0.3f, tmp[0]));
            this.animList.get(this.animList.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            
            this.levelArea = 2f;
            this.spaceLevelArea = 5f;
        }

        @Override
        protected void addSprite(boolean isRight, float startArea, float endArea) {
            
            int indexAnim = (int) (Math.random() * this.animList.size());
            int indexFrame = (int) (Math.random() * this.animList.get(indexAnim).getKeyFrames().length);
            
            Sprite newSprite = new Sprite(this.animList.get(indexAnim).getKeyFrames()[indexFrame]);
            
            
            float scale = (float) (0.5 * Math.random() + 1);
            newSprite.setScale(scale, scale);
            
            if(isRight){
                newSprite.setPosition((float) (Math.random() * (endArea - startArea) + startArea) / P2M, (this.levelArea + (float) (this.spaceLevelArea * Math.random())) / P2M);
            }else{
                newSprite.setPosition((float) (Math.random() * (endArea - startArea) + startArea) / P2M - newSprite.getWidth(), (this.levelArea + (float) (this.spaceLevelArea * Math.random())) / P2M);
            }
            
            
            
            this.spriteMap.put(newSprite, (float) ((2 * Math.random() + 1) * Math.signum(Math.random() - 0.5) / 8));
            
            this.timerAnimMap.put(newSprite, indexFrame * this.animList.get(indexAnim).getFrameDuration());
            this.indexAnimMap.put(newSprite, indexAnim);
        }
        
        public void updateTimerAnim(float deltaTime){
            for(Entry<Sprite, Float> entry : this.timerAnimMap.entrySet()){
                entry.setValue(entry.getValue() + deltaTime);
            }
        }
        
        public void updateSpriteAnim(float deltaTime){
            
            for(Entry<Sprite, Float> entry : this.timerAnimMap.entrySet()){               
                Sprite sprite = entry.getKey();
                int indexAnim = this.indexAnimMap.get(sprite);
                TextureRegion region = this.animList.get(indexAnim).getKeyFrame(this.timerAnimMap.get(sprite));
                sprite.setRegion(region);
            }
        }
        
        @Override
        public void step(float deltaTime, float lowerX, float lowerY, float upperX, float upperY){
            super.step(deltaTime, lowerX, lowerY, upperX, upperY);
            
            updateTimerAnim(deltaTime);
            updateSpriteAnim(deltaTime);
        }
        
    }
    
}
