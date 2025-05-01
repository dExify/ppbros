package inf112.ppbros.model.platform;

/**
 * Responsible for generating sequential {@link PlatformGrid} objects,
 * each positioned vertically above the previous one.
 * <p>
 * Uses a {@link PlatformMaker} to build individual platform patterns within the grid.
 */
public class PlatformGridMaker {
  
  private PlatformMaker platformMaker;
  private int iteration;
  
  /**
   * Constructs a new PlatformGridMaker with an initial vertical iteration value of 0
   * and a new instance of {@link PlatformMaker}.
   */
  public PlatformGridMaker() {
    iteration = 0;
    platformMaker = new PlatformMaker();
  }
  
  /**
   * Returns a platformGrid object with a yPos value above the last platformGrid
   * @return the next sequential {@link PlatformGrid}
   */
  public PlatformGrid getNextPlatformGrid() {
    PlatformGrid platformGridObject = new PlatformGrid(platformMaker, iteration);
    platformGridObject.buildGrid(25);
    iteration += 1;
    return platformGridObject;
  }
  
}
