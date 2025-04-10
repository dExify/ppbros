package inf112.ppbros.model.Platform;

public class PlatformGridMaker {
    private PlatformGrid platformGridObject;
    private PlatformMaker platformMaker;
    private int iteration;

    public PlatformGridMaker() {
        iteration = 0;
        platformMaker = new PlatformMaker();
    }

    /**
     * Returns a platformGrid object with a yPos value above the last platformGrid
     * @return
     */
    public PlatformGrid getNextPlatformGrid() {
        platformGridObject = new PlatformGrid(platformMaker, iteration);
        platformGridObject.buildGrid(25);
        iteration += 1;
        return platformGridObject;
    }
}
