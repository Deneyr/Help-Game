/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import triggered.UpTriggeredObject2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import cosmetics.CosmeticObject2D;
import cosmetics.HitCosmeticObject2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import triggered.BulletTriggeredObject2D;
import triggered.CannonBallTriggeredObject2D;
import triggered.TeethTriggeredObject2D;

/**
 *
 * @author françois
 */
public class StateAnimationHandler implements Disposable, Object2DStateListener{
    
    private static final float TIMER_DURATION = 0.1f;
    
    private static final int MAX_COSMETICS = 10;
    
    WeakReference<GameWorld> gameWorld;
    
    private final Map<Object2D, Integer> currentAnimatedObjectsCounter;
    private final Map<Object2D, Object2DStateListener.Object2DState> currentAnimatedObjectsState;
    
    private final Task mainTask;
    
    private boolean isScheduled;
    
    // Part Pool create
    private final Map<Class, FactoryPool> object2DFactoriesPool;
    
    private List<CosmeticObject2D> cosmeticObject2DPool;
    private final Map<Class, CosmeticFactoryPool> cosmeticObj2DFactoriesPool;
    
    public StateAnimationHandler(GameWorld gameWorld){
        this.gameWorld = new WeakReference(gameWorld);
        
        this.currentAnimatedObjectsCounter = new HashMap<Object2D, Integer>();
        this.currentAnimatedObjectsState = new HashMap<Object2D, Object2DStateListener.Object2DState>();
        
        this.cosmeticObject2DPool = new ArrayList<CosmeticObject2D>();
        this.cosmeticObj2DFactoriesPool = new HashMap<Class, CosmeticFactoryPool>();
        
        // Set pool factory
        this.object2DFactoriesPool = new HashMap<Class, FactoryPool>();
        
        this.object2DFactoriesPool.put(UpTriggeredObject2D.class, new Object2DFactoryPool<UpTriggeredObject2D>(UpTriggeredObject2D.class));
        this.object2DFactoriesPool.put(TeethTriggeredObject2D.class, new Object2DFactoryPool<TeethTriggeredObject2D>(TeethTriggeredObject2D.class));
        this.object2DFactoriesPool.put(CannonBallTriggeredObject2D.class, new Object2DFactoryPool<CannonBallTriggeredObject2D>(CannonBallTriggeredObject2D.class));
        this.object2DFactoriesPool.put(BulletTriggeredObject2D.class, new Object2DFactoryPool<BulletTriggeredObject2D>(BulletTriggeredObject2D.class));
        
        // Set Pool cosmetic
        this.cosmeticObj2DFactoriesPool.put(HitCosmeticObject2D.class, new CosmeticFactoryPool<HitCosmeticObject2D>(HitCosmeticObject2D.class));
        
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
                float deltaScale = -0.5f;
                if(entry.getValue() % 2 == 1){
                    deltaAlpha *= -1;
                    deltaScale *= -1;
                }
                
                entry.getKey().setAlpha(entry.getKey().getAlpha() + deltaAlpha);
                
                if(this.currentAnimatedObjectsState.get(entry.getKey()) == Object2DStateListener.Object2DState.TOOK_BY_PLAYER){
                    entry.getKey().setScale(entry.getKey().getScale() + deltaScale);
                }
                
                if(entry.getKey().getAlpha() <= 0f || entry.getKey().getAlpha() >= 1f){
                    entry.setValue(entry.getValue() - 1);
                }
                
            }else{
                switch(this.currentAnimatedObjectsState.get(entry.getKey())){
                    case DEATH:
                    case TOOK_BY_PLAYER:
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
        
        if(this.cosmeticObject2DPool.size() > MAX_COSMETICS){
            CosmeticObject2D cosmeticObj2D = this.cosmeticObject2DPool.remove(0);
            this.removeObject2D(cosmeticObj2D);
        }
    }

    public void removeObject2D(Object2D obj){
        if(this.gameWorld.get() != null){
            this.gameWorld.get().addObject2D2Flush(obj);
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
    public void onStateChanged(Object2D notifier, Object2DState state, int animCounter) {
        if(state != Object2DState.DEATH || animCounter > 0){
            this.currentAnimatedObjectsCounter.put(notifier, animCounter);
            this.currentAnimatedObjectsState.put(notifier, state);
        }else{
            this.removeObject2D(notifier);
            if(this.currentAnimatedObjectsCounter.containsKey(notifier)){
                
                this.currentAnimatedObjectsCounter.remove(notifier);
                this.currentAnimatedObjectsState.remove(notifier);
            }
        }
    }
    
    @Override
    public void onStateChanged(Object2D notifier, Object2DState state, int animCounter, boolean canReplace){
        if(!this.IsObject2DHandled(notifier) || canReplace){
            if(state != Object2DState.DEATH || animCounter > 0){
                this.onStateChanged(notifier, state, animCounter);
            }else{
                this.removeObject2D(notifier);
                if(this.currentAnimatedObjectsCounter.containsKey(notifier)){

                    this.currentAnimatedObjectsCounter.remove(notifier);
                    this.currentAnimatedObjectsState.remove(notifier);
                }
            }
        }
    }
    
    @Override
    public void onObject2D2Create(Object2D notifier, Class obj2DClass, Vector2 position, Vector2 speed){
        
        if(this.object2DFactoriesPool.containsKey(obj2DClass) && this.gameWorld.get() != null){
            FactoryPool factoryPool = this.object2DFactoriesPool.get(obj2DClass);
            TriggeredObject2D triggeredObj2D = factoryPool.obtainTriggeredObject2D(this.gameWorld.get().getWorld(), position, speed);
            
            this.gameWorld.get().addObject2DToWorld(triggeredObj2D, true);
        }
    }
    
    @Override
    public void onCosmeticObj2D2Create(Object2D giver, Object2D receiver, Class obj2DClass, Vector2 position, Vector2 speed, float strength) {
        
        if(this.cosmeticObj2DFactoriesPool.containsKey(obj2DClass) && this.gameWorld.get() != null){
            
            CosmeticFactoryPool cosmeticFactoryPool = this.cosmeticObj2DFactoriesPool.get(obj2DClass);
            CosmeticObject2D cosmeticObj2D = cosmeticFactoryPool.obtainTriggeredObject2D(this.gameWorld.get().getWorld(), giver, receiver, position, speed, strength);
            
            if(!this.cosmeticObject2DPool.contains(cosmeticObj2D)){
                this.cosmeticObject2DPool.add(cosmeticObj2D);
            }
            
            this.gameWorld.get().addObject2DToWorld(cosmeticObj2D, true);
        }
        
    }
    
    public boolean IsObject2DHandled(Object2D notifier){
        return this.currentAnimatedObjectsCounter.containsKey(notifier);
    }

    public void freeObject2D(Object2D obj){
        if(this.object2DFactoriesPool.containsKey(obj.getClass())){
   
            FactoryPool factoryPool = this.object2DFactoriesPool.get(obj.getClass());
            factoryPool.freeTriggeredObject2D((TriggeredObject2D) obj);
        }else if(this.cosmeticObj2DFactoriesPool.containsKey(obj.getClass())){
            CosmeticObject2D cosmeticObject2D = (CosmeticObject2D) obj;
            
            CosmeticFactoryPool cosmeticFactoryPool = this.cosmeticObj2DFactoriesPool.get(obj.getClass());
            cosmeticFactoryPool.freeTriggeredObject2D(cosmeticObject2D);
            
            this.cosmeticObject2DPool.remove(cosmeticObject2D);
        }
    }
    
    /**
     * @return the isScheduled
     */
    public boolean IsScheduled() {
        return isScheduled;
    }
    
}
