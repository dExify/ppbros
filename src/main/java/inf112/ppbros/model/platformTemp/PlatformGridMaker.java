package inf112.ppbros.model.platformTemp;

public class PlatformGridMaker {
  
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
    PlatformGrid platformGridObject = new PlatformGrid(platformMaker, iteration);
    platformGridObject.buildGrid(25);
    iteration += 1;
    return platformGridObject;
  }
  
}
