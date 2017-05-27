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
import characters.OpponentCAC2;
import characters.OpponentCACElite;
import characters.OpponentDIST1;
import characters.OpponentThief;
import com.mygdx.game.scenery.Orphanage;
import com.mygdx.game.scenery.TestMarioStage;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import guicomponents.CharacterTimeline;
import guicomponents.CharacterTimeline.CinematicStatus;
import guicomponents.CinematicManager;
import guicomponents.Dialogue;
import guicomponents.GuiPortrait;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import triggered.ActivableTriggeredObject2D;
import triggered.EventTriggeredObject2D;

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
        
        opp = new OpponentDIST1(this.getGameWorld().getWorld(), hero.physicBody, -1450, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentDIST1(this.getGameWorld().getWorld(), hero.physicBody, -1450, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1500, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1350, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, -1300, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        /*
        opp = new OpponentDIST1(this.getGameWorld().getWorld(), hero.physicBody, -200, 100);
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
        
        */
        
        OpponentThief thief = new OpponentThief(this.getGameWorld().getWorld(), hero.physicBody, 150, 100);
        this.getGameWorld().addObject2DToWorld(thief, false);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 500, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(this.getGameWorld().getWorld(), hero.physicBody, 600, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCAC2(this.getGameWorld().getWorld(), hero.physicBody, -1600, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC2(this.getGameWorld().getWorld(), hero.physicBody, -1700, 100);
        this.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCACElite(this.getGameWorld().getWorld(), hero.physicBody, -1500, 100);
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
        
        CannonCorpus cannon = new CannonCorpus(this.getGameWorld().getWorld(), hero.physicBody, -3400f, -170f, 0);
        this.getGameWorld().addObject2DToWorld(cannon, true);
        
        cannon = new CannonCorpus(this.getGameWorld().getWorld(), hero.physicBody, -3800f, -100f, (float) -Math.PI / 2);
        this.getGameWorld().addObject2DToWorld(cannon, true);
        
        cannon = new CannonCorpus(this.getGameWorld().getWorld(), hero.physicBody, -3800f, 20f, (float) -Math.PI / 2);
        this.getGameWorld().addObject2DToWorld(cannon, true);
        
        
        // Cinematics
        
        Dialogue dialogue = new Dialogue();
        
        dialogue.addReply("Reviens immédiatement,\nespèce de sale voleur !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("De l'air la vielle,\nretourne chez toi !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("Petit morveux,\nje vais t'apprendre ce qu'il\nen coûte de s'attaquer\naux honnètes gens !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 0);
        dialogue.addReply("Blablabla bla\nTchao vielle peau !\n\nHahahahaha", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.ANGRY, 1);
        
        List<Dialogue> list = new ArrayList<Dialogue>();
        list.add(dialogue);
        
        dialogue = new Dialogue();   
        dialogue.addReply("Haaaaaaaaa !!\nJe vais lui exploser\nsa petite tête\n à coup de parapluie !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Ca va chauffer !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        CinematicManager cin1 = new CinematicManager("roof", list);
        
        cin1.addDialogueTimeline(1f, 0);
        cin1.addDialogueTimeline(2f, 1);
        
        CharacterTimeline charaTimeline = new CharacterTimeline(hero, CinematicStatus.NORMAL);
        
        charaTimeline.addEntry(1.5f, "per_right");
        charaTimeline.addEntry(2f, "per_right");
        charaTimeline.addEntry(1.6f, "attack");     
        cin1.addCharacterTimeline(charaTimeline);
        
        charaTimeline = new CharacterTimeline(thief, CinematicStatus.END_CINEMATIC);

        charaTimeline.addEntry(0f, "per_left");
        charaTimeline.addEntry(1f, "per_left");
        
        charaTimeline.addEntry(1.1f, "per_right");
        charaTimeline.addEntry(3f, "per_right");
        charaTimeline.addEntry(1.5f, "per_jump");
        charaTimeline.addEntry(2f, "per_jump");
        cin1.addCharacterTimeline(charaTimeline);
        
        this.getGameWorld().addCinematicManager(cin1);
        
        EventTriggeredObject2D trigger = new EventTriggeredObject2D(this.getGameWorld().getWorld(), -50f, 180f, GameEventListener.EventType.CINEMATIC, "roof", 100);
        this.getGameWorld().addObject2DToWorld(trigger);
        
        // activable areas
        
        dialogue = new Dialogue();
        
        dialogue.addReply("Au loin, le soleil se couche. ", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Il va falloir faire vite !\nNul n'est sensé se retrouver dehors\naprès le couvre-feu ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        
        list = new ArrayList<Dialogue>();
        list.add(dialogue);
        
        dialogue = new Dialogue();
        dialogue.addReply("Pourquoi ces petits morveux\nse complaisent dans le mal ? ", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Tous cela est bien triste ...", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        CinematicManager cin2 = new CinematicManager("question", list);
        
        charaTimeline = new CharacterTimeline(hero, CinematicStatus.NORMAL);
        cin2.addCharacterTimeline(charaTimeline);
        
        cin2.addDialogueTimeline(0f, 0);
        cin2.addDialogueTimeline(0.4f, 1);
        this.getGameWorld().addCinematicManager(cin2);
        
        ActivableTriggeredObject2D acti = new ActivableTriggeredObject2D(this.getGameWorld().getWorld(), 300f, 50f, Input.Keys.ENTER, GameEventListener.EventType.CINEMATIC, "question", 200);
        this.getGameWorld().addObject2DToWorld(acti);
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
