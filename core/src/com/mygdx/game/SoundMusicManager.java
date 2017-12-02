/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.SoundManager;

/**
 *
 * @author Deneyr
 */
public class SoundMusicManager implements GameEventListener, Disposable{

    private float volume;
    
    private Music music;
    
    
    // Map Sound;
    private Map<String, List<String>> mapAttackSound; 
    
    private Map<String, List<String>> mapActionSound; 
    
    private Map<String, List<String>> mapDamagesTakenSound; 
    
    // Timer score increase.
    private LocalDateTime lastScoreIncreaseTime;
    private int scoreNumber;
    
    public SoundMusicManager(){
        this.volume = 0.6f;
        
        this.scoreNumber = 0;
        this.lastScoreIncreaseTime = LocalDateTime.now();
        
        // Part attack sounds map fill.
        this.mapAttackSound = new HashMap<String, List<String>>();
        this.putAttackSound("umbrella", "sounds/attacks/swingUmbrella.ogg");
        this.putAttackSound("punch", "sounds/attacks/swingPunch.ogg");
        this.putAttackSound("bigPunch", "sounds/attacks/swingBigPunch.ogg");
        this.putAttackSound("bat", "sounds/attacks/swingBat.ogg");
        this.putAttackSound("shot", "sounds/attacks/shot.ogg");
        this.putAttackSound("reloadGun", "sounds/attacks/reloadGun.ogg");
        this.putAttackSound("hitPunch", "sounds/attacks/hitPunch.ogg");
        this.putAttackSound("hitPunch", "sounds/attacks/hitPunch2.ogg");
        this.putAttackSound("cannon", "sounds/attacks/Cannon_Explosion_1.ogg");
        this.putAttackSound("cannon", "sounds/attacks/Cannon_Explosion_2.ogg");
        
        // Part action sounds map fill.
        this.mapActionSound = new HashMap<String, List<String>>();
        this.putActionSound("umbrellaOpen", "sounds/action/umbrellaOpen.ogg");
        this.putActionSound("umbrellaClose", "sounds/action/umbrellaClose.ogg");
        this.putActionSound("scoreIncrease", "sounds/action/music_note.ogg");
        
        // Part damages taken sounds map fill.
        this.mapDamagesTakenSound = new HashMap<String, List<String>>();
        this.putDamagesTakenSound("boxCrash", "sounds/damagesTaken/crash_box.ogg");
    }
    
    
    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        switch(type){
            case GAMESTART:
                this.launchMusic(details);
                break;
            case ATTACK:
                this.launchSoundAttak(location, details);
                break;
            case ACTION:
                this.launchSoundAction(location, details);
                break;
            case DAMAGE:
                this.launchSoundDamagesTaken(location, details);
                break;
        }
    }

    @Override
    public void onHelpGameEvent(HelpGame helpGame, EventType type, String details, Vector2 location) {
        this.onGameEvent(type, details, location);
    }

    @Override
    public void dispose() {
        if(this.music != null){
            this.music.stop();
            this.music = null;
        }
    }
    
    
    // Music launcher.
    private void launchMusic(String details){
        System.out.println(details);
        if(details.equals("MainMenuGameNode")){
            this.music = MusicManager.getInstance().getMusic("sounds/Help_MainTitle.ogg");
        }else if(details.equals("Lvl1GameNode")){
            this.music = MusicManager.getInstance().getMusic("sounds/first_lvl.ogg");
        }
        
        if(this.music != null){
            this.music.setVolume(this.volume);
            this.music.setLooping(true);
            this.music.play();
        }
    }
    
    // Sound launcher.
    
    private void putAttackSound(String category, String pathSound){
        if(!this.mapAttackSound.containsKey(category)){
            this.mapAttackSound.put(category, new ArrayList());
        }
        
        List<String> listPathSounds = this.mapAttackSound.get(category);
        
        listPathSounds.add(pathSound);
    }
    
    private void putActionSound(String category, String pathSound){
        if(!this.mapActionSound.containsKey(category)){
            this.mapActionSound.put(category, new ArrayList());
        }
        
        List<String> listPathSounds = this.mapActionSound.get(category);
        
        listPathSounds.add(pathSound);
    }
    
    private void putDamagesTakenSound(String category, String pathSound){
        if(!this.mapDamagesTakenSound.containsKey(category)){
            this.mapDamagesTakenSound.put(category, new ArrayList());
        }
        
        List<String> listPathSounds = this.mapDamagesTakenSound.get(category);
        
        listPathSounds.add(pathSound);
    }
    
    private void launchSoundAttak(Vector2 location, String details){
        Sound sound = null;
        
        if(this.mapAttackSound.containsKey(details)){
            List<String> listPathSounds = this.mapAttackSound.get(details);
            
            String pathSound = listPathSounds.get( (int) (Math.random() * listPathSounds.size()) );
            sound = SoundManager.getInstance().getSound(pathSound);
        }
        
        if(sound != null){
            sound.play(this.volume, 1, location.x);    
        }
    }
    
    private void launchSoundAction(Vector2 location, String details){
        Sound sound = null;
        
        if(this.mapActionSound.containsKey(details)){
            List<String> listPathSounds = this.mapActionSound.get(details);
            
            String pathSound = listPathSounds.get( (int) (Math.random() * listPathSounds.size()) );
            sound = SoundManager.getInstance().getSound(pathSound);
        }
        
        if(sound != null){
            if(details.equals("scoreIncrease")){
                Duration duration = Duration.between(this.lastScoreIncreaseTime, LocalDateTime.now());
                System.out.println(duration.getSeconds());
                if(duration.getSeconds() > 5){
                    this.scoreNumber = 0;
                }
                this.lastScoreIncreaseTime = LocalDateTime.now();
                
                this.scoreNumber++;
                
                sound.play(this.volume, 0.5f + this.scoreNumber * 0.1f, location.x); 
            }else{
                sound.play(this.volume, 1, location.x);    
            }
        }
    }
    
    private void launchSoundDamagesTaken(Vector2 location, String details){
        Sound sound = null;
        
        if(this.mapDamagesTakenSound.containsKey(details)){
            List<String> listPathSounds = this.mapDamagesTakenSound.get(details);
            
            String pathSound = listPathSounds.get( (int) (Math.random() * listPathSounds.size()) );
            sound = SoundManager.getInstance().getSound(pathSound);
        }
        
        if(sound != null){
            sound.play(this.volume, 1, location.x);    
        }
    }
}
