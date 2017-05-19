/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.io.File;

/**
 *
 * @author Deneyr
 */
public class GuiPortrait extends GuiComponent{

    private static final Texture PORTRAITTEXT = new Texture("gui" + File.separator + "characters.png");
    
    public GuiPortrait(float locX, float locY){
        this.texture = PORTRAITTEXT;
        
        this.location = new Vector2(locX, locY);
    }
    
    @Override
    public void draw(Batch batch, Camera camera, ShapeRenderer shapeRenderer) {
        
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
              
        Sprite portrait = this.createCurrentSprite(); 
      
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
        
    }
    
    public void setCharacterPortrait(Character character, Emotion emotion){
        this.changeAnimation(character.getValue(), true, emotion.getValue());
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
