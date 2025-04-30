package inf112.ppbros.model.platform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.view.TilePositionInPixels;

/**
 * Represents a grid of platforms composed of tiles.
 * <p>
 * Responsible for generating platform layouts, tracking platform hitboxes for collisions,
 * and managing tile occupancy to avoid overlapping platforms or invalid placements.
 * Also supports spawning logic for entities such as enemies by identifying valid spawn coordinates.
 */
public class PlatformGrid {
  private static final int GRID_WIDTH = TileConfig.GRID_WIDTH;
  private static final int GRID_HEIGHT = TileConfig.GRID_HEIGHT;
  private static final int TILE_SIZE = TileConfig.TILE_SIZE;
  private static final int PLATFORM_GRIDHEIGHT_PIXELS = TileConfig.PLATFORM_GRIDHEIGHT_PIXELS;
  
  private int[][] tileGrid = new int[GRID_WIDTH][GRID_HEIGHT];
  private ArrayList<Rectangle> hitboxes;
  private PlatformMaker platformMaker;
  private HashSet<Coordinate> occupiedCoordinates;
  private int yPos;
  private Random random;
  
  /**
  * Constructs a new platform grid based on a platform maker and iteration index.
  *
  * @param maker the platform maker used to generate platform layouts
  * @param iteration the iteration number to determine the vertical offset of this grid
  */
  public PlatformGrid(PlatformMaker maker, int iteration) {
    platformMaker = maker;
    occupiedCoordinates = new HashSet<>();
    hitboxes = new ArrayList<>();
    yPos = iteration * PLATFORM_GRIDHEIGHT_PIXELS;
    this.random = new Random();
  }

  /**
  * Returns the the vertical position above the last platform
  * @return the vertical offset (y-position) of the grid in pixels
  */
  public int getYPos() {
    return yPos;
  }
  
  /**
  * Returns an arraylist of rectangles representing the hitboxes of the platforms
  * @return a list of rectangles representing platform hitboxes
  */
  public List<Rectangle> getHitboxes() {
    return hitboxes;
  }
  
/**
 * Gets a random spawn position and updates the int[][] with the correct tile types
 * @param platformCount the number of additional platforms to generate
 */
public void buildGrid(int platformCount) {
  buildBasePlatform(); // Build base platform at the bottom

  for (int i = 0; i < platformCount; i++) {
      Coordinate start = getPlatformStart();
      if (start != null) {
          buildPlatform(start);
      }
  }
}

/**
* Builds the base platform at the bottom (y = 0)
*/
private void buildBasePlatform() {
  Platform basePlatform;
  if (yPos == 0) {
      basePlatform = platformMaker.getBasePlatform(GRID_WIDTH);
  } else {
      basePlatform = platformMaker.getNext();
  }

  Coordinate baseStart = new Coordinate(0, 0); // left-bottom corner
  int[][] basePattern = basePlatform.getPlatform();
  insertPlatformTiles(basePattern, baseStart);
}

/**
* Builds a platform at a given starting coordinate
* @param start Coordinate
*/
private void buildPlatform(Coordinate start) {
  Platform platform = platformMaker.getNext();
  int[][] pattern = platform.getPlatform();
  insertPlatformTiles(pattern, start);
}

/**
* Inserts the tiles for a given platform pattern and start coordinate
* @param pattern int[][] pattern of the platform
* @param start Coordinate starting position of the platform
*/
private void insertPlatformTiles(int[][] pattern, Coordinate start) {
  for (int y = 0; y < pattern.length; y++) { //riktig rekkefølge
      for (int x = 0; x < pattern[y].length; x++) {
          if (pattern[y][x] != 0) {
              insertTile(pattern[y][x], start, x, pattern.length - 1 - y);
          }
      }
  }
}

  
/**
  * Sets the passed tile type into the int[][] array at a given coordinate. 
  * Updates the hitboxes only with platform tiles, not decorative assets.
  * @param tileType the type of tile to insert
  * @param platformStart the top-left coordinate where the platform starts
  * @param x the relative x position within the platform pattern
  * @param y the relative y position within the platform pattern
  */
  private void insertTile(int tileType, Coordinate platformStart, int x, int y) { //Todo: Update this method so that it is affected by yPos
    int gridX = platformStart.x() + x;
    int gridY = platformStart.y() + y;
    tileGrid[gridX][gridY] = tileType;
    if (tileType == 1) {
      updateOccupiedCoordinates(gridX, gridY);
      Coordinate tilePosInPixels = TilePositionInPixels.getTilePosInPixels(gridX, gridY, TILE_SIZE);
      hitboxes.add(new Rectangle(tilePosInPixels.x(), tilePosInPixels.y() + yPos, TILE_SIZE, TILE_SIZE));
    }
  }
  
/**
 * Generates a random start coordinate on a vacant part of the grid and within the bounds of the grid
 * @return a valid start coordinate, or null if no suitable area was found
 */
private Coordinate getPlatformStart() {
  int platformHeight = platformMaker.getPlatformHeight();
  int platformWidth = platformMaker.getPlatformWidth();
  int xMargin = 1;
  int yMargin = 1;
  int expectedVacantPosCount = (platformHeight + yMargin * 2) * (platformWidth + xMargin * 2);

  for (int checks = 0; checks < 10; checks++) {
      int randomX = 1 + random.nextInt(GRID_WIDTH - platformWidth);
      int randomY = 1 + random.nextInt(GRID_HEIGHT - platformHeight);
      Coordinate startCoordinate = new Coordinate(randomX, randomY);

      if (isAreaVacant(startCoordinate, platformWidth, platformHeight, xMargin, yMargin, expectedVacantPosCount)) {
          return startCoordinate;
      }
  }

  return null;
}

/**
 * Checks whether the area around the given start coordinate is completely vacant.
 * 
 * @param start         the top-left coordinate of the area
 * @param width         the width of the platform
 * @param height        the height of the platform
 * @param xMargin       margin added on the x-axis
 * @param yMargin       margin added on the y-axis
 * @param expectedCount the number of expected vacant tiles
 * @return  {@code true} if the area is vacant; {@code false} otherwise
 */
private boolean isAreaVacant(Coordinate start, int width, int height, int xMargin, int yMargin, int expectedCount) {
  int vacantCount = 0;

  for (int y = start.y() - yMargin; y < start.y() + height + yMargin; y++) {
      for (int x = start.x() - xMargin; x < start.x() + width + xMargin; x++) {
          if (!occupiedCoordinates.contains(new Coordinate(x, y))) {
              vacantCount++;
          } else {
              return false;
          }
      }
  }

  return vacantCount == expectedCount;
}

