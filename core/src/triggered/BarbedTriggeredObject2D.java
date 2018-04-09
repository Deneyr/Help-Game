/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
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
public class BarbedTriggeredObject2D extends TriggeredObject2D{
    private static final String BARBEDTEXT = "urbanObj/WarningBarbed.png";
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    
    public BarbedTriggeredObject2D(World world, float posX, float posY){
        
        // Part graphic
        this.assignTextures();
        
        // Part physic.
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(BARBEDTEXT, this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        PolygonShape damageShape = new PolygonShape();

        damageShape.setAsBox(65 * P2M * SCALE_X, 30 * P2M * SCALE_Y);
        
        super.initialize(world, position, speed, damageShape);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = damageShape;
        fixtureDef.density = 0f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 
        
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);       
        
        fix.setSensor(true);
        fix.setUserData(this);
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
                
                damageApplied = grandma.applyDamage(3, dirDamage, this);
                
            }else{
                Character2D chara = (Character2D) objThatTriggered;
                
                damageApplied = chara.applyDamage(50, dirDamage, this);
            }
            
            if(damageApplied){
                this.notifyGameEventListener(GameEventListener.EventType.ATTACK, "barbed", new Vector2(this.getPositionBody()));
            }
        }
    }
}
