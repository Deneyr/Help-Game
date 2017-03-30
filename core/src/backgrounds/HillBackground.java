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

/**
 *
 * @author françois
 */
public class HillBackground extends BackgroundWorld{

    private static final int NB_ELEM = 15; 
    
    public static final Texture HILL = new Texture("background" + File.separator + "Decors_FondFond_Colline.png");
    
    public static final Texture CLOUD = new Texture("background" + File.separator + "Nuage_Test2.png");
    public static final Texture BIRD = new Texture("background" + File.separator + "SpritemapOiseau.png");
    
    
    private DynamicElemLogic cloudsLogic;
    
    private BirdsDynamicElemLogic birdsLogic;
    
    private float lowerX;
    private float lowerY;
    private float upperX;
    private float upperY;
    
    public HillBackground(int seed){
        super();
        
        this.ratioDist = 0.8f;
        
        BackgroundPart part = new BackgroundPart(200 * P2M, new Vector2(-2000 * P2M, -200 * P2M),  new Vector2(0 * P2M, -200 * P2M), 1f);
        
        part.addObject2D2Scenary(HILL, 30);
        part.addObject2D2Scenary(CityBackground.BUILDING, 30);
        part.createSpriteList(seed);
        
        this.backgroundPartList.put(part.getStartPart().x, part);
        
        // Dynamic elem
        
        this.cloudsLogic = new CloudDynamicElemLogic(1, -2000 * P2M, 0 * P2M, 20);
        this.birdsLogic = new BirdsDynamicElemLogic(2, -2000 * P2M, 0 * P2M, 4);
        
        this.lowerX = 0;
        this.lowerY = 0;
        this.upperX = 0;
        this.upperY = 0;
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
               
                /*System.out.println("sprite x : " + sprite.getX() + " and max : " + (sprite.getX() +sprite.getWidth()));
                System.out.println("cam x : " + lowerX + " and max : " + upperX);*/
                
                spriteList.add(sprite);
            }
        }
        
        // Part clouds
        for(Sprite sprite : this.cloudsLogic.getSpriteMap().keySet()){
            if(sprite.getX() + sprite.getWidth() > lowerX / P2M && sprite.getX() < upperX / P2M){
                
                spriteList.add(sprite);
            }
        }
       
        
        return spriteList;
    }
    
    @Override
    public void step(float delta) {
        
        
        this.cloudsLogic.step(delta, this.lowerX, this.lowerY, this.upperX, this.upperY);
        
        
        this.birdsLogic.step(delta, this.lowerX, this.lowerY, this.upperX, this.upperY);
    }
    
    
    protected class CloudDynamicElemLogic extends DynamicElemLogic{
        
        private final float levelArea;
        
        private final float spaceLevelArea;
        
        public CloudDynamicElemLogic(int indexBehaviour, float startArea, float endArea, int nbSpriteIntoBox) {
            super(indexBehaviour, startArea, endArea, nbSpriteIntoBox);
            
            this.levelArea = 7f;
            this.spaceLevelArea = 2f;
        }
    
        
        @Override
        protected void addSprite(boolean isRight, float startArea, float endArea){
            Sprite newSprite = new Sprite(HillBackground.CLOUD);
            
            
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
        
        public BirdsDynamicElemLogic(int indexBehaviour, float startArea, float endArea, int nbSpriteIntoBox) {
            super(indexBehaviour, startArea, endArea, nbSpriteIntoBox);
            
            TextureRegion[][] tmp = TextureRegion.split(HillBackground.BIRD, 76, 76);
            
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
