/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import guicomponents.GuiText;
import guicomponents.GuiText.ReferenceCorner;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author françois
 */
public class GUIScreen implements Screen{

    private final float P2M = 1.5f/64; 
    
    private final HelpGame game;
    
    private OrthographicCamera camera;
    
    private ShapeRenderer shapeRenderer;
    
    private GuiText scoreText;
    private int currentScore;
    
    public GUIScreen(HelpGame game){
        this.game = game;
        
        this.camera = new OrthographicCamera(800, 480);
        
        this.shapeRenderer = new ShapeRenderer();
        
        this.scoreText = new GuiText("0 $", 30, ReferenceCorner.RIGHT, ReferenceCorner.LEFT, 0.95f, 1);
        this.currentScore = -1;
        
        /*
        Dialogue dialogue = new Dialogue();
        
        dialogue.addReply("Hello Pierre !\n Voici une ligne de dialogue.", GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, 0);
        dialogue.addReply("Tu peux appuyer sur ENTRER\n et ainsi faire avance rapide.", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("C'est cool hein ?", GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("          \n ... \n ...\n\n Et tu n'as encore rien vu !", GuiPortrait.Character.NONE, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, 1);
        
        List<Dialogue> list = new ArrayList<Dialogue>();
        list.add(dialogue);
        
        this.dialogueBlock = new GuiDialogueBlock(list);
        this.dialogueBlock.setCurrentDialogue(0);*/
    }
   
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        this.camera.position.set(this.game.getGameWorld().getHeroPosition().x / P2M, this.game.getGameWorld().getHeroPosition().y / P2M, 0);
        this.camera.update();
        this.shapeRenderer.setProjectionMatrix(camera.combined);
        this.shapeRenderer.begin(ShapeType.Filled);
        
        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.batch.begin();
        
        // Start render
        List<Sprite> listSprites = this.game.getGameWorld().getLifeBarInRegion((this.camera.position.x - this.camera.viewportWidth / 2.f * 1.1f) * P2M,
                                                                           (this.camera.position.y - this.camera.viewportHeight / 2.f * 1.1f) * P2M,
                                                                           (this.camera.position.x + this.camera.viewportWidth / 2.f * 1.1f) * P2M,
                                                                           (this.camera.position.y + this.camera.viewportHeight / 2.f * 1.1f) * P2M);
        
        Iterator<Sprite> it = listSprites.iterator();
        while(it.hasNext()){
            Sprite sprite = it.next();
            
            this.shapeRenderer.setColor(0, 0, 0, 1);
            this.shapeRenderer.rect(sprite.getX() + sprite.getWidth() / 2 - 20 - 2, sprite.getY() + sprite.getHeight() - 2, 44, 9);
            this.shapeRenderer.setColor(1, 1, 1, 1);
            this.shapeRenderer.rect(sprite.getX() + sprite.getWidth() / 2 - 20, sprite.getY() + sprite.getHeight(), 40 * sprite.getScaleX(), 5);
        }
        
        // life bar hero
        Sprite lifeBarHero = this.game.getGameWorld().getLifeBarHero();
        this.game.batch.setColor(lifeBarHero.getColor());
        this.game.batch.draw(lifeBarHero, 
                                        this.camera.position.x - this.camera.viewportWidth / 2.f, this.camera.position.y + this.camera.viewportHeight / 2.f - lifeBarHero.getHeight(),
                                        lifeBarHero.getOriginX(), lifeBarHero.getOriginY(),
                                        lifeBarHero.getWidth(),lifeBarHero.getHeight(),
                                        lifeBarHero.getScaleX(),lifeBarHero.getScaleY(),
                                        lifeBarHero.getRotation());
        
        int score = this.game.getGameWorld().getScore();
        if(this.currentScore < 0 || this.currentScore != score){
            this.scoreText.setText(String.valueOf(score) + " $");
            this.currentScore = score;
        }        
        this.scoreText.drawBatch(this.camera, this.game.batch);
        
        this.game.batch.end();
        this.shapeRenderer.end();
        
        // Start dialogue rendering    
        this.shapeRenderer.begin(ShapeType.Filled);        
        this.game.getGameWorld().drawShapeRenderer(this.camera, this.shapeRenderer);
        this.shapeRenderer.end();
        
        this.game.batch.begin();
        this.game.getGameWorld().drawBatch(this.camera, this.game.batch);
        this.game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
    
    }

    @Override
    public void hide() {
 
    }

    @Override
    public void dispose() {
 
    }
    
}
