/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Random;

/**
 *
 * @author françois
 */
public abstract class BackgroundWorld implements WorldPlane{

    protected TreeMap<Float, BackgroundPart> backgroundPartList;
    
    protected float ratioDist; 
    
    
    public BackgroundWorld(){
        this.backgroundPartList = new TreeMap<Float, BackgroundPart>();
    }
    
    @Override
    public List<Sprite> getSpritesInRegion(float lowerX, float lowerY, float upperX, float upperY) {
        
        List<Sprite> spriteList = new ArrayList<Sprite>();
        
        Entry<Float, BackgroundPart> entryPartStart = this.backgroundPartList.floorEntry(lowerX);
        Entry<Float, BackgroundPart> entryPartEnd = this.backgroundPartList.lowerEntry(upperX);
        
        if(entryPartEnd == null){
            entryPartEnd = this.backgroundPartList.lastEntry();
        }
        
        if(entryPartStart == null){
            entryPartStart = this.backgroundPartList.firstEntry();
        }
        
        NavigableMap<Float, BackgroundPart> subMapRegion = this.backgroundPartList.subMap(entryPartStart.getKey(), true, entryPartEnd.getKey(), true);

        
        for(BackgroundPart part : subMapRegion.values()){
            for(Sprite sprite : part.getSpriteListInBackground()){
                if(sprite.getX() + sprite.getWidth() > lowerX / P2M && sprite.getX() < upperX / P2M){
                    spriteList.add(sprite);
                }
            }
        }
       
        
        
        return spriteList;
    }

    @Override
    public void flushWorld() {
        this.backgroundPartList.clear();
    }
    
    protected void addBackgroundPart(float periodElem, Vector2 startPart, Vector2 endPart, float ratioSprite){
        BackgroundPart part = new BackgroundPart(periodElem, startPart, endPart, ratioSprite);
        
        this.backgroundPartList.put(part.startPart.x, part);
    }

    /**
     * @return the ratioDist
     */
    public float getRatioDist() {
        return ratioDist;
    }
    
    protected class BackgroundPart{
        
        protected float ratioSprite;
        
        private Vector2 startPart;
        private Vector2 endPart;
                
        private float periodElem;
        
        private List<Texture> sceneryElemList;
        private List<Integer> percentPresenceElemList;
        
        private List<Sprite> spriteListInBackground;
       
        
        public BackgroundPart(float periodElem, Vector2 startPart, Vector2 endPart, float ratioSprite){
            this.ratioSprite = ratioSprite;
            
            this.periodElem = periodElem;
            
            this.startPart = startPart;
            this.endPart = endPart;
            
            this.sceneryElemList = new ArrayList<Texture>();
            this.percentPresenceElemList = new ArrayList<Integer>();
            
            this.spriteListInBackground = new ArrayList<Sprite>();
        }
        
        public void addObject2D2Scenary(Texture text, int percentagePresence){
            this.sceneryElemList.add(text);
            this.percentPresenceElemList.add(percentagePresence);
        }
        
        public void createSpriteList(int seed){
            Random generator = new Random(seed);
            
            List<Sprite> spriteList = new ArrayList<Sprite>();
            
            float lengthPart = this.endPart.x - this.startPart.x;
            
            int nbParts = (int) (lengthPart / this.periodElem);
            
            List<Integer> indexParts = new ArrayList<Integer>();
            
            for(int i = 0; i < nbParts; i++){
                indexParts.add(i);
            }
            
            
            int i = 0;
            for(Texture text : this.sceneryElemList){
                int percentagePart = this.percentPresenceElemList.get(i);
                
                int nbIndex2Take = (int) (percentagePart * nbParts/ 100.f);
                
                
                for(int j = 0; j < nbIndex2Take; j++){
                    int index = (int) (generator.nextDouble() * indexParts.size());
                    
                    int indexPart = indexParts.get(index);
                    indexParts.remove(index);
                   
                    
                    Sprite sprite = new Sprite(text);
                    
                    if(this.ratioSprite != 1f){
                        sprite.scale(this.ratioSprite);
                    }
                    sprite.setAlpha((float) Math.min(0.3 + BackgroundWorld.this.ratioDist, 1f));
                    sprite.setPosition((indexPart * this.periodElem + this.startPart.x) / P2M - sprite.getWidth() / 2.f, (this.startPart.y + (this.endPart.y - this.startPart.y) * (((float) indexPart) / nbParts)) / P2M);
                    
                    
                    spriteList.add(sprite);
                }
                
                i++;
            }
            
            this.spriteListInBackground = spriteList;
           
        }

