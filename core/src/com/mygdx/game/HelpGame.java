/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import backgrounds.CityBackground;
import backgrounds.FarBackground;
import backgrounds.HillBackground;
import backgrounds.NearBackground;
import characters.OpponentCAC1;
import characters.Grandma;
import com.mygdx.game.scenery.Orphanage;
import com.mygdx.game.scenery.TestMarioStage;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.scenery.Abribus;
import com.mygdx.game.scenery.ActivableVentilo;
import com.mygdx.game.scenery.Banc;
import com.mygdx.game.scenery.CannonCorpus;
import com.mygdx.game.scenery.GroundUpperCity;
import com.mygdx.game.scenery.Poutrelle;
import com.mygdx.game.scenery.SmallBox;
import com.mygdx.game.scenery.TreeWithoutLeaf;
import com.mygdx.game.scenery.Ventilo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author françois
 */
public class HelpGame extends Game{

    public static float P2M = 1.5f/64; 
    
    /// Game logic Worlds.
    public SpriteBatch batch;
    
    private final GameWorld gameWorld = new GameWorld();
    
    private TreeMap<Float, WorldPlane> mapBackgroundPlanes = new TreeMap<Float, WorldPlane>();
   
    private boolean isTherePhysic;
    
    /// Screens.
    
    private GameScreen gameScreen;
    
    private MenuScreen menuScreen;
    
    private List<Screen> screensDisplayed = new ArrayList<Screen>();
    
    @Override
    public void create() {         
        this.gameScreen = new GameScreen(this);
        this.menuScreen = new MenuScreen(this);
        
        this.batch = new SpriteBatch();
       
        //initMenu();
        initTestLvl();
    }
    
    @Override
    public void render() {
        
        // Compute the next step of the game world logic.
        if(this.isTherePhysic){
            this.getGameWorld().step(Gdx.graphics.getDeltaTime());
        }
        
        // Compute the next step of the background logic.
        for(Entry<Float, WorldPlane> plane : this.mapBackgroundPlanes.entrySet()){
            plane.getValue().step(Gdx.graphics.getDeltaTime());
        }
        
        for(Screen screen : this.screensDisplayed){
            this.setScreen(screen);
            super.render();
        }
    }

    @Override
    public void dispose() {
        this.batch.dispose();
    }

    // LEVELS
    
    public void initMenu(){
        // init screen
        this.isTherePhysic = false;
        
        this.screensDisplayed.clear();
        this.screensDisplayed.add(this.menuScreen);
    }
    
    public void initMarioTestLvl(){
        // init screen
        this.isTherePhysic = true;
        
        this.screensDisplayed.clear();
        this.screensDisplayed.add(this.gameScreen);
        this.screensDisplayed.add(new GUIScreen(this));
        
        
        // init stage
        
        this.getGameWorld().flushWorld();
        
        Grandma hero = new Grandma(this.getGameWorld().getWorld(), 100f, 300f);
        this.getGameWorld().setHero(hero);
        
        OpponentCAC1 opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 400, 200);
        this.getGameWorld().addObject2DToWorld(opp);
        
        TestMarioStage stage = new TestMarioStage(this.getGameWorld().getWorld(), 275f, 206f);
        this.getGameWorld().addObject2DToWorld(stage);
        
