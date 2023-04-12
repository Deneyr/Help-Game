/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;

/**
 *
 * @author Deneyr
 */
public class Antenna extends ObstacleObject2D{
    private static final String[] OBJECT_ARRAY = {
        "urbanObj/Help_Props_Parabole_80x100.png"};
    
    public Antenna(World world, float posX, float posY, int indexAntenna, int side){
        super(world, posX, posY, 0, 1f, side, indexAntenna, OBJECT_ARRAY[indexAntenna]);
    }

    @Override
    protected void createCollisions(Body groundBody) {
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        PolygonShape ground;
        Fixture fix;
        
        switch(this.indexObject){
            case 0:
                
                ground = new PolygonShape();
                ground.setAsBox(10 * P2M * this.scale, 40 * P2M * this.scale, new Vector2(10 * P2M * this.scale * this.side, 10 * P2M * this.scale), this.side == 1 ? -(float) Math.PI / 4 : -(float) (3 * Math.PI / 4));
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(15 * P2M * this.scale, 20 * P2M * this.scale, new Vector2(12 * P2M * this.scale * this.side, -20 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                break;
        }    
    }
}
