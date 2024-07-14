/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;

/**
 *
 * @author Deneyr
 */
public class GravityCannonBallTriggeredObject2D extends CannonBallTriggeredObject2D {
    
    public static final float ACCELERATION_Y = -8;
    
    private float accelerationY;
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        super.initialize(world, position, speed);
    }
    
    @Override
    public void updateLogic(float deltaTime){
        super.updateLogic(deltaTime);
        
        this.updatePhysic(deltaTime);
    }

    private void updatePhysic(float deltaTime) {
        Vector2 velocity = this.getBodyVelocity();
        
        velocity.y += deltaTime * GravityCannonBallTriggeredObject2D.ACCELERATION_Y;
        
        this.physicBody.setLinearVelocity(velocity);
    }
    
    public static Vector2 getSpeedToTarget(float angle, Vector2 offsetTarget){
        double value = 2 * (offsetTarget.y - offsetTarget.x * Math.tan(angle)) / GravityCannonBallTriggeredObject2D.ACCELERATION_Y;
        
        Vector2 dirBall = new Vector2(1, 0).rotate((float) (Math.toDegrees(angle)));       
        if(value > 0){
            double t = Math.sqrt(value);
            double v = offsetTarget.x / (Math.cos(angle) * t);
            
            dirBall.scl((float) v);
        }else{
            dirBall.scl((float) 300);
        }
        
        return dirBall;
    }
}
