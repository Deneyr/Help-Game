/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import ressourcesmanagers.MusicManager;

/**
 *
 * @author Deneyr
 */
public class SoundMusicManager implements GameEventListener, Disposable{

    private float volume;
    
    private Music music;
    
    public SoundMusicManager(){
        this.volume = 1f;
    }
    
    
    @Override
    public void onGameEvent(EventType type, String details, Vector2 location) {
        switch(type){
            case GAMESTART:
                this.launchMusic(details);
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
    private void launchMusic(String musicPath){
        this.music = MusicManager.getInstance().getMusic("sounds/Help_MainTitle.ogg");
        
        if(this.music != null){
            this.music.setVolume(this.volume);
            this.music.setLooping(true);
            this.music.play();
        }
    }
}
