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

    private int[][] tileGrid = new int[GRID_WIDTH][GRID_HEIGHT];
    private ArrayList<Rectangle> hitboxes;
    private PlatformMaker platformMaker;
    private HashSet<Coordinate> occupiedCoordinates;

    public PlatformGrid(PlatformMaker maker) {
        this.platformMaker = maker;
        this.occupiedCoordinates = new HashSet<>();
        this.hitboxes = new ArrayList<>();
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
    public ArrayList<Rectangle> getHitboxes() {
        return hitboxes;
    }

    /**
     * Builds a platform grid and updates the tileGrid 2D array with the correct values. 
     * Also updates the hitboxes simple array to include the new platforms
     */
    public void buildGrid(int platformCount) {
        Coordinate platformStart;
        Platform platform;
        int[][] pattern;
        for (int i = 0; i <= platformCount; i++) {
            try { //Bad code style? Potentially too much code in try
                platformStart = getPlatformStart();
                platform = platformMaker.getNext();
                pattern = platform.getPlatform();
                for (int y = 0; y < pattern.length; y++) {
                    for (int x = 0; x < pattern[y].length; x++) {
                        if (pattern[y][x] != 0) {
                            insertTile(pattern[y][x], platformStart, x, y);
                        }
                    }
                }
                // addNoSpawnZone(platformStart);
            } catch (Exception e) {
                System.out.println("The " + i + "th platform couldnt be placed on grid"); //debugging
            }
        }
    }

    /**
     * Updates the grid above a platform with values so that other platforms dont spawn too close to each other.
     */
    private void addNoSpawnZone(Coordinate platformStart) {
        int startX = platformStart.x();
        int startY = platformStart.y() + 3;
        for (int x = startX; (x < startX + 4) && x < GRID_WIDTH; x++) {
            for (int y = startY; (y < startY + 2) && y < GRID_HEIGHT; y++) {
                // insertTile(-1, new Coordinate(startX, startY), x, y);
                int gridX = platformStart.x() + x;
                int gridY = platformStart.y() + y;
                tileGrid[gridX][gridY] = -1;
                updateOccupiedCoordinates(gridX, gridY);
            }
        }
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
        hitboxes.add(new Rectangle(tilePosInPixels.x(), tilePosInPixels.y(), TILE_SIZE, TILE_SIZE));
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
        int checks = 0;
        while (occupiedPosition && checks < 10) {
            int vacantPosCount = 0;
            randomX = random.nextInt(GRID_WIDTH - 3);
            randomY = random.nextInt(GRID_HEIGHT - 2);
            startCoordinate = new Coordinate(randomX, randomY);
            for (int y = randomY; y < randomY + 3; y++) {
                for (int x = randomX; x < randomX + 4; x++) {
                    coordinate = new Coordinate(x, y);
                    if (!occupiedCoordinates.contains(coordinate)) {
                        vacantPosCount += 1;
                    } //Break earlier than vacantPosCount == 12 if occupiedposition is detected?
                }
            }
            if (vacantPosCount == 12) {
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

}
