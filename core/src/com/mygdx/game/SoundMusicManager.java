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
import java.time.LocalDateTime;
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
    
    private Map<String, List<String>> mapLoopSound; 
    private Map<String, Long> mapIdLoopSound;
    
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
        this.putAttackSound("hitBounce", "sounds/attacks/bounce_1.ogg");
        this.putAttackSound("hitBounce", "sounds/attacks/bounce_2.ogg");
        this.putAttackSound("hitBounce", "sounds/attacks/bounce_3.ogg");
        this.putAttackSound("hitBounce", "sounds/attacks/bounce_4.ogg");
        this.putAttackSound("hitProjectile", "sounds/attacks/bounce_5.ogg");
        
        // Part action sounds map fill.
        this.mapActionSound = new HashMap<String, List<String>>();
        this.putActionSound("umbrellaOpen", "sounds/action/umbrellaOpen.ogg");
        this.putActionSound("umbrellaClose", "sounds/action/umbrellaClose.ogg");
        this.putActionSound("scoreIncrease", "sounds/action/music_note.ogg");
        this.putActionSound("ventiloButton", "sounds/action/Button_Click.ogg");
        
        // Part damages taken sounds map fill.
        this.mapDamagesTakenSound = new HashMap<String, List<String>>();
        this.putDamagesTakenSound("boxCrash", "sounds/damagesTaken/crash_box.ogg");
        
        // Part loop sounds map fill.
        this.mapLoopSound = new HashMap<String, List<String>>();
        this.putLoopSound("ventiloWind", "sounds/environment/Ventilo_Wind_Loop.ogg");
        this.mapIdLoopSound = new HashMap<String, Long>();
    }
    
    
    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        if(Math.abs(location.x) <= 1
                && Math.abs(location.y) <= 1){
            switch(type){
                case GAMESTART:
                    //this.launchMusic(details);
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
                case LOOP:
                    this.launchSoundLoop(location, details);
                    break;
                case LOOP_STOP:
                    this.clearSoundLoop(details);
                    break;
            }
        }else{
            switch(type){
                case LOOP:
                    this.clearSoundLoop(details);
                    break;
            }
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
        
        this.mapIdLoopSound.clear();
    }
    
    
    // Music launcher.
    private void launchMusic(String details){
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
    
    private void putLoopSound(String category, String pathSound){
        if(!this.mapLoopSound.containsKey(category)){
            this.mapLoopSound.put(category, new ArrayList());
        }
        
        List<String> listPathSounds = this.mapLoopSound.get(category);
        
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
    
    private void launchSoundLoop(Vector2 location, String details){
        Sound sound = null;
        
        String[] keys = details.split(":");
        String keySound = keys[0];
        String idObject = keys[1];
        
        float scaleVolumeOut = Math.abs(1f - Math.max(0, Math.abs(location.x) - 0.7f)); 
        scaleVolumeOut *= Math.abs(1f - Math.max(0, Math.abs(location.y) - 0.7f)); 
        
        if(!this.mapIdLoopSound.containsKey(idObject)){
        
            if(this.mapLoopSound.containsKey(keySound)){
                
                List<String> listPathSounds = this.mapLoopSound.get(keySound);
                
                String pathSound = listPathSounds.get( (int) (Math.random() * listPathSounds.size()) );
                sound = SoundManager.getInstance().getSound(pathSound);
                
                long idSoundInstance = sound.play(this.volume * scaleVolumeOut, 1, location.x); 
                
                sound.setLooping(idSoundInstance, true);
                
                this.mapIdLoopSound.put(idObject, idSoundInstance);
            }
        }else{
            if(this.mapLoopSound.containsKey(keySound)){
                
                List<String> listPathSounds = this.mapLoopSound.get(keySound);

                String pathSound = listPathSounds.get( (int) (Math.random() * listPathSounds.size()) );
                sound = SoundManager.getInstance().getSound(pathSound);
                
                sound.setPan(this.mapIdLoopSound.get(idObject), location.x, this.volume * scaleVolumeOut);
            }
        }
    }
    
    public void clearSoundLoop(String details){
        Sound sound = null;
        
        String[] keys = details.split(":");
        String keySound = keys[0];
        String idObject = keys[1];
        
        if(this.mapIdLoopSound.containsKey(idObject)){
            if(this.mapLoopSound.containsKey(keySound)){
                
                List<String> listPathSounds = this.mapLoopSound.get(keySound);

                String pathSound = listPathSounds.get( (int) (Math.random() * listPathSounds.size()) );
                sound = SoundManager.getInstance().getSound(pathSound);
                
                sound.stop(this.mapIdLoopSound.get(idObject));
                
                this.mapIdLoopSound.remove(idObject);
            }
        }
    }
}
