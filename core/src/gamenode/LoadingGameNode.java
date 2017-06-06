/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.HelpGame;
import ressourcesmanagers.LoadingScreen;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class LoadingGameNode extends GameNode{

    public LoadingGameNode(Batch batch){
        super();
        
        this.screensDisplayed.add(new LoadingScreen(batch));
    }
    
    
    @Override
    public void updateLogic(HelpGame game, float deltaTime) {
        TextureManager.getInstance().updateLogic(deltaTime);
    }
    
}
