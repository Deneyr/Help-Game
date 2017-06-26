/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.HelpGame;

/**
 *
 * @author Deneyr
 */
public abstract class MenuGameNode extends GameNode{
    
    public MenuGameNode(String id, HelpGame game, Batch batch) {
        super(id);
    }
    
}
