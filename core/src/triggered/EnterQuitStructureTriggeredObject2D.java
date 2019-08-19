/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameEventListener;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;
import java.util.UUID;

/**
 *
 * @author Deneyr
 */
public class EnterQuitStructureTriggeredObject2D extends TriggeredObject2D{

    private String structureID;
    
    private float width;
    private float height;
    
    public EnterQuitStructureTriggeredObject2D(World world, float posX, float posY, float width, float height){
        super();
        
        this.width = width;
        this.height = height;
        
        this.structureID = UUID.randomUUID().toString();
             
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(this.width / 2 * P2M, this.height / 2 * P2M);
        
        super.initialize(world, position, speed, shape);
    }
 
    
    @Override
    public void onObj2DEnteredArea(Object2D obj){
        if(obj instanceof Grandma){ 
            this.notifyGameEventListener(GameEventListener.EventType.ENTERSTRUCT, this.structureID, new Vector2(obj.getPositionBody()));
        }
    }
    
    @Override
    public void onObj2DExitedArea(Object2D obj){
        if(obj instanceof Grandma){
            this.notifyGameEventListener(GameEventListener.EventType.QUITSTRUCT, this.structureID, new Vector2(obj.getPositionBody()));
        }
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        // nothing to do.
    }
    
     /**
     * @return the structureID
     */
    public String getStructureID() {
        return structureID;
    }
}
