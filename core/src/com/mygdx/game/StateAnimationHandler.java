/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author françois
 */
public class StateAnimationHandler implements Disposable, Object2DStateListener{
    
    private static final float TIMER_DURATION = 0.1f;
    
    WeakReference<GameWorld> gameWorld;
    
    private final Map<Object2D, Integer> currentAnimatedObjectsCounter;
    private final Map<Object2D, Object2DStateListener.Object2DState> currentAnimatedObjectsState;
    
    private final Task mainTask;
    
    private boolean isScheduled;
    
    public StateAnimationHandler(GameWorld gameWorld){
        this.gameWorld = new WeakReference(gameWorld);
        
        this.currentAnimatedObjectsCounter = new HashMap<Object2D, Integer>();
        this.currentAnimatedObjectsState = new HashMap<Object2D, Object2DStateListener.Object2DState>();
        
        // Set Timer
        
        this.mainTask = new Task(){
            @Override
            public void run(){
                StateAnimationHandler.this.run();
            }
        };
        
        this.isScheduled = false;
    }
    
    public void scheduleTask(){
        if(!this.IsScheduled()){
            Timer.instance().scheduleTask(this.mainTask, 0, TIMER_DURATION);
            this.isScheduled = true;
        }
    }
    
    private void run(){
        List<Object2D> listAnimatedObject2Remove = new ArrayList<Object2D>();
        
        for(Entry<Object2D, Integer> entry : this.currentAnimatedObjectsCounter.entrySet()){
            
            if(entry.getValue() > 0){
                float deltaAlpha = 0.3f;
                if(entry.getValue() % 2 == 1){
                    deltaAlpha *= -1;
                }
                
                entry.getKey().setAlpha(entry.getKey().getAlpha() + deltaAlpha);
                
                System.out.println(entry.getKey().getAlpha());
                
                if(entry.getKey().getAlpha() <= 0f || entry.getKey().getAlpha() >= 1f){
                    entry.setValue(entry.getValue() - 1);
                }           
                
            }else{
                switch(this.currentAnimatedObjectsState.get(entry.getKey())){
                    case DEATH:
                        if(this.gameWorld.get() != null){
                            this.gameWorld.get().addObject2D2Flush(entry.getKey());
                        }
                        break;
                    case DAMAGE_TOOK:
                            entry.getKey().setAlpha(1f);
                        break;
                }
                
                listAnimatedObject2Remove.add(entry.getKey());
            }
        }
        
        for(Object2D obj : listAnimatedObject2Remove){
            this.currentAnimatedObjectsCounter.remove(obj);
            this.currentAnimatedObjectsState.remove(obj);
        }
    }

    @Override
    public void dispose() {
        this.mainTask.cancel();
        this.isScheduled = false;
        
        this.currentAnimatedObjectsCounter.clear();
        this.currentAnimatedObjectsState.clear();
    }

    @Override
    public void notifyStateChanged(Object2D notifier, Object2DState state, int animCounter) {
        this.currentAnimatedObjectsCounter.put(notifier, animCounter);
        this.currentAnimatedObjectsState.put(notifier, state);
    }

    /**
     * @return the isScheduled
     */
    public boolean IsScheduled() {
        return isScheduled;
    }
    
}
