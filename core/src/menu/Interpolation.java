/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Deneyr
 */
public class Interpolation {
    // Part lerp sprite attributes.
    
    public static Color lerp(Color startColor, Color endColor, float alpha){
        return new Color(startColor.lerp(endColor, alpha));
    }
    
    public static float lerp(float start, float end, float alpha){
        return (end - start) * alpha;
    }
    
    public static double lerp(double start, double end, float alpha){
        return (end - start) * alpha;
    }
    
    public static Vector2 lerp(Vector2 startPosition, Vector2 endPosition, float alpha){
        return new Vector2(startPosition.lerp(endPosition, alpha));
    }
    
    // Part time interpolation.
    
    public static float interpolate1(float timeStart, float timeEnd, float currentTime){
        if(currentTime < timeStart){
            return 0;
        }
        if(currentTime > timeEnd){
            return 1;
        }
        
        float alpha = (currentTime - timeStart) / (timeEnd - timeStart);
        
        return alpha;
    }
    
    public static float interpolate2Inc(float timeStart, float timeEnd, float currentTime){
        if(currentTime < timeStart){
            return 0;
        }
        if(currentTime > timeEnd){
            return 1;
        }
        
        float alpha = (currentTime - timeStart) / (timeEnd - timeStart);
        
        return alpha * alpha;
    }
    
    public static float interpolate2Dec(float timeStart, float timeEnd, float currentTime){
        if(currentTime < timeStart){
            return 0;
        }
        if(currentTime > timeEnd){
            return 1;
        }
        
        float alpha = (currentTime - timeStart) / (timeEnd - timeStart);
        
        return 1 - (alpha - 1) * (alpha - 1);
    }
    
    public static float interpolate(float timeStart, float timeEnd, float currentTime, InterType type){
        switch(type){
            case LINEAR:
                return Interpolation.interpolate1(timeStart, timeEnd, currentTime);
            case QUADRAINC:
                return Interpolation.interpolate2Inc(timeStart, timeEnd, currentTime);
            case QUADRADEC:
                return Interpolation.interpolate2Dec(timeStart, timeEnd, currentTime);
        }
        return 0;
    }
    
    public enum InterType{
        LINEAR,
        QUADRAINC,
        QUADRADEC
    }
}
