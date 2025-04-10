package inf112.ppbros.model.platform;

import com.badlogic.gdx.Gdx;

public final class TileConfig {
    public static final int GRID_WIDTH = 24;
    public static final int GRID_HEIGHT = 14;
    public static final int TILE_SIZE = Gdx.graphics.getWidth()/GRID_WIDTH;
    public static final int PLATFORM_GRIDHEIGHT_PIXELS = TILE_SIZE * GRID_HEIGHT;

    private TileConfig() {
        
    }

}
