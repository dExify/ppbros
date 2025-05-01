package inf112.ppbros.view;

import inf112.ppbros.model.Coordinate;

/**
 * Utility class for converting tile-based coordinates to pixel-based coordinates.
 */
public class TilePositionInPixels {
  
  private TilePositionInPixels() {}
  
  /**
  * Converts the tile position to pixel position and returns the coordinate.
  * @param x        The x-coordinate in tile units.
  * @param y        The y-coordinate in tile units.
  * @param tileSize The size of one tile in pixels.
  * @return A {@link Coordinate} representing the pixel position.
  */
  public static Coordinate getTilePosInPixels(int x, int y, int tileSize) {
    int tileX = tileSize * x;
    int tileY = tileSize * y;
    return new Coordinate(tileX, tileY);
  }
}
