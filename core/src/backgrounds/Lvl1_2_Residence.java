/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.BackgroundWorld;
import com.mygdx.game.GameWorld;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class Lvl1_2_Residence extends HostelBackground{
    
    public static final String RESIDENCE1 = "background/house/House_Hostel.png";
    public static final String RESIDENCE2 = "background/house/House_Hostel2.png";
    public static final String RESIDENCE3 = "background/house/House_Hostel3.png";
    
    public Lvl1_2_Residence(int seed, float startPointX, float startPointY, int canvasWidth, int canvasHeight) {
        super(seed, startPointX, startPointY, canvasWidth, canvasHeight);
        
        this.ratioDist = 1.00001f;
    }
    
    @Override
    public void assignTextures(){

        Texture house1 = TextureManager.getInstance().getTexture(RESIDENCE1, this);
        Texture house2 = TextureManager.getInstance().getTexture(RESIDENCE2, this);
        Texture house3 = TextureManager.getInstance().getTexture(RESIDENCE3, this);
        
        if(house1 != null && house2 != null && house3 != null){
            
            String[][] residenceMap = {
                {"1:1:0", "1:1:1", "1:1:1", "1:1:2", "1:1:1", "1:1:3"},
                {"0:1:0", "2:1:0", "1:1:4", "2:0:1", "0:1:1", "0:2:1"},
                {"2:1:0", "1:1:4", "2:1:5", "2:1:0", "0:1:4", "0:2:1"},
                {"2:2:5", "2:1:5", "0:0:3", "0:0:0", "2:1:1", "2:2:1"},
                {"0:1:0", "2:1:0", "2:1:0", "2:1:0", "2:1:0", "2:1:0"},
                {"2:1:0", "2:1:0", "2:1:0", "2:1:0", "2:1:0", "2:1:0"},
                {"2:1:0", "2:1:0", "2:1:0", "2:1:0", "2:1:0", "2:1:0"},
            };
            
            
            BackgroundWorld.ResidencePart part = new BackgroundWorld.ResidencePart(this.startPointPart, residenceMap, 200, 200, 1f);
        
            part.addTexture2Scenary(house1);
            part.addTexture2Scenary(house2);
            part.addTexture2Scenary(house3);
            part.createSpriteList(this.seed);

            this.backgroundPartList.put(part.getStartPart().x, part);
            
        }
    }
    
    @Override
    public void createSolidObj(GameWorld gameWorld){

        ArrayList<BackgroundWorld.RoomCollisionType> listLeftRoof = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listLeftRoof.add(BackgroundWorld.RoomCollisionType.LEFT_ROOF);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listRightRoof = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listRightRoof.add(BackgroundWorld.RoomCollisionType.RIGHT_ROOF);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listRoof = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listRoof.add(BackgroundWorld.RoomCollisionType.WHOLE_ROOF);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listTrapRoof = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listTrapRoof.add(BackgroundWorld.RoomCollisionType.TRAP_ROOF);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listTrap = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listTrap.add(BackgroundWorld.RoomCollisionType.TRAPDOOR);
        
        ArrayList<RoomCollisionType> listOpenLeft= new ArrayList<RoomCollisionType>();
        listOpenLeft.add(RoomCollisionType.TOP);
        listOpenLeft.add(RoomCollisionType.LEFT_DOOR);
        
        ArrayList<RoomCollisionType> listTopWindowLeft = new ArrayList<RoomCollisionType>();
        listTopWindowLeft.add(RoomCollisionType.TOP);
        listTopWindowLeft.add(RoomCollisionType.LEFT_WINDOW);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listWhole = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listWhole.add(BackgroundWorld.RoomCollisionType.WHOLE);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listStairOnlyRight = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listStairOnlyRight.add(BackgroundWorld.RoomCollisionType.STAIRS_ONLY_RIGHT);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listStairLeft = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listStairLeft.add(BackgroundWorld.RoomCollisionType.STAIRS_LEFT);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listStairUpLeft = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listStairUpLeft.add(BackgroundWorld.RoomCollisionType.STAIRS_UP_LEFT);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listTopRight = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listTopRight.add(BackgroundWorld.RoomCollisionType.TOP);
        listTopRight.add(BackgroundWorld.RoomCollisionType.RIGHT);
        
        ArrayList<BackgroundWorld.RoomCollisionType> listRight = new ArrayList<BackgroundWorld.RoomCollisionType>();
        listRight.add(BackgroundWorld.RoomCollisionType.RIGHT);
       
        
        this.residenceMap = new ArrayList[][]{
            {listLeftRoof, listRoof, listRoof, listTrapRoof, listRoof, listRightRoof},
            {listWhole, listWhole, listStairUpLeft, listTrap, listOpenLeft, listTopRight},
            {listWhole, listStairUpLeft, listStairOnlyRight, listWhole, listStairLeft, listTopRight},
            {listTopWindowLeft, listStairOnlyRight, null, null, listOpenLeft, listRight},
            {listWhole, listWhole, listWhole, listWhole, listWhole, listWhole},
            {listWhole, listWhole, listWhole, listWhole, listWhole, listWhole},
            {listWhole, listWhole, listWhole, listWhole, listWhole, listWhole}
        };
        
        this.createSolidObjFrom(gameWorld, this.residenceMap, 1f);
    }
    
    @Override
    public void createForegroundObj(GameWorld gameWorld, StructureForeground structureForeground){
        this.createForegroundObjFrom(gameWorld, structureForeground, 1, 1f);
    }
}