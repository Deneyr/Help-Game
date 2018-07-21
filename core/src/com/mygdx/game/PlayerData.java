/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.io.Serializable;

/**
 *
 * @author Deneyr
 */
public class PlayerData implements Serializable{
    private  static  final  long serialVersionUID =  1350092881346723535L;

    private int currentMoney;

    private String idCurrentLevel;

    private transient int currentCheckpointInex;

    PlayerData(){
        this.currentMoney = 0;

        this.idCurrentLevel = null;

        this.currentCheckpointInex = -1;
    }

    /**
     * @return the currentMoney
     */
    public int getCurrentMoney() {
        return currentMoney;
    }

    /**
     * @param currentMoney the currentMoney to set
     */
    public void setCurrentMoney(int currentMoney) {
        this.currentMoney = currentMoney;
    }

    /**
     * @return the currentLevel
     */
    public String getCurrentLevel() {
        return idCurrentLevel;
    }

    /**
     * @param currentLevel the currentLevel to set
     */
    public void setCurrentLevel(String currentLevel) {
        this.idCurrentLevel = currentLevel;

        this.currentCheckpointInex = -1;
    }

    /**
     * @return the currentCheckpointInex
     */
    public int getCurrentCheckpointInex() {
        return currentCheckpointInex;
    }

    /**
     * @param currentCheckpointInex the currentCheckpointInex to set
     */
    public void setCurrentCheckpointInex(int currentCheckpointInex) {
        this.currentCheckpointInex = currentCheckpointInex;
    }
}