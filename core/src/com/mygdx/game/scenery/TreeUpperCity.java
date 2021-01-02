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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author pir77
 */
public class TreeUpperCity extends SolidObject2D{
    private static final String[] TREE_ARRAY = {
    "tree/Help_Props_ArbreFleur_1_240x260.png",
    "tree/Help_Props_ArbreFleur_2_320x310.png",
    "tree/Help_Props_ArbreFleur_3_480x380.png",
    "tree/Help_Props_ArbreGuirlande_1_480x280.png",
    "tree/Help_Props_ArbreGuirlande_2_660x322.png",
    "tree/Help_Props_ArbreGuirlande_3_1020x380.png",
    "tree/Help_Props_arbre_240x260_Arbre.png",
    "tree/Help_Props_Arbre_2_330x310.png",
    "tree/Help_Props_Arbre_3_500x390.png"};  
    
    private int signIndex;
    
    public TreeUpperCity(World world, float posX, float posY, int index){
        
        this.signIndex = index;

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
        PolygonShape ground;
        Fixture fix;
        
        switch(this.signIndex){
            case 0:
            case 3:
            case 6:
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * this.scale, 65 * P2M * this.scale, new Vector2(0 * P2M * this.scale, -65 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                CircleShape circle = new CircleShape();
                circle.setRadius(105 * P2M * this.scale);
                circle.setPosition(new Vector2(0 * P2M * this.scale, 15 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 1:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(40 * P2M * this.scale, 50 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-100 * P2M * this.scale, -73 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * this.scale, 75 * P2M * this.scale, new Vector2(40 * P2M * this.scale, -75 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(-50 * P2M * this.scale, -90 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 2:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(-40 * P2M * this.scale, 80 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-180 * P2M * this.scale, -70 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * this.scale, 95 * P2M * this.scale, new Vector2(-35 * P2M * this.scale, -95 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(-90 * P2M * this.scale, -75 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(180 * P2M * this.scale, 105 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(90 * P2M * this.scale, 75 * P2M * this.scale), (float) Math.PI / 8);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 4:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(40 * P2M * this.scale, 45 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-100 * P2M * this.scale, -73 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * this.scale, 75 * P2M * this.scale, new Vector2(45 * P2M * this.scale, -75 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(-45 * P2M * this.scale, -85 * P2M * this.scale), (float) -Math.PI / 8);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 7:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(40 * P2M * this.scale, 45 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-100 * P2M * this.scale, -73 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * this.scale, 75 * P2M * this.scale, new Vector2(45 * P2M * this.scale, -75 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(-45 * P2M * this.scale, -85 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 5:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(-40 * P2M * this.scale, 80 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-190 * P2M * this.scale, -80 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * this.scale, 95 * P2M * this.scale, new Vector2(-40 * P2M * this.scale, -95 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(-90 * P2M * this.scale, -110 * P2M * this.scale), (float) -Math.PI / 10);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(200 * P2M * this.scale, 90 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(100 * P2M * this.scale, 65 * P2M * this.scale), (float) Math.PI / 10);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
            case 8:
                circle = new CircleShape();
                circle.setRadius(100 * P2M * this.scale);
                circle.setPosition(new Vector2(-40 * P2M * this.scale, 80 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(-190 * P2M * this.scale, -80 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(18 * P2M * this.scale, 95 * P2M * this.scale, new Vector2(-40 * P2M * this.scale, -95 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(-90 * P2M * this.scale, -90 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(200 * P2M * this.scale, 90 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(60 * P2M * this.scale, 14 * P2M * this.scale, new Vector2(100 * P2M * this.scale, 65 * P2M * this.scale), (float) Math.PI / 10);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
        }
        
        this.physicBody = groundBody;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(TREE_ARRAY[this.signIndex], this);
        
        if(this.texture != null){
            
            switch(this.signIndex){
                case 3:
                    TextureRegion[][] tmp = TextureRegion.split(this.texture, 240, 260);
        
                    Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
                    this.listAnimations.add(new Animation(1f, array, Animation.PlayMode.LOOP));

                    this.changeAnimation(0, false);
                    break;
                case 4:
                    tmp = TextureRegion.split(this.texture, 330, 320);
        
                    array = new Array<TextureRegion>(tmp[0]);
                    this.listAnimations.add(new Animation(1f, array, Animation.PlayMode.LOOP));

                    this.changeAnimation(0, false);
                    break;
                case 5:
                    tmp = TextureRegion.split(this.texture, 510, 380);
        
                    array = new Array<TextureRegion>(tmp[0]);
                    this.listAnimations.add(new Animation(1f, array, Animation.PlayMode.LOOP));

                    this.changeAnimation(0, false);
                    break;
            }
        }
    }
}