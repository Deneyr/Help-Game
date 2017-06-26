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
import menu.Interpolation.InterpolationType;

/**
 *
 * @author Deneyr
 */
public class Animation implements Disposable{

    private float startTime;
    private float lenghtPreAnimationTime;
    private float lengthAnimationTime;
    private float totalLengthTime;

    private GuiComponent guiComponent;
    
    private RunType runType;
    private AnimationState animationState;
    private InterpolationType interpolationType;
    
    private Color startColor;
    private Color endColor;  
    private boolean colorAnimate;
    
    private Vector2 startPosition;
    private Vector2 endPosition;
    private boolean positionAnimate;
    
    private Vector2 startScale;
    private Vector2 endScale;
    private boolean scaleAnimate;
    
    private float startRotation;
    private float endRotation;
    private boolean rotationAnimate;
    
    private int indexSpriteAnimationStart;
    private int indexSpriteAnimationRun;
    private boolean spriteAnimate;
    
    // TODO Camera
    
    public Animation(GuiComponent guiComponent, RunType runType, InterpolationType interpolationType, float startTime, float lenghtPreAnimationTime, float lengthAnimationTime, float totallengthTime){
        this.guiComponent = guiComponent;
        
        this.startTime = startTime;
        this.lenghtPreAnimationTime = lenghtPreAnimationTime;
        this.lengthAnimationTime = lengthAnimationTime;
        this.totalLengthTime = totallengthTime;
        
        this.colorAnimate = false;
        this.positionAnimate = false;
        this.scaleAnimate = false;
        this.spriteAnimate = false;
        
        this.animationState = AnimationState.START;
        
        this.runType = runType;
        this.interpolationType = interpolationType;
    }
    
    public void updateAnimation(float timeElapsed, MenuManager menuManager, boolean isCameraAnimation){
             
        switch(this.animationState){
            case START:
                
                if(timeElapsed >= this.startTime + this.lenghtPreAnimationTime){
                    this.animationState = AnimationState.RUN;
                    
                    if(this.spriteAnimate){
                        this.guiComponent.changeAnimation(this.indexSpriteAnimationRun, false);
                    }
                    
                }
                
                break;
            case RUN:
                
                if(timeElapsed >= this.startTime + this.lenghtPreAnimationTime + this.lengthAnimationTime){
                    this.animationState = AnimationState.END;
                }
                
                break;
            case END:
                
                if(this.runType == RunType.RESTART){
                    this.startTime = timeElapsed;
                    this.animationState = AnimationState.START;
                    
                    if(this.spriteAnimate){
                        this.guiComponent.changeAnimation(this.indexSpriteAnimationStart, false);
                    }
                }
                
                break;
        }
        
        
        if(this.animationState == AnimationState.RUN){
            if(isCameraAnimation){
                if(this.isPositionAnimate()){
                    Vector2 newPosition = this.createCurrentVector2(this.startPosition, this.endPosition, timeElapsed);
                    menuManager.setCameraPosition(newPosition.x, newPosition.y);
                }
                
                if(this.isScaleAnimate()){
                    Vector2 newScale = this.createCurrentVector2(this.startScale, this.endScale, timeElapsed);
                    menuManager.setCameraScale(newScale.x, newScale.y);
                }
                
                if(this.isRotationAnimate()){
                    float newRotation = this.createCurrentFloat(this.startRotation, this.endRotation, timeElapsed);
                    menuManager.setCameraRotation(newRotation);
                }
            }else{
                if(this.isPositionAnimate()){
                    Vector2 newPosition = this.createCurrentVector2(this.startPosition, this.endPosition, timeElapsed);
                    this.guiComponent.setLocation(newPosition.x, newPosition.y);
                }
                
                if(this.isScaleAnimate()){
                    Vector2 newScale = this.createCurrentVector2(this.startScale, this.endScale, timeElapsed);
                    this.guiComponent.setSpriteScale(newScale);
                }
                
                if(this.isRotationAnimate()){
                    float newRotation = this.createCurrentFloat(this.startRotation, this.endRotation, timeElapsed);
                    this.guiComponent.setSpriteRotation(newRotation);
                }   
                
                if(this.isColorAnimate()){
                    Color newColor = this.createCurrentColor(this.startColor, this.endColor, timeElapsed);
                    this.guiComponent.setSpriteColor(newColor);
                }
            }
        }
    }
    
    private Color createCurrentColor(Color startColor, Color endColor, float timeElapsed){
        float alpha = Interpolation.interpolate(this.startTime + this.lenghtPreAnimationTime, this.startTime + this.lenghtPreAnimationTime + this.lengthAnimationTime, timeElapsed, this.interpolationType);
        Color resultColor = new Color(startColor);
        return resultColor.lerp(endColor, alpha);
    }
    
    private Vector2 createCurrentVector2(Vector2 startVector, Vector2 endVector, float timeElapsed){
        float alpha = Interpolation.interpolate(this.startTime + this.lenghtPreAnimationTime, this.startTime + this.lenghtPreAnimationTime + this.lengthAnimationTime, timeElapsed, this.interpolationType);
        Vector2 resultVector = new Vector2(startVector);
        return resultVector.lerp(endVector, alpha);
    }
    
    private float createCurrentFloat(float startFloat, float endFloat, float timeElapsed){
        float alpha = Interpolation.interpolate(this.startTime + this.lenghtPreAnimationTime, this.startTime + this.lenghtPreAnimationTime + this.lengthAnimationTime, timeElapsed, this.interpolationType);
        
        return (endFloat - startFloat) * alpha + startFloat;
    }
    
    /* Part Add Animation */
    
    public void setPositionAnimation(Vector2 startPosition, Vector2 endPosition){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.positionAnimate = true;
    }
    
    public void setScaleAnimation(Vector2 startScale, Vector2 endScale){
        this.startScale = startScale;
        this.endScale = endScale;
        this.scaleAnimate = true;
    }
    
    public void setRotationAnimation(float startRotation, float endRotation){
        this.startRotation = startRotation;
        this.endRotation = endRotation;
        this.rotationAnimate = true;
    }
    
    public void setColorAnimation(Color startColor, Color endColor){
        this.startColor = startColor;
        this.endColor = endColor;
        this.colorAnimate = true;
    }
    
    public void setSpriteIndexAnimation(int startIndex, int runIndex){
        this.indexSpriteAnimationStart = startIndex;
        this.indexSpriteAnimationRun = runIndex;
        this.spriteAnimate = true;
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
    
    /**
     * @return the isColorAnimate
     */
    public boolean isColorAnimate() {
        return this.colorAnimate;
    }

    /**
     * @return the scaleAnimate
     */
    public boolean isScaleAnimate() {
        return scaleAnimate;
    }

    /**
     * @return the rotationAnimate
     */
    public boolean isRotationAnimate() {
        return rotationAnimate;
    }

    /**
     * @return the positionAnimate
     */
    public boolean isPositionAnimate() {
        return positionAnimate;
    }
    
    /**
     * @return the positionAnimate
     */
    public boolean isSpriteAnimate() {
        return spriteAnimate;
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
