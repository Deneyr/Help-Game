/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Object2DEditorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class GuiEditorBlock extends GuiComponent{

    private static final String TEXT = "gui/BulleDialogue3.png";
    
    private Map<GuiEditorItem, Object2DEditorFactory> mapModelGuiComponents = new HashMap<GuiEditorItem, Object2DEditorFactory>();
    private List<GuiEditorItem> editorItemsList = new ArrayList<GuiEditorItem>();
    
    private int startLine;
    
    private int maxLine;
    
    private volatile List<GuiEditorItem> itemsToDraw;
    
    
    public GuiEditorBlock(){
        super();
        // Part graphic
        this.assignTextures();
        
        this.startLine = 0;
        
        this.maxLine = 1;
        
        this.itemsToDraw = new ArrayList<GuiEditorItem>();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(TEXT, this);    
    }
    
    @Override
    public void drawBatch(Camera camera, Batch batch) {
        Sprite sprite = this.createCurrentSprite(camera);
        
        float totalWidth = sprite.getWidth();
        float totalHeight = sprite.getHeight();
        float width = sprite.getHeight() / 4;
        
        int nbItemLine = (int) ((totalWidth - 120) / width);
        int nbItemRow = (int) ((totalHeight - 50) / width);
        
        
        int i = 0;
        int j = 0;
        
        this.itemsToDraw.clear();
        for(GuiEditorItem editorItem : this.editorItemsList){           
            
            if(i >= nbItemLine){
                i = 0;
                j++;
            }
            
            if(j >= this.startLine && (j - this.startLine) < nbItemRow){            
                Sprite spriteItem = editorItem.getSprite();

                spriteItem.setScale(width / spriteItem.getRegionWidth(), width / spriteItem.getRegionHeight());

                editorItem.setLocation(sprite.getX() + 70 + (width) * i, sprite.getY() + sprite.getHeight() - 70 - (width) * (j - this.startLine));
                
                this.itemsToDraw.add(editorItem);               
            }
            i++;
        }
        this.maxLine = j + 1;
        
        if(sprite != null){
            batch.setColor(sprite.getColor());
            batch.draw(sprite, 
                sprite.getX(), sprite.getY(),
                sprite.getOriginX(), sprite.getOriginY(),
                sprite.getWidth(),sprite.getHeight(),
                sprite.getScaleX(),sprite.getScaleY(),
                sprite.getRotation());
        }
        
        for(GuiEditorItem editorItem : this.itemsToDraw){
            editorItem.drawBatch(camera, batch);
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
      
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        for(GuiEditorItem editorItem : this.itemsToDraw){
            Sprite sprite = editorItem.getSprite();
            
            Vector2 locationItem = editorItem.getLocation();
            float width = sprite.getWidth() * sprite.getScaleX();
            float height = sprite.getHeight() * sprite.getScaleY();
            
            Rectangle rectangle = new Rectangle(locationItem.x - width/2, locationItem.y - height/2, width, height);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            shapeRenderer.end();
 
        }
    }
    
    public void AddObject2DAsComponent(Object2DEditorFactory factory){
        GuiEditorItem editorItem = new GuiEditorItem(factory.getTemplate());
        
        this.mapModelGuiComponents.put(editorItem, factory);
        this.editorItemsList.add(editorItem);
    }
    
    @Override
    public void dispose(){
        for(GuiEditorItem item : this.editorItemsList){
            item.dispose();
        }
        
        this.editorItemsList.clear();
        this.mapModelGuiComponents.clear();
        
        super.dispose();
    }
    
    public void scroll(int amount){
        
        if(this.startLine + amount < 0){
            this.startLine = 0;
        }else if(this.startLine + amount > this.maxLine - 1){
            this.startLine = this.maxLine - 1;
        }else{
            this.startLine += amount;
        }      
    }
    
    public Object2DEditorFactory getFactoryFrom(GuiEditorItem item){
        return this.mapModelGuiComponents.get(item);
    }
    
    public GuiEditorItem getSelectedGuiComponentAt(float screenX, float screenY){
        Vector2 point = new Vector2(screenX, screenY);
        
        for(GuiEditorItem editorItem : this.itemsToDraw){
            Sprite sprite = editorItem.getSprite();
            
            Vector2 locationItem = editorItem.getLocation();
            float width = sprite.getWidth() * sprite.getScaleX();
            float height = sprite.getHeight() * sprite.getScaleY();
            
            Rectangle rectangle = new Rectangle(locationItem.x - width/2, locationItem.y - height/2, width, height);
            if(rectangle.contains(point)){
                return editorItem;
            }
        }
        return null;
    }
    
}
