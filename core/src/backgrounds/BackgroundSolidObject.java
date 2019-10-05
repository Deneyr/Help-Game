/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgrounds;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BackgroundWorld;
import static com.mygdx.game.HelpGame.P2M;
import com.mygdx.game.SolidObject2D;
import java.util.ArrayList;

/**
 *
 * @author Deneyr
 */
public class BackgroundSolidObject extends SolidObject2D{
    public BackgroundSolidObject(World world, float posX, float posY, ArrayList<BackgroundWorld.RoomCollisionType> listRoomCollisionType, int canvasWidth, int canvasHeight, float ratioObject){
        
        // Part physic 
        BodyDef groundBodyDef = new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX * P2M, posY * P2M));  
        
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);  
        
        groundBody.setType(BodyDef.BodyType.StaticBody);
        
        this.collisionFixture = new ArrayList<Fixture>();
        
        FixtureDef fixtureDef = new FixtureDef();
        
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit
        // Create a fixture from our polygon shape and add it to our ground body  
        
        this.setCollisionFilterMask(fixtureDef, false);

        for(BackgroundWorld.RoomCollisionType type : listRoomCollisionType){

            // Create a polygon shape
            PolygonShape ground = new PolygonShape();
            Fixture fix;
            
            switch(type){
                case LEFT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 20 * P2M * ratioObject, 
                                    canvasHeight / 2 * P2M * ratioObject, 
                                    new Vector2(-canvasWidth / 2 * ratioObject * P2M + canvasWidth / 20 * P2M * ratioObject, 0), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);

                    break;
                case RIGHT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 20 * P2M * ratioObject, 
                                    canvasHeight / 2 * P2M * ratioObject, 
                                    new Vector2(canvasWidth / 2 * ratioObject * P2M - canvasWidth / 20 * P2M * ratioObject, 0), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case WHOLE:                  
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 2 * P2M * ratioObject, 
                                    canvasHeight / 2 * P2M * ratioObject, 
                                    new Vector2(0, 0), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case LEFT_DOOR:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 20 * P2M * ratioObject, 
                                    canvasHeight / 4 * P2M * ratioObject, 
                                    new Vector2(-canvasWidth / 2 * ratioObject * P2M + canvasWidth / 20 * P2M * ratioObject, canvasHeight / 4 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                    
                case RIGHT_DOOR:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 20 * P2M * ratioObject, 
                                    canvasHeight / 4 * P2M * ratioObject, 
                                    new Vector2(canvasWidth / 2 * ratioObject * P2M - canvasWidth / 20 * P2M * ratioObject, canvasHeight / 4 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case LEFT_WINDOW:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 20 * P2M * ratioObject, 
                                    canvasHeight / 4 * P2M * ratioObject, 
                                    new Vector2(-canvasWidth / 2 * ratioObject * P2M + canvasWidth / 20 * P2M * ratioObject, -canvasHeight / 4 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                    
                case RIGHT_WINDOW:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 20 * P2M * ratioObject, 
                                    canvasHeight / 4 * P2M * ratioObject, 
                                    new Vector2(canvasWidth / 2 * ratioObject * P2M - canvasWidth / 20 * P2M * ratioObject, -canvasHeight / 4 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case TOP:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 2 * P2M * ratioObject,
                                    canvasHeight / 20 * P2M * ratioObject, 
                                    new Vector2(0, canvasHeight / 2 * P2M * ratioObject - canvasHeight / 20 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case BOTTOM:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 2 * P2M * ratioObject,
                                    canvasHeight / 20 * P2M * ratioObject, 
                                    new Vector2(0, -canvasHeight / 2 * P2M * ratioObject + canvasHeight / 20 * P2M * ratioObject), 0);  
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case STAIRS_RIGHT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject,
                        canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 8 * P2M * ratioObject,
                                    canvasHeight / 20 * P2M * ratioObject, 
                                    new Vector2(-canvasWidth / 2 * P2M * ratioObject + canvasWidth / 8 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject - canvasHeight / 20 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case STAIRS_LEFT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{-canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject,
                        canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 8 * P2M * ratioObject,
                                    canvasHeight / 20 * P2M * ratioObject, 
                                    new Vector2(canvasWidth / 2 * P2M * ratioObject - canvasWidth / 8 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject - canvasHeight / 20 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case STAIRS_ONLY_RIGHT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject,
                        canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case STAIRS_ONLY_LEFT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{-canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject,
                        canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case STAIRS_UP_RIGHT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case STAIRS_UP_LEFT:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{-canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case RIGHT_ROOF:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{-canvasWidth / 2 * P2M * ratioObject, 0,
                        canvasWidth / 4 * P2M * ratioObject, 0,
                        canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case WHOLE_ROOF:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 2 * P2M * ratioObject, 
                                    canvasHeight / 4 * P2M * ratioObject, 
                                    new Vector2(0, -canvasHeight / 4 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case TRAP_ROOF:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 8 * P2M * ratioObject, 
                                    canvasHeight / 4 * P2M * ratioObject, 
                                    new Vector2(-3*canvasWidth / 8 * P2M * ratioObject, -canvasHeight / 4 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 8 * P2M * ratioObject, 
                                    canvasHeight / 4 * P2M * ratioObject, 
                                    new Vector2(3*canvasWidth / 8 * P2M * ratioObject, -canvasHeight / 4 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case LEFT_ROOF:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.set(new float[]{-canvasWidth / 4 * P2M * ratioObject, 0,
                        canvasWidth / 2 * P2M * ratioObject, 0,
                        canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject,
                        -canvasWidth / 2 * P2M * ratioObject, -canvasHeight / 2 * P2M * ratioObject
                    });
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                case TRAPDOOR:
                    /*ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 8 * P2M * ratioObject,
                                    canvasHeight / 20 * P2M * ratioObject, 
                                    new Vector2(-canvasWidth / 2 * P2M * ratioObject + canvasWidth / 8 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject - canvasHeight / 20 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 8 * P2M * ratioObject,
                                    canvasHeight / 20 * P2M * ratioObject, 
                                    new Vector2(canvasWidth / 2 * P2M * ratioObject - canvasWidth / 8 * P2M * ratioObject, canvasHeight / 2 * P2M * ratioObject - canvasHeight / 20 * P2M * ratioObject), 0); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);*/
                    
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 3 * P2M * ratioObject, 
                                    canvasHeight / 10 * P2M * ratioObject, 
                                    new Vector2(-canvasWidth / 3 * P2M * ratioObject, canvasHeight / 3 * P2M * ratioObject), -1.05f); 
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
                    
                    break;
                default:
                    // Create a polygon shape
                    ground = new PolygonShape();
                    ground.setAsBox(canvasWidth / 2 * P2M * ratioObject,
                                    canvasHeight / 20 * P2M * ratioObject, 
                                    new Vector2(0, canvasHeight / 2 * P2M * ratioObject - canvasHeight / 20 * P2M * ratioObject), 0);  
                    fixtureDef.shape = ground;
                    
                    fix = groundBody.createFixture(fixtureDef); 

                    fix.setUserData(this);
                    this.collisionFixture.add(fix);
            }

            this.physicBody = groundBody;
        }
    }
}
