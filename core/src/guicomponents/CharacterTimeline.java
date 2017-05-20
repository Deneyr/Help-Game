/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import com.mygdx.game.Character2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Deneyr
 */
public class CharacterTimeline{
        
        private final Character2D character;
        
        private final TreeMap<Float, String> timeline;
        
        private Float currentKey;
        
        private final Set<String> persistentInfluences;
        
        public CharacterTimeline(Character2D character){
            
            this.character = character;
            
            this.timeline = new TreeMap<Float, String>();
            
            this.currentKey = null;
            
            this.persistentInfluences = new HashSet();
        }
        
        public void reset(){
            this.currentKey = null;
        }
        
        public void addEntry(float key, String value){
            this.timeline.put(key, value);
        }
        
        public String getValueAt(float key){
            Map.Entry<Float, String> lowerEntry = this.timeline.floorEntry(key);
            
            if(lowerEntry != null &&
                    (this.currentKey == null || this.currentKey != lowerEntry.getKey())){
                this.currentKey = lowerEntry.getKey();
                
                String result = lowerEntry.getValue();
                
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
                if(this.timeline.lastEntry() == null || this.currentKey == this.timeline.lastEntry().getKey()){
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
