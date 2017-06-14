/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import guicomponents.GuiComponent;

/**
 *
 * @author Deneyr
 */
public class Animation implements Disposable{

    private float startTime;
    private float lengthAnimationTime;
    private float totallengthTime;

    private GuiComponent guiComponent;
    
    private RunType runType;
    private AnimationState animationState;
    
    private Color startColor;
    private Color endColor;
    private boolean isColorAnimate;
    
    private Vector2 startPosition;
    private Vector2 endPosition;
    private boolean isPositionAnimate;
    
    private Vector2 startScale;
    private Vector2 endScale;
    private boolean isScaleAnimate;
    
    private float startRotation;
    private float endRotation;
    private boolean isRotationAnimate;
    
    // TODO Camera
    
    public Animation(GuiComponent guiComponent, float startTime, float lengthAnimationTime, float totallengthTime){
        this.guiComponent = guiComponent;
        
        this.startTime = startTime;
        this.lengthAnimationTime = lengthAnimationTime;
        this.totallengthTime = totallengthTime;
        
        this.isColorAnimate = false;
        this.isPositionAnimate = false;
        this.isScaleAnimate = false;
        
        this.animationState = AnimationState.START;
    }
    
    public void updateAnimation(float timeElapsed, MenuManager menuManager){
        
    }
    
    /**
     * @return the animationState
     */
    public AnimationState getAnimationState() {
        return animationState;
    }
    
    /**
     * @return the GuiComponent
     */
    public GuiComponent getGuiComponent() {
        return this.guiComponent;
    }

    @Override
    public void dispose() {
        this.getGuiComponent().dispose();
    }
    
    public enum RunType{
        NORMAL,
        RESTART
    }
    
    public enum AnimationState{
        START,
        RUN,
        END
    }
}
