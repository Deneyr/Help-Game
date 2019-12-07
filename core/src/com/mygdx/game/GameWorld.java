/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import guicomponents.CinematicManager;
import guicomponents.CinematicManager.CinematicState;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import triggered.CheckPointTriggeredObject2D;

/**
 *
 * @author françois
 */
public class GameWorld implements WorldPlane, GameEventListener{

    private World world;
    
    private List<Object2D> listCurrentObject2D = new ArrayList<Object2D>();
    
    private Character2D hero; 
    
    private Set<Object2D> object2D2Flush;
    
    private List<CheckPointTriggeredObject2D> listCheckpoints;
    
    private double screenFOV;
    private float timerOutOfScreen;
    
    private int currentMoney;
    
    protected List<WeakReference<GameEventListener>> listEventGameListeners;
    
    // Auxiliary modules
    
    private StateAnimationHandler stateAnimationHandler;
    
    private GameEventManager gameEventManager;
    
    private Map<String, CinematicManager> mapCinematicManagers;
    
    
    public GameWorld(){
        this.world = new World(new Vector2(0, -20f), true);
        this.hero = null;
        
        this.world.setContactListener(new GameContactListener());
        
        this.object2D2Flush = new HashSet<Object2D>();
        
        this.listCheckpoints = new ArrayList<CheckPointTriggeredObject2D>();
        
        this.screenFOV = -1d;
        this.timerOutOfScreen = 0;
        
        this.currentMoney = 0;
        
        this.listEventGameListeners = new ArrayList<WeakReference<GameEventListener>>();
        
        // Initialize modules.
        this.stateAnimationHandler = new StateAnimationHandler(this);
        
        this.gameEventManager = new GameEventManager(this);
        this.addGameEventListener(this.gameEventManager);
        
        this.mapCinematicManagers = new HashMap<String, CinematicManager>();
    }
    
    public void addCinematicManager(CinematicManager cinematicManager, int indexCheckpoint, int indexCinematic){    
        cinematicManager.setStateListener(this.stateAnimationHandler);
        cinematicManager.setGameEventListener(this);
        
        if(indexCheckpoint < 0 || indexCinematic > indexCheckpoint){
            this.mapCinematicManagers.put(cinematicManager.getId(), cinematicManager);
        }else{
            cinematicManager.reset();
            cinematicManager.dispose();
        }
    }
    
    public void addCinematicManager(CinematicManager cinematicManager){
        cinematicManager.setStateListener(this.stateAnimationHandler);
        cinematicManager.setGameEventListener(this);
        this.mapCinematicManagers.put(cinematicManager.getId(), cinematicManager);
    }
    
