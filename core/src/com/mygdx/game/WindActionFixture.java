/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.Character2D.SideCharacter;
import java.util.Set;

/**
 *
 * @author françois
 */
public class WindActionFixture extends ActionFixtures{

    private float strength;
    
    public WindActionFixture(Set<Fixture> fixtures, float strength ){
        super(fixtures);
        
        this.strength = strength;
    }
    
    @Override
    public void addObject2dInside(Object2D obj){
        if(obj instanceof Character2D){
            super.addObject2dInside(obj);
        }
    }
    
    @Override
    public void applyAction(float deltaTime, Object2D owner) {
        super.applyAction(deltaTime, owner);
        
        for(Object2D obj : this.setObject2DInside){
            float angle = owner.physicBody.getAngle();
            Vector2 normVector = new Vector2(1, 0);
            normVector = normVector.rotateRad(angle);
           
            if(obj instanceof Grandma){
                Grandma grandma = (Grandma) obj;
                
                /*
                double computedAngle = angle%(2*Math.PI);
                if(computedAngle < 0){
                    computedAngle += 2*Math.PI;
                }
                
                computedAngle = Math.toDegrees(computedAngle);
                
                if((computedAngle > Math.PI/5 && computedAngle < 4*Math.PI/5)
                        || (computedAngle > 6 * Math.PI/5 && computedAngle < 9*Math.PI/5)){
                    if(grandma.stateUmbrella() == 1){
                        obj.physicBody.applyForceToCenter(normVector.setLength(strength*deltaTime*10), true);
                        return;
                    }
                }else{
                    if(grandma.stateUmbrella() != -1 || grandma.getSideCharacter() == ((computedAngle > Math.PI/2 && computedAngle < 3*Math.PI/2) ? SideCharacter.LEFT : SideCharacter.RIGHT)){
                        obj.physicBody.applyForceToCenter(normVector.setLength(strength*deltaTime*10), true);
                        return;
                    }
                }*/
                
                Vector2 umbrellaDirection = grandma.GetUmbrellaDirection();
                boolean isUmbrellaUnfolded = grandma.isUmbrellaUnfolded();
                
                float umbrellaVentiloDot = normVector.dot(umbrellaDirection);
                
                float forceStrength = this.strength;
                
                if(isUmbrellaUnfolded){
                    if(umbrellaVentiloDot < -0.3f){
                        forceStrength = forceStrength / 100;
                    }else if(umbrellaVentiloDot > 0.3f){
                        forceStrength = this.strength;
                    }else{
                        
                        float ventiloOrientation = (new Vector2(1, 0)).dot(normVector);
                        
                        if(Math.abs(ventiloOrientation) > 0.3){
                            forceStrength = forceStrength * 10;
                        }else{
                            forceStrength = forceStrength / 10;
                        }
                    }
                }else{
                    if(Math.abs(umbrellaVentiloDot) > 0.3){
                        forceStrength = forceStrength * 10;
                    }else{
                        forceStrength = forceStrength / 10;
                    }
                }
                
                obj.physicBody.applyForceToCenter(normVector.setLength(forceStrength), true);
            }else{
                obj.physicBody.applyForceToCenter(normVector.setLength(this.strength*20), true);
            }
        }
    }
    
}
