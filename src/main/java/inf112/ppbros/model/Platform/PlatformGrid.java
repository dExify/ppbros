package inf112.ppbros.model.Platform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.view.TilePositionInPixels;

public class PlatformGrid {
    private static final int GRID_WIDTH = TileConfig.GRID_WIDTH;
    private static final int GRID_HEIGHT = TileConfig.GRID_HEIGHT;
    private static final int TILE_SIZE = TileConfig.TILE_SIZE;
    private static final int platformGridHeight = TileConfig.platformGridHeightInPixels;;

    private int[][] tileGrid = new int[GRID_WIDTH][GRID_HEIGHT];
    private ArrayList<Rectangle> hitboxes;
    private PlatformMaker platformMaker;
    private HashSet<Coordinate> occupiedCoordinates;
    private int yPos;

    public PlatformGrid(PlatformMaker maker, int iteration) {
        platformMaker = maker;
        occupiedCoordinates = new HashSet<>();
        hitboxes = new ArrayList<>();
        yPos = iteration * platformGridHeight;
    }

    /**
     * Returns the 2D array containing the platform tile positions in a grid
     * @return int[][]
     */
    public int[][] returnGrid() {
        return tileGrid;
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
    public ArrayList<Rectangle> getHitboxes() {
        return hitboxes;
    }

    /** 
     * Gets a random spawn position and updates the int[][] with the correct tile types
     * @param platformCount int
     */
    public void buildGrid(int platformCount) {
        for (int i = 0; i < platformCount; i++) {
            Coordinate start = getPlatformStart();
            if (start == null) {
                continue;
            }
            Platform platform = platformMaker.getNext();
            int[][] pattern = platform.getPlatform();
            for (int y = 0; y < pattern.length; y++) {
                for (int x = 0; x < pattern[y].length; x++) {
                    if (pattern[y][x] != 0) {
                        insertTile(pattern[y][x], start, x, pattern.length - 1 - y);
                    }
                }   
            }
        }
    }

    /**
     * Sets the passed tile type into the int[][] array at a given coordinate.
     * @param tileType int
     * @param platformStart coordinate
     * @param x int
     * @param y int
     */
    private void insertTile(int tileType, Coordinate platformStart, int x, int y) { //Todo: Update this method so that it is affected by yPos
        int gridX = platformStart.x() + x;
        int gridY = platformStart.y() + y;
        tileGrid[gridX][gridY] = tileType;
        updateOccupiedCoordinates(gridX, gridY);
        Coordinate tilePosInPixels = TilePositionInPixels.getTilePosInPixels(gridX, gridY, TILE_SIZE);
        hitboxes.add(new Rectangle(tilePosInPixels.x(), tilePosInPixels.y() + yPos, TILE_SIZE, TILE_SIZE));
    }

    /**
     * Generates a random start coordinate on a vacant part of the grid and within the bounds of the grid
     * @return Coordinate
     */
    private Coordinate getPlatformStart() {
        Boolean occupiedPosition = true;
        Random random = new Random();
        Coordinate coordinate = null;
        Coordinate startCoordinate = null;
        int platformHeight = platformMaker.getPlatformHeight();
        int platformWidth = platformMaker.getPlatformWidth();
        int randomX;
        int randomY;
        int checks = 0;
        int xMargin = 1;
        int yMargin = 1;
        int expectedVacantPosCount = (platformHeight + yMargin * 2) * (platformWidth + xMargin * 2);
        while (occupiedPosition && checks < 10) {
            int vacantPosCount = 0;
            randomX = 1 + random.nextInt(GRID_WIDTH - platformWidth); //-1
            randomY = 1 + random.nextInt(GRID_HEIGHT - platformHeight); //-1
            startCoordinate = new Coordinate(randomX, randomY);
            outerLoop:
            for (int y = randomY - yMargin; y < randomY + platformHeight + yMargin; y++) {
                for (int x = randomX - xMargin; x < randomX + platformWidth + xMargin; x++) {
                    coordinate = new Coordinate(x, y);
                    if (!occupiedCoordinates.contains(coordinate)) {
                        vacantPosCount += 1;
                    } else {
                        break outerLoop;
                    }
                }
            }
            if (vacantPosCount == expectedVacantPosCount) {
                occupiedPosition = false;
            }
            checks += 1;
        }
        if (checks < 10) {
            return startCoordinate;
        } else {
            return startCoordinate = null;
        }
    }

    /**
     * Updates the occupiedCoordinates hashSet with occupied coordinates
     * @param x
     * @param y
     */
    private void updateOccupiedCoordinates(int x, int y) {
        occupiedCoordinates.add(new Coordinate(x, y));
    }

}
