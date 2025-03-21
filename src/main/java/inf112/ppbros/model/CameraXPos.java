package inf112.ppbros.model;

import java.util.TimerTask;

public class CameraXPos extends TimerTask {

    private volatile int cameraXPos;

    public CameraXPos() {
        cameraXPos = 0;
    }
    @Override
    public void run() {
        cameraXPos += 1;
        System.out.println(cameraXPos);
    }

    public int getCameraPos() {
        return cameraXPos;
    }
}
