/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import java.util.ArrayList;

/**
 *
 * @author françois
 */
public class TeethTriggeredObject2D extends TriggeredObject2D{

    private static final Texture UPTEXTURE = new Texture("Collectible_Dent.png");
    
    
    public TeethTriggeredObject2D(){
        super();
        
        // Part graphic
        this.texture = UPTEXTURE;
        
        
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        
        float radius = 15;
        
        super.initialize(world, position, speed, radius * 1.1f);
        
        // Collision fixture
        CircleShape circle = new CircleShape();
        circle.setRadius(radius * P2M);
        circle.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        
        this.setCollisionFilterMask(fixtureDef, false);
        
        fixtureDef.shape = circle;
        fixtureDef.density = 2f; 
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.8f; 
        
        Fixture fix = this.physicBody.createFixture(fixtureDef);
        
        this.collisionFixture = new ArrayList<Fixture>();
        this.collisionFixture.add(fix);
        fix.setUserData(this);
        
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        
        if(!this.isTriggered && objThatTriggered instanceof Grandma){
            
            Grandma grandma = (Grandma) objThatTriggered;
            
            if(grandma.getLifePoints() < grandma.getLifePointsMax()){

                grandma.setLifePoints(grandma.getLifePoints() + 1);

                super.trigger(objThatTriggered);
            }
        }
    }
    
    
    
    @Override
    public boolean IsDynamicObject(){
        return true;
    }
    
    
}
