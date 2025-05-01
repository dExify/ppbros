package inf112.ppbros.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

/**
 * AudioController crates and handles all audio, sound and music within the game. 
 */
public class AudioController {
  private Music backgroundMusic;
  private HashMap<String, Sound> soundEffects;
  
  /**
  * Constructs a AudioController-object, responsible for playing background music and sound effects.
  */
  public AudioController() {
    soundEffects = new HashMap<>();
    loadBackgroundMusic("audio/sewer_theme.mp3");
    loadSoundEffect("jump", "audio/jump_initiated.mp3");
    loadSoundEffect("landing", "audio/jump_landing.mp3");
    loadSoundEffect("attack", "audio/sword_attack.mp3");
    loadSoundEffect("bossfight", "audio/boss_battle.mp3");
    
  }
  
  private void loadBackgroundMusic(String filePath) {
    if (backgroundMusic != null) {
      backgroundMusic.dispose();
    }
    backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath));
  }
  
  /**
  * Starts playing the loaded background music.
  * @param looping replays the track after finishing if true
  */
  public void playBackgroundMusic(boolean looping) {
    if (backgroundMusic != null) {
      backgroundMusic.setLooping(looping);
      backgroundMusic.play();
    }
  }
  
  /**
  * Stops the loaded background music.
  */
  public void stopBackgroundMusic() {
    if (backgroundMusic != null) {
      backgroundMusic.stop();
    }
  }
  
  private void loadSoundEffect(String name, String filePath) {
    Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
    soundEffects.put(name, sound);
  }
  
  /**
  * Plays the selected sound effect
  * @param name sound effect to play
  */
  public void playSoundEffect(String name) {
    Sound sound = soundEffects.get(name);
    if (sound != null) {
      sound.play();
    }
  }
  
  /**
   * Disposes of any sounds and musics
   */
  public void dispose() {
    if (backgroundMusic != null) {
      backgroundMusic.dispose();
    }
    for (Sound sound : soundEffects.values()) {
      sound.dispose();
    }
    soundEffects.clear();
  }
}
