/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameEventListener.EventType;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class EventTriggeredObject2D extends TriggeredObject2D{

    private float height;
    private float width;
    
    private EventType eventType;
    private String details;
    
    private boolean repeatEvent;
    
    public EventTriggeredObject2D(World world, float posX, float posY, EventType eventType, String details, float width, float height, boolean repeatEvent){
        super();
        
        this.width = width;
        this.height = height;
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
        this.eventType = eventType;
        this.details = details;
        
        this.repeatEvent = repeatEvent;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
         PolygonShape eventShape = new PolygonShape();

        eventShape.setAsBox(this.width * P2M, this.height * P2M);
        
        super.initialize(world, position, speed, eventShape);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        // nothing to do.
    }
    
    @Override
    public void onObj2DEnteredArea(Object2D obj){
        if(!this.isTriggered && 
                (obj instanceof Grandma)){
            this.notifyGameEventListener(this.eventType, this.details, Vector2.Zero);   

            this.isTriggered = true;
        }
    }
    
    @Override
    public void onObj2DExitedArea(Object2D obj){
        if(this.isTriggered && 
                (obj instanceof Grandma)){
            
            if(this.repeatEvent){
                this.isTriggered = false;
            }
        }
    }
    
}