  /**
   * Updates the occupiedCoordinates hashSet with occupied coordinates
   * @param x  the x-position of the tile
   * @param y  the y-position of the tile
   */
  private void updateOccupiedCoordinates(int x, int y) {
    occupiedCoordinates.add(new Coordinate(x, y));
  }
  
  /**
   * Finds a valid spawn coordinate for the enemy in the platform grid
   * @param grid the platform grid to check for valid spawn coordinates
   */
  public Coordinate getValidSpawnPos() {
    Coordinate coordinate;
    
    // Loop until a valid spawn coordinate is found
    // The enemy can only spawn on a platform, so we check if the tile at (x, y) is occupied (1) and the tile below it is not occupied (0)
    // This ensures that the enemy spawns on top of a platform and not in the air or below it
    while (true) {
      int x = random.nextInt(TileConfig.GRID_WIDTH); // random x coordinate for enemy
      int y = random.nextInt(TileConfig.GRID_HEIGHT - 1) + 1; // random y coordinate for enemy
      coordinate = new Coordinate(x, y); // random coordinate for enemy
      if (isFree(x, y) && isOnGround(x, y)) {
        // TODO: MAKE A CHECK THAT TAKES IN CONSIDERATION OF ENEMY'S SURROUNDING AREA (DON'T LET IT SPAWN WHEN ANOTHER ENEMY IS NEARBY)
        return coordinate; // returns the valid spawn coordnate for the enemy
      } else {
        continue; // if the coordinate is not valid, continue to search for a new one
      }
    }
  }

  private boolean isFree(int x, int y) {
    return tileGrid[x][y] == 0;
  }

  private boolean isOnGround(int x, int y) {
    return tileGrid[x][y-1] != 0;
  }

  /**
   * Returns the height (in tiles) of the tile grid.
   *
   * @return the number of tile rows
   */
  public int tileGridHeight() {
    return tileGrid[0].length;
  }

  /**
   * Returns the width (in tiles) of the tile grid.
   *
   * @return the number of tile columns
   */
  public int tileGridWidth() {
    return tileGrid.length;
  }

  /**
   * Returns the tile type at the specified grid coordinate.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @return the tile type at (x, y)
   */
  public int get(int x, int y) {
    return tileGrid[x][y];
  }
}
