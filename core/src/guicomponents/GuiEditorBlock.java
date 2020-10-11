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
    
    private int startLine;
    
    
    public GuiEditorBlock(){
        super();
        // Part graphic
        this.assignTextures();
        
        this.startLine = 0;
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
        
        List<GuiEditorItem> itemsToDraw = new ArrayList<GuiEditorItem>();
        for(GuiEditorItem editorItem : this.mapModelGuiComponents.keySet()){           
            
            if(j >= this.startLine && (j - this.startLine) < nbItemRow){            
                Sprite spriteItem = editorItem.getSprite();

                spriteItem.setScale(width / spriteItem.getRegionWidth(), width / spriteItem.getRegionHeight());

                editorItem.setLocation(sprite.getX() + 70 + (width) * i, sprite.getY() + sprite.getHeight() - 70 - (width) * j);
                i++;
                if(i >= nbItemLine){
                    i = 0;
                    j++;
                }
                
                itemsToDraw.add(editorItem);
            }
        }
        
        if(sprite != null){
            batch.setColor(sprite.getColor());
            batch.draw(sprite, 
                sprite.getX(), sprite.getY(),
                sprite.getOriginX(), sprite.getOriginY(),
                sprite.getWidth(),sprite.getHeight(),
                sprite.getScaleX(),sprite.getScaleY(),
                sprite.getRotation());
        }
        
        for(GuiEditorItem editorItem : itemsToDraw){
            editorItem.drawBatch(camera, batch);
        }
    }
    
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        
    }
    
    public void AddObject2DAsComponent(Object2DEditorFactory factory){
        this.mapModelGuiComponents.put(new GuiEditorItem(factory.getTemplate()), factory);
    }
    
    @Override
    public void dispose(){
        for(GuiEditorItem item : this.mapModelGuiComponents.keySet()){
            item.dispose();
        }
        
        this.mapModelGuiComponents.clear();
        
        super.dispose();
    }
    
}
