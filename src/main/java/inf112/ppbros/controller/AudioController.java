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
    }

    public void loadBackgroundMusic(String filePath) {
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

    public void loadSoundEffect(String name, String filePath) {
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
