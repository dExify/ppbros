package inf112.ppbros.controller;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Audio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AudioControllerTest {

  private Music mockMusic;
  private Sound mockSound;
  private AudioController audioController;

  @BeforeEach
  void setUp() {
    mockMusic = mock(Music.class);
    mockSound = mock(Sound.class);

    Audio mockAudio = mock(Audio.class);
    FileHandle mockFile = mock(FileHandle.class);

    // Mock static Gdx fields directly
    Gdx.audio = mockAudio;
    Gdx.files = mock(com.badlogic.gdx.Files.class);

    // Define behavior
    when(Gdx.files.internal(anyString())).thenReturn(mockFile);
    when(mockAudio.newMusic(any())).thenReturn(mockMusic);
    when(mockAudio.newSound(any())).thenReturn(mockSound);

    audioController = new AudioController();
  }

  @Test
  void testPlayBackgroundMusic() {
    audioController.playBackgroundMusic(true);
    verify(mockMusic).setLooping(true);
    verify(mockMusic).play();
  }

  @Test
  void testStopBackgroundMusic() {
    audioController.stopBackgroundMusic();
    verify(mockMusic).stop();
  }

  @Test
  void testPlaySoundEffect() {
    audioController.playSoundEffect("jump");
    verify(mockSound).play();
  }

  @Test
  void testDispose() {
    audioController.dispose();
    verify(mockMusic).dispose();
    verify(mockSound, atLeastOnce()).dispose();
  }
}
