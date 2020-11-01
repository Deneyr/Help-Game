/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.HelpGame;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DEditorFactory;
import guicomponents.GuiComponent;
import guicomponents.GuiEditorBlock;
import guicomponents.GuiEditorItem;
import guicomponents.GuiText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deneyr
 */
public class EditorMenuManager extends MenuManager{
    
    private GuiEditorBlock canevas;
    
    private GuiEditorItem selectedItem;
    
    private float positionCursorX;
    private float positionCursorY;
    
    private Map<String, Object2DEditorFactory> mapIDToFactory; 
    
    public EditorMenuManager(){
        super();
        
        this.canevas = null;
        
        this.selectedItem = null;
        
        positionCursorX = 0;
        positionCursorY = 0;
        
        this.mapIDToFactory = new HashMap<String, Object2DEditorFactory>();
    }
    
    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location){      
        switch(type){
            case LVLSTART :
                this.onStart();
                break;
        }
        
    }
    
    private void onStart(){
        super.dispose();
        
        this.canevas.setLocation(-1f, -2f);
        this.addGuiComponent(this.canevas, 0);
        
        Animation animation = new Animation(this.canevas, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0f, 0f, 1f);
        animation.setColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1));      
        this.addAnimation(animation);
        
        animation = new Animation(this.canevas, Animation.RunType.NORMAL, Interpolation.InterpolationType.QUADRA_INC, 0.5f, 0f, 0.5f);
        animation.setPositionAnimation(new Vector2(-1f, -2f), new Vector2(-1f, -1f));      
        this.addAnimation(animation);
    }
    
    
    @Override
    public void step(float deltaTime)
    {
        super.step(deltaTime);
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL)){
            this.notifyGameEventListeners(EventType.EDITORDELETETOUCHEDOBJ, "", Vector2.Zero);
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            this.notifyGameEventListeners(EventType.EDITORROTATIONRIGHT, String.valueOf(deltaTime), Vector2.Zero);
        }else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            this.notifyGameEventListeners(EventType.EDITORROTATIONLEFT, String.valueOf(deltaTime), Vector2.Zero);
        }
    }
    
    @Override
    public void drawBatch(Camera camera, Batch batch){
        super.drawBatch(camera, batch);
        
        if(this.selectedItem != null){
            
            Sprite sprite = this.selectedItem.createNewSprite();
            
            sprite.setPosition(this.positionCursorX - sprite.getWidth() / 2, this.positionCursorY - sprite.getHeight() / 2);
            
            batch.setColor(sprite.getColor());
            batch.draw(sprite , 
                sprite.getX(), sprite.getY(),
                sprite.getOriginX(), sprite.getOriginY(),
                sprite.getWidth(), sprite.getHeight(),
                sprite.getScaleX(), sprite.getScaleY(),
                sprite.getRotation());
        }
    }
    
    @Override
    public void dispose(){
        super.dispose();
        
        this.mapListAnimations.clear();
    }

    public void AddObject2DAsComponent(Object2DEditorFactory factory){
        this.canevas.AddObject2DAsComponent(factory);
        
        this.mapIDToFactory.put(factory.ID, factory);
    }
    
    /**
     * @param canevas the canevas to set
     */
    public void setCanevas(GuiEditorBlock canevas) {
        this.canevas = canevas;
    }
    
    public Object2DEditorFactory getFactoryFromID(String id){
        return this.mapIDToFactory.get(id);
    }
    
    @Override
    public void onTouchDown(float positionX, float positionY, int pointer, int button){
        if(button == 0){
            GuiEditorItem item = this.canevas.getSelectedGuiComponentAt(positionX, positionY);
            
            if(item != null){                
                this.selectedItem = item;
                this.notifyGameEventListeners(EventType.EDITORSELECTFACTORY, this.canevas.getFactoryFrom(this.selectedItem).ID, new Vector2(this.positionCursorX, this.positionCursorY));
            }else if(this.selectedItem != null){
                this.notifyGameEventListeners(EventType.EDITORADDOBJECT, this.canevas.getFactoryFrom(this.selectedItem).ID, new Vector2(this.positionCursorX, this.positionCursorY));
            }
        }else if(button == 1){
            this.selectedItem = null;
            this.notifyGameEventListeners(EventType.EDITORUNSELECTFACTORY, "", new Vector2(positionX, positionY));
        }
    }
    
    @Override
    public void onTouchUp(float positionX, float positionY, int pointer, int button){
        
    }

    @Override
    public void onTouchDragged(float positionX, float positionY, int pointer){
        
    }

    @Override
    public void onMouseMoved(float positionX, float positionY){
        this.positionCursorX = positionX;
        this.positionCursorY = positionY;
    }
    
    @Override
    public void onScrolled(int amount) {
        this.canevas.scroll(amount);
    }
    
    
}
