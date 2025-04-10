package inf112.ppbros.model;

import java.util.TimerTask;

import com.badlogic.gdx.Gdx;

public class CameraYPos extends TimerTask {
  
  private int cameraPosY;
  
  public CameraYPos() {
    cameraPosY = Gdx.graphics.getHeight()/2;
  }
  @Override
  public void run() {
    cameraPosY++;
  }
  
  public int getCameraPos() {
    return cameraPosY;
  }
}
