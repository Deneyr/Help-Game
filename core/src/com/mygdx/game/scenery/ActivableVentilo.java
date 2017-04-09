/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;

/**
 *
 * @author françois
 */
public class ActivableVentilo extends Ventilo{

    private ButtonObject2D button;
    
    private boolean canBeActivated;
    
    public ActivableVentilo(World world, float posX, float posY, float strength, float angle, boolean start) {
        super(world, posX, posY, strength, angle, start);
        
        TextureRegion[][] tmp = TextureRegion.split(this.texture, 152, 152);
        
        Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
        array.removeRange(2, 2);
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.NORMAL);
        
        this.listAnimations.add(new Animation(0.2f, array));
        this.listAnimations.get(this.listAnimations.size()-1).setPlayMode(Animation.PlayMode.REVERSED);
        
        this.button = new ButtonObject2D(this, world, posX + (40 * SCALE_X), posY, 40 * SCALE_X);
        
        this.changeAnimation(1, false);
        this.isWorking = true;
        
        this.canBeActivated = true;
    }
    
    public void activateVentilo(){
        
        if(!this.canBeActivated){
            return;
        }
        
        if(this.isWorking){
            this.changeAnimation(3, false);
            
            for(WindObject2D windUnit : this.wind){
                windUnit.changeAnimation(-1, false);
            }
        }else{
            this.changeAnimation(2, false);
            
            for(WindObject2D windUnit : this.wind){
                windUnit.changeAnimation(0, false);
            }
        }
        
        this.isWorking = !this.isWorking;
        
        this.canBeActivated = false;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                ActivableVentilo.this.canBeActivated = true;
            }
        }, 1f);
    }
    
    @Override
    public void dispose(){
        for(WindObject2D windUnit : this.wind){
            windUnit.dispose();
        }
        this.wind.clear();
        this.button.dispose();
        this.button = null;
        
        super.dispose();
    }
    
    public class ButtonObject2D extends Object2D{
        private ActivableVentilo ventilo;
        
        public ButtonObject2D(ActivableVentilo ventilo, World world, float posX, float posY, float offsetX){
        
            this.ventilo = ventilo;
            
            // Part physic
            BodyDef groundBodyDef = new BodyDef();    
            groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
            // Create a body from the defintion and add it to the world
            Body groundBody = world.createBody(groundBodyDef);
            groundBody.setType(BodyDef.BodyType.DynamicBody);


            PolygonShape ground = new PolygonShape();
            ground.setAsBox(75 * P2M * SCALE_X, 30 * P2M * SCALE_Y, new Vector2(0, 0), 0);
            // Set the polygon shape as a box which is twice the size of our view port and 20 high
            // (setAsBox takes half-width and half-height as arguments)
            FixtureDef fixtureDef2 = new FixtureDef();
            fixtureDef2.shape = ground;
            fixtureDef2.density = 1f; 
            fixtureDef2.friction = 0.05f;
            fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
            // Create a fixture from our polygon shape and add it to our ground body  
            Fixture fix = groundBody.createFixture(fixtureDef2); 
            fix.setSensor(true);
            fix.setUserData(this);

            this.physicBody = groundBody;

            WeldJointDef jointDef = new WeldJointDef ();
            jointDef.bodyA = ventilo.physicBody;
            jointDef.bodyB = this.physicBody;
            jointDef.localAnchorA.set(new Vector2(offsetX * P2M ,0));
            jointDef.localAnchorB.set(new Vector2(0 ,0));
            jointDef.collideConnected = false;

            world.createJoint(jointDef);    
            
        }
        
        @Override
        public boolean applyDamage(int damage, Vector2 dirDamage, Object2D damageOwner){
            this.ventilo.activateVentilo();
            return true;
        }
        
        @Override
        public void dispose(){
            this.ventilo = null;
            
            super.dispose();
        }
    }
    
}
