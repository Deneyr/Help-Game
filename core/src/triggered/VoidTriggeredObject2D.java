/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Character2D;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class VoidTriggeredObject2D extends TriggeredObject2D{
    
    private static final String VOIDTEXT = "void.png";
    
    private static final float SCALE_X = 2f;
    private static final float SCALE_Y = 2f;
    
    private int repeatWidth;
    
    public VoidTriggeredObject2D(World world, float posX, float posY, int repeatWidth){
        
        this.priority = 100;
        
        this.repeatWidth = repeatWidth;
        
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(VOIDTEXT, this);
        
        if(this.texture != null){
            this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        TextureRegion imgTextureRegion = new TextureRegion(this.texture);
        imgTextureRegion.setRegion(0, 0, this.texture.getWidth() * this.repeatWidth, this.texture.getHeight());
        sprite.setRegion(imgTextureRegion);  
        
        sprite.setScale(sprite.getScaleX() * this.repeatWidth * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        PolygonShape damageShape = new PolygonShape();

        damageShape.setAsBox(256 * this.repeatWidth * P2M * SCALE_X, 256 * P2M * SCALE_Y / 3, new Vector2(0, 0), 0);
        
        super.initialize(world, position, speed, damageShape);

        damageShape.setAsBox(256 * this.repeatWidth * P2M * SCALE_X, 256 * P2M * SCALE_Y, new Vector2(0, 0), 0);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = damageShape;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 
              
        Fixture fix = this.physicBody.createFixture(fixtureDef);       
        
        fix.setSensor(true);
        fix.setUserData(this);
        
        //this.physicBody.setTransform(this.getPositionBody(), 0);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && 
                (objThatTriggered instanceof Character2D)){
            Vector2 dirDamage = new Vector2(0, -1);
            
            Character2D chara = (Character2D) objThatTriggered;
                
            boolean damageApplied = chara.applyDamage(100, Vector2.Zero, this);
            
            /*if(damageApplied){
                this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "barbed", new Vector2(this.getPositionBody()));
            }*/
        }
    }
}