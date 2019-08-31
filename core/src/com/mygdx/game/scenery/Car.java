/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public class Car extends ObstacleObject2D{
    private static final String[] OBJECT_ARRAY = {
        "car/Obstacle_Voiture.png"};
    
    public Car(World world, float posX, float posY, int indexTrash, int side){
        super(world, posX, posY, 0f, 0.8f, side, indexTrash, OBJECT_ARRAY[indexTrash]);
    }

    @Override
    protected void createCollisions(Body groundBody) {
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = ground;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body 
        
        switch(this.indexObject){
            case 0:
                ground.setAsBox(95 * P2M * this.scale, 21 * P2M * this.scale, new Vector2(0, -20 * P2M * this.scale), 0);
                
                Fixture fix = groundBody.createFixture(fixtureDef); 
                
                ground.setAsBox(63 * P2M * this.scale, 20 * P2M * this.scale, new Vector2(35 * P2M * this.scale * this.side, 20 * P2M * this.scale), 0);
                
                fix = groundBody.createFixture(fixtureDef); 
                break;
        }
    }
}
