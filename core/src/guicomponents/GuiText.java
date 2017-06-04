/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.io.File;

/**
 *
 * @author Deneyr
 */
public class GuiText extends GuiComponent{

    protected static FreeTypeFontGenerator staticGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts" + File.separator + "helpFont.ttf"));
    
    protected BitmapFont bitmapFont;
    
    protected StringBuffer text;
    
    protected ReferenceCorner refCornerWidth;
    protected ReferenceCorner refCornerHeight;
    
    protected GlyphLayout glyphLayout;
    
    public GuiText(String str, int size, ReferenceCorner refCornerWidth, ReferenceCorner refCornerHeight, float locX, float locY){
        super();
        
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameters.size = size;
        fontParameters.color = Color.LIGHT_GRAY;
        fontParameters.borderColor = Color.DARK_GRAY;
        fontParameters.borderWidth = 2;
        
        this.bitmapFont = staticGenerator.generateFont(fontParameters);
        
        this.text = new StringBuffer(str);
        
        this.refCornerWidth = refCornerWidth;
        this.refCornerHeight = refCornerHeight;
        
        this.glyphLayout = new GlyphLayout();
        this.glyphLayout.setText(this.bitmapFont, this.text);
        
        this.location = new Vector2(locX, locY);
    }
    
    /*@Override
    public void draw(Batch batch, Camera camera, ShapeRenderer shapeRenderer) {
        
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
            case LEFT:
                
                posY -= this.glyphLayout.height;
                
                break;
        }
        
        this.bitmapFont.draw(batch, this.glyphLayout, posX, posY);
    }*/
    
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = new StringBuffer(text);
        
        this.glyphLayout.setText(this.bitmapFont, this.text);
    }
    
    public void appendText(String text) {
        this.text.append(text);
        
        this.glyphLayout.setText(this.bitmapFont, this.text);
    }
    
    public void appendChar(char character) {
        this.text.append(character);
        
        this.glyphLayout.setText(this.bitmapFont, this.text);
    }

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
            case LEFT:
                
                posY -= this.glyphLayout.height;
                
                break;
        }
        
        this.bitmapFont.draw(batch, this.glyphLayout, posX, posY);
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        // nothing to do
    }
           
    public enum ReferenceCorner{
        LEFT,
        MIDDLE,
        RIGHT
    }
    
    @Override
    public void dispose(){
        if(this.bitmapFont != null){
            this.bitmapFont.dispose();
        }
        
        super.dispose();
    }
}
