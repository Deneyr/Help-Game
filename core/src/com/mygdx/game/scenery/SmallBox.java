/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Character2D;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.Object2DStateListener;
import com.mygdx.game.UpTriggeredObject2D;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author françois
 */
public class SmallBox extends Character2D{

    private static final Texture SMALLBOXTEXT = new Texture("destroyable" + File.separator + "SpritemapCaisse.png");
    
    public SmallBox(World world, float posX, float posY) {
        super(3);
        
        this.priority = 2;
        
        this.side = SideCharacter.RIGHT;
       
        
        // Part graphic
        this.texture = SMALLBOXTEXT;
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 40, 37);
        // walk folded
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(1, 3);
        this.listAnimations.add(new Animation(0.2f, array));

        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 0);
        array.removeRange(1, 2);
        this.listAnimations.add(new Animation(0.2f, array));

        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 1);
        array.removeRange(1, 1);
        this.listAnimations.add(new Animation(0.2f, array));
        
        array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(0, 2);
        this.listAnimations.add(new Animation(0.2f, array));
        
        this.changeAnimation(0, true);
        
        // Part Physic
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);
        this.physicBody = body;

        // Collision fixture
        FixtureDef fixtureDef = new FixtureDef();
        
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(20 * P2M, 16 * P2M, new Vector2( 0, 0), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = collisionBox;
        fixtureDef.density = 8f; 
        fixtureDef.friction = 0.02f;
        fixtureDef.restitution = 0.005f; 

        Fixture fix = this.physicBody.createFixture(fixtureDef);
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);

        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox(21 * P2M, 17 * P2M, new Vector2( 0, 0), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = feet;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f; 

        fix = body.createFixture(fixtureDef);
        fix.setSensor(true);
        fix.setUserData(this);
        
        // damage & bounce scale
        //this.scaleDamageForce = 0.01f;
        
        this.feetFixture = fix;
        
        this.hasLifeBar = false;
        
    }
    
    @Override
    public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
        damage = 1;
        
        float sign = -Math.signum((new Vector2(1f, 0f).dot(dirDamage)));
        
        Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);
        ptApplication.add(this.physicBody.getPosition());

        boolean result = this.applyDamage(damage, dirDamage, damageOwner, ptApplication);
        
        int animationUpdated = 3 - this.getLifePoints();
        
        if(this.currentAnimation != animationUpdated){
            this.changeAnimation(animationUpdated, true);
        }
        
        if(result && this.getLifePoints() <= 0){
            this.notifyObject2DStateListener(Object2DStateListener.Object2DState.DEATH, 6, false);
            
            this.notifyObject2D2CreateListener(UpTriggeredObject2D.class, this.getPositionBody().scl(1 / P2M), dirDamage.scl(0.1f));
        }
        
        return result;
    }
    
    
    @Override
    public void applyBounce(Vector2 bounceVector, Object2D bounceOwner){
            
        float sign = -Math.signum((new Vector2(1f, 0f).dot(bounceVector)));

        Vector2 ptApplication = new Vector2(15 * P2M * sign, 0);
        
        super.applyBounce(bounceVector, bounceOwner, ptApplication.add(this.physicBody.getPosition()));
    }
}
