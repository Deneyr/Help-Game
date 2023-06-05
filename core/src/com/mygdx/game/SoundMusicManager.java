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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ressourcesmanagers.MusicManager;
import ressourcesmanagers.SoundManager;

/**
 *
 * @author Deneyr
 */
public class SoundMusicManager implements GameEventListener, Disposable{

    private float volume;
    
    private Music music;
    private Music mainMusic;
    
    private MusicChangeManager musicChangeManager;
    
    // Map Sound;
    private Map<String, List<String>> mapAttackSound; 
    
    private Map<String, List<String>> mapActionSound; 
    
    private Map<String, List<String>> mapDamagesTakenSound; 
    
    private Map<String, List<String>> mapLoopSound; 
    private Map<String, Long> mapIdLoopSound;
    private Set<String> setLoopSoundPathUsed;
    
    private Map<String, String> mapEndCinematicMusic; 
    private Map<String, String> mapCinematicMusic;  
    private Map<String, String> mapLvlStartMusic;
    private Map<String, String> mapLvlEndMusic;
    
    // Timer score increase.
    private LocalDateTime lastScoreIncreaseTime;
    private int scoreNumber;
    
    public SoundMusicManager(){
        this.volume = 0.3f;
        
        this.music = null;
        this.mainMusic = null;
        
        this.scoreNumber = 0;
        this.lastScoreIncreaseTime = LocalDateTime.now();
        
        this.musicChangeManager = new MusicChangeManager();
        
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
        this.putAttackSound("barbed", "sounds/attacks/Metal_Wire.ogg");
        
        // Part action sounds map fill.
        this.mapActionSound = new HashMap<String, List<String>>();
        this.putActionSound("umbrellaOpen", "sounds/action/umbrellaOpen.ogg");
        this.putActionSound("umbrellaClose", "sounds/action/umbrellaClose.ogg");
        this.putActionSound("scoreIncrease", "sounds/action/music_note.ogg");
        this.putActionSound("ventiloButton", "sounds/action/Button_Click.ogg");
        this.putActionSound("metalHit", "sounds/action/metalHit1.ogg");
        this.putActionSound("metalHit", "sounds/action/metalHit2.ogg");
        this.putActionSound("onGround", "sounds/cinematic/onGround.ogg");
        this.putActionSound("onFeet", "sounds/cinematic/backOnFeet.ogg");
        this.putActionSound("strongChestOpen", "sounds/action/StrongChestOpen.ogg");
        this.putActionSound("phoneBoxOpen", "sounds/action/PhoneBoxOpen.ogg");
        this.putActionSound("0", "sounds/action/checkPointTaken.ogg");
        this.putActionSound("smoke", "sounds/action/smoke.ogg");
        
        // Part damages taken sounds map fill.
        this.mapDamagesTakenSound = new HashMap<String, List<String>>();
        this.putDamagesTakenSound("boxCrash", "sounds/damagesTaken/crash_box.ogg");
        this.putDamagesTakenSound("metalHitDamage", "sounds/damagesTaken/metalHitDamage.ogg");
        
        // Part loop sounds map fill.
        this.mapLoopSound = new HashMap<String, List<String>>();
        this.putLoopSound("ventiloWind", "sounds/environment/Ventilo_Wind_Loop.ogg");
        this.putLoopSound("conveyorBelt", "sounds/environment/conveyorBelt_Loop.ogg");
        this.mapIdLoopSound = new HashMap<String, Long>();
        this.setLoopSoundPathUsed = new HashSet<String>();
        
        // Part musics.
        this.mapEndCinematicMusic = new HashMap<String, String>();
        this.mapEndCinematicMusic.put("1:L'orgueil\nd'une grand-mère", "sounds/Help_Niveau1.mp3");
        
        this.mapCinematicMusic = new HashMap<String, String>();
        this.mapCinematicMusic.put("encounterTemeri", "sounds/temeri.ogg");
        
        this.mapLvlStartMusic = new HashMap<String, String>();
        this.mapLvlStartMusic.put("MainMenuGameNode", "sounds/Help_MainTitle.ogg");
        
        this.mapLvlEndMusic = new HashMap<String, String>();
        this.mapLvlEndMusic.put("defeat", "sounds/GameOver.ogg");
    }
    
    
    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {

        if(type == EventType.LVLSTART
                || type == EventType.GAMESTART
                || type == EventType.GAMEOVER
                || type == EventType.CINEMATIC
                || type == EventType.ENDCINEMATIC){
            this.launchMusic(type, details);
        }
        
        if(location != null 
                && Math.abs(location.x) <= 1
                && Math.abs(location.y) <= 1){
            switch(type){
                case ATTACK:
                    this.launchSoundAttak(location, details);
                    break;
                case ACTION:
                case CHECKPOINT:
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
                case EDITORSTOPPED:
                    this.clearAllLoopSound();
                    break;
            }
        }
    }

