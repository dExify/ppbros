package inf112.ppbros.model.Platform;

import com.badlogic.gdx.Gdx;

public final class TileConfig {
    public static final int GRID_WIDTH = 24;
    public static final int GRID_HEIGHT = 14;
    public static final int TILE_SIZE = Gdx.graphics.getWidth()/GRID_WIDTH;

    private TileConfig() {
        
    }

}
