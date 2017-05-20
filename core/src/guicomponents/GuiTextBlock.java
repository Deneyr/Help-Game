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
    
    private float width;
    private float height;
    
    private float period2Add;
    private float timeElapsed;
    
    private String finalText;
    
    public GuiTextBlock(float period2Add, float locX, float locY, float width, float height) {
        super("", 20, ReferenceCorner.LEFT, ReferenceCorner.LEFT, locX, locY);
        
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
                super.appendChar(this.finalText.charAt(currentIndex2Add));

                // Sound
            }
        }
        
        if(this.text.length() < this.finalText.length()){
            if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                int currentIndex2Add = this.text.length();
                super.appendChar(this.finalText.charAt(currentIndex2Add));
            }
            // Sound
        }
    }
    
    @Override
    public void setText(String text) {
        this.timeElapsed = 0;
        this.finalText = text;
        super.setText("");
    }
    
    /*@Override
    public void draw(Batch batch, Camera camera, ShapeRenderer shapeRenderer) {
        
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(posX, posY, this.width * camera.viewportWidth * 1.1f, this.height * camera.viewportHeight * 1.1f);
        
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(posX, posY, this.width * camera.viewportWidth, this.height * camera.viewportHeight);
        
        super.draw(batch, camera, shapeRenderer);
    }*/

    @Override
    public void drawBatch(Camera camera, Batch batch) {
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        switch(this.refCornerWidth){
            case MIDDLE:
                
                posX -= this.glyphLayout.width / 2;
                
                break;
            case RIGHT:
                
                posX -= this.glyphLayout.width;
                
                break;
        }
        
        switch(this.refCornerHeight){
            case MIDDLE:
                
                posY -= this.glyphLayout.height / 2;
                
                break;
            case RIGHT:
                
                posY -= this.glyphLayout.height;
                
                break;
        }
        
        posX -= this.width * camera.viewportWidth * 0.7 / 4;
        posY += this.height * camera.viewportHeight * 1.6 / 4;
        
        this.bitmapFont.draw(batch, this.glyphLayout, posX, posY);
    }
    
    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        posX -= this.width * camera.viewportWidth / 4;
        
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(posX, posY, this.width * camera.viewportWidth / 2, this.height * camera.viewportHeight / 2);
    }
    
    public boolean AllTextPassed(){
        return this.text.length() == this.finalText.length();
    }
}
