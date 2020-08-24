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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.Object2DStateListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Deneyr
 */
public class CinematicManager implements Disposable{
  
    private String id;
    
    private CinematicState cinematicState;
    
    private float currentTime;
    
    private final List<CharacterTimeline> charactersTimeline;
    
    private TreeMap<Float, Integer> dialogueTimeline;
    private Float currentKeyDialogue;
    // Part dialogue
    private GuiDialogueBlock dialogueBlock;
    
    // Event part
    
    private WeakReference<Object2DStateListener> stateListener;
    private WeakReference<GameEventListener> gameEventListener;
    
    private boolean isStartCinematic;
    private boolean isEndCinematic;
    private String levelId;
    
    public CinematicManager(String id, List<Dialogue> dialogues, boolean isStartCinematic, boolean isEndCinematic, String levelId){
        this.id = id;
        
        this.currentTime = 0;
        
        this.cinematicState = CinematicState.STOP;
        
        this.charactersTimeline = new ArrayList<CharacterTimeline>();
        
        this.dialogueTimeline = new TreeMap<Float, Integer>();
        
        this.currentKeyDialogue = null;
        
        this.isStartCinematic = isStartCinematic;
        this.isEndCinematic = isEndCinematic;
        this.levelId = levelId;
        
        this.dialogueBlock = new GuiDialogueBlock(dialogues);
    }
    
    public CinematicManager(String id, List<Dialogue> dialogues){
        this.id = id;
        
        this.currentTime = 0;
        
        this.cinematicState = CinematicState.STOP;
        
        this.charactersTimeline = new ArrayList<CharacterTimeline>();
        
        this.dialogueTimeline = new TreeMap<Float, Integer>();
        
        this.currentKeyDialogue = null;
        
        this.isStartCinematic = false;
        this.isEndCinematic = false;
        this.levelId = new String();
        
        this.dialogueBlock = new GuiDialogueBlock(dialogues);
    }
    
    public void setStateListener(Object2DStateListener stateListener){
        if(stateListener != null){
            this.stateListener = new WeakReference<Object2DStateListener>(stateListener);     
        }
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
    
    private void notifyListenerObj2Removed(CharacterTimeline timeline){
        if(this.stateListener.get() != null){
            Character2D character = timeline.getCharacter();
            
            if(character != null){
                timeline.clearCharacter();
                this.stateListener.get().onStateChanged(character, Object2DStateListener.Object2DState.DEATH, 0);
            }         
        }
    }
    
    private void notifyEndCinematic(Vector2 cinematicEndPosition){
        if(this.gameEventListener.get() != null){

            if(this.isStartCinematic){
                this.gameEventListener.get().onGameEvent(GameEventListener.EventType.GAMESTART, this.levelId, cinematicEndPosition);
            }else if(this.isEndCinematic){
                this.gameEventListener.get().onGameEvent(GameEventListener.EventType.GAMEOVER, "success", cinematicEndPosition);
            }else{
                this.gameEventListener.get().onGameEvent(GameEventListener.EventType.ENDCINEMATIC, this.levelId, cinematicEndPosition);
            }
        }
    }
    
    public void updateLogic(float deltaTime){
        if(this.getCinematicState() == CinematicState.STOP){
            return;
        }
        
        if(this.getCinematicState() == CinematicState.END){
            
            Vector2 gameStartPosition = Vector2.Zero;
            for(CharacterTimeline timeline : this.charactersTimeline){
                
                if(this.isEndCinematic == false){
                    timeline.getCharacter().isCinematicEntity(false);
                }
                
                gameStartPosition = timeline.getCharacter().getPositionBody();
                switch(timeline.getCinematicStatus()){
                    case END_CINEMATIC:
                        this.notifyListenerObj2Removed(timeline);
                        break;
                }
            }
            
            this.notifyEndCinematic(gameStartPosition);
            
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
        
        boolean hasEnded = true;
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            for(CharacterTimeline timeline : this.charactersTimeline){
                timeline.onEndTimeline();
            }
            
            this.dialogueBlock.endDialogue();
            this.cinematicState = CinematicState.END;
        }else if(this.dialogueBlock.getDialogueState() == CinematicState.STOP){
            this.currentTime += deltaTime;
            
            for(CharacterTimeline timeline : this.charactersTimeline){
                hasEnded &= timeline.updateTimeline(this.currentTime);
            }
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            hasEnded = true;
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

    @Override
    public void dispose() {
        if(this.getCinematicState() != CinematicState.STOP){
            
            for(CharacterTimeline timeline : this.charactersTimeline){
                timeline.getCharacter().isCinematicEntity(false);
                switch(timeline.getCinematicStatus()){
                    case END_CINEMATIC:
                        this.notifyListenerObj2Removed(timeline);
                        break;
                }
            }
            this.cinematicState = CinematicState.STOP;
        }
        
        this.dialogueBlock.dispose();
    }
    
    /**
     * @param gameEventListener the gameEventListener to set
     */
    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = new WeakReference(gameEventListener);
    }
    
    public enum CinematicState{
        STOP,
        START,
        RUNNING,
        END
    }
}
