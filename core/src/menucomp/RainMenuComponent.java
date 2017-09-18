/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menucomp;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import guicomponents.GuiComponent;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class RainMenuComponent extends GuiComponent{
    private static final String TEXT = "background/Pluie-01.png";
    
    private static final float SCALE_X = 2f;
    private static final float SCALE_Y = 2f;
    
    public RainMenuComponent(){
        super();
        // Part graphic
        this.assignTextures();
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(TEXT, this); 
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, /*228*/ 76, 76);
            // walk folded
            Array<TextureRegion> array = new Array<TextureRegion>();
            array.addAll(tmp[0]);
            //array.addAll(tmp[2]);
            array.addAll(tmp[3]);
            this.listAnimations.add(new Animation(0.2f, array));
            this.listAnimations.get(this.listAnimations.size() - 1).setPlayMode(Animation.PlayMode.LOOP);

            array = new Array<TextureRegion>();
            array.add(tmp[1][0]);
            this.listAnimations.add(new Animation(1f, array));

            this.changeAnimation(0, false, (float) (Math.random() * this.listAnimations.get(0).getAnimationDuration()));  
        }
    }
    
    @Override
    public void drawBatch(Camera camera, Batch batch) {
        Sprite sprite = this.createCurrentSprite(camera);
        
        if(sprite != null){
            sprite.setAlpha(0.3f);
            
            batch.setColor(sprite.getColor());
            batch.draw(sprite, 
                        sprite.getX(), sprite.getY(),
                        sprite.getOriginX(), sprite.getOriginY(),
                        sprite.getWidth(),sprite.getHeight(),
                        sprite.getScaleX() * SCALE_X,sprite.getScaleY() * SCALE_Y,
                        sprite.getRotation());
        }
    }

    @Override
    public void drawShapeRenderer(Camera camera, ShapeRenderer shapeRenderer) {
        
    }
}
