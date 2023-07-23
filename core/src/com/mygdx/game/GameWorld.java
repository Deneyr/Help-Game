/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import characters.OpponentCAC1;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.scenery.GroundUpperCity;
import guicomponents.CinematicManager;
import guicomponents.CinematicManager.CinematicState;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private GameEditorManager gameEditorManager;
    
    private Map<String, CinematicManager> mapCinematicManagers;
    
    // Screen shake
    private float shakeTime;
    private Vector2 targetOffsetCameraPoint;
    private Vector2 offsetCameraPoint;
    
    
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
        
        this.gameEditorManager = new GameEditorManager();
        
        this.mapCinematicManagers = new HashMap<String, CinematicManager>();
        
        // Screen shake       
        this.shakeTime = -1;
        this.targetOffsetCameraPoint = null;
        this.offsetCameraPoint = new Vector2(0, 0);
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
        this.screenFOV = Math.sqrt((upperY - lowerY) * (upperY - lowerY) + (upperX - lowerX) * (upperX - lowerX));
        
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
    
    public List<Object2D> getObject2DInRegion(float lowerX, float lowerY, float upperX, float upperY){
        
        ScreenQueryCallback query = new ScreenQueryCallback();
        
        this.world.QueryAABB(query, lowerX, lowerY, upperX, upperY);

        List<Object2D> listObject2D = query.getListObject2D();
        
        return listObject2D;
    }
    
    public List<Sprite> getLifeBarInRegion(float lowerX, float lowerY, float upperX, float upperY){
        
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
        
        this.updateShakingScreen(delta);
        
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
    
    public Object2D getHero(){
        return this.hero;
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
    
    private Vector2 getHeroPosition(){
        if(this.hero.getSideCharacter() == Character2D.SideCharacter.LEFT){
            return new Vector2(this.hero.getPositionBody().add(Grandma.LEFT_RIGHT_DIST * P2M / 2f, 0));
        }else{
            return new Vector2(this.hero.getPositionBody().add(-Grandma.LEFT_RIGHT_DIST * P2M / 2f, 0));
        }
    }
    
    public Vector2 getCameraPosition(){
        /*if(this.hero == null){
            return this.getGameEditorManager().getCameraPosition();
        }*/
        if(this.hero != null){
            return this.getHeroPosition().add(this.offsetCameraPoint);
        }
        return Vector2.Zero;
    }
    
    public void setCameraPosition(Vector2 newPosition){
        /*if(this.hero == null){
            return this.getGameEditorManager().getCameraPosition();
        }*/
        if(this.hero != null){
            this.hero.setPosition(new Vector2(newPosition.x, newPosition.y));
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
            case ATTACK:
                if(details.equals("hitPunch")){
                    this.shakeTime = 0;
                    this.offsetCameraPoint = new Vector2(0, 0);
                    this.targetOffsetCameraPoint = new Vector2((float) Math.random() * 0.2f, (float) Math.random() * 0.2f);
                }
                break;
        }
        
        this.notifyGameEventListeners(type, details, location);
    }
    
    private void updateShakingScreen(float deltaTime){
        if(this.shakeTime >= 0){
            
            Vector2 offsetDir = (new Vector2(this.targetOffsetCameraPoint)).sub(this.offsetCameraPoint);

            Vector2 newOffset = (new Vector2(this.offsetCameraPoint)).add(offsetDir.setLength(deltaTime * 2));
            
            Vector2 newOffsetDir = (new Vector2(this.targetOffsetCameraPoint)).sub(newOffset);
            
            if(offsetDir.dot(newOffsetDir) < 0){
                this.offsetCameraPoint = this.targetOffsetCameraPoint;
                this.targetOffsetCameraPoint = new Vector2((float) Math.random() * 0.2f, (float) Math.random() * 0.2f);
                
                this.targetOffsetCameraPoint.setLength(this.targetOffsetCameraPoint.len() * (1 - this.shakeTime / 0.3f));
            }else{
                this.offsetCameraPoint = newOffset;
            }
            
            this.shakeTime += deltaTime;
            
            if(this.shakeTime > 0.30){
               this.shakeTime = -1; 
               this.targetOffsetCameraPoint = null;
               this.offsetCameraPoint = new Vector2(0, 0);
            }            
        }
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
                
                Vector2 heroPosition = this.getCameraPosition();
                
                Vector2 distHeroObj = obj.getPositionBody().sub(heroPosition);
                double margin = distHeroObj.len() - this.screenFOV/2;
                if(margin > 0){
                    TriggeredObject2D triggerObj2D = (TriggeredObject2D) obj;

                    triggerObj2D.onOutOfScreen(margin);
                }
            }
        }
    }
    
    private void computeObjectsInScreen(float deltaTime){
        for(Object2D obj : this.listCurrentObject2D){
            Vector2 distHeroObj = obj.getPositionBody().sub(this.hero.getPositionBody());
            double margin = this.screenFOV/2 - distHeroObj.len();
            if(margin > 0){
                obj.updateLogic(deltaTime);
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
    
    // Editor part
    
    public void onTouchDown(float positionX, float positionY, int pointer, int button){
        this.getGameEditorManager().onTouchDown(this, positionX, positionY, pointer, button);
    }
    
    public void onTouchUp(float positionX, float positionY, int pointer, int button){
        this.getGameEditorManager().onTouchUp(this, positionX, positionY, pointer, button);
    }

    public void onTouchDragged(float positionX, float positionY, int pointer){
        this.getGameEditorManager().onTouchDragged(this, positionX, positionY, pointer);
    }

    public void onMouseMoved(float positionX, float positionY){
        this.getGameEditorManager().onMouseMoved(this, positionX, positionY);
    }
    
    public Object2D getEditorFirstSelectedObject(){
        if(this.gameEditorManager.touchedObjects.isEmpty()){
            return null;
        }
        
        return this.gameEditorManager.touchedObjects.iterator().next();
    }
    
    public Object2D createObject(Object2DEditorFactory factory){
        Object2D obj = this.getGameEditorManager().createObject2D(this, factory);
        
        if(obj instanceof Grandma){
            this.hero = (Grandma) obj;
        }
        
        return obj;
    }
    
    public Object2D createObject(Object2DEditorFactory factory, Vector2 position, float angle){
        Object2D obj = this.getGameEditorManager().createObject2D(this, factory, position, angle);
        
        if(obj instanceof Grandma){
            this.hero = (Grandma) obj;
        }
        
        return obj;
    }
    
    public void onFactorySelected(){
        this.getGameEditorManager().setIsFactorySelected(this, true);
    }
    
    public void onFactoryUnSelected(){
        this.getGameEditorManager().setIsFactorySelected(this, false);
    }
    
    public void onDeleteTouchedObj(){
        
        HashSet<Object2D> objTouched = this.getGameEditorManager().getSelectedObjects();
        
        if(objTouched.size() > 0){
            if(objTouched.contains(this.hero)){
                System.out.println("hero deletion");
                this.hero = null;
            }
            
            this.getGameEditorManager().deleteSelectedObjects(this);
        }
    }
    
    public void onRotateObj(EventType eventType, float deltaTime){
        this.getGameEditorManager().rotateSelectedObjects(eventType, deltaTime);
    }
    
    public void onChangePriorityObj(EventType eventType){
        this.getGameEditorManager().changePrioritySelectedObjects(eventType);
    }
    
    public void onMultipleSelectionStateChanged(EventType eventType){
        if(eventType == EventType.EDITORCTRLPRESSED){
            this.getGameEditorManager().setMultipleSelectionState(true);
        }else if(eventType == EventType.EDITORCTRLRELEASED){
            this.getGameEditorManager().setMultipleSelectionState(false);
        }
    }
    
    public void saveObject2Ds(String filename){
        this.getGameEditorManager().saveObject2Ds(this, filename);
    }
    
    /**
     *
     * @return
     */
    public GameEditorManager getGameEditorManager(){
        return this.gameEditorManager;
    }
    
    public class GameEditorManager{

        private HashSet<Object2D> touchedObjects;
        
        private boolean isTouchedAgain; 
        
        private boolean isFactorySelected;
        
        private Map<Object2D, Object2DEditorFactory> mapObject2DToFactory;
        
        private float positionCursorX;
        private float positionCursorY; 
        
        //private Vector2 cameraPosition;
        
        private Vector2 positionTouched;
        private Vector2 firstPositionTouched;
        
        private boolean gameRunning;
        
        private boolean multipleSelectionState;
        
        private Vector2 cameraPosition;
        
        private boolean isInEditorMode;
        
        public GameEditorManager(){
            this.touchedObjects = new HashSet<Object2D>();
            
            this.isTouchedAgain = false;
            
            this.isFactorySelected = false;
            
            this.mapObject2DToFactory = new HashMap<Object2D, Object2DEditorFactory>();
            
            this.positionCursorX = 0;
            this.positionCursorY = 0;   
            
            this.gameRunning = false;
            
            //this.cameraPosition = new Vector2();
            
            this.positionTouched = new Vector2();
            this.firstPositionTouched = null;
            
            this.cameraPosition = new Vector2(0, 0);
            
            this.multipleSelectionState = false;
            
            this.isInEditorMode = false;
        }
        
        public void onTouchDown(GameWorld world, float positionX, float positionY, int pointer, int button){
            
            boolean isCurrentTouchedAgain = false;
            
            this.firstPositionTouched = new Vector2(positionX, positionY);
            this.positionTouched = new Vector2(this.getFirstPositionTouched());
            
            if(button == 0 && this.isFactorySelected == false){
                
                System.out.println(world.screenFOV / 2);
                
                Object2D nearestObject2D = this.getObject2DInNearestPosition(world, new Vector2(positionX, positionY)); 
                
                if(nearestObject2D == null || this.touchedObjects.contains(nearestObject2D)){
                    this.firstPositionTouched = null;
                    isCurrentTouchedAgain = true;
                }else{
                    if(this.multipleSelectionState == false){
                        this.touchedObjects.clear();
                    }
                    
                    this.touchedObjects.add(nearestObject2D);
                    
                    this.UpdateObject2D(world);
                }             
                                                       
                /*if(this.multipleSelectionState)
                {
                    List<Object2D> listObject2D = this.getObject2DInSelectionArea(world);

                    if(listObject2D != null && listObject2D.size() > 0){ 
                        this.touchedObjects = new HashSet<Object2D>(listObject2D);
                        
                        this.UpdateObject2D(world);
                    }
                }else{
                    Object2D nearestObject2D = this.getObject2DInNearestPosition(world, new Vector2(positionX, positionY));

                    if(nearestObject2D != null && this.touchedObjects.contains(nearestObject2D) == false){
                        this.touchedObjects.clear();

                        this.touchedObjects.add(nearestObject2D);

                        this.UpdateObject2D(world);

                        System.out.println("object touched : " + nearestObject2D);
                    }else{
                        isCurrentTouchedAgain = true;
                    }                   
                }*/
            }
            else
            {
                this.firstPositionTouched = null;
                
                this.touchedObjects.clear();
                
                this.UpdateObject2D(world);
            }
            
            this.isTouchedAgain = isCurrentTouchedAgain;
        }
        
        private List<Object2D> getObject2DInSelectionArea(GameWorld world){
            List<Object2D> listObject2D = new ArrayList<Object2D>();
            if(this.getFirstPositionTouched() != null){
                
                float lowerX = Math.min(this.getFirstPositionTouched().x, this.getPositionTouched().x);
                float upperX = Math.max(this.getFirstPositionTouched().x, this.getPositionTouched().x);
                
                float lowerY = Math.min(this.getFirstPositionTouched().y, this.getPositionTouched().y);
                float upperY = Math.max(this.getFirstPositionTouched().y, this.getPositionTouched().y);
                
                List<Object2D> requestListObject2D = world.getObject2DInRegion(
                                                        lowerX - 2f, 
                                                        lowerY - 2f, 
                                                        upperX + 2f, 
                                                        upperY + 2f);
                
                for(Object2D obj2D : requestListObject2D){
                    if(this.IsObject2DSelectable(obj2D)){
                        listObject2D.add(obj2D);
                    }
                }             
            }
            return listObject2D;
        }
        
        private Object2D getObject2DInNearestPosition(GameWorld world, Vector2 position){
             
            Object2D nearestObject2D = null;
            
            List<Object2D> listObject2D = world.getObject2DInRegion(
                            (float) (position.x - world.screenFOV / 2), 
                            (float) (position.y - world.screenFOV / 2), 
                            (float) (position.x + world.screenFOV / 2), 
                            (float) (position.y + world.screenFOV / 2));

            if(listObject2D.size() > 0){ 

                nearestObject2D = null;
                float minimalDist2 = 0;
                for(Object2D obj : listObject2D){
                    if(this.IsObject2DSelectable(obj)){
                        Vector2 positionObj = obj.getPositionBody();
                        float distObj = Vector2.dst2(positionObj.x, positionObj.y, position.x, position.y);

                        if(nearestObject2D == null || distObj < minimalDist2){
                            nearestObject2D = obj;
                            minimalDist2 = distObj;
                        }
                    }
                }                  
            }
            
            return nearestObject2D;
        }
        
        private boolean IsObject2DSelectable(Object2D obj2D){
            if(obj2D instanceof GroundUpperCity){
                return false;
            }
            return true;
        }
        
        private void UpdateObject2D(GameWorld world){          
            for(Object2D obj : world.listCurrentObject2D){
                if(this.touchedObjects.size() > 0){
                    if(this.touchedObjects.contains(obj) == false){
                        obj.setAlpha(0.3f);
                    }else{
                        obj.setAlpha(1f);
                    }
                }else{
                    obj.setAlpha(1f);
                }
            }
        }
        
        public void onTouchUp(GameWorld world, float positionX, float positionY, int pointer, int button){
            this.firstPositionTouched = null;
        }

        public void onTouchDragged(GameWorld world, float positionX, float positionY, int pointer){
            
            Vector2 offsetPosition = (new Vector2(positionX, positionY)).sub(this.getPositionTouched());
            
            if(this.isTouchedAgain){
                for(Object2D obj : this.touchedObjects){
                    obj.physicBody.setTransform(obj.physicBody.getPosition().add(offsetPosition), obj.physicBody.getAngle());
                }
            }
            
            this.positionTouched = new Vector2(positionX, positionY);
            
            if(this.getFirstPositionTouched() != null){
                if(this.isTouchedAgain == false && this.getFirstPositionTouched().equals(this.getPositionTouched()) == false){
                    List<Object2D> listObject2D = this.getObject2DInSelectionArea(world);

                    if(this.multipleSelectionState == false){
                        this.touchedObjects.clear();
                    }

                    for(Object2D object2DSelected : listObject2D){
                        this.touchedObjects.add(object2DSelected);
                    }

                    this.UpdateObject2D(world);             
                }
            }
        }

        public void onMouseMoved(GameWorld world, float positionX, float positionY){
            this.positionCursorX = positionX;
            this.positionCursorY = positionY;
        }
        
        public Object2D createObject2D(GameWorld world, Object2DEditorFactory factory){
            Object2D object = factory.createObject2D(world.world, this.positionCursorX / P2M, this.positionCursorY / P2M, 0);

            world.addObject2DToWorld(object, true);
                 
            this.mapObject2DToFactory.put(object, factory);
            
            return object;
        }
        
        public Object2D createObject2D(GameWorld world, Object2DEditorFactory factory, Vector2 position, float angle){
            Object2D object = factory.createObject2D(world.world, position.x, position.y, angle);

            world.addObject2DToWorld(object, true);
                 
            this.mapObject2DToFactory.put(object, factory);
            
            return object;
        }
        
        public void deleteSelectedObjects(GameWorld world){
            for(Object2D obj : this.touchedObjects){
                world.addObject2D2Flush(obj); 
                
                this.mapObject2DToFactory.remove(obj);
                
                this.UpdateObject2D(world); 
                
                world.step(0); 
            }
            
            this.touchedObjects.clear();
        }
        
        public void rotateSelectedObjects(EventType eventType, float deltaTime){
            for(Object2D obj : this.touchedObjects){
                if(eventType == EventType.EDITORROTATIONRIGHT){
                    obj.physicBody.setTransform(obj.getPositionBody(), obj.physicBody.getAngle() - deltaTime * 2);
                }else if(eventType == EventType.EDITORROTATIONLEFT){
                    obj.physicBody.setTransform(obj.getPositionBody(), obj.physicBody.getAngle() + deltaTime * 2);
                }
            }
        }
        
        public void changePrioritySelectedObjects(EventType eventType){
            
            for(Object2D obj : this.touchedObjects){
                int priority = obj.getPriority();
                
                System.out.println("-----------------");
                System.out.println("old priority " + priority);
                
                if(eventType == EventType.EDITORUPPRIORITY){
                    priority++;
                }else if(eventType == EventType.EDITORDOWNPRIORITY){
                    priority--;
                }
                obj.setPriority(priority);
                
                System.out.println("new priority " + priority);
            }
        }
        
        public void saveObject2Ds(GameWorld world, String fileName){
            File saveFile = new File(fileName);
            
            if (saveFile.exists() && saveFile.isFile()) 
            { 
                saveFile.delete(); 
            } 
            try { 
                saveFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(GameWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                FileWriter writer = new FileWriter(saveFile);
                
                writer.write("//" + this.cameraPosition.x + ";" + this.cameraPosition.y);
                
                writer.write("\n\n");
                
                HashSet<String> hashSet = new HashSet<String>();
                for(Object2D obj : world.listCurrentObject2D){
                    Object2DEditorFactory factory = this.mapObject2DToFactory.get(obj);
                    
                    if(factory != null){                      
                        String line = factory.serializeStartVariable();
                        if(hashSet.contains(line) == false){
                            writer.write(line);
                            hashSet.add(line);
                        }
                    }
                }
                
                writer.write("\n\n");
                
                for(Object2D obj : world.listCurrentObject2D){
                    Object2DEditorFactory factory = this.mapObject2DToFactory.get(obj);
                    
                    if(factory != null){
                        Vector2 position = obj.getPositionBody();
                        String line = factory.serializeObject2D(position.x / P2M, position.y / P2M, obj.getAngleBody(), obj.getPriority());
                        writer.write(line);
                    }
                }
                
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(GameWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * @param world
         * @param isFactorySelected the isFactorySelected to set
         */
        public void setIsFactorySelected(GameWorld world, boolean isFactorySelected) {
            this.isFactorySelected = isFactorySelected;
            
            this.touchedObjects.clear();
                
            this.UpdateObject2D(world);
        }
        
        /**
         * @return the objectTouched
         */
        public HashSet<Object2D> getSelectedObjects() {
            return this.touchedObjects;
        }
        
        /**
         * @return the cameraPosition
         */
        /*public Vector2 getCameraPosition() {
            return this.cameraPosition;
        }*/

        /**
         * @param cameraPosition the cameraPosition to set
         */
        /*public void setCameraPosition(Vector2 cameraPosition) {
            this.cameraPosition = cameraPosition;
        }*/
        
        /**
         * @return the gameRunning
         */
        public boolean isGameRunning() {
            return this.gameRunning;
        }

        /**
         * @param multipleSelectionState the multipleSelectionState to set
         */
        public void setMultipleSelectionState(boolean multipleSelectionState) {
            this.multipleSelectionState = multipleSelectionState;
        }
        
        /**
         * @param gameRunning the gameRunning to set
         */
        public void setGameRunning(GameWorld world, boolean gameRunning) {
            this.gameRunning = gameRunning;
            
            if(this.gameRunning == false){
                for(Object2D obj2D: world.listCurrentObject2D){
                    obj2D.ReinitPlatform(world.getWorld());
                }
            }
        }

        /**
         * @return the positionTouched
         */
        public Vector2 getPositionTouched() {
            return this.positionTouched;
        }

        /**
         * @return the firstPositionTouched
         */
        public Vector2 getFirstPositionTouched() {
            return this.firstPositionTouched;
        }

        /**
         * @return the cameraPosition
         */
        public Vector2 getCameraPosition() {
            return this.cameraPosition;
        }

        /**
         * @param cameraPosition the cameraPosition to set
         */
        public void setCameraPosition(float x, float y) {
            this.cameraPosition.set(x, y);
        }

        /**
         * @return the isInEditorMode
         */
        public boolean isInEditorMode() {
            return this.isInEditorMode;
        }

        /**
         * @param isInEditorMode the isInEditorMode to set
         */
        public void setIsInEditorMode(boolean isInEditorMode) {
            this.isInEditorMode = isInEditorMode;
        }
    }
}
