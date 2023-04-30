/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class BarbedBalloonTriggeredObject2D extends TriggeredObject2D{
    private static final String BARBEDBALLOONTEXT = "urbanObj/Help_Props_360x135_BallonPiqueV2.png";
    
    private static final float SCALE = 1f;
    
    private float angle;
    
    public BarbedBalloonTriggeredObject2D(World world, float posX, float posY, float angle, float scale){
        
        this.scale = scale;
        
        this.angle = angle;
        
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);     
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(BARBEDBALLOONTEXT, this);
        
        if(this.texture != null){
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 90, 135);
        
            Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
            this.listAnimations.add(new Animation(0.2f, array, Animation.PlayMode.LOOP));

            this.changeAnimation(0, false);
        }
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        sprite.setScale(sprite.getScaleX() * SCALE, sprite.getScaleY() * SCALE);
        return sprite;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        CircleShape circle = new CircleShape();
        circle.setRadius(40 * P2M * this.scale * SCALE);
        circle.setPosition(new Vector2(0, 0));
        
        super.initialize(world, position, speed, circle);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 
              
        Fixture fix = this.physicBody.createFixture(fixtureDef);       
        
        fix.setSensor(true);
        fix.setUserData(this);
        
        this.physicBody.setTransform(this.getPositionBody(), this.angle);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && 
                (objThatTriggered instanceof Character2D)){
            
            Vector2 targetPhysicBody = new Vector2(objThatTriggered.getPositionBody());
            Vector2 dirDamage = targetPhysicBody.sub(this.getPositionBody());
            dirDamage = dirDamage.nor();
            
            boolean damageApplied = false;
            if(objThatTriggered instanceof Grandma){
                Grandma grandma = (Grandma) objThatTriggered;
                
                damageApplied = grandma.applyDamage(4, dirDamage, this);
                
            }else{
                Character2D chara = (Character2D) objThatTriggered;
                
                damageApplied = chara.applyDamage(40, dirDamage, this);
            }
            
            if(damageApplied){
                this.notifyGameEventListener(GameEventListener.EventType.ACTION, "metalHit", new Vector2(this.getPositionBody()));
            }
        }
    }
}