/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;

/**
 *
 * @author Deneyr
 */
public class TriangleDummyTriggerObject2D extends DummyTriggerObject2D{

    public TriangleDummyTriggerObject2D(World world, float posX, float posY, GameEventListener.EventType eventType, String details, float width, float height, boolean repeatEvent){
        super(world, posX, posY, eventType, details, TriangleDummyTriggerObject2D.constructShapeTriangle(width, height), repeatEvent);
    }
    
    private static Shape constructShapeTriangle(float width, float height){
        PolygonShape ground = new PolygonShape();
        ground.set(new float[]{0, height * P2M,
                width * P2M, -height * P2M,
                -width * P2M, -height * P2M
        });
        
        return ground;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
