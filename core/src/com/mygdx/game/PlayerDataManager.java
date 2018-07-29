/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Deneyr
 */
public class PlayerDataManager {
    
    private static PlayerDataManager instance = null;
    
    private PlayerDataManager(){
        
    }
    
    public static PlayerDataManager getInstance(){
        if(PlayerDataManager.instance == null){
            PlayerDataManager.instance = new PlayerDataManager();
        }
        return PlayerDataManager.instance;
    }
    
    public boolean serializePlayerData(String filePath, PlayerData playerData){
        ObjectOutputStream objectOutputStream = null;

        try {
            synchronized(this){
                final FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(playerData);

                objectOutputStream.flush();
            }
            
            return true;
            
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.flush();
                    objectOutputStream.close();
                }
            } catch (final IOException ex) {

            }
        }
        
        return false;
    }
    
    public PlayerData deserializePlayerData(String filePath){
        ObjectInputStream objectInputStream = null;

        try {
            synchronized(this){
                final FileInputStream fileInputStream = new FileInputStream(filePath);
                objectInputStream = new ObjectInputStream(fileInputStream);

                return (PlayerData) objectInputStream.readObject();
            }
            
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        } catch (ClassNotFoundException ex) {
            
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (final IOException ex) {

            }
        }
        
        return null;
    }
}
