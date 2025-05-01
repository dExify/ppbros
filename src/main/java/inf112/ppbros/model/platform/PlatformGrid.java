package inf112.ppbros.model.platform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.view.TilePositionInPixels;

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
  
  public PlatformGrid(PlatformMaker maker, int iteration) {
    platformMaker = maker;
    occupiedCoordinates = new HashSet<>();
    hitboxes = new ArrayList<>();
    yPos = iteration * PLATFORM_GRIDHEIGHT_PIXELS;
    this.random = new Random();
  }

  /**
  * Returns the the vertical position above the last platform
  * @return int
  */
  public int getYPos() {
    return yPos;
  }
  
  /**
  * Returns an arraylist of rectangles representing the hitboxes of the platforms
  * @return ArrayList<Rectangle>
  */
  public List<Rectangle> getHitboxes() {
    return hitboxes;
  }
  
/**
 * Gets a random spawn position and updates the int[][] with the correct tile types
 * @param platformCount int
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
  * @param tileType int
  * @param platformStart coordinate
  * @param x int
  * @param y int
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
 * @return Coordinate
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
* Checks if the area around the coordinate is vacant
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
  * @param x
  * @param y
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
      int x = random.nextInt(TileConfig.GRID_WIDTH - 1) + 1; // random x coordinate for enemy
      int y = random.nextInt(TileConfig.GRID_HEIGHT - 1) + 1; // random y coordinate for enemy
      coordinate = new Coordinate(x, y); // random coordinate for enemy
      if (isFree(x, y) && isOnGround(x, y)) {
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
    boolean occupiedTile = (tileGrid[x][y-1] != 0)
                        && (tileGrid[x][y-1] != 5) 
                        && (tileGrid[x][y-1] != 7)
                        && (tileGrid[x-1][y-1] != 0);
    return (occupiedTile);
  }

  public int tileGridHeight() {
    return tileGrid[0].length;
  }

  public int tileGridWidth() {
    return tileGrid.length;
  }

  public int get(int x, int y) {
    return tileGrid[x][y];
  }
}
