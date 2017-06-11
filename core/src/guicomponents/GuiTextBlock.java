/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import static guicomponents.GuiText.staticGenerator;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class GuiTextBlock extends GuiText{
  
    private static final String DIALOGUETEXT = "gui/BulleDialogue524x178.png";
    
    private float width;
    private float height;
    
    private float period2Add;
    private float timeElapsed;
    
    private String finalText;
    
    private TextBlockState blockState;
    
    protected BitmapFont bitmapFontName;
    protected GlyphLayout glyphLayoutName;
    
    public GuiTextBlock(float period2Add, float locX, float locY, float width, float height) {
        super("", 18, ReferenceCorner.LEFT, ReferenceCorner.LEFT, locX, locY, 1);
        
        this.width = width;
        this.height = height;
        
        this.period2Add = period2Add;
        this.timeElapsed = 0;
        
        this.finalText = new String();
        
        this.blockState = TextBlockState.CENTER;
        
        // Part graphic.
        this.assignTextures();
        
        // Part name
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameters.size = 15;
        fontParameters.color = Color.WHITE;
        fontParameters.borderColor = Color.LIGHT_GRAY;
        fontParameters.borderWidth = 1;
        
        this.bitmapFontName = staticGenerator.generateFont(fontParameters);
        
        this.glyphLayoutName = new GlyphLayout();
    }
    
    public void setNameText(String name){    
        this.glyphLayoutName.setText(this.bitmapFontName, name);
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(DIALOGUETEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 524, 178);
        
            Array<TextureRegion> array;
            for(TextureRegion[] listTextureRegion : tmp){
                array = new Array<TextureRegion>(listTextureRegion);
                this.listAnimations.add(new Animation(1f, array));
            }

            this.changeAnimation(0, true);
        }
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
        
        Sprite bubble = this.createCurrentSprite();
        
        if(bubble != null){
            float bubblePosX = posX - bubble.getWidth() / 2;
            float bubblePosY = posY - bubble.getHeight() / 2;
            
            posX -= bubble.getWidth() * 0.8 / 2;
            posY -= bubble.getHeight() * 0.8 / 2 - bubble.getHeight() * 0.55;
            
            boolean drawCharaName = true;
            switch(this.blockState){
                case RIGHT:
                    //this.changeAnimation(1, true);
                    bubble.setFlip(true, false);
                    
                    posX += camera.viewportWidth * 0.06;
                    bubblePosX += camera.viewportWidth * 0.06;
                    break;
                case LEFT:
                    //this.changeAnimation(1, true);
                    
                    posX -= camera.viewportWidth * 0.06;
                    bubblePosX -= camera.viewportWidth * 0.06;
                    break;
                case CENTER:
                    //this.changeAnimation(0, true);
                    drawCharaName = false;
                    break;
            }
            
            bubble.setPosition(bubblePosX, bubblePosY);
            
            batch.setColor(bubble.getColor());
            batch.draw(bubble, 
                        bubble.getX(), bubble.getY(),
                        bubble.getOriginX(), bubble.getOriginY(),
                        bubble.getWidth(),bubble.getHeight(),
                        bubble.getScaleX(),bubble.getScaleY(),
                        bubble.getRotation());
         
            if(drawCharaName){
                this.bitmapFontName.draw(batch, this.glyphLayoutName, bubblePosX + bubble.getWidth() / 2 - this.glyphLayoutName.width / 2, posY + bubble.getHeight() * 0.28f);
            }
        }
        
        this.bitmapFont.draw(batch, this.glyphLayout, posX, posY);
    }
    
    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        /*float posX = camera.position.x + this.getLocation().x * camera.viewportWidth / 2;
        float posY = camera.position.y + this.getLocation().y * camera.viewportHeight / 2;
        
        posX -= this.width * camera.viewportWidth / 4;
        
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(posX, posY, this.width * camera.viewportWidth / 2, this.height * camera.viewportHeight / 2);*/
    }
    
    public boolean AllTextPassed(){
        return this.text.length() == this.finalText.length();
    }
    
    /**
     * @param blockState the block state.
     */
    public void setTextBlockState(TextBlockState blockState) {
        this.blockState = blockState;
        switch(this.blockState){
            case RIGHT:
                this.changeAnimation(1, true);
                break;
            case LEFT:
                this.changeAnimation(1, true);
                break;
            case CENTER:
                this.changeAnimation(0, true);
                break;
        }
    }
    
    public enum TextBlockState{
        CENTER,
        RIGHT,
        LEFT
    }
}
