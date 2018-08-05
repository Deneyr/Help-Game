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
import characters.Grandma;
import characters.OpponentThief;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEventListener;
import com.mygdx.game.HelpGame;
import com.mygdx.game.scenery.Abribus;
import com.mygdx.game.scenery.Banc;
import com.mygdx.game.scenery.GroundCity;
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
        
        // init solid objects
        
        
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
        
        // init opponent
        OpponentThief thief = new OpponentThief(game.getGameWorld().getWorld(), hero, -390f, 0f);
        game.getGameWorld().addObject2DToWorld(thief, false);
        
        // init scenary
        
        for(int i = -5 ;i < 10; i++){
            GroundCity ground = new GroundCity(game.getGameWorld().getWorld(), i * 256f, -150f);
            game.getGameWorld().addObject2DToWorld(ground);
        }
        /*
        Abribus abribus = new Abribus(game.getGameWorld().getWorld(), 400f, 0f);
        game.getGameWorld().addObject2DToWorld(abribus);
        
        Banc banc = new Banc(game.getGameWorld().getWorld(), 400f, -15f);
        game.getGameWorld().addObject2DToWorld(banc);
        
        TreeWithoutLeaf tree = new TreeWithoutLeaf(game.getGameWorld().getWorld(), 600f, 50);
        game.getGameWorld().addObject2DToWorld(tree);
        tree = new TreeWithoutLeaf(game.getGameWorld().getWorld(), 700, 50);
        game.getGameWorld().addObject2DToWorld(tree);*/
        
        
        // box 
        

        
        // Cannon
        

        
        // Cinematics
        
        // Start cinematic
        Dialogue dialogue = new Dialogue();
        dialogue.addReply("Vous chercher\nUn monde exempt de souffrance ?\n Un mode exempt de mal ?\n  Exempt de criminels ?", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Bienvenue à TEO Cité,\nla ville ou la criminalité est punie\npar le plus juste des chatiments :", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Le banissement !", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Nous vous garantissons\nune sécurité sans faille,\net ce, de 6h à 21h !", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);
        dialogue.addReply("Notre histoire commence\ndans un quartier uppée de la ville ...", GuiPortrait.Character.NONE, GuiPortrait.Emotion.ANGRY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, -1);        
        List<Dialogue> list = new ArrayList<Dialogue>();
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply("Il fait un peu frisquet,\nj'ai interet à rentrer vite !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Habitants de TEO Cité,\nil est actuellement 20h50,\nle couvre-feu va bientôt débuter.", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("Conformement à la loi\n421-HJ156-543.14159265359\nAlinea ln(129i+12)²", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("Toute personne présente passé 21h\nsera severement poursuivie !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 1);
        dialogue.addReply("Juste ciel !\nJe dois me dépeche de rentrer !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.HAPPY, GuiPortrait.Character.SPEAKER, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply("Haaaaaaaaaaa !!", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply("Qu'est ce qui se passe ?!\nAu secours !\nQuelqu'un ...\n Aidez moi !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.NONE, GuiPortrait.Emotion.DEFAULT, 0);
        dialogue.addReply("Hahahahaha !\nAlors la vielle ?\n tu ne sais pas qu'après 21h\nc'est nous les boss de la cité ?", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 1);
        dialogue.addReply("Personne ne va venir t'aider !\nTu peux crier \"Help\"\nautant que tu veux !", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 1);
        list.add(dialogue);
        
        dialogue = new Dialogue();  
        dialogue.addReply(".........", GuiPortrait.Character.GRANDMA, GuiPortrait.Emotion.SORROW, GuiPortrait.Character.PRIDE, GuiPortrait.Emotion.HAPPY, 0);
        list.add(dialogue);
        
        CinematicManager cin1 = new CinematicManager("startCinematic", list, true, this.getId());
        
        cin1.addDialogueTimeline(0f, 0);
        cin1.addDialogueTimeline(1f, 1);
        cin1.addDialogueTimeline(5f, 2);
        cin1.addDialogueTimeline(6f, 3);
        cin1.addDialogueTimeline(7f, 4);
        
        CharacterTimeline charaTimeline = new CharacterTimeline(hero, CharacterTimeline.CinematicStatus.NORMAL);
        
        charaTimeline.addEntry(0.1f, "switch");
        charaTimeline.addEntry(0.2f, "per_gotPurse");
        charaTimeline.addEntry(0.3f, "per_walkRight");
        charaTimeline.addEntry(0.4f, "per_right");
        charaTimeline.addEntry(5.0f, "per_walkRight");
        charaTimeline.addEntry(5.1f, "per_right");
        charaTimeline.addEntry(5.2f, "per_gotPurse");
        charaTimeline.addEntry(5.3f, "onGround");
        charaTimeline.addEntry(5.4f, "left");
        charaTimeline.addEntry(5.5f, "right");
        charaTimeline.addEntry(5.7f, "left");
        charaTimeline.addEntry(5.9f, "right");
        cin1.addCharacterTimeline(charaTimeline);
        
        charaTimeline = new CharacterTimeline(thief, CharacterTimeline.CinematicStatus.END_CINEMATIC);

        charaTimeline.addEntry(0.2f, "per_right");
        charaTimeline.addEntry(5.8f, "per_right");
        
        charaTimeline.addEntry(5.2f, "per_jump");
        charaTimeline.addEntry(5.5f, "per_jump");
        charaTimeline.addEntry(5.9f, "left");
        cin1.addCharacterTimeline(charaTimeline);
        
        game.getGameWorld().addCinematicManager(cin1, index, 0);
        
        EventTriggeredObject2D trigger = new EventTriggeredObject2D(game.getGameWorld().getWorld(), 0f, 0f, GameEventListener.EventType.CINEMATIC, "startCinematic", 500);
        game.getGameWorld().addObject2DToWorld(trigger);
        
        
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
