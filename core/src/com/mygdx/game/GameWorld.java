/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author françois
 */
public class GameWorld implements WorldPlane{
    private World world;
    
    private List<Object2D> listCurrentObject2D = new ArrayList<Object2D>();
    
    private Character2D hero; 
    
    public GameWorld(){
        this.world = new World(new Vector2(0, -20f), true);
        this.hero = null;
        
        this.world.setContactListener(new GameContactListener());
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
        Iterator<Object2D> it = this.listCurrentObject2D.iterator();
        while(it.hasNext()){
            it.next().updateLogic(delta);
        }
        
        this.world.step(delta, 6, 2);
    }
    
    public void addObject2DToWorld(Object2D obj){
        this.listCurrentObject2D.add(obj);
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
        
        addObject2DToWorld(hero);
    }
    
    @Override
    public void flushWorld(){
        this.world.dispose();
        this.world = new World(new Vector2(0, -20f), true);
        this.hero = null;
        this.listCurrentObject2D = new ArrayList();
        
        this.world.setContactListener(new GameContactListener());
    }
    
    public Vector2 getHeroPosition(){
        if(this.hero.getSideCharacter() == Character2D.SideCharacter.LEFT){
            return new Vector2(this.hero.getPositionBody().add(Grandma.LEFT_RIGHT_DIST * P2M / 2f, 0));
        }else{
            return new Vector2(this.hero.getPositionBody().add(-Grandma.LEFT_RIGHT_DIST * P2M / 2f, 0));
        }
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
    
}
