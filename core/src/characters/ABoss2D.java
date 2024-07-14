/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Character2D;
import com.mygdx.game.Object2D;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public abstract class ABoss2D extends Character2D {
    
    public ABoss2D(int lifePoints) {
        super(lifePoints);
    }
    
    public abstract void initialize(World world, Object2D target, float posX, float posY);  
}
