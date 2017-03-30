/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author françois
 */
public class GameContactListener implements ContactListener{
    
    @Override
    public void beginContact(Contact cntct) {
        Fixture fixA = cntct.getFixtureA();
        Fixture fixB = cntct.getFixtureB();
        
        if(fixA.getUserData() != null && fixB.getUserData() != null){
            if(!(fixA.getUserData() instanceof ActionFixtures) && !(fixB.getUserData() instanceof ActionFixtures)){
                Object2D objA = null;
                Object2D objB = null;

                if(fixA.isSensor() && !fixB.isSensor()){
                    objA = (Object2D) fixA.getUserData();
                    objB = (Object2D) fixB.getUserData();
                }else if(fixB.isSensor() && !fixA.isSensor()){
                    fixA = cntct.getFixtureB();
                    objA = (Object2D) fixA.getUserData();
                    fixB = cntct.getFixtureA();
                    objB = (Object2D) fixB.getUserData();
                }
                if(objA != null && objB != null){
                    // Part check on ground
                    if(objA instanceof Character2D && ((Character2D)objA).isFixtureFeet(fixA)){
                        ((Character2D)objA).addStaticObj();
                    }
                }
            }else if(fixA.getUserData() instanceof ActionFixtures && !(fixB.getUserData() instanceof ActionFixtures)){
                ActionFixtures actionFixtures = (ActionFixtures) fixA.getUserData();
                actionFixtures.addObject2dInside((Object2D) fixB.getUserData());
            }else if(fixB.getUserData() instanceof ActionFixtures && !(fixA.getUserData() instanceof ActionFixtures)){
                ActionFixtures actionFixtures = (ActionFixtures) fixB.getUserData();
                actionFixtures.addObject2dInside((Object2D) fixA.getUserData());
            }
        }
    }

    @Override
    public void endContact(Contact cntct) {
        Fixture fixA = cntct.getFixtureA();
        Fixture fixB = cntct.getFixtureB();

        if(fixA.getUserData() != null && fixB.getUserData() != null){
            if(!(fixA.getUserData() instanceof ActionFixtures) && !(fixB.getUserData() instanceof ActionFixtures)){
                Object2D objA = null;
                Object2D objB = null;

                if(fixA.isSensor() && !fixB.isSensor()){
                    objA = (Object2D) fixA.getUserData();
                    objB = (Object2D) fixB.getUserData();
                }else if(fixB.isSensor() && !fixA.isSensor()){
                    fixA = cntct.getFixtureB();
                    objA = (Object2D) fixA.getUserData();
                    fixB = cntct.getFixtureA();
                    objB = (Object2D) fixB.getUserData();
                }
                if(objA != null && objB != null){
                    // Part check on ground
                    if(objA instanceof Character2D && ((Character2D)objA).isFixtureFeet(fixA)){
                        ((Character2D)objA).removeStaticObj();
                    }
                }
            }else if(fixA.getUserData() instanceof ActionFixtures && !(fixB.getUserData() instanceof ActionFixtures)){
                ActionFixtures actionFixtures = (ActionFixtures) fixA.getUserData();
                actionFixtures.removeObject2dInside((Object2D) fixB.getUserData());
            }else if(fixB.getUserData() instanceof ActionFixtures && !(fixA.getUserData() instanceof ActionFixtures)){
                ActionFixtures actionFixtures = (ActionFixtures) fixB.getUserData();
                actionFixtures.removeObject2dInside((Object2D) fixA.getUserData());
            }
        }
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {

    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {

    }
   
}