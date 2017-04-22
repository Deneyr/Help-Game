/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import characters.Grandma;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;

/**
 *
 * @author françois
 */
public class UpTriggeredObject2D extends TriggeredObject2D{

    private static final Texture UPTEXTURE = new Texture("Collectible_Piece_spritemap.png");
    
    private boolean isTriggered;
    
    public UpTriggeredObject2D(){
        super();
        
        // Part graphic
        this.texture = UPTEXTURE;
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 36, 34);
        // walk folded
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.LOOP);

        
        array = new Array<TextureRegion>(tmp[1]);
        array.removeRange(3, 7);
        this.listAnimations.add(new Animation(0.2f, array));
        
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        float radius = 18;
        
        super.initialize(world, position, speed, radius * 1.1f);
        
        this.isTriggered = false;
        
        this.changeAnimation(0, false);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(radius * P2M);
        circle.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.8f; 
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        fix.
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && objThatTriggered instanceof Grandma){
            
            Grandma grandma = (Grandma) objThatTriggered;
            
            if(grandma.getLifePoints() < grandma.getLifePointsMax()){
                
                this.isTriggered = true;
                grandma.setLifePoints(grandma.getLifePoints() + 1);

                this.changeAnimation(1, false);

                super.trigger(objThatTriggered);
            }
        }
    }
    
    
    
    @Override
    public boolean IsDynamicObject(){
        return true;
    }
}
