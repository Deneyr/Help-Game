/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Object2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class GuiDialogueBlock extends Object2D{
    
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
        
        this.rightPortrait = new GuiPortrait(0.2f, 0.2f);
        this.leftPortrait = new GuiPortrait(0.8f, 0.2f);
        
        this.textBlock = new GuiTextBlock(0.5f, 0.5f, 0.2f, 0.5f, 0.2f);
        
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
        
        if(currentDialogue.getNbReply() < currentReply){
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
            if(currentDialogue.getNbReply() < this.indexCurrentReply){
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
}
