/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import backgrounds.CityBackground;
import backgrounds.FarBackground;
import backgrounds.HillBackground;
import backgrounds.HostelBackground;
import backgrounds.Lvl1Foreground;
import backgrounds.Lvl1_1_Residence;
import backgrounds.NearBackground;
import characters.AllyTemeri;
import characters.Grandma;
import characters.OpponentCAC1;
import characters.OpponentThief;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import com.mygdx.game.scenery.Abribus;
import com.mygdx.game.scenery.Bench;
import com.mygdx.game.scenery.Bus;
import com.mygdx.game.scenery.Car;
import com.mygdx.game.scenery.Crane;
import com.mygdx.game.scenery.GroundCity;
import com.mygdx.game.scenery.MetalBox;
import com.mygdx.game.scenery.Sign;
import com.mygdx.game.scenery.SmallBox;
import com.mygdx.game.scenery.TestMarioStage;
import com.mygdx.game.scenery.Pipe;
import com.mygdx.game.scenery.PoutrelleObstacle;
import com.mygdx.game.scenery.StrongBox;
import com.mygdx.game.scenery.Trashcan;
import com.mygdx.game.scenery.TreeWithoutLeaf;
import cosmetics.HitCosmeticObject2D;
import guicomponents.CharacterTimeline;
import guicomponents.CinematicManager;
import guicomponents.Dialogue;
import guicomponents.GuiPortrait;
import java.util.ArrayList;
import java.util.List;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.SoundManager;
import ressourcesmanagers.TextureManager;
import triggered.ActivableTriggeredObject2D;
import triggered.BulletTriggeredObject2D;
import triggered.CannonBallTriggeredObject2D;
import triggered.CheckPointTriggeredObject2D;
import triggered.EventTriggeredObject2D;
import triggered.TeethTriggeredObject2D;
import triggered.UpTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class Lvl1UpperCity extends LvlGameNode{
    
    public Lvl1UpperCity(HelpGame game, Batch batch) {
        super("1:L'orgueil\nd'une grand-mère", game, batch);
    }
    
    @Override
    protected Vector2 initCheckpoints(HelpGame game, int checkpointIndex)
    {   
        /*CheckPointTriggeredObject2D checkPoint = new CheckPointTriggeredObject2D(game.getGameWorld().getWorld(), 600f, -0f);
        game.getGameWorld().addCheckPoint(checkPoint, checkpointIndex);
        
        return game.getGameWorld().getPositionAtCheckpoint(checkpointIndex);*/
        return null;
    }
    
    
    @Override
    protected void initializeLevel(HelpGame game){
        // --- init stage ---
        int index = game.getPlayerData().getCurrentCheckpointIndex();
        
        super.initializeLevel(game);
        
        // import dynamic resources (created at runtime).
        TextureManager.getInstance().getTexture(BulletTriggeredObject2D.BULLETTEXTURE, null);
        
        TextureManager.getInstance().getTexture(CannonBallTriggeredObject2D.CANNONBALLTEXTURE, null);
        
        TextureManager.getInstance().getTexture(TeethTriggeredObject2D.TEETHTEXTURE, null);
        
        TextureManager.getInstance().getTexture(UpTriggeredObject2D.UPTEXTURE, null);
        
        TextureManager.getInstance().getTexture(HitCosmeticObject2D.HIT_TEXTURE, null);
        
        // init background
        int seed = 80;
        
        CityBackground cityBackground = new CityBackground(seed);
        game.getMapBackgroundPlanes().put(cityBackground.getRatioDist(), cityBackground);
        
        HillBackground hillBackground = new HillBackground(seed);
        game.getMapBackgroundPlanes().put(hillBackground.getRatioDist(), hillBackground);
        
        NearBackground nearBackground = new NearBackground(seed);
        game.getMapBackgroundPlanes().put(nearBackground.getRatioDist(), nearBackground);
        
        FarBackground farBackground = new FarBackground(seed);
        game.getMapBackgroundPlanes().put(farBackground.getRatioDist(), farBackground);
        
        Lvl1_1_Residence lvl1_1_Residence = new Lvl1_1_Residence(seed, 6500f, -25, 200, 200);
        game.getMapBackgroundPlanes().put(lvl1_1_Residence.getRatioDist(), lvl1_1_Residence);
        
        // init background solid objects
        
        lvl1_1_Residence.createSolidObj(game.getGameWorld());
        
        // init foreground
        
        Lvl1Foreground lvl1Foreground = new Lvl1Foreground();
        
        lvl1_1_Residence.createForegroundObj(game.getGameWorld(), lvl1Foreground);
        
        lvl1Foreground.assignTextures();
        
        game.getMapForegroundPlanes().put(lvl1Foreground.getRatioDist(), lvl1Foreground);
        
        // init solid objects
        
        GroundCity ground = new GroundCity(game.getGameWorld().getWorld(), 1024, -150f, 100);
        game.getGameWorld().addObject2DToWorld(ground);
        
        Sign sign = new Sign(game.getGameWorld().getWorld(), 1100f, 0, 0.02f, 2);
        game.getGameWorld().addObject2DToWorld(sign);
        
        sign = new Sign(game.getGameWorld().getWorld(), 1200f, 0, -0.05f, 1);
        game.getGameWorld().addObject2DToWorld(sign);
        
        sign = new Sign(game.getGameWorld().getWorld(), 1700f, 0, 0, 6);
        game.getGameWorld().addObject2DToWorld(sign);
        
        Pipe pipe = new Pipe(game.getGameWorld().getWorld(), 1900f, 20f, false);
        game.getGameWorld().addObject2DToWorld(pipe, true);
        
        Abribus abribus = new Abribus(game.getGameWorld().getWorld(), 2400f, 15f);
        game.getGameWorld().addObject2DToWorld(abribus);
        
        Bench banc = new Bench(game.getGameWorld().getWorld(), 2400f, -15f);
        game.getGameWorld().addObject2DToWorld(banc);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 2700f, 150f, true);
        game.getGameWorld().addObject2DToWorld(pipe, true);
        
        Car car = new Car(game.getGameWorld().getWorld(), 3000f, 10f, 0, 1);
        game.getGameWorld().addObject2DToWorld(car, true);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 3200f, 150f, true);
        game.getGameWorld().addObject2DToWorld(pipe, true);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 3500f, 220f, true);
        game.getGameWorld().addObject2DToWorld(pipe, true);
        
        TreeWithoutLeaf tree = new TreeWithoutLeaf(game.getGameWorld().getWorld(), 3800f, 70f);
        game.getGameWorld().addObject2DToWorld(tree);
        
        Trashcan trashcan = new Trashcan(game.getGameWorld().getWorld(), 4000f, 0, 1, 1);
        game.getGameWorld().addObject2DToWorld(trashcan, true);
        
        trashcan = new Trashcan(game.getGameWorld().getWorld(), 4150f, 0, 0, 1);
        game.getGameWorld().addObject2DToWorld(trashcan, true);
        
        Bus bus = new Bus(game.getGameWorld().getWorld(), 4500f, 60, 0, -1);
        game.getGameWorld().addObject2DToWorld(bus, true);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 4000f, 420f, true);
        game.getGameWorld().addObject2DToWorld(pipe, true);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 4400f, 320f, true);
        game.getGameWorld().addObject2DToWorld(pipe, true);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 4800f, 220f, true);
        game.getGameWorld().addObject2DToWorld(pipe, true);
        
        sign = new Sign(game.getGameWorld().getWorld(), 5200f, 0, -0.05f, 4);
        game.getGameWorld().addObject2DToWorld(sign);
        
        PoutrelleObstacle poutrelleObst = new PoutrelleObstacle(game.getGameWorld().getWorld(), 5360f, 210f, (float)Math.PI/2, 0, 1);
        game.getGameWorld().addObject2DToWorld(poutrelleObst);
        
        Crane crane = new Crane(game.getGameWorld().getWorld(), 6000f, 270, 0.8f, 0.1f, 0.9f, 0.2f, true);
        game.getGameWorld().addObject2DToWorld(crane, true);
        
        /*Crane crane = new Crane(game.getGameWorld().getWorld(), -600f, 270, 0.7f, 0f, false);
        game.getGameWorld().addObject2DToWorld(crane, true);*/
        /*
        Abribus abribus = new Abribus(game.getGameWorld().getWorld(), 400f, 0f);
        game.getGameWorld().addObject2DToWorld(abribus);
        
        Bench banc = new Bench(game.getGameWorld().getWorld(), 400f, -15f);
        game.getGameWorld().addObject2DToWorld(banc);
        
        Pipe tree = new Pipe(game.getGameWorld().getWorld(), 600f, 50);
        game.getGameWorld().addObject2DToWorld(tree);
        tree = new Pipe(game.getGameWorld().getWorld(), 700, 50);
        game.getGameWorld().addObject2DToWorld(tree);*/
        
        /*TestMarioStage test = new TestMarioStage(game.getGameWorld().getWorld(), 600, 75);
        game.getGameWorld().addObject2DToWorld(test);*/
        
        // init checkpoints
        Vector2 heroPosition = this.initCheckpoints(game, index);
        
        // init hero
        Grandma hero = null;
        if(heroPosition != null){
            hero = new Grandma(game.getGameWorld().getWorld(), heroPosition.x, heroPosition.y);
        }else{
            hero = new Grandma(game.getGameWorld().getWorld(), 0f, 0f);
        }
        game.getGameWorld().setHero(hero);
        
        // init opponents
        OpponentThief thief = new OpponentThief(game.getGameWorld().getWorld(), hero, -390f, 0f);
        game.getGameWorld().addObject2DToWorld(thief, false);
        
        /*OpponentCAC1 opp = new OpponentCAC1(game.getGameWorld().getWorld(), hero, -500, 0);
        opp.setMaxDistance(200);
        game.getGameWorld().addObject2DToWorld(opp, true);*/
        
        AllyTemeri temeri = new AllyTemeri(game.getGameWorld().getWorld(), hero, 2400f, 0f);
        game.getGameWorld().addObject2DToWorld(temeri, true);
        
        // init boxes & destroyables
        SmallBox box = new SmallBox(game.getGameWorld().getWorld(), 730f, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        
        box = new SmallBox(game.getGameWorld().getWorld(), 3500f, 300f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 3520f, 300f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 3520f, 350f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        
        box = new SmallBox(game.getGameWorld().getWorld(), 4000f, 500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 3960f, 500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 4000f, 550f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 3960f, 550f);
        game.getGameWorld().addObject2DToWorld(box, true);

        
        StrongBox strongBox = new StrongBox(game.getGameWorld().getWorld(), 5360f, 0f);
        game.getGameWorld().addObject2DToWorld(strongBox, true);
        /*box = new MetalBox(game.getGameWorld().getWorld(), 1300f, 200f);
        game.getGameWorld().addObject2DToWorld(box, true);*/
        
        box = new SmallBox(game.getGameWorld().getWorld(), 5500f, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 5550f, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 5525f, 50f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        strongBox = new StrongBox(game.getGameWorld().getWorld(), 6000f, 100f);
        game.getGameWorld().addObject2DToWorld(strongBox, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 6300f, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 6350f, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 6300f, 50f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 6350f, 50f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 6325f, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        // Cannon
        

        
        // Cinematics
        
        // OutBounds Cinematic
        Dialogue dialogue = new Dialogue();
        dialogue.addReply("Hors de questions\nde laisser ce sacripan\ns'en sortir !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        List<Dialogue> list = new ArrayList<Dialogue>();
        list.add(dialogue);
        
        CinematicManager cin1 = new CinematicManager("outBoundsCinematic", list, false, this.getId());
        
        cin1.addDialogueTimeline(0f, 0);
        
        CharacterTimeline charaTimeline = new CharacterTimeline(hero, CharacterTimeline.CinematicStatus.NORMAL, new Vector2(0, 10f));
        
        charaTimeline.addEntry(0.2f, "per_right");
        charaTimeline.addEntry(1.2f, "per_right");
        cin1.addCharacterTimeline(charaTimeline);
        
        game.getGameWorld().addCinematicManager(cin1, index, Integer.MAX_VALUE);
        
        EventTriggeredObject2D trigger = new EventTriggeredObject2D(game.getGameWorld().getWorld(), -300f, 0f, GameEventListener.EventType.CINEMATIC, "outBoundsCinematic", 10f, 1000f, true);
        game.getGameWorld().addObject2DToWorld(trigger);
        
        // Start cinematic
        dialogue = new Dialogue();
        dialogue.addReply("Vous chercher\nUn monde exempt de souffrance ?\n Un mode exempt de mal ?\n  Exempt de criminels ?", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Bienvenue à TEO Cité,\nla ville ou la criminalité est punie\npar le plus juste des chatiments :", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Le banissement !", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Nous vous garantissons\nune sécurité sans faille,\net ce, de 6h à 21h !", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Notre histoire commence\ndans un quartier huppé de la ville ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);        
        list = new ArrayList<Dialogue>();
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply("Il fait un peu frisquet,\nj'ai interet à rentrer vite !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Habitants de TEO Cité,\nil est actuellement 20h50,\nle couvre-feu va bientôt débuter.", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("Conformement à la loi\n421-HJ156-543.14159265359\nAlinea ln(129i+12)²", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("Toute personne présente passé 21h\nsera severement poursuivie !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("Juste ciel !\nJe dois me dépecher de rentrer !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply("Haaaaaaaaaaa !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply("Qu'est ce qui se passe ?!\nAu secours !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Quelqu'un !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Aidez moi ...", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Hahahahaha !\nAlors la vieille ?\n tu ne sais pas qu'après 21h\nc'est nous les boss de la cité ?", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Personne ne va venir t'aider !\nTu peux crier \"Help\"\nautant que tu veux !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 1);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply(".........", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply(" ... Qu'est ce que ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Petit sacripan !\ntu n'aurais jamais du quitter\nle taudis qui te sert de maison !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 0);
        dialogue.addReply("Tu vas voir\nce qu'une vieille dame respectable\nva faire à une crapule dans ton genre !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply(" Aie !!\nmais elle est completement folle !", GuiPortrait.Character.NONE, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.SORROW, 1);
        dialogue.addReply("Petit polisson,\nrend moi mon sac à main !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 0);
        dialogue.addReply("Jamais ! les bourges dans ton genre\nen ont de toute façon 36\ncomme ça à la maison !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Ca va chauffer !!!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply("HELPPPP !\nje suis poursuivi par\nune vielle peau enragée !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.SORROW, 1);
        dialogue.addReply("Mauvais graine !\nreviens ici !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        cin1 = new CinematicManager("startCinematic", list, true, this.getId());
        
        cin1.addDialogueTimeline(0f, 0);
        cin1.addDialogueTimeline(1f, 1);
        cin1.addDialogueTimeline(5f, 2);
        cin1.addDialogueTimeline(6f, 3);
        cin1.addDialogueTimeline(7f, 4);
        cin1.addDialogueTimeline(8f, 5);
        cin1.addDialogueTimeline(9f, 6);
        cin1.addDialogueTimeline(11f, 7);
        
        charaTimeline = new CharacterTimeline(hero, CharacterTimeline.CinematicStatus.NORMAL, new Vector2(1000f, 10f));
        
        charaTimeline.addEntry(0.1f, "switch");
        charaTimeline.addEntry(0.2f, "per_gotPurse");
        charaTimeline.addEntry(0.3f, "per_walk");
        charaTimeline.addEntry(0.4f, "per_right");
        charaTimeline.addEntry(5.0f, "per_walk");
        charaTimeline.addEntry(5.1f, "per_right");
        charaTimeline.addEntry(5.2f, "per_gotPurse");
        charaTimeline.addEntry(5.3f, "onGround");
        charaTimeline.addEntry(5.4f, "left");
        charaTimeline.addEntry(5.5f, "right");
        charaTimeline.addEntry(5.7f, "left");
        charaTimeline.addEntry(5.9f, "right");
        
        charaTimeline.addEntry(7.1f, "onFeet");
        
        charaTimeline.addEntry(8.1f, "per_right");
        charaTimeline.addEntry(8.3f, "per_right");
        charaTimeline.addEntry(8.4f, "attack");
        
        charaTimeline.addEntry(9.1f, "per_right");
        charaTimeline.addEntry(9.2f, "per_walk");
        charaTimeline.addEntry(10.8f, "per_walk");
        charaTimeline.addEntry(10.2f, "per_jump");
        charaTimeline.addEntry(10.25f, "per_jump");
        charaTimeline.addEntry(10.9f, "per_right");
        cin1.addCharacterTimeline(charaTimeline);
        
        charaTimeline = new CharacterTimeline(thief, CharacterTimeline.CinematicStatus.END_CINEMATIC);

        charaTimeline.addEntry(0.1f, "per_noPurse");
        charaTimeline.addEntry(0.2f, "per_right");
        charaTimeline.addEntry(5.8f, "per_right");
        
        charaTimeline.addEntry(5.1f, "per_noPurse");
        charaTimeline.addEntry(5.2f, "per_jump");
        charaTimeline.addEntry(5.5f, "per_jump");
        charaTimeline.addEntry(5.9f, "left");
        
        charaTimeline.addEntry(9.1f, "per_right");
        charaTimeline.addEntry(10.9f, "per_right");
        charaTimeline.addEntry(11.1f, "per_right");
        charaTimeline.addEntry(13f, "per_right");
        cin1.addCharacterTimeline(charaTimeline);
        
        game.getGameWorld().addCinematicManager(cin1, index, 0);
        
        trigger = new EventTriggeredObject2D(game.getGameWorld().getWorld(), 0f, 0f, GameEventListener.EventType.CINEMATIC, "startCinematic", 100f, 100f, false);
        game.getGameWorld().addObject2DToWorld(trigger);
        
        // Encounter Temeri cinematic
        
        dialogue = new Dialogue();
        
        dialogue.addReply("Une jeune fille se tient devant vous,\nelle semble attendre quelqu'un ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
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
        
        CinematicManager cinematicManager = new CinematicManager("encounterTemeri", list);
        
        charaTimeline = new CharacterTimeline(hero, CharacterTimeline.CinematicStatus.NORMAL);
        charaTimeline.addEntry(0f, "per_walk");
        charaTimeline.addEntry(0.2f, "per_right");
        charaTimeline.addEntry(1f, "per_right");
        charaTimeline.addEntry(1.2f, "per_walk");
        cinematicManager.addCharacterTimeline(charaTimeline);
        
        cinematicManager.addDialogueTimeline(1.4f, 0);
        cinematicManager.addDialogueTimeline(1.6f, 1);
        cinematicManager.addDialogueTimeline(1.8f, 2);
        game.getGameWorld().addCinematicManager(cinematicManager);
        
        trigger = new EventTriggeredObject2D(game.getGameWorld().getWorld(), 2300f, 0f, GameEventListener.EventType.CINEMATIC, "encounterTemeri", 100f, 1000f, false);
        game.getGameWorld().addObject2DToWorld(trigger);
        
        // dialogue Temeri
        
        list = new ArrayList<Dialogue>();
       
        dialogue = new Dialogue();
        dialogue.addReply("Cela fait une demi-heure que j'attends.\nJ'ai du rater le dernier bus ...", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("*sigh*", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.ANGRY, 1);
        dialogue.addReply("j'imagine que je vais devoir\nrejoindre les bas-fonds à pieds ...", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.ANGRY, 1);
        dialogue.addReply("Comment une jeune fille aussi polie\npeut elle vivre dans les bas-fonds ?", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Seul les criminels et les sales\npetits voleurs vivent là-bas.", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Les criminels ... ... et Temeri !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("(Temeri c'est moi,\nc'est mon nom)", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.DEFAULT, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Je serais ravie de t'aider\nma petite Temeri,\nmais j'ai un voleur à admonester !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Pas de soucis madame,\nbonne poursuite !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.TEMERI, GuiPortrait.Emotion.HAPPY, 1);
            
        list.add(dialogue);
        
        cinematicManager = new CinematicManager("dialogueTemeri", list);
        
        cinematicManager.addDialogueTimeline(0f, 0);
        game.getGameWorld().addCinematicManager(cinematicManager);
        
        ActivableTriggeredObject2D activableTrigger = new ActivableTriggeredObject2D(game.getGameWorld().getWorld(), temeri, Input.Keys.ENTER, GameEventListener.EventType.CINEMATIC, "dialogueTemeri", 50);
        game.getGameWorld().addObject2DToWorld(activableTrigger);
             
        // Music & Sounds.
        
        MusicManager.getInstance().registerResource("sounds/first_lvl.ogg");
        
        this.initSoundsLvl();
    }
    
    @Override
    protected void initSoundsLvl(){
        super.initSoundsLvl();
        
        // Cinematic sounds.
        SoundManager.getInstance().getSound("sounds/cinematic/backOnFeet.ogg");
        SoundManager.getInstance().getSound("sounds/cinematic/onGround.ogg");
    }
}
