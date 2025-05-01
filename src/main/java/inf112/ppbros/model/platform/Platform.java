package inf112.ppbros.model.platform;

/**
 * Represents a platform pattern consisting of a 2D grid of tile types.
 * <p>
 * Each integer in the array corresponds to a specific type of tile,
 * where 0 typically represents empty space and non-zero values represent platform or decorative tiles.
 */
public class Platform {
  int[][] pattern;
  
  /**
   * Constructs a new Platform with the given tile pattern.
   *
   * @param pattern a 2D array representing the platform's tile layout
   */
  public Platform(int[][] pattern) {
    this.pattern = pattern;
  }
  
  /**
  * Returns a 2D integer array containing the pattern for the platform
  * @return a 2D integer array representing the platform pattern
  */
  public int[][] getPlatform() {
    return pattern;
  }
}
