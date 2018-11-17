/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BackgroundWorld;
import com.mygdx.game.GameWorld;
import static com.mygdx.game.HelpGame.P2M;
import java.util.ArrayList;
import ressourcesmanagers.TextureManager;

/**
 *
 * @author Deneyr
 */
public class HostelBackground extends BackgroundWorld{

    public static final String HOSTEL = "background/House_Hostel.png";
    
    private Vector2 startPointPart;
    
    public HostelBackground(int seed, float startPointX, float startPointY){
        super(seed);
        
        this.ratioDist = 1f;
        
        this.startPointPart = new Vector2(startPointX * P2M, startPointY * P2M);
        
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
        
            part.addObject2D2Scenary(house);
            part.createSpriteList(this.seed);

            this.backgroundPartList.put(part.getStartPart().x, part);
            
        }
    }
    
    @Override
    public void createSoldidObj(GameWorld gameWorld){
        
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
        
        ArrayList[][] residenceMap = {
            {listWhole, listTop, listTop, listTopWallRight},
            {listWhole, listTrapdoor, listTopDoorLeft, listTopWallRight},
            {listTopDoorLeft, listTopDoorLeft, listTop, listStairRight}
        };
        
        this.createSoldidObjFrom(gameWorld, residenceMap, 200, 200, 1f);
    }
    
    protected void createSoldidObjFrom(GameWorld gameWorld, ArrayList[][] residenceMap, int canvasWidth, int canvasHeight, float ratioObject){
            
        for(int i = 0; i < residenceMap.length; i++){
            for(int j = 0; j < residenceMap[0].length; j++){
        
                // We add the width/height over two because the origin of the sprite is not at the middle for the background parts.
                float posX = this.startPointPart.x / P2M + canvasWidth/2 + canvasWidth * j * ratioObject;
                float posY = this.startPointPart.y / P2M + canvasHeight/2 + canvasHeight * (residenceMap.length - i - 1) * ratioObject;
                
                BackgroundSolidObject backgroundSolidObject = new BackgroundSolidObject(gameWorld.getWorld(), posX , posY , residenceMap[i][j], canvasWidth, canvasHeight, ratioObject);

                gameWorld.addObject2DToWorld(backgroundSolidObject);
            }
        }
                  
    }
}
