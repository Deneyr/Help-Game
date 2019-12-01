/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class GuiPortrait extends GuiComponent{

    private static final String PORTRAITTEXT = "gui/Characters200x300.png";
    
    private GuiText.ReferenceCorner refCornerWidth;
    
    private boolean isMainCharacter;
    
    private boolean isRightPortrait;
    
    public GuiPortrait(GuiText.ReferenceCorner refCornerWidth, float locX, float locY, boolean isRightPortrait){     
        this.refCornerWidth = refCornerWidth;
        
        this.location = new Vector2(locX, locY);
        
        this.isMainCharacter = true;
        
        this.isRightPortrait = isRightPortrait;
        
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(PORTRAITTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 200, 300);
        
            Array<TextureRegion> array;
            for(TextureRegion[] listTextureRegion : tmp){
                array = new Array<TextureRegion>(listTextureRegion);
                this.listAnimations.add(new Animation(1f, array));
            }
            
            this.setCharacterPortrait(Character.GRANDMA, Emotion.DEFAULT);
        }
    }
    
    public Sprite createCurrentSprite(Camera camera){
        Sprite sprite;
        
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        if(this.currentAnimation < 0){
            sprite = null;
        }else{
            TextureRegion region = this.listAnimations.get(this.currentAnimation).getKeyFrame(this.animationTime);
            sprite = new Sprite(region);  
        }
        
        
        if(sprite != null){
            
            if(!this.spriteColor.equals(sprite.getColor())){
                sprite.setColor(this.spriteColor);
            }
            
            if(this.spriteRotation != sprite.getRotation()){
                sprite.setColor(this.spriteColor);
            }
            
            if(this.spriteScale.x != sprite.getScaleX() || this.spriteScale.y != sprite.getScaleX()){
                sprite.setScale(this.spriteScale.x, this.spriteScale.y);
            }
            
            sprite.setPosition(posX, posY);
        }
        
        return sprite;
    }
    
    
    
    public void setCharacterPortrait(Character character, Emotion emotion){
        this.changeAnimation(character.getValue(), true, emotion.getValue());
    }

    @Override
    public void drawBatch(Camera camera, Batch batch) {
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
              
        Sprite portrait = this.createCurrentSprite(camera); 
        
        if(portrait != null){
            
            portrait.setFlip(!this.isRightPortrait, false);
            
            if(!this.isMainCharacter){
                portrait.setAlpha(portrait.getColor().a * 0.5f);
            }
            
            switch(this.refCornerWidth){
                case MIDDLE:

                    posX -= portrait.getWidth() / 2;

                    break;
                case RIGHT:

                    posX -= portrait.getWidth();

                    break;
            }
            
            portrait.setPosition(posX, posY);
            
            batch.setColor(portrait.getColor());
            batch.draw(portrait, 
                        portrait.getX(), portrait.getY(),
                        portrait.getOriginX(), portrait.getOriginY(),
                        portrait.getWidth(),portrait.getHeight(),
                        portrait.getScaleX(),portrait.getScaleY(),
                        portrait.getRotation());
        }
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        /*float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
              
        Sprite portrait = this.createCurrentSprite(camera); 
      
        if(portrait != null){
            
            switch(this.refCornerWidth){
                case MIDDLE:

                    posX -= portrait.getWidth() / 2;

                    break;
                case RIGHT:

                    posX -= portrait.getWidth();

                    break;
            }
            
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.rect(posX - portrait.getWidth() * 0.03f, posY - portrait.getHeight() * 0.03f, portrait.getWidth() * 1.06f, portrait.getHeight() * 1.06f);      
        }*/
    }
    
    public enum Character{
        NONE(-1, "Unamed"),
        GRANDMA(0, "Grand-mère"),
        TEMERI(1, "Temeri"),
        PRIDE(2, "Voleur"),
        THIEF_KNIFE(3, "Voleur au couteau"),
        THIEF_BAT(4, "Voleur à la batte"),
        SPEAKER(5, "Haut-Parleur");
        
        private final int value;
        
        private final String name;

        private Character(int value, String name) {
            this.value = value;
            
            this.name = name;
        }  
        
        public int getValue() {
            return this.value;
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean isCharacter(){
            return this != NONE;
        }
    }
   
    
    public enum Emotion{
        DEFAULT(0),
        HAPPY(1),
        ANGRY(2),
        SORROW(0);
        
        private final int value;

        private Emotion(int value) {
            this.value = value;
        }  
        
        public int getValue() {
            return this.value;
        }
    }

    /**
     * @return the isMainCharacter
     */
    public boolean isIsMainCharacter() {
        return this.isMainCharacter;
    }

    /**
     * @param isMainCharacter the isMainCharacter to set
     */
    public void setIsMainCharacter(boolean isMainCharacter) {
        this.isMainCharacter = isMainCharacter;
    }
}
