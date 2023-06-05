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
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author françois
 */
public abstract class ObstacleObject2D extends SolidObject2D{
    protected String texturePath;
    
    protected int side;
    
    protected int indexObject;
    
    public ObstacleObject2D(){
        
    }
    
    public ObstacleObject2D(World world, float posX, float posY, float rotation, float scale, int side, int indexObject, String texturePath){
        
        this.scale = scale;
        
        this.texturePath = texturePath;
        
        this.side = side;
        
        this.indexObject = indexObject;
        
        // Part graphic
        this.assignTextures();
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
        groundBodyDef.angle = rotation;
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create collisions (to override).
        this.createCollisions(groundBody);
        
        for(Fixture fixture: groundBody.getFixtureList()){
            fixture.setUserData(this);
            this.collisionFixture.add(fixture);
        }

        this.physicBody = groundBody;
        //this.physicBody.setLinearVelocity(new Vector2(0.5f, 0));
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(this.texturePath, this);
    }
    
    protected abstract void createCollisions(Body groundBody);
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();

        sprite.setScale(sprite.getScaleX() * this.side, sprite.getScaleY());

        return sprite;
    }
}