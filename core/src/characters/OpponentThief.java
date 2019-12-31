/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Character2D;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import cosmetics.HitCosmeticObject2D;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class OpponentThief extends OpponentCAC1{
    private static final String OPPTHIEFTEXT = "character/spritemapkairaVoleur-01.png";
    
    public OpponentThief(World world, Object2D target){
        super(400, target);
    }
    
    public OpponentThief(World world, Object2D target, float posX, float posY){
        super(400, target);
        this.spawnPoint = new Vector2(posX * P2M, posY * P2M);
        
        this.maxSpeed = 6f;
        
        // Part graphic
        this.assignTextures();
        
        this.initializePhysicCAC1(world, posX, posY);
        
        this.updateFixture();
        
        this.previousSide = this.side;
        
        this.hasLifeBar = false;
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        
        boolean isDamaged = super.applyDamage(damage, Vector2.Zero, damageOwner);
        
        if(isDamaged){
            Vector2 upVector = new Vector2(0, 1);
            float angle = dirDamage.angle(upVector) / 2f;
            dirDamage = dirDamage.rotate(angle);
            
            float ratioDamage = damage / 100f;
            if(ratioDamage > 0 && !dirDamage.epsilonEquals(Vector2.Zero, 0.01f)){
                                
                this.physicBody.applyLinearImpulse(dirDamage.scl(ratioDamage * 100.f * this.scaleDamageForce), Vector2.Zero, true);
            }
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner, Vector2 ptApplication){
        boolean isDamaged = super.applyDamage(damage, Vector2.Zero, damageOwner, ptApplication);
        
        if(isDamaged){
            
            Vector2 upVector = new Vector2(0, 1);
            float angle = dirDamage.angle(upVector) / 2f;
            dirDamage = dirDamage.rotate(angle);
            
            float ratioDamage = damage / 100f;
            if(ratioDamage > 0 && !dirDamage.epsilonEquals(Vector2.Zero, 0.01f)){                
                this.physicBody.applyLinearImpulse(dirDamage.scl(ratioDamage * 100.f * this.scaleDamageForce), ptApplication, true);
            }
            return true;
        }
        
        return false;
    }
    
    @Override
    public void assignTextures(){
        
        this.texture = TextureManager.getInstance().getTexture(OPPTHIEFTEXT, this);
        
        if(this.texture != null){
            this.initializeGraphic();
            
            // Part graphic
            TextureRegion[][] tmp = TextureRegion.split(this.texture, 76, 76);
            this.listAnimations.add(new Animation(0.2f, tmp[4]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);

            this.listAnimations.add(new Animation(0.2f, tmp[5]));
            this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);
            
            this.listAnimations.get(2).setFrameDuration(0.15f);
            this.listAnimations.get(3).setFrameDuration(0.15f);
        }
    }
}
