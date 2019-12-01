/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameEventListener.EventType;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Deneyr
 */
public class GameEventManager implements GameEventListener{

    private WeakReference<GameEventListener> gameEventListener;
    
    private HashMap<String, EventContainer> eventContainerDictionary;  
    
    public GameEventManager(GameEventListener gameEventListener){
        this.gameEventListener = new WeakReference<GameEventListener>(gameEventListener);
        
        this.eventContainerDictionary = new HashMap<String, EventContainer>();
    }
    
    private void NotifyGameEventListener(EventType type, String details){
        if(this.gameEventListener.get() != null){
            this.gameEventListener.get().onGameEvent(type, details, Vector2.Zero);
        }
    }
    
    public void flushGameEventManager() {
        
        for(EventContainer eventContainer : this.eventContainerDictionary.values()){
            eventContainer.dispose();
        }
        
        this.eventContainerDictionary.clear();
    }

    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        for(EventContainer eventContainer : this.eventContainerDictionary.values()){
            if(!eventContainer.isComplete()){
                eventContainer.onGameEvent(type, details, location);

                if(eventContainer.isComplete()){
                    this.NotifyGameEventListener(eventContainer.getEventToSend(), eventContainer.getDetailToSend());
                }
            }
        }
    }

    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
        // Nothing to do;
    }

    public void addGameEventContainer(String id, TriggerType triggerType, EventType eventToSend, String detailToSend){
        if(!this.eventContainerDictionary.containsKey(id)){
            this.eventContainerDictionary.put(id, new EventContainer(triggerType, eventToSend, detailToSend));
        }
    }
    
    public void addEventToCompleteTo(String id, String name, EventType eventToComplete, String detailToComplete){
        if(this.eventContainerDictionary.containsKey(id)){
            this.eventContainerDictionary.get(id).addEventToComplete(name, eventToComplete, detailToComplete);
        }
    }
    
    public enum TriggerType{
        AND,
        OR
    }
    
    public class EventContainer implements Disposable, GameEventListener{
        
        private TriggerType triggerType;
        
        private EventType eventToSend;
        private String detailToSend;
        
        private HashMap<String, EventToComplete> character2DEventsToComplete;
        
        private boolean complete;
        
        public EventContainer(TriggerType triggerType, EventType eventToSend, String detailToSend){
           this.triggerType = triggerType;
           
           this.eventToSend = eventToSend;
           this.detailToSend = detailToSend;
           
           this.character2DEventsToComplete = new HashMap<String, EventToComplete>();
           
           this.complete = false;
        }

        public void addEventToComplete(String name, EventType eventToComplete, String detailToComplete){
            if(!this.character2DEventsToComplete.containsKey(name)){
                this.character2DEventsToComplete.put(name, new EventToComplete(eventToComplete, detailToComplete));
            }
        }
        
        @Override
        public void onGameEvent(EventType type, String details, Vector2 location) {        

            if(this.complete){
                return;
            }
            
            ArrayList<EventToComplete> listEventToComplete = new ArrayList<EventToComplete>();
            
            for(EventToComplete eventContainer : this.character2DEventsToComplete.values()){
                if(!eventContainer.isComplete() 
                        && (type == eventContainer.getEventToComplete() && (eventContainer.getDetailToComplete() == null || details.equals(eventContainer.getDetailToComplete())))){
                    eventContainer.setComplete(true);
                }
                
                if(eventContainer.isComplete()){
                    listEventToComplete.add(eventContainer);
                }
            }
            
            switch(this.triggerType){
                case AND:
                    
                    if(listEventToComplete.size() == this.character2DEventsToComplete.size()){
                        this.complete = true;
                    }
                    
                    break;
                case OR:
                    
                    if(listEventToComplete.size() > 0){
                        this.complete = true;
                    }
                    
                    break;           
            }
            
        }
     
        @Override
        public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
            // Nothing to do.
        }
        
        /**
         * @return the complete
         */
        public boolean isComplete() {
            return complete;
        }
        
        /**
        * @return the detailToSend
        */
        public String getDetailToSend() {
            return detailToSend;
        }

        /**
        * @return the eventToSend
        */
        public EventType getEventToSend() {
            return eventToSend;
        }

        @Override
        public void dispose() {
            this.character2DEventsToComplete.clear();
        }
        
        public class EventToComplete{
            private EventType eventToComplete;
            private String detailToComplete;
            
            private boolean complete;
            
            public EventToComplete(EventType eventToComplete, String detailToComplete){
                this.eventToComplete = eventToComplete;
                
                this.detailToComplete = detailToComplete;
               
                this.complete = false;
            }

            /**
             * @return the complete
             */
            public boolean isComplete() {
                return complete;
            }

            /**
             * @param complete the complete to set
             */
            public void setComplete(boolean complete) {
                this.complete = complete;
            }

            /**
             * @return the eventToComplete
             */
            public EventType getEventToComplete() {
                return this.eventToComplete;
            }

            /**
             * @return the detailToComplete
             */
            public String getDetailToComplete() {
                return this.detailToComplete;
            }
        }
    }
}
