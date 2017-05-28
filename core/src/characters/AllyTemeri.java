/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.graphics.Texture;
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
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Deneyr
 */
public class AllyTemeri extends OpponentCAC1{
    
    private static final Texture TEMERITEXT = new Texture("character" + File.separator + "spritemapTemeri.png");
    
    public AllyTemeri(World world, Body target){
        super(100, target);
    }
    
    public AllyTemeri(World world, Body target, float posX, float posY) {
        super(100, target);
        
        this.texture = TEMERITEXT;
        
        this.maxSpeed = 4f;
        
        this.initializeSimpleGraphic();
        this.listAnimations.get(2).setFrameDuration(0.1f);
        this.listAnimations.get(3).setFrameDuration(0.1f);
        
        this.initializePhysicTemeri(world, posX, posY);
        
        this.updateFixture();
        
        this.previousSide = this.side;
    }
    
    @Override
    public void updateLogic(float deltaTime){
        if(this.currentAnimation >=0 && !this.pause){
            this.animationTime += deltaTime;
        }
        
        this.createInfluencesTemeri();
        
        this.influences2Actions(deltaTime);
        
        if(this.side != this.previousSide){
            this.updateFixture();
            this.previousSide = this.side;
        }
    }
    @Override
    public Sprite createCurrentSprite(){
        Sprite sprite = super.createCurrentSprite();
        sprite.setScale(sprite.getScaleX() * 0.8f, sprite.getScaleY() * 0.8f);
        return sprite;
    }
    
    protected final void initializePhysicTemeri(World world, float posX, float posY){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX * P2M, posY * P2M); 

        Body body = world.createBody(bodyDef);

        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(20 * P2M);
        circle.setPosition(new Vector2(0, -4 * P2M));
        //circle.setPosition(new Vector2(38 * P2M, 14 * P2M));
        /*PolygonShape granMa = new PolygonShape();
        granMa.setAsBox(25 * P2M, 18 * P2M, new Vector2(0, -13 * P2M), 0);*/  
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 5f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f; 
        body.setFixedRotation(true);
        Fixture fix = body.createFixture(fixtureDef);
        
        this.setCollisionFilterMask(fixtureDef, true);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);

        // Feet fixture
        PolygonShape feet = new PolygonShape();
        feet.setAsBox(8 * P2M, 3 * P2M, new Vector2(0, -24 * P2M), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = feet;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; 

        fix = body.createFixture(fixtureDef);
        fix.setSensor(true);
        fix.setUserData(this);
        
        this.feetFixture = fix;
        this.physicBody = body;
    }
    
}