        Orphanage orph = new Orphanage(this.getGameWorld().getWorld(), 800f, 250f);
        this.getGameWorld().addObject2DToWorld(orph);
        orph = new Orphanage(this.getGameWorld().getWorld(), 1500f, 250f);
        this.getGameWorld().addObject2DToWorld(orph);
    }
    
    public void initTestLvl(){
         // --- init screen ---
        this.isTherePhysic = true;
        
        this.screensDisplayed.clear();
        this.screensDisplayed.add(new BackgroundScreen(this));
        this.screensDisplayed.add(this.gameScreen);
        this.screensDisplayed.add(new GUIScreen(this));
        
        // --- init stage ---
        
        // init background
        
        int seed = 100;
        
        CityBackground cityBackground = new CityBackground(seed);
        this.mapBackgroundPlanes.put(cityBackground.getRatioDist(), cityBackground);
        
        HillBackground hillBackground = new HillBackground(seed);
        this.mapBackgroundPlanes.put(hillBackground.getRatioDist(), hillBackground);
        
        NearBackground nearBackground = new NearBackground(seed);
        this.mapBackgroundPlanes.put(nearBackground.getRatioDist(), nearBackground);
        
        FarBackground farBackground = new FarBackground(seed);
        this.mapBackgroundPlanes.put(farBackground.getRatioDist(), farBackground);
        
        // init hero
        Grandma hero = new Grandma(this.getGameWorld().getWorld(), -2000f, 0f);
        this.getGameWorld().setHero(hero);
        
        // init opponent
        
        OpponentCAC1 opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1400, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1450, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1450, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1500, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1350, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1300, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -200, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -150, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -100, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -50, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 0, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 100, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 150, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 200, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 250, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 300, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        // init scenary
        Orphanage orph = new Orphanage(this.getGameWorld().getWorld(), 0f, 0f);
        this.getGameWorld().addObject2DToWorld(orph);
        
        GroundUpperCity ground = new GroundUpperCity(this.getGameWorld().getWorld(), 0f, -430f);
        this.getGameWorld().addObject2DToWorld(ground);
        
        Abribus abribus = new Abribus(this.getGameWorld().getWorld(), -700f, -163f);
        this.getGameWorld().addObject2DToWorld(abribus);
        
        Banc banc = new Banc(this.getGameWorld().getWorld(), -700f, -192f);
        this.getGameWorld().addObject2DToWorld(banc);
        
        TreeWithoutLeaf tree = new TreeWithoutLeaf(this.getGameWorld().getWorld(), -900f, -110f);
        this.getGameWorld().addObject2DToWorld(tree);
        tree = new TreeWithoutLeaf(this.getGameWorld().getWorld(), -500f, -110f);
        this.getGameWorld().addObject2DToWorld(tree);
        
        Poutrelle poutrelle = new Poutrelle(this.getGameWorld().getWorld(), -1600f, -50f);
        //poutrelle.physicBody.setTransform(poutrelle.physicBody.getPosition(), -70);
        this.getGameWorld().addObject2DToWorld(poutrelle);
        
        Ventilo ventilo = new Ventilo(this.getGameWorld().getWorld(), -2200f, -100f, 3f, (float) (Math.PI / 2), true);
        this.getGameWorld().addObject2DToWorld(ventilo);
        
        ventilo = new Ventilo(this.getGameWorld().getWorld(), -2400f, -150f, 3f, (float) Math.PI, true);
        this.getGameWorld().addObject2DToWorld(ventilo);
        
        ActivableVentilo activableVentilo = new ActivableVentilo(this.getGameWorld().getWorld(), -3000f, -130f, 3f, (float) Math.PI*3/4, true);
        this.getGameWorld().addObject2DToWorld(activableVentilo);
        
        // box 
        
        SmallBox box = new SmallBox(this.getGameWorld().getWorld(), -2800f, -50f);
        this.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(this.getGameWorld().getWorld(), -2800f, -75f);
        this.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(this.getGameWorld().getWorld(), -2800f, -100f);
        this.getGameWorld().addObject2DToWorld(box, true);
        
        // Cannon
        
        CannonCorpus cannon = new CannonCorpus(this.getGameWorld().getWorld(), -3400f, -170f, 0);
        this.getGameWorld().addObject2DToWorld(cannon);
    }

    /**
     * @return the gameWorld
     */
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    /**
     * @return the mapBackgroundPlanes
     */
    public TreeMap<Float, WorldPlane> getMapBackgroundPlanes() {
        return mapBackgroundPlanes;
    }
}
