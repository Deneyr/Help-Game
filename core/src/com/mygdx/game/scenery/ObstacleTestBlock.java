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
import com.badlogic.gdx.physics.box2d.CircleShape;
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
public class ObstacleTestBlock extends SolidObject2D{
    private static final String[] OBSTACLETESTBLOCK_ARRAY = {
    "editorTestBlock/Blocs_100x100.png",
    "editorTestBlock/Blocs_50x100.png",
    "editorTestBlock/Blocs_150x50.png",
    "editorTestBlock/Blocs_200x100.png",
    "editorTestBlock/Blocs_200x200.png",
    "editorTestBlock/Blocs_300x300.png",
    "editorTestBlock/Blocs_300x50.png",
    "editorTestBlock/Blocs_100x50.png",
    "editorTestBlock/Blocs_50x300.png",
    "editorTestBlock/Blocs_50x50.png",
    "editorTestBlock/Help_BlocNoir_150x150.png",
    "editorTestBlock/Blocs_Help_Fenetre_50x100.png",
    "editorTestBlock/Bloc_Cercle_102x102.png",
    "editorTestBlock/Bloc_Cercle_302x302.png"};  
    
    private int testBlockIndex;
    
    private static final float SCALE_X = 1f;
    private static final float SCALE_Y = 1f;
    
    public ObstacleTestBlock(World world, float posX, float posY, int index, float scale){
        
        this.testBlockIndex = index;
        
        this.scale = scale;
        
        // Part graphic
        this.assignTextures();
        
        // Part physic
        
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        this.priority = 4;
        
        // Create a polygon shape
        
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        PolygonShape ground = new PolygonShape();
        CircleShape circle = null;
        
        switch(this.testBlockIndex){
            case 0:
                ground.setAsBox(50 * this.scale * P2M * SCALE_X, 50 * this.scale * P2M * SCALE_Y);
                break;
            case 1:
                ground.setAsBox(25 * this.scale * P2M * SCALE_X, 50 * this.scale * P2M * SCALE_Y);
                break;
            case 2:
                ground.setAsBox(75 * this.scale * P2M * SCALE_X, 25 * this.scale * P2M * SCALE_Y);
                break;
            case 3:
                ground.setAsBox(100 * this.scale * P2M * SCALE_X, 50 * this.scale * P2M * SCALE_Y);
                break; 
            case 4:
                ground.setAsBox(100 * this.scale * P2M * SCALE_X, 100 * this.scale * P2M * SCALE_Y);
                break; 
            case 5:
                ground.setAsBox(150 * this.scale * P2M * SCALE_X, 150 * this.scale * P2M * SCALE_Y);
                break;
            case 6:
                ground.setAsBox(150 * this.scale * P2M * SCALE_X, 25 * this.scale * P2M * SCALE_Y);
                break;
            case 7:
                ground.setAsBox(50 * this.scale * P2M * SCALE_X, 25 * this.scale * P2M * SCALE_Y);
                break;
            case 8:
                ground.setAsBox(25 * this.scale * P2M * SCALE_X, 150 * this.scale * P2M * SCALE_Y);
                break;
            case 9:
                ground.setAsBox(25 * this.scale * P2M * SCALE_X, 25 * this.scale * P2M * SCALE_Y);
                break;
                
            // Help_Decor
            case 10:
                ground.setAsBox(76 * this.scale * P2M * SCALE_X, 76 * this.scale * P2M * SCALE_Y);               
                break;
                
            case 11:
                this.priority = 5;
                
                ground.setAsBox(11 * this.scale * P2M * SCALE_X, 16 * this.scale * P2M * SCALE_Y);
                break;
                
            // Circles
            case 12:
                circle = new CircleShape();
                circle.setRadius(51 * P2M * this.scale);              
                break;
                
            case 13:
                circle = new CircleShape();
                circle.setRadius(151 * P2M * this.scale);    
                break;
        }
        
        if(circle != null){
            fixtureDef.shape = circle; 
        }else{
            fixtureDef.shape = ground; 
        }
        
        Fixture fix = groundBody.createFixture(fixtureDef); 
        fix.setUserData(this);
        
        if(this.testBlockIndex == 11){
            fix.setSensor(true);
        }
        
        this.collisionFixture.add(fix);
        
        this.physicBody = groundBody;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(OBSTACLETESTBLOCK_ARRAY[this.testBlockIndex], this);
    }
    
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * SCALE_X, sprite.getScaleY() * SCALE_Y);
        return sprite;
    }
}