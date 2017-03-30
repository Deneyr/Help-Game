/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author françois
 */
public abstract class ActionFixtures{
    
    protected Set<Fixture> fixtures;
    
    protected List<Object2D> object2DInside;
    
    protected Set<Object2D> setObject2DInside;
    
    public ActionFixtures(Set<Fixture> fixtures){
        this.fixtures = fixtures;
        
        this.object2DInside = new ArrayList<Object2D>();
        
        this.setObject2DInside = new HashSet<Object2D>();
        
        for(Fixture fix : this.fixtures){
            fix.setUserData(this);
            fix.setSensor(true);
        }
    }
    
    public void addObject2dInside(Object2D obj){
        
        this.object2DInside.add(obj);
    }
    
    public void removeObject2dInside(Object2D obj){
        
        this.object2DInside.remove(obj);
    }
    
    public void applyAction(float deltaTime, Object2D owner){
        applyDefaultAction();
    }

    /**
     * @return the fixtures
     */
    public Set<Fixture> getFixtures() {
        return this.fixtures;
    }
    
    public void dispose(Body owner){
        for(Fixture fixture : this.fixtures){
            owner.destroyFixture(fixture);
        }
        this.object2DInside.clear();
        this.setObject2DInside.clear();
    }
    
    protected void applyDefaultAction(){
        this.setObject2DInside.clear();
        for(Object2D obj : this.object2DInside){
            if(!this.setObject2DInside.contains(obj)){
                this.setObject2DInside.add(obj);
            }
        }
    }
}
