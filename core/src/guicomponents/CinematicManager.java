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
    
    private CinematicState state;
    
    private float currentTime;
    
    private final List<CharacterTimeline> charactersTimeline;
    
    private TreeMap<Float, String> dialogueTimeline;
    
    public CinematicManager(){
        this.currentTime = 0;
        
        this.state = CinematicState.START;
        
        this.charactersTimeline = new ArrayList<CharacterTimeline>();
    }
    
    public void updateLogic(float deltaTime){
        if(this.state == CinematicState.START){
            this.currentTime = 0;
            this.state = CinematicState.RUNNING;
            for(CharacterTimeline timeline : this.charactersTimeline){
                timeline.getCharacter().isCinematicEntity(true);
            }
        }
        
        this.currentTime += deltaTime;
        
        boolean hasEnded = true;
        
        for(CharacterTimeline timeline : this.charactersTimeline){
            hasEnded |= timeline.updateTimeline(this.currentTime);
        }
        
        if(hasEnded){
            this.state = CinematicState.END;
            for(CharacterTimeline timeline : this.charactersTimeline){
                timeline.getCharacter().isCinematicEntity(false);
            }
        }
        
    }
    
    public void draw(Batch batch, Camera camera, ShapeRenderer shapeRenderer){
        
    }
    
    public class CharacterTimeline{
        
        private final Character2D character;
        
        private final TreeMap<Float, String> timeline;
        
        private Entry<Float, String> currentEntry;
        
        private final Set<String> persistentInfluences;
        
        public CharacterTimeline(Character2D character){
            
            this.character = character;
            
            this.timeline = new TreeMap<Float, String>();
            
            this.currentEntry = null;
            
            this.persistentInfluences = new HashSet();
        }
        
        public void addEntry(float key, String value){
            this.timeline.put(key, value);
        }
        
        public String getValueAt(float key){
            Entry<Float, String> lowerEntry = this.timeline.floorEntry(key);
            
            if(lowerEntry != null &&
                    (this.currentEntry == null || this.currentEntry != lowerEntry)){
                this.currentEntry = lowerEntry;
                
                String result = this.currentEntry.getValue();
                
                if(result.contains("per_")){
                    
                    result = result.replaceAll("per_", "");
                    
                    if(this.persistentInfluences.contains(result)){
                        this.persistentInfluences.remove(result);
                    }else{
                        this.persistentInfluences.add(result);
                    }
                }
                
                return result;
            }
            return null;
        }
        
        
        public boolean updateTimeline(float time){
            boolean result = false;
            
            String influence = this.getValueAt(time);
            
            List<String> influences = new ArrayList<String>();
            
            if(influence != null){
                if(this.timeline.lastEntry() == null || this.currentEntry == this.timeline.lastEntry()){
                    result = true;
                }
                
               influences.add(influence);
            }
            
            influences.addAll(this.persistentInfluences);
            
            this.character.setInfluenceList(influences);
            
            return result;
        }
        
        public Set<String> getPersistentInfluences(){
            return this.persistentInfluences;
        }

        /**
         * @return the character
         */
        public Character2D getCharacter() {
            return character;
        }
    }
     
    public enum CinematicState{
        START,
        RUNNING,
        END
    }
}
