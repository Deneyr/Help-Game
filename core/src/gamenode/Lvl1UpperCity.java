/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamenode;

import backgrounds.CityBackground;
import backgrounds.FarBackground;
import backgrounds.HillBackground;
import backgrounds.Lvl1Foreground;
import backgrounds.Lvl1_1_Residence;
import backgrounds.Lvl1_2_Residence;
import backgrounds.Lvl1_3_Residence;
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
import com.mygdx.game.scenery.CityString;
import com.mygdx.game.scenery.Crane;
import com.mygdx.game.scenery.GroundCity;
import com.mygdx.game.scenery.MetalBox;
import com.mygdx.game.scenery.ObstacleHouse;
import com.mygdx.game.scenery.Sign;
import com.mygdx.game.scenery.SmallBox;
import com.mygdx.game.scenery.Pipe;
import com.mygdx.game.scenery.Poutrelle;
import com.mygdx.game.scenery.PoutrelleObstacle;
import com.mygdx.game.scenery.Scaffolding;
import com.mygdx.game.scenery.Speaker;
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
import triggered.BarbedTriggeredObject2D;
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
        
        Lvl1_2_Residence lvl1_2_Residence = new Lvl1_2_Residence(seed, 8300f, -25, 200, 200);
        game.getMapBackgroundPlanes().put(lvl1_2_Residence.getRatioDist(), lvl1_2_Residence);
        
        Lvl1_3_Residence lvl1_3_Residence = new Lvl1_3_Residence(seed, 11200f, -25, 200, 200);
        game.getMapBackgroundPlanes().put(lvl1_3_Residence.getRatioDist(), lvl1_3_Residence);
        
        // init background solid objects
        
        lvl1_1_Residence.createSolidObj(game.getGameWorld());
        
        lvl1_2_Residence.createSolidObj(game.getGameWorld());
        
        lvl1_3_Residence.createSolidObj(game.getGameWorld());
        
        // init foreground
        
        Lvl1Foreground lvl1Foreground = new Lvl1Foreground();
        
        lvl1_1_Residence.createForegroundObj(game.getGameWorld(), lvl1Foreground);
        
        lvl1_2_Residence.createForegroundObj(game.getGameWorld(), lvl1Foreground);
        
        lvl1_3_Residence.createForegroundObj(game.getGameWorld(), lvl1Foreground);
        
        lvl1Foreground.assignTextures();
        
        game.getMapForegroundPlanes().put(lvl1Foreground.getRatioDist(), lvl1Foreground);
        
        // init solid objects
        
        GroundCity ground = new GroundCity(game.getGameWorld().getWorld(), 5024, -150f, 150);
        game.getGameWorld().addObject2DToWorld(ground);
        
        Sign sign = new Sign(game.getGameWorld().getWorld(), 1100f, 0, 0.02f, 2);
        game.getGameWorld().addObject2DToWorld(sign);
        
        sign = new Sign(game.getGameWorld().getWorld(), 1200f, 0, -0.05f, 1);
        game.getGameWorld().addObject2DToWorld(sign);
        
        sign = new Sign(game.getGameWorld().getWorld(), 1700f, 0, 0, 6);
        game.getGameWorld().addObject2DToWorld(sign);
        
        Pipe pipe = new Pipe(game.getGameWorld().getWorld(), 1900f, 20f, 1, false);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        Abribus abribus = new Abribus(game.getGameWorld().getWorld(), 2400f, 15f);
        game.getGameWorld().addObject2DToWorld(abribus);
        
        Bench banc = new Bench(game.getGameWorld().getWorld(), 2400f, -15f);
        game.getGameWorld().addObject2DToWorld(banc);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 2700f, 150f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        Car car = new Car(game.getGameWorld().getWorld(), 3000f, 10f, 0, 1);
        game.getGameWorld().addObject2DToWorld(car);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 3200f, 150f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 3500f, 220f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        TreeWithoutLeaf tree = new TreeWithoutLeaf(game.getGameWorld().getWorld(), 3800f, 70f);
        game.getGameWorld().addObject2DToWorld(tree);
        
        Trashcan trashcan = new Trashcan(game.getGameWorld().getWorld(), 4000f, 0, 1, 1);
        game.getGameWorld().addObject2DToWorld(trashcan);
        
        trashcan = new Trashcan(game.getGameWorld().getWorld(), 4150f, 0, 0, 1);
        game.getGameWorld().addObject2DToWorld(trashcan);
        
        Bus bus = new Bus(game.getGameWorld().getWorld(), 4500f, 60f, 0, -1);
        game.getGameWorld().addObject2DToWorld(bus);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 4000f, 420f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 4400f, 320f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 4800f, 220f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        sign = new Sign(game.getGameWorld().getWorld(), 5200f, 0, -0.05f, 4);
        game.getGameWorld().addObject2DToWorld(sign);
        
        PoutrelleObstacle poutrelleObst = new PoutrelleObstacle(game.getGameWorld().getWorld(), 5360f, 210f, (float)Math.PI/2, 0, 1);
        game.getGameWorld().addObject2DToWorld(poutrelleObst);
        
        Crane crane = new Crane(game.getGameWorld().getWorld(), 6000f, 270, 0.8f, 0.1f, 0.9f, 0.2f, true);
        game.getGameWorld().addObject2DToWorld(crane);
        
        CityString cityString = new CityString(game.getGameWorld().getWorld(), 8000f, 600f);
        game.getGameWorld().addObject2DToWorld(cityString);
            
        Speaker speaker = new Speaker(game.getGameWorld().getWorld(), 10000f, 15f);
        game.getGameWorld().addObject2DToWorld(speaker);
        
        Scaffolding scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 10300f, 70f, 2); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 10600f, 110f, 1); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 12450f, 70f, 3); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 13000f, 110f, 1); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        Poutrelle poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 13300f, 100f, (float) (Math.PI / 2), new Vector2(1, 0), 5f, 5, 1);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        BarbedTriggeredObject2D barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 13300f, 0, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        crane = new Crane(game.getGameWorld().getWorld(), 14000f, 270f, 0.8f, 0.1f, 0.55f, 0.8f, false);
        game.getGameWorld().addObject2DToWorld(crane);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 14100, 40f, 2); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 14550f, 130f, 0f, null, 0f, 0f, 0.5f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        ObstacleHouse obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 15100f, 70f, 2); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 16000, 10f, 2); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 16300, 70f, 3); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 16500f, 300f, 4); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 15550f, 450f, (float)-Math.PI/4, null, 0f, 0f, 0.5f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 15800, 300f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 16100, 500f, 1, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        crane = new Crane(game.getGameWorld().getWorld(), 16550f, 885f, 0.8f, 0.1f, 0.55f, 0.9f, false);
        game.getGameWorld().addObject2DToWorld(crane);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 17200f, 700, 0f, null, 0f, 0f, 1f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 18000, 250f, 3); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 16800f, 250, 0f, new Vector2(0, 1), 3f, 5, 0.25f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 18500, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 19000, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 19400, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        pipe = new Pipe(game.getGameWorld().getWorld(), 20000, 500f, 0.5f, true);
        game.getGameWorld().addObject2DToWorld(pipe);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 20600, 190f, 5); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 18300f, 250, 0f, new Vector2(0, 1), 3f, 5, 0.25f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 19000f, 350, 0f, new Vector2(1, 0), 3f, 6, 0.5f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        
        crane = new Crane(game.getGameWorld().getWorld(), 22200f, 270f, 0.8f, 0.1f, 0.55f, 0.45f, false);
        game.getGameWorld().addObject2DToWorld(crane);
            
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 22720f, 240f, 0f, null, 0f, 0f, 0.5f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        bus = new Bus(game.getGameWorld().getWorld(), 23250f, 60f, 0, 1);
        game.getGameWorld().addObject2DToWorld(bus);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 23700f, 220f, 2); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        crane = new Crane(game.getGameWorld().getWorld(), 23790f, 550f, 0.8f, 0.1f, 0.55f, 1f, true);
        game.getGameWorld().addObject2DToWorld(crane);
        
        poutrelle = new Poutrelle(game.getGameWorld().getWorld(), 23250f, 350f, 0f, null, 0f, 0f, 0.5f);
        game.getGameWorld().addObject2DToWorld(poutrelle);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 24000f, 300f, 4); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        scaffolding = new Scaffolding(game.getGameWorld().getWorld(), 24210f, 500f, 0); 
        game.getGameWorld().addObject2DToWorld(scaffolding);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 24430f, 400f, 3); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 24720f, 600f, 3); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 25000f, 800f, 8); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        speaker = new Speaker(game.getGameWorld().getWorld(), 24750f, 900f);
        game.getGameWorld().addObject2DToWorld(speaker);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 25300f, 750f, 3); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 25600f, 650f, 3); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 25900f, 600f, 3); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        obstacleHouse = new ObstacleHouse(game.getGameWorld().getWorld(), 26200f, 550f, 9); 
        game.getGameWorld().addObject2DToWorld(obstacleHouse);
        
        speaker = new Speaker(game.getGameWorld().getWorld(), 25900f, 900f);
        game.getGameWorld().addObject2DToWorld(speaker);
        
        speaker = new Speaker(game.getGameWorld().getWorld(), 26250f, 800f);
        game.getGameWorld().addObject2DToWorld(speaker);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 23580f, 450f, (float) Math.PI / 4);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 23920f, 600f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 24450f, 680f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 24500f, 680f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 24380f, 680f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 24540f, 820f, (float) Math.PI / 2);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 24830f, 1030f, (float) Math.PI / 2);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 24880f, 1070f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25200f, 1030f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25360f, 1030f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25480f, 1000f, (float) -Math.PI / 2);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25500f, 930f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25550f, 930f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25730f, 930f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25800f, 880f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 25970f, 880f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 26030f, 880f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 26250f, 880f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 26350f, 800f, 0);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        barbed = new BarbedTriggeredObject2D(game.getGameWorld().getWorld(), 26450f, 780f, (float) -Math.PI / 4);
        game.getGameWorld().addObject2DToWorld(barbed, true);
        
        /*Crane crane = new Crane(game.getGameWorld().getWorld(), -600f, 270, 0.7f, 0f, false);
        game.getGameWorld().addObject2DToWorld(crane, true);*/
        /*
        Speaker abribus = new Speaker(game.getGameWorld().getWorld(), 400f, 0f);
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
            hero = new Grandma(game.getGameWorld().getWorld(), 0, 1100f);
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
        
        box = new SmallBox(game.getGameWorld().getWorld(), 5500f, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 5550f, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 5525f, 50f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        strongBox = new StrongBox(game.getGameWorld().getWorld(), 6000f, 100f);
        game.getGameWorld().addObject2DToWorld(strongBox, true);
        
        strongBox = new StrongBox(game.getGameWorld().getWorld(), 6000f, 200f);
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
        
        box = new SmallBox(game.getGameWorld().getWorld(), 8000f, 600f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 8000f, 650f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 9400f, 600f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 8900f, 600f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 8950, 600f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 8925, 650f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 9200f, 1500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 9250, 1500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 9225, 1550f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        
        box = new MetalBox(game.getGameWorld().getWorld(), 10500f, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 12500f, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 13100f, 150f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 13120f, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 13270f, 500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 13320f, 500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 15000f, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 15700f, 350f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new MetalBox(game.getGameWorld().getWorld(), 16000f, 550f);
        game.getGameWorld().addObject2DToWorld(box, true);
                    
        box = new MetalBox(game.getGameWorld().getWorld(), 16350f, 250f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        
        box = new SmallBox(game.getGameWorld().getWorld(), 18475, 550f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 18525f, 550f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 18500, 600f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 20025, 550f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 19975, 550f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 20000, 600f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 19000, 650f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21110, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21115, 40f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21120, 80f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21125, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 40f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 80f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21290, 0f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21285, 40f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21280, 80f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21275, 100f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        strongBox = new StrongBox(game.getGameWorld().getWorld(), 21160, 150f);
        game.getGameWorld().addObject2DToWorld(strongBox, true);
        
        strongBox = new StrongBox(game.getGameWorld().getWorld(), 21240, 150f);
        game.getGameWorld().addObject2DToWorld(strongBox, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21140, 200f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21145, 240f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21150, 280f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21155, 300f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21260, 200f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21255, 240f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21250, 280f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21245, 300f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        strongBox = new StrongBox(game.getGameWorld().getWorld(), 21200, 350f);
        game.getGameWorld().addObject2DToWorld(strongBox, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 400f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 440f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 480f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        box = new SmallBox(game.getGameWorld().getWorld(), 21200, 500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        
        box = new MetalBox(game.getGameWorld().getWorld(), 23810f, 500f);
        game.getGameWorld().addObject2DToWorld(box, true);
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 2; j++){
                box = new SmallBox(game.getGameWorld().getWorld(), 11100f + j * 40f, i * 40f);
                game.getGameWorld().addObject2DToWorld(box, true);
            } 
        }      
             
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 2; j++){
                box = new SmallBox(game.getGameWorld().getWorld(), 10850f + j * 40f, i * 40f);
                game.getGameWorld().addObject2DToWorld(box, true);
            } 
        } 
        
        
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                box = new SmallBox(game.getGameWorld().getWorld(), 14500f + j * 40f, 180f + i * 40f);
                game.getGameWorld().addObject2DToWorld(box, true);
            } 
        } 
        
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                box = new SmallBox(game.getGameWorld().getWorld(), 18975f + j * 40f, 550f + i * 40f);
                game.getGameWorld().addObject2DToWorld(box, true);
            } 
        } 
        
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                box = new SmallBox(game.getGameWorld().getWorld(), 19375f + j * 40f, 550f + i * 40f);
                game.getGameWorld().addObject2DToWorld(box, true);
            } 
        } 
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
              
        this.initSoundsLvl();
    }
    
    @Override
    protected void initSoundsLvl(){
        super.initSoundsLvl();
        
        // Musics
        MusicManager.getInstance().registerResource("sounds/first_lvl.ogg");
        MusicManager.getInstance().registerResource("sounds/temeri.ogg");
        
        // Cinematic sounds.
        SoundManager.getInstance().getSound("sounds/cinematic/backOnFeet.ogg");
        SoundManager.getInstance().getSound("sounds/cinematic/onGround.ogg");
    }
}
