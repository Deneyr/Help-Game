/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import backgrounds.CityBackground;
import backgrounds.FarBackground;
import backgrounds.HillBackground;
import backgrounds.NearBackground;
import characters.AllyTemeri;
import characters.Grandma;
import characters.OpponentCAC1;
import characters.OpponentCAC2;
import characters.OpponentCACElite;
import characters.OpponentDIST1;
import characters.OpponentThief;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import com.mygdx.game.scenery.Abribus;
import com.mygdx.game.scenery.ActivableVentilo;
import com.mygdx.game.scenery.AutoCannonCorpus;
import com.mygdx.game.scenery.Banc;
import triggered.BarbedTriggeredObject2D;
import com.mygdx.game.scenery.CannonCorpus;
import com.mygdx.game.scenery.DirectCannonCorpus;
import com.mygdx.game.scenery.GroundUpperCity;
import com.mygdx.game.scenery.Orphanage;
import com.mygdx.game.scenery.Poutrelle;
import com.mygdx.game.scenery.SmallBox;
import com.mygdx.game.scenery.TreeWithoutLeaf;
import com.mygdx.game.scenery.Ventilo;
import guicomponents.CharacterTimeline;
import guicomponents.CinematicManager;
import guicomponents.Dialogue;
import guicomponents.GuiPortrait;
import java.util.ArrayList;
import java.util.List;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.TextureManager;
import triggered.ActivableTriggeredObject2D;
import triggered.BulletTriggeredObject2D;
import triggered.CannonBallTriggeredObject2D;
import triggered.EventTriggeredObject2D;
import triggered.TeethTriggeredObject2D;
import triggered.UpTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class Lvl1GameNode extends LvlGameNode{
    
    public Lvl1GameNode(HelpGame game, Batch batch) {
        super("Lvl1GameNode", game, batch);
    }
    
    @Override
    protected void initializeLevel(HelpGame game){
        
        // --- init stage ---
        
        // import dynamic resources (created at runtime).
        TextureManager.getInstance().getTexture(BulletTriggeredObject2D.BULLETTEXTURE, null);
        
        TextureManager.getInstance().getTexture(CannonBallTriggeredObject2D.CANNONBALLTEXTURE, null);
        
        TextureManager.getInstance().getTexture(TeethTriggeredObject2D.UPTEXTURE, null);
        
        TextureManager.getInstance().getTexture(UpTriggeredObject2D.UPTEXTURE, null);
        
        // init background
        int seed = 100;
        
        CityBackground cityBackground = new CityBackground(seed);
        game.getMapBackgroundPlanes().put(cityBackground.getRatioDist(), cityBackground);
        
        HillBackground hillBackground = new HillBackground(seed);
        game.getMapBackgroundPlanes().put(hillBackground.getRatioDist(), hillBackground);
        
        NearBackground nearBackground = new NearBackground(seed);
        game.getMapBackgroundPlanes().put(nearBackground.getRatioDist(), nearBackground);
        
        FarBackground farBackground = new FarBackground(seed);
        game.getMapBackgroundPlanes().put(farBackground.getRatioDist(), farBackground);
        
        // init solid objects
        
        BarbedTriggeredObject2D barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), -1900f, -185);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), -1850f, -185);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), -1780f, -185);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        // init hero
        Grandma hero = new Grandma(game.getGameWorld().getWorld(), -2000f, 0f);
        game.getGameWorld().setHero(hero);
        
        // init opponent
        
        OpponentCAC1 opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -1400, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentDIST1(game.getGameWorld().getWorld(), hero, -1450, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentDIST1(game.getGameWorld().getWorld(), hero, -1450, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -1500, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -1350, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -1300, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        /*
        opp = new OpponentDIST1(game.getGameWorld().getWorld(), hero, -200, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -150, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -100, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -50, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, 0, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, 100, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, 150, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, 200, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        */
        
        OpponentThief thief = new OpponentThief(game.getGameWorld().getWorld(), hero, 150, 100);
        game.getGameWorld().addObject2DToWorld(thief, false);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, 500, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, 600, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCAC2(game.getGameWorld().getWorld(), hero, -1600, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC2(game.getGameWorld().getWorld(), hero, -1700, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCACElite(game.getGameWorld().getWorld(), hero, -1500, 100);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        AllyTemeri temeri = new AllyTemeri(game.getGameWorld().getWorld(), hero, -800, 100);
        game.getGameWorld().addObject2DToWorld(temeri, true);
        
        // init scenary
        Orphanage orph = new Orphanage(game.getGameWorld().getWorld(), 0f, 0f);
        game.getGameWorld().addObject2DToWorld(orph);
        
        GroundUpperCity ground = new GroundUpperCity(game.getGameWorld().getWorld(), 0f, -430f);
        game.getGameWorld().addObject2DToWorld(ground);
        
        Abribus abribus = new Abribus(game.getGameWorld().getWorld(), -700f, -163f);
        game.getGameWorld().addObject2DToWorld(abribus);
        
        Banc banc = new Banc(game.getGameWorld().getWorld(), -700f, -192f);
        game.getGameWorld().addObject2DToWorld(banc);
        
        TreeWithoutLeaf tree = new TreeWithoutLeaf(game.getGameWorld().getWorld(), -900f, -110f);
        game.getGameWorld().addObject2DToWorld(tree);
        tree = new TreeWithoutLeaf(game.getGameWorld().getWorld(), -500f, -110f);
        game.getGameWorld().addObject2DToWorld(tree);
        
        Poutrelle poutrelle = new Poutrelle(game.getGameWorld().getWorld(), -1600f, -50f);
        //poutrelle.physicBody.setTransform(poutrelle.physicBody.getPosition(), -70);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        Ventilo ventilo = new Ventilo(game.getGameWorld().getWorld(), -2200f, -100f, 3f, (float) (Math.PI / 2), true);
        game.getGameWorld().addObject2DToWorld(ventilo);
        
        ventilo = new Ventilo(game.getGameWorld().getWorld(), -2400f, -150f, 3f, (float) Math.PI, true);
        game.getGameWorld().addObject2DToWorld(ventilo);
        
        ActivableVentilo activableVentilo = new ActivableVentilo(game.getGameWorld().getWorld(), -3000f, -130f, 3f, (float) Math.PI*3/4, true);
        game.getGameWorld().addObject2DToWorld(activableVentilo);
        
        // box 
        
        SmallBox box = new SmallBox(game.getGameWorld().getWorld(), -2800f, -50f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), -2800f, -75f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), -2800f, -100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        // Cannon
        
        CannonCorpus cannon = new AutoCannonCorpus(game.getGameWorld().getWorld(), hero, -3400f, -170f, 0, false, 1);
        game.getGameWorld().addObject2DToWorld(cannon, true);
        
        cannon = new AutoCannonCorpus(game.getGameWorld().getWorld(), hero, -3800f, -100f, (float) -Math.PI / 2, false, 0);
        game.getGameWorld().addObject2DToWorld(cannon, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -3800f, 130);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -3820f, 130);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC2(game.getGameWorld().getWorld(), hero, -3830f, 130);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC2(game.getGameWorld().getWorld(), hero, -3840f, 130);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        opp = new OpponentCAC2(game.getGameWorld().getWorld(), hero, -3860, 130);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
        
        opp = new OpponentCACElite(game.getGameWorld().getWorld(), hero, -3880, 130);
        game.getGameWorld().addObject2DToWorld(opp, true);
        
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
        
        CharacterTimeline charaTimeline = new CharacterTimeline(hero, CharacterTimeline.CinematicStatus.NORMAL);
        
        charaTimeline.addEntry(1.5f, "per_right");
        charaTimeline.addEntry(2f, "per_right");
        charaTimeline.addEntry(1.6f, "attack");     
        cin1.addCharacterTimeline(charaTimeline);
        
        charaTimeline = new CharacterTimeline(thief, CharacterTimeline.CinematicStatus.END_CINEMATIC);

        charaTimeline.addEntry(0f, "per_left");
        charaTimeline.addEntry(1f, "per_left");
        
        charaTimeline.addEntry(1.1f, "per_right");
        charaTimeline.addEntry(3f, "per_right");
        charaTimeline.addEntry(1.5f, "per_jump");
        charaTimeline.addEntry(2f, "per_jump");
        cin1.addCharacterTimeline(charaTimeline);
        
        game.getGameWorld().addCinematicManager(cin1);
        
        EventTriggeredObject2D trigger = new EventTriggeredObject2D(game.getGameWorld().getWorld(), -50f, 180f, GameEventListener.EventType.CINEMATIC, "roof", 100);
        game.getGameWorld().addObject2DToWorld(trigger);
        
        // activable areas
        
        dialogue = new Dialogue();
        
        dialogue.addReply("Une jeune fille se tiens devant vous,\nelle semble attendre quelqu'un ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        //dialogue.addReply("Il va falloir faire vite !\nNul n'est sensé se retrouver dehors\naprès le couvre-feu ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        
        list = new ArrayList<Dialogue>();
        list.add(dialogue);
        
        dialogue = new Dialogue();
        dialogue.addReply("Que fais tu dehors à cette heure\nma petite ?\nLe couvre feu ne va pas tarder", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Je pourrais vous demander\nla même chose madame ...\n Vous semblez essouflée\ntout va bien ?", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Oui ... non ...\nen fait un petit voyou\nm'a volé mon sac.", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Lorsque je le retrouverais\nje lui ferais passer\nl'envie de nuir\naux honnètes gens !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();
        dialogue.addReply("La jeune fille sourit gentillement ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Je vois ...\nEt bien je vous souhaite\n bonne chance !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Néanmoins ...", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Qui a t'il ?", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, 0);
        dialogue.addReply("Rien ...\nmais si vous le retrouvez,\nsoyez indulgente\ns'il vous plait ...", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.SORROW, 1);
        dialogue.addReply("Certainement pas !\nPas de pitié pour\nles petits voyous !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.SORROW, 0);
        list.add(dialogue);
        
        CinematicManager cin2 = new CinematicManager("question", list);
        
        charaTimeline = new CharacterTimeline(hero, CharacterTimeline.CinematicStatus.NORMAL);
        cin2.addCharacterTimeline(charaTimeline);
        
        cin2.addDialogueTimeline(0f, 0);
        cin2.addDialogueTimeline(0.4f, 1);
        cin2.addDialogueTimeline(0.8f, 2);
        game.getGameWorld().addCinematicManager(cin2);
        
        ActivableTriggeredObject2D acti = new ActivableTriggeredObject2D(game.getGameWorld().getWorld(), temeri, Input.Keys.ENTER, GameEventListener.EventType.CINEMATIC, "question", 50);
        game.getGameWorld().addObject2DToWorld(acti);
        
        // Music & Sounds.
        
        MusicManager.getInstance().registerResource("sounds/first_lvl.ogg");
        
        this.initSoundsLvl();
    }
    
}
