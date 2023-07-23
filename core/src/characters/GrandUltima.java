/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package characters;

import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author Deneyr
 */
public class GrandUltima extends Grandma{
    
    public GrandUltima(World world, float posX, float posY) {
        super(world, posX, posY);
        
        this.lifePoints = Integer.MAX_VALUE;
    }
    
    /**
     * @param lifePoints the lifePoints to set
     */
    //@Override
    /*public boolean setLifePoints(int lifePoints) {
        if(super.setLifePoints(lifePoints)){
            
            this.lifePoints = this.getLifePointsMax();
            
            return true;
        }
        return false;
    }*/
    
}
