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
 * @author Deneyr
 */
public class Sign extends SolidObject2D{
    private static final String[] SIGNS_ARRAY = {
    "signs/Panneau_Danger.png",
    "signs/Panneau_Deplacement.png",
    "signs/Panneau_Direction_GaucheDroite.png",
    "signs/Panneau_Direction_HautBas.png",
    "signs/Panneau_Frapper.png",
    "signs/Panneau_Plannage.png",
    "signs/Panneau_Saut.png",
    "signs/Panneau_Switch.png"};
    
    private static final float SCALE_X = 0.5f;
    private static final float SCALE_Y = 0.5f;
    
    private float rotation;
    private int signIndex;
    
    public Sign(World world, float posX, float posY, float rotation, int signIndex){
        
        this.signIndex = signIndex;
       
        // Part graphic
        this.assignTextures();
        
        // Part physic
        this.rotation = rotation;
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M)); 
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 1;
        
        // Create a polygon shape
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(100 * P2M * SCALE_X, 200 * P2M * SCALE_Y, new Vector2(0, 0), 0);
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
        fix.setUserData(this);
        fix.setSensor(true);
        this.collisionFixture.add(fix);

        this.physicBody = groundBody;

        this.physicBody.setTransform(this.physicBody.getPosition().x, this.physicBody.getPosition().y, this.rotation);
        //this.physicBody.setLinearVelocity(new Vector2(0.5f, 0));
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(SIGNS_ARRAY[this.signIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        
        if(sprite != null){
            sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        }
        return sprite;
    }
}
