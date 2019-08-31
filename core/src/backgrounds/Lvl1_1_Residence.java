/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import static backgrounds.HostelBackground.HOSTEL;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.GameWorld;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class Lvl1_1_Residence extends HostelBackground{
    
    public static final String RESIDENCE1 = "background/house/House_Hostel.png";
    public static final String RESIDENCE2 = "background/house/House_Hostel2.png";
    public static final String RESIDENCE3 = "background/house/House_Hostel3.png";
    
    public Lvl1_1_Residence(int seed, float startPointX, float startPointY, int canvasWidth, int canvasHeight) {
        super(seed, startPointX, startPointY, canvasWidth, canvasHeight);
    }
    
    @Override
    public void assignTextures(){

        Texture house1 = TextureManager.getInstance().getTexture(RESIDENCE1, this);
        Texture house2 = TextureManager.getInstance().getTexture(RESIDENCE2, this);
        Texture house3 = TextureManager.getInstance().getTexture(RESIDENCE3, this);
        
        if(house1 != null && house2 != null && house3 != null){
            
            String[][] residenceMap = {
                {"1:1:0", "1:1:1", "1:1:1", "1:1:2", "1:1:1", "1:1:3"},
                {"0:1:0", "0:3:1", "0:0:2", "0:0:3", "0:0:5", "0:2:4"},
                {"2:1:0", "0:0:2", "0:0:4", "0:0:0", "2:1:5", "0:1:0"},
                {"2:1:0", "2:0:3", "2:0:5", "2:1:5", "2:1:0", "2:1:0"},
                {"0:1:1", "2:0:2", "2:1:5", "2:1:0", "2:1:0", "2:1:0"},
            };
            
            
            ResidencePart part = new ResidencePart(this.startPointPart, residenceMap, 200, 200, 1f);
        
            part.addTexture2Scenary(house1);
            part.addTexture2Scenary(house2);
            part.addTexture2Scenary(house3);
            part.createSpriteList(this.seed);

            this.backgroundPartList.put(part.getStartPart().x, part);
            
        }
    }
    
    @Override
    public void createSolidObj(GameWorld gameWorld){

        ArrayList<RoomCollisionType> listLeftRoof = new ArrayList<RoomCollisionType>();
        listLeftRoof.add(RoomCollisionType.LEFT_ROOF);
        
        ArrayList<RoomCollisionType> listRightRoof = new ArrayList<RoomCollisionType>();
        listRightRoof.add(RoomCollisionType.RIGHT_ROOF);
        
        ArrayList<RoomCollisionType> listRoof = new ArrayList<RoomCollisionType>();
        listRoof.add(RoomCollisionType.WHOLE_ROOF);
        
        ArrayList<RoomCollisionType> listTrapRoof = new ArrayList<RoomCollisionType>();
        listTrapRoof.add(RoomCollisionType.TRAP_ROOF);
        
        ArrayList<RoomCollisionType> listTopWindowRight = new ArrayList<RoomCollisionType>();
        listTopWindowRight.add(RoomCollisionType.TOP);
        listTopWindowRight.add(RoomCollisionType.RIGHT_WINDOW);
        
        ArrayList<RoomCollisionType> listTopDoorRight = new ArrayList<RoomCollisionType>();
        listTopDoorRight.add(RoomCollisionType.TOP);
        listTopDoorRight.add(RoomCollisionType.RIGHT);
        
        ArrayList<RoomCollisionType> listTop = new ArrayList<RoomCollisionType>();
        listTop.add(RoomCollisionType.TOP);
        
        ArrayList<RoomCollisionType> listWhole = new ArrayList<RoomCollisionType>();
        listWhole.add(RoomCollisionType.WHOLE);
        
        ArrayList<RoomCollisionType> listOpenLeft= new ArrayList<RoomCollisionType>();
        listOpenLeft.add(RoomCollisionType.TOP);
        listOpenLeft.add(RoomCollisionType.LEFT_DOOR);
        
        ArrayList<RoomCollisionType> listStairOnlyRight = new ArrayList<RoomCollisionType>();
        listStairOnlyRight.add(RoomCollisionType.STAIRS_ONLY_RIGHT);
       
        
        ArrayList[][] residenceMap = {
            {listLeftRoof, listRoof, listRoof, listTrapRoof, listRoof, listRightRoof},
            {listWhole, listTopDoorRight, listTop, null, listTop, listTopWindowRight},
            {listWhole, listTop, listTop, listTop, listStairOnlyRight, listWhole},
            {listWhole, null, null, listStairOnlyRight, null, listWhole},
            {listOpenLeft, null, listStairOnlyRight, null, null, listWhole}
        };
        
        this.createSolidObjFrom(gameWorld, residenceMap, 1f);
    }
    
    @Override
    public void createForegroundObj(GameWorld gameWorld, StructureForeground structureForeground){
        this.createForegroundObjFrom(gameWorld, structureForeground, 6, 4, 1f);
    }
}
