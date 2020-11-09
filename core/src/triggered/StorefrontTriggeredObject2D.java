/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Character2D;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class StorefrontTriggeredObject2D extends TriggeredObject2D{

    private static final String STOREFRONTTEXT = "urbanObj/Help_Props_360x60_Deventure.png";
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    private float side;
    
    public StorefrontTriggeredObject2D(World world, float posX, float posY, int side){
        
        this.side = side;
        
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(STOREFRONTTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 120, 60);

            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            array.add(tmp[0][0]);
            this.listAnimations.add(new Animation(0.15f, array));
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        sprite.setScale(sprite.getScaleX() * SCALE_X * this.side , sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        PolygonShape damageShape = new PolygonShape();
        damageShape.set(new float[]{
            -35 * P2M, 0,
            35 * P2M, 0,
            -35 * P2M * this.side, 25 * P2M
        });
        
        super.initialize(world, position, speed, damageShape);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = damageShape;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 
              
        Fixture fix = this.physicBody.createFixture(fixtureDef);       
        
        fix.setUserData(this);
        
        this.changeAnimation(0, true);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && 
                (objThatTriggered instanceof Character2D)){
            
            Vector2 dirVector = new Vector2(30 * this.side, 50); 
            
            objThatTriggered.applyBounce(dirVector, this);
            
            this.changeAnimation(0, false);
        }
    }
}
