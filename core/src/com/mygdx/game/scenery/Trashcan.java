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
 * @author françois
 */
public class Trashcan extends ObstacleObject2D{
    private static final String[] OBJECT_ARRAY = {
        "urbanObj/Obstacle_Poubelle1.png",
        "urbanObj/Obstacle_Poubelle2.png"};
    
    public Trashcan(World world, float posX, float posY, int indexTrash, int side){
        super(world, posX, posY, 0f, 0.4f, side, indexTrash, OBJECT_ARRAY[indexTrash]);
    }

    @Override
    protected void createCollisions(Body groundBody) {
        PolygonShape ground = new PolygonShape();
        
        switch(this.indexObject){
            case 1:
                ground.setAsBox(100 * P2M * this.scale, 55 * P2M * this.scale, new Vector2(60 * P2M * this.scale * this.side, 0), 0);
                break;
            default :
                ground.setAsBox(63 * P2M * this.scale, 102 * P2M * this.scale, new Vector2(0, 0), 0);
        }
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        Fixture fix = groundBody.createFixture(fixtureDef); 
    }
}
