/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Character2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Deneyr
 */
public class CinematicManager {
  
    private String id;
    
    private CinematicState cinematicState;
    
    private float currentTime;
    
    private final List<CharacterTimeline> charactersTimeline;
    
    private TreeMap<Float, Integer> dialogueTimeline;
    private Float currentKeyDialogue;
    // Part dialogue
    private GuiDialogueBlock dialogueBlock;
    
    public CinematicManager(String id, List<Dialogue> dialogues){
        this.id = id;
        
        this.currentTime = 0;
        
        this.cinematicState = CinematicState.STOP;
        
        this.charactersTimeline = new ArrayList<CharacterTimeline>();
        
        this.dialogueTimeline = new TreeMap<Float, Integer>();
        
        this.currentKeyDialogue = null;
        
        this.dialogueBlock = new GuiDialogueBlock(dialogues);
    }
    
    public void addCharacterTimeline(CharacterTimeline characterTimeline){
        this.charactersTimeline.add(characterTimeline);
    }
    
    public void addDialogueTimeline(float time, int indexDialogue){
        this.dialogueTimeline.put(time, indexDialogue);
    }
    
    public void reset(){

        this.currentTime = 0;
        this.cinematicState = CinematicState.START;
        
        this.currentKeyDialogue = null;
        for(CharacterTimeline timeline : this.charactersTimeline){
            timeline.reset();
            timeline.getCharacter().resetSpeed();
        }
    }
    
    public void updateLogic(float deltaTime){
        if(this.getCinematicState() == CinematicState.STOP){
            return;
        }
        
        if(this.getCinematicState() == CinematicState.END){
            
            for(CharacterTimeline timeline : this.charactersTimeline){
                timeline.getCharacter().isCinematicEntity(false);
            }
            this.cinematicState = CinematicState.STOP;
        }
        
        if(this.getCinematicState() == CinematicState.START){
            this.cinematicState = CinematicState.RUNNING;
            for(CharacterTimeline timeline : this.charactersTimeline){
                timeline.getCharacter().isCinematicEntity(true);
            }
        }

        int nextDialogue = this.getNextDialogue();
        if(nextDialogue >= 0){
            this.dialogueBlock.setCurrentDialogue(nextDialogue);
        }
        
        this.dialogueBlock.updateLogic(deltaTime);
        
        if(this.dialogueBlock.getDialogueState() == CinematicState.STOP){
            this.currentTime += deltaTime;
        }
        
        boolean hasEnded = true;
        
        for(CharacterTimeline timeline : this.charactersTimeline){
            hasEnded &= timeline.updateTimeline(this.currentTime);
        }
        
        if(hasEnded &&
                (this.dialogueTimeline.lastEntry() == null 
                || (this.currentKeyDialogue == this.dialogueTimeline.lastEntry().getKey() && this.dialogueBlock.getDialogueState() == CinematicState.STOP))){
            if(this.getCinematicState() != CinematicState.STOP){
                this.cinematicState = CinematicState.END;
            }
        }
        
    }
    
    /**
     * @return the cinematicState
     */
    public CinematicState getCinematicState() {
        return this.cinematicState;
    }
    
    public int getNextDialogue(){
        Entry<Float, Integer> entry = this.dialogueTimeline.floorEntry(this.currentTime);
        
        if(entry != null){
            if(this.currentKeyDialogue == null || this.currentKeyDialogue != entry.getKey()){
                this.currentKeyDialogue = entry.getKey();
                return entry.getValue();
            }
        }
        return -1;
    }
    
    public void drawBatch(Camera camera, Batch batch){
        this.dialogueBlock.drawBatch(camera, batch);
    }
    
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer){
        this.dialogueBlock.drawShapeRenderer(camera, shapeRenderer);
    }
     
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    public enum CinematicState{
        STOP,
        START,
        RUNNING,
        END
    }
}
