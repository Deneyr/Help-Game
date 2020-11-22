/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.scenery;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
public class Signboard extends SolidObject2D{
    private static final String[] SIGN_ARRAY = {
    "urbanObj/Help_Props_enseigne_Bijoux_320x180.png",
    "urbanObj/Help_Props_Enseigne_Coiffeur_440x520.png",
    "urbanObj/Help_Props_enseigne_Dent_360x380.png",
    "urbanObj/Help_Props_Enseigne_Hotel_320x340.png",
    "urbanObj/Help_Props_Enseigne_Okujo_620x530.png",
    "urbanObj/Help_Props_Enseigne_Omar_480x380.png",
    "urbanObj/Help_Props_Enseigne_Parapluie_430x340.png",
    "urbanObj/Help_Props_Enseigne_Parfum_200x220.png",
    "urbanObj/Help_Props_Enseigne_Vente_Neuf_390x530.png",
    "urbanObj/Help_Props_Enseigne_Vin_670x600.png",
    "urbanObj/Help_Props_JazzInsigne_360x200.png"};  
    
    private int signIndex;
    
    private float angle;
    
    public Signboard(World world, float posX, float posY, float angle, int index){
        
        this.signIndex = index;
        
        this.angle = angle;
        
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
                this.scale = 1;
                
                ground = new PolygonShape();
                ground.setAsBox(100 * P2M * this.scale, 15 * P2M * this.scale, new Vector2(-40 * P2M * this.scale, -10 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                CircleShape circle = new CircleShape();
                circle.setRadius(50 * P2M * this.scale);
                circle.setPosition(new Vector2(100 * P2M * this.scale, -10 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 1:
                this.scale = 0.5f;
                
                ground = new PolygonShape();
                ground.setAsBox(55 * P2M * this.scale, 240 * P2M * this.scale, new Vector2(-15 * P2M * this.scale, 0), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(110 * P2M * this.scale, 180 * P2M * this.scale, new Vector2(100 * P2M * this.scale, 0 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(210 * P2M * this.scale, 30 * P2M * this.scale, new Vector2(-10 * P2M * this.scale, -230 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 2:
                this.scale = 0.5f;
                
                ground = new PolygonShape();
                ground.setAsBox(80 * P2M * this.scale, 110 * P2M * this.scale, new Vector2(-85 * P2M * this.scale, 50 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(90 * P2M * this.scale, 100 * P2M * this.scale, new Vector2(80 * P2M * this.scale, 10 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 3:
                this.scale = 1;
                
                ground = new PolygonShape();
                ground.setAsBox(65 * P2M * this.scale, 160 * P2M * this.scale, new Vector2(-70 * P2M * this.scale, 0), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(90 * P2M * this.scale, 130 * P2M * this.scale, new Vector2(50 * P2M * this.scale, -15 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 4:
                this.scale = 0.5f;
                
                circle = new CircleShape();
                circle.setRadius(190 * P2M * this.scale);
                circle.setPosition(new Vector2(-60 * P2M * this.scale, 30 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(125 * P2M * this.scale, 100 * P2M * this.scale, new Vector2(180 * P2M * this.scale, 0), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 5:
                this.scale = 0.5f;
                
                ground = new PolygonShape();
                ground.setAsBox(110 * P2M * this.scale, 180 * P2M * this.scale, new Vector2(-100 * P2M * this.scale, -10 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(110 * P2M * this.scale, 120 * P2M * this.scale, new Vector2(110 * P2M * this.scale, -10 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;   
            case 6:
                this.scale = 0.5f;
                
                circle = new CircleShape();
                circle.setRadius(130 * P2M * this.scale);
                circle.setPosition(new Vector2(-80 * P2M * this.scale, 10 * P2M * this.scale));
                
                fixtureDef.restitution = 0.5f;
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(50 * P2M * this.scale, 180 * P2M * this.scale, new Vector2(20 * P2M * this.scale, -20 * P2M * this.scale), 0);
                
                fixtureDef.restitution = 0.1f;
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(110 * P2M * this.scale, 10 * P2M * this.scale, new Vector2(100 * P2M * this.scale, 30 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break; 
            case 7:
                this.scale = 1f;
                
                ground = new PolygonShape();
                ground.setAsBox(70 * P2M * this.scale, 105 * P2M * this.scale, new Vector2( 0 * P2M * this.scale, -10 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                break;
            case 8:
                this.scale = 1;
                
                ground = new PolygonShape();
                ground.setAsBox(150 * P2M * this.scale, 250 * P2M * this.scale, new Vector2(30 * P2M * this.scale, 2 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(30 * P2M * this.scale, 120 * P2M * this.scale, new Vector2(-155 * P2M * this.scale, -33 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                break;
            case 9:
                this.scale = 1;
                
                ground = new PolygonShape();
                ground.setAsBox(190 * P2M * this.scale, 290 * P2M * this.scale, new Vector2(135 * P2M * this.scale, -2 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(130 * P2M * this.scale, 170 * P2M * this.scale, new Vector2(-190 * P2M * this.scale, -12 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                break;
            case 10:
                this.scale = 1;
                
                circle = new CircleShape();
                circle.setRadius(70 * P2M * this.scale);
                circle.setPosition(new Vector2(-10 * P2M * this.scale, 0 * P2M * this.scale));
                
                fixtureDef.shape = circle;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
                
                ground = new PolygonShape();
                ground.setAsBox(40 * P2M * this.scale, 90 * P2M * this.scale, new Vector2(50 * P2M * this.scale, -2 * P2M * this.scale), 0);
                
                fixtureDef.shape = ground;
                fix = groundBody.createFixture(fixtureDef); 
                fix.setUserData(this);
                this.collisionFixture.add(fix);
            
                break;
        }
        
        this.physicBody = groundBody;
        
        this.physicBody.setTransform(this.getPositionBody(), this.angle);
    }
    
    @Override
    public void assignTextures(){
        this.texture = TextureManager.getInstance().getTexture(SIGN_ARRAY[this.signIndex], this);
        
        if(this.texture != null){
            
            switch(this.signIndex){
                case 10:
                    TextureRegion[][] tmp = TextureRegion.split(this.texture, 180, 200);
        
                    Array<TextureRegion> array = new Array<TextureRegion>(tmp[0]);
                    this.listAnimations.add(new Animation(1f, array, Animation.PlayMode.LOOP));

                    this.changeAnimation(0, false);
                    break;
            }
        }
    }
}
