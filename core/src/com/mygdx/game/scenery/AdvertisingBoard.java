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
 * @author Deneyr
 */
public class AdvertisingBoard extends SolidObject2D{
    private static final String[] SIGN_ARRAY = {
    "signs/Help_Props_PanneauPubCafe_680x800.png",
    "signs/Help_Props_PanneauPubCoffre_680x680.png",
    "signs/Help_Props_PanneauPubDent_680x400.png",
    "signs/Help_Props_PanneauPubDentierA_680x400.png",
    "signs/Help_Props_PanneauPubDentierB_680x400.png",
    "signs/Help_Props_PanneauPubImmeuble_680x800.png",
    "signs/Help_Props_PanneauPubPanneau_680x400.png",
    "signs/Help_Props_PanneauPubPillule_680x400.png",
    "signs/Help_Props_PanneauPubTrampoline_680x400.png",
    "signs/Help_Props_PanneauPubvoiture_680x400.png",
    "signs/Help_Props_Panneau_VilleHaute_1400x780.png"};  
    
    private int signIndex;
    
    public AdvertisingBoard(World world, float posX, float posY, int index){
        
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
        
        this.priority = 1;
        
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
                ground = new PolygonShape();
                ground.setAsBox(300 * P2M * this.scale, 100 * P2M * this.scale, new Vector2(0, 125 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(300 * P2M * this.scale, 100 * P2M * this.scale, new Vector2(0, -172 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 400 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 1:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, 52 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, 297 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 340 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 2:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, -93 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 200 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 3:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, 150 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 200 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 4:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, -93 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 200 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 5:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, 245 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, -52 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 400 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;   
            case 6:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, 150 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 200 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 7:
                ground = new PolygonShape();
                ground.setAsBox(300 * P2M * this.scale, 100 * P2M * this.scale, new Vector2(0, 25 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);   
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 200 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 8:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, 150 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 200 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 9:
                ground = new PolygonShape();
                ground.setAsBox(320 * P2M * this.scale, 25 * P2M * this.scale, new Vector2(0, 150 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(340 * P2M * this.scale, 200 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;                
            case 10:
                this.priority = 4;
                
                ground = new PolygonShape();
                ground.setAsBox(295 * P2M * this.scale, 290 * P2M * this.scale, new Vector2(0, 47 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(700 * P2M * this.scale, 390 * P2M * this.scale);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setSensor(true);
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
        }
        
        this.physicBody = groundBody;
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(SIGN_ARRAY[this.signIndex], this);
        
        if(this.texture != null){
            
            switch(this.signIndex){
                case 10:
                    TextureRegion[][] tmp = TextureRegion.split(this.texture, 700, 780);
        
                    Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
                    this.listAnimations.add(new Animation(1f, array, Animation.PlayMode.LOOP));

                    this.changeAnimation(0, false);
                    break;
            }
        }
    }
}