package inf112.ppbros.model.Platform;

import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.view.TilePositionInPixels;

public class PlatformGrid {
    private static final int GRID_WIDTH = TileConfig.GRID_WIDTH;
    private static final int GRID_HEIGHT = TileConfig.GRID_HEIGHT;
    private static final int TILE_SIZE = TileConfig.TILE_SIZE;

    private int[][] tileGrid = new int[GRID_WIDTH][GRID_HEIGHT];
    private Rectangle[] hitboxes;
    private PlatformMaker platformMaker;
    private HashSet<Coordinate> occupiedCoordinates;

    public PlatformGrid(PlatformMaker maker) {
        this.platformMaker = maker;
        this.occupiedCoordinates = new HashSet<>();
    }

    /**
     * Returns the 2D array containing the platform tile positions in a grid
     * @return int[][]
     */
    public int[][] returnGrid() {
        return tileGrid;
    }

    /**
     * Returns a simple array of rectangles representing the hitboxes of the platforms
     * @return Rectangle[]
     */
    public Rectangle[] getHitboxes() {
        return hitboxes;
    }

    /**
     * Builds a platform grid and updates the tileGrid 2D array with the correct values. 
     * Also updates the hitboxes simple array to include the new platforms
     */
    public void buildGrid() {
        Coordinate platformStart = getPlatformStart();
        Platform platform = platformMaker.getNext();
        int[][] pattern = platform.getPlatform();
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                if (pattern[y][x] != 0) {
                    insertTile(pattern[y][x], platformStart, x, y);
                }
            }
        }
        System.out.println("Occupied coordinates: " + "\n" + occupiedCoordinates + "\n"); //Debugging
    }

    /**
     * Updates the tileGrid 2D array with the correct tile type
     * @param tileType
     * @param platformStart
     * @param x
     * @param y
     */
    private void insertTile(int tileType, Coordinate platformStart, int x, int y) {
        int gridX = platformStart.x() + x;
        int gridY = platformStart.y() + y;
        tileGrid[gridX][gridY] = tileType;
        updateOccupiedCoordinates(gridX, gridY);
        Coordinate tilePosInPixels = TilePositionInPixels.getTilePosInPixels(gridX, gridY, TILE_SIZE);
        new Rectangle(tilePosInPixels.x(), tilePosInPixels.y(), TILE_SIZE, TILE_SIZE);
    }

    /**
     * Generates a random coordinate on a vacant part of the grid and within the bounds of the grid
     * @return Coordinate
     */
    private Coordinate getPlatformStart() {
        Boolean occupiedPosition = true;
        Random random = new Random();
        Coordinate coordinate = null;
        Coordinate startCoordinate = null;
        int randomX;
        int randomY;
        while (occupiedPosition) {
            int vacantPosCount = 0;
            randomX = random.nextInt(GRID_WIDTH - 4);
            randomY = random.nextInt(GRID_HEIGHT - 3);
            startCoordinate = new Coordinate(randomX, randomY);
            for (int y = randomY; y < randomY + 3; y++) {
                for (int x = randomX; x < randomX + 4; x++) {
                    coordinate = new Coordinate(x, y);
                    if (!occupiedCoordinates.contains(coordinate)) {
                        vacantPosCount += 1;
                    }
                }
            }
            if (vacantPosCount == 12) {
                occupiedPosition = false;
            }
        }
        return startCoordinate;
    }

    /**
     * Updates the occupiedCoordinates hashSet
     * @param x
     * @param y
     */
    private void updateOccupiedCoordinates(int x, int y) {
        occupiedCoordinates.add(new Coordinate(x, y));
    }

    public void printArray() { // debugging
        System.out.println("Platform grid upside down :");
        for (int y = 0; y < tileGrid[0].length; y++) {
            for (int x = 0; x < tileGrid.length; x++) {
                System.out.print(tileGrid[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // public void printArray() { // debugging
    //     System.out.println("Platform grid upside down :");
    //     for (int y = tileGrid[0].length; y < 0; y--) {
    //         for (int x = 0; x < tileGrid.length; x++) {
    //             System.out.print(tileGrid[x][y] + " ");
    //         }
    //         System.out.println();
    //     }
    //     System.out.println();
    // }

}