    public void step(float deltaTime){
        this.musicChangeManager.updateMusicVolume(deltaTime);
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
        
        if(this.mainMusic != null){
            this.mainMusic.stop();
            this.mainMusic = null;
        }
        
        this.musicChangeManager.dispose();
        
        this.clearAllLoopSound();
    }
    
    
    // Music launcher.
    private void launchMusic(EventType type, String details){ 
        Music newMusic = null;
        Music newMainMusic = null;
        
        switch (type) {
            case GAMESTART:
                if(this.mapEndCinematicMusic.containsKey(details)){
                    newMusic = MusicManager.getInstance().getMusic(this.mapEndCinematicMusic.get(details));
                    newMainMusic = newMusic;
                }   
                break;
            case LVLSTART:
                if(this.mapLvlStartMusic.containsKey(details)){
                    newMusic = MusicManager.getInstance().getMusic(this.mapLvlStartMusic.get(details));
                }   
                break;
            case GAMEOVER:
                if(this.mapLvlEndMusic.containsKey(details)){
                    newMusic = MusicManager.getInstance().getMusic(this.mapLvlEndMusic.get(details));
                }   
                break;
            case CINEMATIC:
                if(this.mapCinematicMusic.containsKey(details)){
                    newMusic = MusicManager.getInstance().getMusic(this.mapCinematicMusic.get(details));
                }   
                break;
            case ENDCINEMATIC:
                newMusic = this.mainMusic;

                if(this.mapEndCinematicMusic.containsKey(details)){
                    newMusic = MusicManager.getInstance().getMusic(this.mapEndCinematicMusic.get(details));
                    newMainMusic = newMusic;
                }   
                break;

        }
        
        if(newMainMusic != null && this.mainMusic != newMainMusic){
            this.musicChangeManager.setMainMusic(newMainMusic);
            this.mainMusic = newMainMusic;
        }
        
        if(newMusic != null && this.music != newMusic){
            /*if(this.music != null && this.mainMusic == this.music){
                this.music.pause();
            }else if(this.music != null){
                this.music.stop();
            }*/
            
            this.music = newMusic;
            this.musicChangeManager.changeMusic(this.music, this.volume);
            
            /*
            if(this.music != null){
                this.music.setVolume(this.volume);
                this.music.setLooping(true);
                this.music.play();
            }*/
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
                
                sound.play(this.volume, 0.5f + (this.scoreNumber%8) * 0.25f, location.x); 
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
        Sound sound;
        
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
                
                this.setLoopSoundPathUsed.add(pathSound);
                
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
    
    public void clearAllLoopSound(){
        for(String pathSound : this.setLoopSoundPathUsed){
            
            Sound sound = SoundManager.getInstance().getSound(pathSound);
                
            sound.stop();
        }
        
        this.setLoopSoundPathUsed.clear();
        this.mapIdLoopSound.clear();
    }
    
    private class MusicChangeManager implements Disposable{
        private static final float TOTALTIME = 3f;
        private float currentTime;
        private float startVolumeFirst;
        
        private Music mainMusic;
        
        private Music previousMusic;
        private Music currentMusic;
        private float volumeToReach;
        
        public MusicChangeManager(){
            this.previousMusic = null;
            this.currentMusic = null;
            
            this.mainMusic = null;
        }
        
        public void changeMusic(Music newMusic, float volumeToReach){
            
            if(this.previousMusic != null){
                if(this.previousMusic != this.mainMusic){
                    this.previousMusic.stop();
                }else{
                    this.previousMusic.pause();
                }
            }
            
            this.previousMusic = this.currentMusic;
            this.currentMusic = newMusic;
            this.volumeToReach = volumeToReach;
            
            if(this.previousMusic != null){
                this.startVolumeFirst = this.previousMusic.getVolume();
            }else{
                this.startVolumeFirst = 0;
            }
            
            if(this.currentMusic != null){
                this.currentMusic.setVolume(0);
                this.currentMusic.setLooping(true);
                this.currentMusic.play();
            }
            
            this.currentTime = 0;
        }
        
        public void updateMusicVolume(float deltaTime){
            
            if(this.previousMusic != null || this.currentMusic != null){
                if(this.previousMusic != null){
                    float newVolume = this.getVolumeLinear(this.startVolumeFirst, 0, this.currentTime);
                    if(newVolume > 0){
                        this.previousMusic.setVolume(newVolume);
                    }else{
                        if(this.previousMusic != this.mainMusic){
                            this.previousMusic.stop();
                        }else{
                            this.previousMusic.pause();
                        }
                        this.previousMusic = null;
                    }
                }

                if(this.currentMusic != null){
                    float newVolume = this.getVolumeLinear(0, this.volumeToReach, this.currentTime);
                    if(newVolume < this.volumeToReach){
                        this.currentMusic.setVolume(newVolume);
                    }else{
                        this.currentMusic.setVolume(this.volumeToReach);
                    }
                }

                this.currentTime += deltaTime;
            }
        }

        public void setMainMusic(Music mainMusic){
            if(this.mainMusic != null){
                this.mainMusic.stop();
            }
            
            this.mainMusic = mainMusic;
        }
        
        @Override
        public void dispose() {
            if(this.previousMusic != null){
                this.previousMusic.stop();
                this.previousMusic = null;
            }
            
            if(this.currentMusic != null){
                this.currentMusic.stop();
                this.currentMusic = null;
            }
        } 
        
        private float getVolumeLinear(float startVolume, float endVolume, float currentTime){
            if(currentTime > TOTALTIME){
                return endVolume;
            }
            
            return startVolume * (1 - currentTime / TOTALTIME) + endVolume * currentTime / TOTALTIME;
        }
    }
}