        /**
         * @return the endPart
         */
        public Vector2 getEndPart() {
            return endPart;
        }

        /**
         * @return the startPart
         */
        public Vector2 getStartPart() {
            return startPart;
        }

        /**
         * @return the spriteListInBackground
         */
        public List<Sprite> getSpriteListInBackground() {
            return spriteListInBackground;
        }
    }
    
    protected abstract class DynamicElemLogic{
        
        protected final float SCALEBOXREMOVESPRITES = 2f;
        protected final float SCALEBOXADDSPRITES = 1.5f;
        
        private int indexBehaviour;
        
        protected HashMap<Sprite, Float> spriteMap;
        
        private float startArea;
        private float endArea;
        
        private int nbSpriteIntoBox;
       
        
        public DynamicElemLogic(int indexBehaviour, float startArea, float endArea, int nbSpriteIntoBox){
            this.indexBehaviour = indexBehaviour;
            
            this.spriteMap = new HashMap<Sprite, Float>();
            
            this.startArea = startArea;
            this.endArea = endArea;
            
            this.nbSpriteIntoBox = nbSpriteIntoBox;
        }
        
        public void runBehaviour(float deltaTime){
            
            switch(this.indexBehaviour){
                case 1:
                    this.cloudBehaviour(deltaTime);
                    return;
                case 2:
                    this.birdBehaviour(deltaTime);
                    return;
            }
            this.defaultBehaviour(deltaTime);
        }
        
        public void step(float deltaTime, float lowerX, float lowerY, float upperX, float upperY){
            this.flushSpriteOutsideBox(lowerX, lowerY, upperX, upperY);
            
            this.addSpriteInsideBox(lowerX, lowerY, upperX, upperY);
            
            this.runBehaviour(deltaTime);
        }
        
        /**
         * @return the spriteMap
         */
        public HashMap<Sprite, Float> getSpriteMap() {
            return spriteMap;
        }
        
        public void flushSpriteOutsideBox(float lowerX, float lowerY, float upperX, float upperY){
            float centerX = (upperX + lowerX) / 2;
            float centerY = (upperY + lowerY) / 2;
            float widthCanevas = (upperX - lowerX) * this.SCALEBOXREMOVESPRITES;
            float heightCanevas = (upperY - lowerY) * this.SCALEBOXREMOVESPRITES;
            
            
            lowerX = centerX - widthCanevas/2; 
            lowerY = centerY - heightCanevas/2; 
            upperX = centerX + widthCanevas/2; 
            upperY = centerY + heightCanevas/2; 
            
            List<Sprite> keys2Flush = new ArrayList();
            for(Entry<Sprite, Float> spriteEntry : this.getSpriteMap().entrySet()){
                float x = spriteEntry.getKey().getX();
                float width = spriteEntry.getKey().getWidth();
                float y = spriteEntry.getKey().getY();
                float height = spriteEntry.getKey().getHeight();
                
                if(! (x + width > lowerX / P2M && x < upperX / P2M 
                        && y + height > lowerY / P2M && y < upperY / P2M)){
                    keys2Flush.add(spriteEntry.getKey());
                }
                
            }
            
            for(Sprite key2Flush : keys2Flush){
                this.flushSprite(key2Flush);
            }
            
        }
        
        protected void flushSprite(Sprite sprite){
            this.spriteMap.remove(sprite);
        }
        
