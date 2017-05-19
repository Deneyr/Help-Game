/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author Deneyr
 */
public class GuiTextBlock extends GuiText{
    
    private int maxNbLine;
    
    private float width;
    private float height;
    
    private float period2Add;
    private float timeElapsed;
    
    private String finalText;
    
    public GuiTextBlock(float period2Add, float locX, float locY, float width, float height) {
        super("", 20, ReferenceCorner.MIDDLE, ReferenceCorner.MIDDLE, locX, locY);
        
        this.maxNbLine = 3; // Math.max((int) (height / this.bitmapFont.getCapHeight()), 1);
        
        this.width = width;
        this.height = height;
        
        this.period2Add = period2Add;
        this.timeElapsed = 0;
        
        this.finalText = new String();
    }
    
    @Override
    public void updateLogic(float deltaTime){
        this.timeElapsed += deltaTime;
        
        if(this.timeElapsed > this.period2Add){
            this.timeElapsed = 0;
            
            if(this.text.length() < this.finalText.length()){
            
                int currentIndex2Add = this.text.length();
                super.appendChar(this.finalText.charAt(currentIndex2Add), maxNbLine);

                // Sound
            }
        }
        
        if(this.text.length() < this.finalText.length()){
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                super.setText(this.finalText.substring(0, this.finalText.length() - 2), this.maxNbLine);
            }
            // Sound
        }
    }
    
    public void setText(String text) {
        this.timeElapsed = 0;
        this.finalText = text;
    }
    
    @Override
    public void draw(Batch batch, Camera camera, ShapeRenderer shapeRenderer) {
        
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(posX, posY, this.width * camera.viewportWidth * 1.1f, this.height * camera.viewportHeight * 1.1f);
        
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(posX, posY, this.width * camera.viewportWidth, this.height * camera.viewportHeight);
        
        super.draw(batch, camera, shapeRenderer);
    }
    
    public boolean AllTextPassed(){
        return this.text.length() == this.finalText.length();
    }
}
