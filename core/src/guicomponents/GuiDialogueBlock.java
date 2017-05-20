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
    
    private boolean dialogueFinished;
    
    public GuiDialogueBlock(List<Dialogue> dialogues){
        super();
        
        this.dialogues = new ArrayList(dialogues);
        
        this.rightPortrait = new GuiPortrait(ReferenceCorner.RIGHT, 1f, -1f);
        this.leftPortrait = new GuiPortrait(ReferenceCorner.LEFT, -1f, -1f);
        
        this.textBlock = new GuiTextBlock(0.1f, 0f, -0.9f, 1.6f, 0.6f);
        
        this.indexCurrentDialogue = 0;
        this.indexCurrentReply = 0;
        
        this.dialogueFinished = false;
    }
    
    public void setCurrentDialogue(int index){
        this.indexCurrentDialogue = index;
        
        this.dialogueFinished = false;
        this.setCurrentReply(0);
    }
    
    public void setCurrentReply(int currentReply){
        Dialogue currentDialogue = this.dialogues.get(this.indexCurrentDialogue);

        if(currentDialogue.getNbReply() > currentReply){
            this.indexCurrentReply = currentReply;

            this.textBlock.setText(currentDialogue.get(this.indexCurrentReply));
            this.leftPortrait.setCharacterPortrait(currentDialogue.getCharacterLeft(this.indexCurrentReply), currentDialogue.getEmotionLeft(this.indexCurrentReply));
            this.rightPortrait.setCharacterPortrait(currentDialogue.getCharacterRight(this.indexCurrentReply), currentDialogue.getEmotionRight(this.indexCurrentReply));
        }
    }
    
    @Override
    public void updateLogic(float deltaTime){
        this.textBlock.updateLogic(deltaTime);
        
        Dialogue currentDialogue = this.dialogues.get(this.indexCurrentDialogue);
        
        if(this.textBlock.AllTextPassed()){
            if(currentDialogue.getNbReply() > this.indexCurrentReply){
                if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                    this.indexCurrentReply++;
                    this.setCurrentReply(this.indexCurrentReply);
                }
            }else{
                this.dialogueFinished = true;
            }
        }
        
    }
    
     /**
     * @return the hasDialogueFinished
     */
    public boolean IsDialogueFinished() {
        return dialogueFinished;
    }

    @Override
    public void drawBatch(Camera camera, Batch batch) {
        this.textBlock.drawBatch(camera, batch);

        this.leftPortrait.drawBatch(camera, batch);
        this.rightPortrait.drawBatch(camera, batch);
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        this.textBlock.drawShapeRenderer(camera, shapeRenderer);

        this.leftPortrait.drawShapeRenderer(camera, shapeRenderer);
        this.rightPortrait.drawShapeRenderer(camera, shapeRenderer);
    }
}