    public void drawBatch(Camera camera, Batch batch){
        for(CinematicManager cinematicManager : this.mapCinematicManagers.values()){
            cinematicManager.drawBatch(camera, batch);
        }
    }
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer){
        for(CinematicManager cinematicManager : this.mapCinematicManagers.values()){
            cinematicManager.drawShapeRenderer(camera, shapeRenderer);
        }
    }
    
    @Override
    public List<Sprite> getSpritesInRegion(float lowerX, float lowerY, float upperX, float upperY){
        ScreenQueryCallback query = new ScreenQueryCallback();
        
        this.world.QueryAABB(query, lowerX, lowerY, upperX, upperY);
        
        List<Object2D> listObject2D = query.getListObject2D();
        
        Iterator<Object2D> it = listObject2D.iterator();
        List<Sprite> listSprites = new ArrayList<Sprite>();
        while(it.hasNext()){
            Sprite sprite = it.next().createCurrentSprite();
            if(sprite != null){
                listSprites.add(sprite);
            }
        }
        return listSprites;
    }
    
    public List<Sprite> getLifeBarInRegion(float lowerX, float lowerY, float upperX, float upperY){
        this.screenFOV = Math.sqrt((upperY - lowerY) * (upperY - lowerY) + (upperX - lowerX) * (upperX - lowerX));
        
        ScreenQueryCallback query = new ScreenQueryCallback();
        
        this.world.QueryAABB(query, lowerX, lowerY, upperX, upperY);
        
        List<Object2D> listObject2D = query.getListObject2D();
        
        Iterator<Object2D> it = listObject2D.iterator();
        List<Sprite> listSprites = new ArrayList<Sprite>();
        while(it.hasNext()){
            Object2D obj = it.next();
            if(obj instanceof Character2D && obj != this.hero){
                Character2D chara = (Character2D) obj;
                if(chara.getLifePoints() > 0 && chara.HasLifeBar()){
                    Sprite sprite = obj.createCurrentSprite();
                    if(sprite != null){
                        sprite.setScale(((Character2D)obj).getLifePoints() / 100.f, 1.f);
                        listSprites.add(sprite);
                    }
                }
            }
        }
        return listSprites;
    }
    
    public Sprite getLifeBarHero(){
        return this.hero.createLifeBar();
    }
    
    @Override
    public void step(float delta){
        delta = Math.min(delta, 0.1f);
        this.timerOutOfScreen += delta;
        
        if(!this.stateAnimationHandler.IsScheduled()){
            this.stateAnimationHandler.scheduleTask();
        }
        
        for(CinematicManager cinematicManager : this.mapCinematicManagers.values()){
            cinematicManager.updateLogic(delta);
        }
        
        this.gameEventManager.updateLogic(delta);
        
        List<Object2D> copyListCurrentObject2D = new ArrayList<Object2D>(this.listCurrentObject2D);      
        for(Object2D obj : copyListCurrentObject2D)
        {
            obj.updateLogic(delta);
        }
        
        this.handleObject2D2Flush();
        
        this.world.step(delta, 6, 2);
        
        if(this.timerOutOfScreen > 1f){
            this.timerOutOfScreen = 0;
            this.computeObjectsOutOfScreen();
        }
    }
    
    public void addObject2DToWorld(Object2D obj){
        if(obj != null){
            this.listCurrentObject2D.add(obj);
            
            obj.addGameEventListener(this);
        }
    }
    public void addObject2DToWorld(Object2D obj, boolean isStateHandled){
        if(obj != null){
            this.listCurrentObject2D.add(obj);

            if(isStateHandled){
                obj.addObject2DStateListener(this.stateAnimationHandler);
            }
            
            obj.addGameEventListener(this);
        }
    }
    
    public void removeObject2DToWorld(Object2D obj){
        obj.removeBody(this.world);
        
        this.listCurrentObject2D.remove(obj);
    }
    
    public void setHero(Character2D hero){
        if(this.hero != null){
            removeObject2DToWorld(this.hero);
        }
        
        this.hero = hero;
        
        addObject2DToWorld(hero, true);
    }
    
    @Override
    public void flushWorld(){
        this.dispose();
        
        this.world = new World(new Vector2(0, -20f), true);
        this.listCurrentObject2D = new ArrayList();
        
        this.stateAnimationHandler = new StateAnimationHandler(this);               
        this.getGameEventManager().flushGameEventManager();
        
        this.world.setContactListener(new GameContactListener());
    }
    
    public Vector2 getHeroPosition(){
        if(this.hero.getSideCharacter() == Character2D.SideCharacter.LEFT){
            return new Vector2(this.hero.getPositionBody().add(Grandma.LEFT_RIGHT_DIST * P2M / 2f, 0));
        }else{
            return new Vector2(this.hero.getPositionBody().add(-Grandma.LEFT_RIGHT_DIST * P2M / 2f, 0));
        }
    }

    @Override
    public void dispose() {
        //Timer.instance().clear();
        
        this.hero = null;

        this.currentMoney = 0;
        
        this.stateAnimationHandler.dispose();
        
        this.object2D2Flush.clear();
        for(Object2D obj : this.listCurrentObject2D){
            obj.removeBody(this.world);
        }
        
        this.listCheckpoints.clear();
        
        this.world.dispose();       
        
        for(CinematicManager cinematicManager : this.mapCinematicManagers.values()){
            cinematicManager.dispose();
        }
        this.mapCinematicManagers.clear();
        
        assert this.world.getBodyCount() == 0;
    }

    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        
        switch(type){
            case SCORE:
                this.setCurrentMoney(this.currentMoney + Integer.parseInt(details));
                break;
            case CINEMATIC:
                if(this.mapCinematicManagers.containsKey(details) && this.isCinematicManagersFree(details)){
                    CinematicManager cinematicManager = this.mapCinematicManagers.get(details);
                    cinematicManager.reset();
                }
                break;
        }
        
        this.notifyGameEventListeners(type, details, location);
    }
    
    private boolean isCinematicManagersFree(String key){
        for(CinematicManager cinematicManager : this.mapCinematicManagers.values()){
            if(cinematicManager.getCinematicState() != CinematicState.STOP){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
        // Nothing to do.
    }
    
    private class ScreenQueryCallback implements QueryCallback{

        private Set<Object2D> setBody = new HashSet<Object2D>();
        
        @Override
        public boolean reportFixture(Fixture fxtr) {
            if(fxtr.getUserData() != null && fxtr.getUserData() instanceof Object2D){
                this.setBody.add((Object2D)fxtr.getUserData());
            }
            return true;
        }
        
        public List<Object2D> getListObject2D(){
            List<Object2D> listObj2D = new ArrayList<Object2D>();
            
            Iterator<Object2D> it = this.setBody.iterator();
            while(it.hasNext()){
                listObj2D.add(it.next());
            }
            listObj2D.sort(new Obj2DComparator());
            
            return listObj2D;
        }
        
    }
    
    public class Obj2DComparator implements Comparator<Object2D>{

        @Override
        public int compare(Object2D o1, Object2D o2) {
            if(o1.getPriority() < o2.getPriority())
                return -1;
            else if(o1.getPriority() > o2.getPriority())
                return 1;
            else
                return 0;
        }
    
    }
    
    public World getWorld(){
        return this.world;
    }
    
    /**
     * @return the gameEventManager
     */
    public GameEventManager getGameEventManager() {
        return this.gameEventManager;
    }
    
    public void addObject2D2Flush(Object2D obj){
        this.object2D2Flush.add(obj);
    }
    
    private void handleObject2D2Flush(){
        for(Object2D obj : this.object2D2Flush){
            this.removeObject2DToWorld(obj);
            this.stateAnimationHandler.freeObject2D(obj);
        }
        
        this.object2D2Flush.clear();
    }
    
    private void computeObjectsOutOfScreen(){
        for(Object2D obj : this.listCurrentObject2D){
            if(obj instanceof TriggeredObject2D){
                Vector2 distHeroObj = obj.getPositionBody().sub(this.hero.getPositionBody());
                double margin = distHeroObj.len() - this.screenFOV/2;
                if(margin > 0){
                    TriggeredObject2D triggerObj2D = (TriggeredObject2D) obj;

                    triggerObj2D.onOutOfScreen(margin);
                }
            }
        }
    }
    
    public void addGameEventListener(GameEventListener listener){
        if(listener != null){
            this.listEventGameListeners.add(new WeakReference(listener));
        }
    }
    
    public void clearGameEventListenersList(){
        this.listEventGameListeners.clear();
    }
    
    public void notifyGameEventListeners(EventType event, String details, Vector2 location){
        if(location != null && this.hero != null && this.screenFOV > 0){
            float distHeroX = (float) ((location.x - this.hero.getPositionBody().x) * 2f / this.screenFOV);
            float distHeroY = (float) ((location.y - this.hero.getPositionBody().y) * 2f / this.screenFOV);

            location = new Vector2(distHeroX, distHeroY);
        }

        if(event != EventType.LOOP
                || (Math.abs(location.x) < 1.5 && Math.abs(location.y) < 1.5)){
            for(WeakReference<GameEventListener> refEventGameListener : this.listEventGameListeners){
                if(refEventGameListener.get() != null){

                    refEventGameListener.get().onGameEvent(event, details, location);
                }
            }
        }
    }
    
    public void addCheckPoint(CheckPointTriggeredObject2D checkPoint, int currentCheckpointIndex){      
        checkPoint.setIndex(this.listCheckpoints.size());
        this.listCheckpoints.add(checkPoint);
               
        checkPoint.setIsActive(checkPoint.getIndex() > currentCheckpointIndex);
        
        this.addObject2DToWorld(checkPoint);
    }
    
    public Vector2 getPositionAtCheckpoint(int index){
        if(index < 0){
            return null;
        }
        Vector2 position = this.listCheckpoints.get(index).getPositionBody();
        position.x /= HelpGame.P2M;
        position.y /= HelpGame.P2M;
        return position;
    }
    
    /**
     * @return the currentMoney
     */
    public int getCurrentMoney() {
        return currentMoney;
    }

    /**
     * @param score the currentMoney to set
     */
    public void setCurrentMoney(int score) {
        this.currentMoney = score;
        
        if(this.currentMoney < 0){
            this.currentMoney = 0;
        }
    }
}
