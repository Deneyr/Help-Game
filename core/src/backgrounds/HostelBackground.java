/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BackgroundWorld;
import com.mygdx.game.GameWorld;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;
import triggered.EnterQuitStructureTriggeredObject2D;

/**
 *
 * @author Deneyr
 */
public class HostelBackground extends BackgroundWorld{

    public static final String HOSTEL = "background/house/House_Hostel.png";
    
    protected Vector2 startPointPart;
    
    protected int canvasWidth;
    protected int canvasHeight;
    
    protected ArrayList[][] residenceMap;
    
    public HostelBackground(int seed, float startPointX, float startPointY, int canvasWidth, int canvasHeight){
        super(seed);
        
        this.ratioDist = 1f;
        
        this.startPointPart = new Vector2(startPointX * P2M, startPointY * P2M);
        
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        
        this.residenceMap = new ArrayList[][]{};
        
        this.assignTextures();
    }
    
    @Override
    public void step(float delta) {
        // nothing to do for now.
    }
    
    @Override
    public void assignTextures(){

        Texture house = TextureManager.getInstance().getTexture(HOSTEL, this);
        
        if(house != null){
            
            String[][] residenceMap = {
                {"0:1", "3:0", "5:0", "1:2"},
                {"0:1", "1:0", "1:1", "1:2"},
                {"1:1", "1:1", "5:0", "5:1"},
            };
            
            
            ResidencePart part = new ResidencePart(this.startPointPart, residenceMap, 200, 200, 1f);
        
            part.addTexture2Scenary(house);
            part.createSpriteList(this.seed);

            this.backgroundPartList.put(part.getStartPart().x, part);
            
        }
    }
    
    @Override
    public void createSolidObj(GameWorld gameWorld){

        ArrayList<RoomCollisionType> listTopDoorLeft = new ArrayList<RoomCollisionType>();
        listTopDoorLeft.add(RoomCollisionType.TOP);
        listTopDoorLeft.add(RoomCollisionType.LEFT_DOOR);
        
        ArrayList<RoomCollisionType> listTopDoorRight = new ArrayList<RoomCollisionType>();
        listTopDoorRight.add(RoomCollisionType.TOP);
        listTopDoorRight.add(RoomCollisionType.RIGHT_DOOR);
        
        ArrayList<RoomCollisionType> listTop = new ArrayList<RoomCollisionType>();
        listTop.add(RoomCollisionType.TOP);
        
        ArrayList<RoomCollisionType> listTopWallRight = new ArrayList<RoomCollisionType>();
        listTopWallRight.add(RoomCollisionType.TOP);
        listTopWallRight.add(RoomCollisionType.RIGHT);
        
        ArrayList<RoomCollisionType> listWhole = new ArrayList<RoomCollisionType>();
        listWhole.add(RoomCollisionType.WHOLE);
        
        ArrayList<RoomCollisionType> listStairRight = new ArrayList<RoomCollisionType>();
        listStairRight.add(RoomCollisionType.STAIRS_RIGHT);
       
        ArrayList<RoomCollisionType> listTrapdoor = new ArrayList<RoomCollisionType>();
        listTrapdoor.add(RoomCollisionType.TRAPDOOR);
        
        this.residenceMap = new ArrayList[][]{
            {listWhole, listTop, listTop, listTopWallRight},
            {listWhole, listTrapdoor, listTopDoorLeft, listTopWallRight},
            {listTopDoorLeft, listTopDoorLeft, listTop, listStairRight}
        };
        
        this.createSolidObjFrom(gameWorld, this.residenceMap, 1f);
    }
    
    @Override
    public void createForegroundObj(GameWorld gameWorld, StructureForeground structureForeground){
        this.createForegroundObjFrom(gameWorld, structureForeground, 0, 1f);
    }
    
    protected void createSolidObjFrom(GameWorld gameWorld, ArrayList[][] residenceMap, float ratioObject){
            
        for(int i = 0; i < residenceMap.length; i++){
            for(int j = 0; j < residenceMap[0].length; j++){
                if(residenceMap[i][j] != null){
                    // We add the width/height over two because the origin of the sprite is not at the middle for the background parts.
                    float posX = this.startPointPart.x / P2M + this.canvasWidth/2 + this.canvasWidth * j * ratioObject;
                    float posY = this.startPointPart.y / P2M + this.canvasHeight/2 + this.canvasHeight * (residenceMap.length - i - 1) * ratioObject;

                    BackgroundSolidObject backgroundSolidObject = new BackgroundSolidObject(gameWorld.getWorld(), posX , posY , residenceMap[i][j], this.canvasWidth, this.canvasHeight, ratioObject);

                    gameWorld.addObject2DToWorld(backgroundSolidObject);
                }
            }
        }
                  
    }
    
    protected void createForegroundObjFrom(GameWorld gameWorld, StructureForeground structureForeground, int indexTexture, float ratioObject){
        
        int nbCanvasWidth = 0;
        int nbCanvasHeight = 0;
        if(this.residenceMap.length > 0){
            nbCanvasWidth = this.residenceMap[0].length;
            nbCanvasHeight = this.residenceMap.length - 1;
        }
        
        EnterQuitStructureTriggeredObject2D trigger = new EnterQuitStructureTriggeredObject2D(gameWorld.getWorld(), this.startPointPart.x / P2M + nbCanvasWidth * this.canvasWidth * ratioObject / 2, this.startPointPart.y / P2M + nbCanvasHeight * this.canvasHeight * ratioObject / 2, nbCanvasWidth * this.canvasWidth * ratioObject, nbCanvasHeight * this.canvasHeight * ratioObject);
        gameWorld.addObject2DToWorld(trigger, true);
        
        structureForeground.addStructurePart(trigger.getStructureID(), indexTexture, new Vector2(this.startPointPart.x, this.startPointPart.y), new Vector2(this.startPointPart.x + nbCanvasWidth * this.canvasWidth * ratioObject * P2M, this.startPointPart.y + nbCanvasHeight * ratioObject * this.canvasHeight * P2M));
    }
}
