package inf112.ppbros.model.Platform;

import java.util.Random;

public class PlatformMaker {

    // public PlatformMaker() {
        
    // }

    public Platform getNext() {
        Random random = new Random();
        int randomInt = random.nextInt(4);
        Platform platform = Platform.newPlatform(randomInt);
        return platform;
    }

}
