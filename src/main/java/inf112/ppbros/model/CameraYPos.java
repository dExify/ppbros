package inf112.ppbros.model;

import java.util.TimerTask;

import com.badlogic.gdx.Gdx;

public class CameraYPos extends TimerTask {

    private volatile int cameraYPos;

    public CameraYPos() {
        cameraYPos = Gdx.graphics.getHeight()/2;
    }
    @Override
    public void run() {
        cameraYPos += 1;
    }

    public int getCameraPos() {
        return cameraYPos;
    }
}
