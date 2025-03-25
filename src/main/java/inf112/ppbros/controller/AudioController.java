package inf112.ppbros.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class AudioController {
    private Music backgroundMusic;
    private HashMap<String, Sound> soundEffects;

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

    public void playBackgroundMusic(boolean looping) {
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(looping);
            backgroundMusic.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    private void loadSoundEffect(String name, String filePath) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
        soundEffects.put(name, sound);
    }

    public void playSoundEffect(String name) {
        Sound sound = soundEffects.get(name);
        if (sound != null) {
            sound.play();
        }
    }

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