        public void addSpriteInsideBox(float lowerX, float lowerY, float upperX, float upperY){
            float centerX = (upperX + lowerX) / 2;
            float centerY = (upperY + lowerY) / 2;
            float widthCanevas = (upperX - lowerX) * this.SCALEBOXADDSPRITES;
            float heightCanevas = (upperY - lowerY) * this.SCALEBOXADDSPRITES;
            
            float maxUpperX = centerX + widthCanevas/2; 
            float maxLowerX = centerX - widthCanevas/2;   
            float maxUpperY = centerY + heightCanevas/2;
            float maxLowerY = centerY - heightCanevas/2;
            
            int spritesToAdd = this.nbSpriteIntoBox - this.spriteMap.size();
            int nbSpritesToAdd = 0;
            
            if(spritesToAdd > 0){
                float spawnAreaUpperX = Math.min(maxUpperX, this.endArea);
                float spawnAreaLowerX = Math.max(upperX, this.startArea);

                if(spawnAreaUpperX > spawnAreaLowerX){
                    nbSpritesToAdd = (int) (Math.random() * (spritesToAdd + 1));
                    
                    for(int i=0; i<nbSpritesToAdd; i++){
                        this.addSprite(true, spawnAreaLowerX, spawnAreaUpperX);
                    }
                    
                } 
            }
            
            spritesToAdd -= nbSpritesToAdd;
            
            if(spritesToAdd > 0){
                float spawnAreaUpperX = Math.min(lowerX, this.endArea);
                float spawnAreaLowerX = Math.max(maxLowerX, this.startArea);

                if(spawnAreaUpperX > spawnAreaLowerX){
                    nbSpritesToAdd = (int) (Math.random() * (spritesToAdd + 1));
                    
                    for(int i=0; i<nbSpritesToAdd; i++){
                        this.addSprite(false, spawnAreaLowerX, spawnAreaUpperX);
                    }
                    
                } 
            }
            
        }
        
        protected abstract void addSprite(boolean isRight, float startArea, float endArea);
        
        /// Behaviour Functions.
       
        
        private void defaultBehaviour(float deltaTime){
            // nothing to do.
        }
        
        private void cloudBehaviour(float deltaTime){
            for(Entry<Sprite, Float> spriteEntry : this.getSpriteMap().entrySet()){
                Sprite sprite = spriteEntry.getKey();
                float speed = spriteEntry.getValue();
                
                float newPositionX = ((sprite.getX() * P2M) + speed * deltaTime) / P2M;
                
                sprite.setPosition(newPositionX, sprite.getY());

            }
        }
        
        private void birdBehaviour(float deltaTime){
            for(Entry<Sprite, Float> spriteEntry : this.getSpriteMap().entrySet()){
                Sprite sprite = spriteEntry.getKey();
                
                if(Math.random() > 0.995){
                    spriteEntry.setValue(spriteEntry.getValue() * -1);
                }
                
                float speed = spriteEntry.getValue();
                
                float newPositionX = ((sprite.getX() * P2M) + speed * deltaTime) / P2M;
                
                sprite.setPosition(newPositionX, sprite.getY());

            }
        }

        /**
         * @return the startArea
         */
        public float getStartArea() {
            return startArea;
        }

        /**
         * @return the endArea
         */
        public float getEndArea() {
            return endArea;
        }

    }
    
    protected abstract class AnimDynamicElemLogic extends DynamicElemLogic{
        
        protected HashMap<Sprite, Float> timerAnimMap; 
        protected HashMap<Sprite, Integer> indexAnimMap; 
        
        public AnimDynamicElemLogic(int indexBehaviour, float startArea, float endArea, int nbSpriteIntoBox) {
            super(indexBehaviour, startArea, endArea, nbSpriteIntoBox);
            
            this.timerAnimMap = new HashMap<Sprite, Float>();
            this.indexAnimMap = new HashMap<Sprite, Integer>();
        }
        
        @Override
        protected void flushSprite(Sprite sprite){
            super.flushSprite(sprite);
            
            this.timerAnimMap.remove(sprite);
            this.indexAnimMap.remove(sprite);
        }
        
    }
}
