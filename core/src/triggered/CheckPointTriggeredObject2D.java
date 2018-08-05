/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;

/**
 *
 * @author Deneyr
 */
public class CheckPointTriggeredObject2D extends TriggeredObject2D{

    private static final Texture PILLSTEXTURE = new Texture("PillsCheckpoint.png");
    private static final Texture SAVEGEARSTEXTURE = new Texture("SaveGears.png");
    
    private float radius;
    
    private Grandma target;
    
    private int index;
    
    private Animation animationSave;
    private float animationSaveTime;
    private Texture saveTexture;
    
    private boolean isActive;
    
    public CheckPointTriggeredObject2D(World world, float posX, float posY){
        super();
        
        this.priority = 5;
        
        this.radius = 50;
        
        this.index = -1;
        
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
        // Part graphic
        this.assignTextures();
    }
    
    
    @Override
    public void trigger(Object2D objThatTriggered){
        if(this.isTriggered == false && objThatTriggered instanceof Grandma){
            this.isTriggered = true;
            
            this.target = (Grandma) objThatTriggered;
            if(this.getIndex() >= 0){
                this.notifyGameEventListener(GameEventListener.EventType.CHECKPOINT, String.valueOf(this.getIndex()), objThatTriggered.getPositionBody());
            }
            this.changeAnimation(1, false);
        }
    }

    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        this.initialize(world, position, speed, this.radius);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(this.radius * P2M);
        circle.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.8f; 
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        fix.setSensor(true);
        
        this.target = null;
        
        this.animationSaveTime = 0f;
        
        this.isActive = true;
        
        this.changeAnimation(0, false);
    }
    
    @Override
    public void updateLogic(float deltaTime){
        if(this.isActive){
            super.updateLogic(deltaTime);

            if(this.isTriggered){
                this.animationSaveTime += deltaTime;
                
                if(this.animationSave.isAnimationFinished(this.animationSaveTime)){
                    this.isActive = false;
                }
            }
        }
    }
    
    @Override
    public void assignTextures(){      
        // Part save
        this.saveTexture = SAVEGEARSTEXTURE;
        TextureRegion[][] tmp = TextureRegion.split(this.saveTexture, 72, 73);
        // walk folded
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        this.animationSave = new Animation(0.4f, array, Animation.PlayMode.NORMAL);
        
        // Part pills
        this.texture = PILLSTEXTURE;
        tmp = TextureRegion.split(this.texture, 50, 50);
        // walk folded
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(8, 11);
        this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.LOOP));
        
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 7);
        this.listAnimations.add(new Animation(0.1f, array, Animation.PlayMode.NORMAL));
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = null;
        if(this.isActive){
            if(this.isTriggered && this.isCurrentAnimationOver()){
                sprite = this.createCurrentSaveSprite();

                if(sprite != null){
                    Sprite spriteTarget = this.target.createCurrentSprite();

                    if(spriteTarget != null){
                        sprite.setPosition(spriteTarget.getX() + spriteTarget.getWidth() / 2 - 20 - 2, spriteTarget.getY() + spriteTarget.getHeight() - 2);
                    }else{
                        sprite.setPosition(this.target.getPositionBody().x / P2M, this.target.getPositionBody().y / P2M);
                    }
                }
            }else{
                sprite = super.createCurrentSprite();
            }
        }
        
        return sprite;
    }
    
    public Sprite createCurrentSaveSprite(){
        Sprite sprite = null;
        
        if(this.animationSaveTime > 0){

            TextureRegion region = this.animationSave.getKeyFrame(this.animationSaveTime);
            sprite = new Sprite(region);  
        }
        
        return sprite;
    }
    
    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }
    
        /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
