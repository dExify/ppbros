package inf112.ppbros.view;

import inf112.ppbros.model.Coordinate;

public class TilePositionInPixels {
    /**
     * Converts the tile position to pixel position and returns the coordinate
     * @param x
     * @param y
     * @param tileSize
     * @return Coordinate
     */
    public static Coordinate getTilePosInPixels(int x, int y, int tileSize) {
        int tileX = tileSize * x;
        int tileY = tileSize * y;
        return new Coordinate(tileX, tileY);
    }
}
