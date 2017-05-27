/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import guicomponents.CinematicManager.CinematicState;
import guicomponents.GuiText.ReferenceCorner;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class GuiDialogueBlock extends GuiComponent{

    
    
    private GuiPortrait leftPortrait;
    private GuiPortrait rightPortrait;
    
    private GuiTextBlock textBlock;
    
    private List<Dialogue> dialogues;
    
    private int indexCurrentDialogue;
    
    private int indexCurrentReply;
    
    private CinematicState dialogueState;
    
    public GuiDialogueBlock(List<Dialogue> dialogues){
        super();
        
        this.dialogues = new ArrayList(dialogues);
        
        this.rightPortrait = new GuiPortrait(ReferenceCorner.RIGHT, 1f, -1f);
        this.leftPortrait = new GuiPortrait(ReferenceCorner.LEFT, -1f, -1f);
        
        this.textBlock = new GuiTextBlock(0.1f, 0f, -0.9f, 1.6f, 0.6f);
        
        this.indexCurrentDialogue = 0;
        this.indexCurrentReply = 0;
        
        this.dialogueState = CinematicState.STOP;
    }
    
    public void setCurrentDialogue(int index){
        this.indexCurrentDialogue = index;
        
        this.dialogueState = CinematicState.START;
        this.setCurrentReply(0);
    }
    
    public void setCurrentReply(int currentReply){
        Dialogue currentDialogue = this.dialogues.get(this.getIndexCurrentDialogue());

        if(currentDialogue.getNbReply() > currentReply){
            this.indexCurrentReply = currentReply;

            this.textBlock.setText(currentDialogue.get(this.indexCurrentReply));
            this.leftPortrait.setCharacterPortrait(currentDialogue.getCharacterLeft(this.indexCurrentReply), currentDialogue.getEmotionLeft(this.indexCurrentReply));
            this.rightPortrait.setCharacterPortrait(currentDialogue.getCharacterRight(this.indexCurrentReply), currentDialogue.getEmotionRight(this.indexCurrentReply));
            
            int mainCharacter = currentDialogue.getMainCharacter(this.indexCurrentReply);
            if(mainCharacter >= 0){
                switch(mainCharacter){
                    case 0:
                        this.leftPortrait.setIsMainCharacter(true);
                        this.rightPortrait.setIsMainCharacter(false);
                        break;
                    case 1:
                        this.leftPortrait.setIsMainCharacter(false);
                        this.rightPortrait.setIsMainCharacter(true);
                        break;
                }
            }else{
                this.leftPortrait.setIsMainCharacter(true);
                this.rightPortrait.setIsMainCharacter(true);
            }
            
            if(!currentDialogue.getCharacterLeft(this.indexCurrentReply).isCharacter()
                    && !currentDialogue.getCharacterRight(this.indexCurrentReply).isCharacter()){
                this.textBlock.setIsDialogue(false);
            }else{
                this.textBlock.setIsDialogue(true);
            }
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        
        if(this.dialogueState == CinematicState.STOP){
            return;
        }
      
        if(this.dialogueState == CinematicState.END){
            this.dialogueState = CinematicState.STOP;
        }
        
        if(this.dialogueState == CinematicState.START){
            this.dialogueState = CinematicState.RUNNING;
        }

        this.textBlock.updateLogic(deltaTime);
        
        Dialogue currentDialogue = this.dialogues.get(this.getIndexCurrentDialogue());
        
        if(this.textBlock.AllTextPassed()){
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                if(currentDialogue.getNbReply() > this.indexCurrentReply){
                    this.indexCurrentReply++;
                    this.setCurrentReply(this.indexCurrentReply);
                }
                if(currentDialogue.getNbReply() == this.indexCurrentReply){
                    if(this.dialogueState != CinematicState.STOP){
                        this.dialogueState = CinematicState.END;
                    }
                }
            }
        }
        
    }
    
    /**
     * @return the dialogueState
     */
    public CinematicState getDialogueState() {
        return dialogueState;
    }

    @Override
    public void drawBatch(Camera camera, Batch batch) {
        if(this.dialogueState == CinematicState.STOP){
            return;
        }
        
        this.textBlock.drawBatch(camera, batch);

        this.leftPortrait.drawBatch(camera, batch);
        this.rightPortrait.drawBatch(camera, batch);
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        if(this.dialogueState == CinematicState.STOP){
            return;
        }
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(1f, 1f, 1f, 0.6f);
        shapeRenderer.rect(camera.position.x - camera.viewportWidth * 1.1f / 2, camera.position.y - camera.viewportHeight * 1.1f / 2, camera.viewportWidth * 1.1f, camera.viewportHeight * 1.1f);
        
        this.textBlock.drawShapeRenderer(camera, shapeRenderer);

        this.leftPortrait.drawShapeRenderer(camera, shapeRenderer);
        this.rightPortrait.drawShapeRenderer(camera, shapeRenderer);
    }

    /**
     * @return the indexCurrentDialogue
     */
    public int getIndexCurrentDialogue() {
        return indexCurrentDialogue;
    }
}
