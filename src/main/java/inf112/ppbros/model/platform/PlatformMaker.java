package inf112.ppbros.model.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Responsible for managing and generating platform patterns used in the game level.
 * 
 * This class maintains a list of predefined 2D tile patterns that represent platforms.
 * Patterns can be added, removed, and selected randomly to generate varied level layouts.
 */
public class PlatformMaker {
  private Random random;
  private final int platformWidth;
  private final int platformHeight;
  private List<int[][]> patterns;
  
  /**
   * Constructs a new PlatformMaker with default platform dimensions and adds
   * a predefined set of platform patterns to the internal list.
   */
  public PlatformMaker() {
    platformWidth = 5;
    platformHeight = 3;
    patterns = new ArrayList<>();
    random = new Random();
    addDefaultPatterns();
    // patterns.add(new int[][] {
    //     { 1, 1, 1, 1, 1 },
    //     { 1, 0, 0, 0, 1 },
    //     { 1, 1, 1, 1, 1 },
    // });
  }
  
  private void addDefaultPatterns() {
    patterns.add(new int[][] {
      { 0, 0, 0, 0, 0 },
      { 1, 5, 0, 1, 1 },
      { 1, 1, 1, 0, 0 },
    });
    patterns.add(new int[][] {
      { 0, 0, 0, 0, 0 },
      { 7, 0, 0, 1, 1 },
      { 1, 1, 1, 0, 0 },
    });
    patterns.add(new int[][] {
      { 1, 0, 0, 0, 0 },
      { 0, 0, 5, 0, 0 },
      { 0, 1, 1, 1, 0 },
    });
    patterns.add(new int[][] {
      { 0, 0, 0, 7, 0 },
      { 7, 5, 0, 1, 1 },
      { 1, 1, 0, 0, 0 },
    });
    patterns.add(new int[][] {
      { 0, 0, 0, 0, 0 },
      { 1, 1, 7, 0, 0 },
      { 0, 0, 1, 1, 1 },
    });
    patterns.add(new int[][] {
      { 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0 },
      { 0, 0, 1, 1, 1 },
    });
    patterns.add(new int[][] {
      { 0, 0, 0, 0, 0 },
      { 0, 5, 0, 0, 7 },
      { 1, 1, 1, 1, 1 },
    });
  }

  /**
   * Adds a new platform pattern to the list of available patterns.
   * The pattern must match the predefined width and height.
   *
   * @param pattern A 2D integer array representing a platform layout
   * @throws IllegalStateException if the pattern dimensions are invalid
   */
  public void addPattern(int[][] pattern) {
    if (pattern.length == platformHeight && pattern[0].length == platformWidth) {
      patterns.add(pattern);
    } else {
      throw new IllegalStateException("Platform is not of the right length");
    }
  }
  
  /**
   * Removes the passed platform from the list of possible platforms
   * @param pattern A 2D integer array to be removed
   * @throws IllegalStateException if the pattern does not exist in the list
   */
  public void removePattern(int[][] pattern) {
    if (patterns.contains(pattern)) {
      patterns.remove(pattern);
    } else {
      throw new IllegalStateException("This pattern does not exist.");
    }
  }
  
  /**
   * Returns a random platform from the list of possible platforms
   * @return A {@link Platform} object constructed from a randomly selected pattern
   * @throws IllegalStateException if no patterns are available
   */
  public Platform getNext() {
    if (patterns.isEmpty()) {
      throw new IllegalStateException("No patterns available.");
    }
    int index = random.nextInt(patterns.size());
    return new Platform(patterns.get(index));
  }
  
  /**
   * Returns platform width
   * @return the width in tiles
   */
  public int getPlatformWidth() {
    return platformWidth;
  }
  
  /**
   * Returns platform height
   * @return the height in tiles
   */
  public int getPlatformHeight() {
    return platformHeight;
  }
  
  /**
   * Generates a flat base platform of specified width.
   * This is typically used for the bottom-most platform in the level.
   *
   * @param width The number of horizontal tiles in the base platform
   * @return A {@link Platform} consisting of a single row of platform tiles
   */
  public Platform getBasePlatform(int width) {
    int[][] pattern = new int[1][width];
    for (int i = 0; i < width; i++) {
      pattern[0][i] = 1; // or another tile type
    }
    return new Platform(pattern);
  }
  
}
