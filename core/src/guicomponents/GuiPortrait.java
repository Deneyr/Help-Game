/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.io.File;

/**
 *
 * @author Deneyr
 */
public class GuiPortrait extends GuiComponent{

    private static final Texture PORTRAITTEXT = new Texture("gui" + File.separator + "characters.png");
    
    private GuiText.ReferenceCorner refCornerWidth;
    
    public GuiPortrait(GuiText.ReferenceCorner refCornerWidth, float locX, float locY){
        this.texture = PORTRAITTEXT;
        
        this.refCornerWidth = refCornerWidth;
        
        this.location = new Vector2(locX, locY);
        
        // Part graphic
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 120, 120);
        
        Array<TextureRegion> array;
        for(TextureRegion[] listTextureRegion : tmp){
            array = new Array<TextureRegion>(listTextureRegion);
            this.listAnimations.add(new Animation(0.2f, array));
        }
        this.setCharacterPortrait(Character.GRANDMA, Emotion.DEFAULT);
    }
    
    /*@Override
    public void draw(Batch batch, Camera camera, ShapeRenderer shapeRenderer) {
        
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
              
        Sprite portrait = this.createCurrentSprite(camera); 
      
        if(portrait != null){
            
            portrait.setPosition(posX, posY);
        
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.rect(posX - portrait.getWidth() * 0.1f, posY - portrait.getHeight() * 0.1f, portrait.getWidth() * 1.2f, portrait.getWidth() * 1.2f);
            
            batch.setColor(portrait.getColor());
            batch.draw(portrait, 
                        portrait.getX(), portrait.getY(),
                        portrait.getOriginX(), portrait.getOriginY(),
                        portrait.getWidth(),portrait.getHeight(),
                        portrait.getScaleX(),portrait.getScaleY(),
                        portrait.getRotation());
        }
        
    }*/
    
    public void setCharacterPortrait(Character character, Emotion emotion){
        this.changeAnimation(character.getValue(), true, emotion.getValue());
    }

    @Override
    public void drawBatch(Camera camera, Batch batch) {
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
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
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
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
        }
    }
    
    public enum Character{
        NONE(-1),
        GRANDMA(0),
        TEMERI(1),
        PRIDE(2);
        
        private final int value;

        private Character(int value) {
            this.value = value;
        }  
        
        public int getValue() {
            return this.value;
        }
    }
   
    
    public enum Emotion{
        DEFAULT(0),
        HAPPY(1),
        ANGRY(2),
        SORROW(3);
        
        private final int value;

        private Emotion(int value) {
            this.value = value;
        }  
        
        public int getValue() {
            return this.value;
        }
    }
}
