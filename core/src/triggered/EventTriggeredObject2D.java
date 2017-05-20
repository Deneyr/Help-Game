/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triggered;

import characters.Grandma;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameEventListener.EventType;
import com.mygdx.game.Object2D;
import com.mygdx.game.TriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class EventTriggeredObject2D extends TriggeredObject2D{

    private float radius;
    
    private EventType eventType;
    private String details;
    
    public EventTriggeredObject2D(World world, float posX, float posY, EventType eventType, String details, float radius){
        super();
        
        this.radius = radius;
        this.initialize(world, new Vector2(posX, posY), Vector2.Zero);
        
        this.eventType = eventType;
        this.details = details;
    }
    
    @Override
    public void initialize(World world, Vector2 position, Vector2 speed) {
        super.initialize(world, position, speed, this.radius);
    }
    
    @Override
    public void trigger(Object2D objThatTriggered){
        if(!this.isTriggered && 
                (objThatTriggered instanceof Grandma)){
            this.notifyGameEventListener(this.eventType, this.details, Vector2.Zero);
            super.trigger(objThatTriggered);
        }
    }
    
}
