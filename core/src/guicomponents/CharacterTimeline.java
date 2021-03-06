/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Character2D;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Deneyr
 */
public class CharacterTimeline{
     
        private Character2D character;
        
        private final TreeMap<Float, String> timeline;
        
        private Float currentKey;
        
        private final Set<String> persistentInfluences;
       
        private CinematicStatus cinematicStatus;
        
        private Vector2 endPosition;
        
        public CharacterTimeline(Character2D character, CinematicStatus cinematicStatus){
            
            this.endPosition = null;
            
            this.character = character;
            
            this.timeline = new TreeMap<Float, String>();
            
            this.currentKey = null;
            
            this.persistentInfluences = new HashSet();
            
            this.cinematicStatus = cinematicStatus;
            switch(this.cinematicStatus){
                case START_CINEMATIC:
                case END_CINEMATIC:
                    character.isCinematicEntity(true);
                    break;
            }
        }
        
        public CharacterTimeline(Character2D character, CinematicStatus cinematicStatus, Vector2 endPosition){
            this(character, cinematicStatus);
            
            this.endPosition = new Vector2(endPosition.x * P2M, endPosition.y * P2M);
        }
        
        public void reset(){
            this.currentKey = null;
        }
        
        public void addEntry(float key, String value){
            this.timeline.put(key, value);
        }
        
        public List<String> getValueAt(float key){
            Map.Entry<Float, String> lowerEntry = this.timeline.floorEntry(key);
            
            if(lowerEntry != null &&
                    (this.currentKey == null || this.currentKey != lowerEntry.getKey())){
                
                SortedMap<Float, String> subMap;
                if(this.currentKey == null){
                    subMap = new TreeMap();
                    subMap.put(lowerEntry.getKey(), lowerEntry.getValue());
                }else{
                    subMap = this.timeline.subMap(this.currentKey, false, lowerEntry.getKey(), true);
                }
                
                this.currentKey = lowerEntry.getKey();
                
                List<String> result = new ArrayList<String>();
                for(String value : subMap.values()){

                    if(value.contains("per_")){

                        value = value.replaceAll("per_", "");

                        if(this.persistentInfluences.contains(value)){
                            this.persistentInfluences.remove(value);
                        }else{
                            this.persistentInfluences.add(value);
                        }
                    }else{
                        result.add(value);
                    }
                }
                
                return result;
            }
            return null;
        }
        
        public void onEndTimeline(){
            if(this.endPosition != null){
                this.character.setPosition(endPosition);
            }
        }
        
        public boolean updateTimeline(float time){
            if(this.character == null){
                return true;
            }
            
            boolean result = false;
            
            List<String> influencesPunctual = this.getValueAt(time);
            
            List<String> influences = new ArrayList<String>();
            
            if(influencesPunctual != null){
                
               influences.addAll(influencesPunctual);
            }
            
            influences.addAll(this.persistentInfluences);
            
            if(this.character.getLifePoints() > 0){
                this.character.setInfluenceList(influences);
            }
            
            if(this.timeline.lastEntry() == null || this.currentKey == this.timeline.lastEntry().getKey()){
                result = true;
            }
            
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
        
        public void clearCharacter(){
            this.character = null;
        }
        /**
        * @return the cinematicStatus
        */
       public CinematicStatus getCinematicStatus() {
           return cinematicStatus;
       }
        
        public enum CinematicStatus{
            START_CINEMATIC,
            NORMAL,
            END_CINEMATIC
        }
    }
