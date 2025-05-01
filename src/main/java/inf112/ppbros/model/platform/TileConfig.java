package inf112.ppbros.model.platform;

import com.badlogic.gdx.Gdx;

/**
 * Provides global configuration constants related to tile grid dimensions and sizing.
 * 
 * This utility class defines the size of the tile grid used in the platform system, including
 * the tile size and the pixel height of a single platform grid.
 * 
 * This class is non-instantiable and contains only static members.
 */
public final class TileConfig {
  /** The number of horizontal tiles in the grid. */
  public static final int GRID_WIDTH = 24;

  /** The number of vertical tiles in the grid. */
  public static final int GRID_HEIGHT = 14;

  /** The size (width and height in pixels) of a single tile, calculated based on the screen width. */
  public static final int TILE_SIZE = Gdx.graphics.getWidth()/GRID_WIDTH;

    /** 
   * The total pixel height of the platform grid, based on tile size and grid height.
   */
  public static final int PLATFORM_GRIDHEIGHT_PIXELS = TILE_SIZE * GRID_HEIGHT;
  
  private TileConfig() {}
}
