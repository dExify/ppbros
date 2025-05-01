package inf112.ppbros.model;

import java.util.TimerTask;

import com.badlogic.gdx.Gdx;

/**
 * Represents the vertical position of the camera in the game.
 * 
 * <p>This class extends {@link TimerTask} and increments the camera's Y-position each time
 * the {@code run()} method is called (e.g., on a timer schedule). The camera starts at the vertical
 * center of the screen and moves upwards over time.</p>
 */
public class CameraYPos extends TimerTask {
  
  private int cameraPosY;
  
  /**
   * Constructs a new {@code CameraYPos} object with the camera's Y-position 
   * initialized to the vertical center of the screen.
   */
  public CameraYPos() {
    cameraPosY = Gdx.graphics.getHeight()/2;
  }

  @Override
  public void run() {
    cameraPosY++;
  }
  
  /**
   * Returns the current Y-position of the camera.
   * 
   * @return the Y-coordinate of the camera in pixels
   */
  public int getCameraPos() {
    return cameraPosY;
  }
}
