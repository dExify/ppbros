package inf112.ppbros.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Platform.Platform;
import inf112.ppbros.model.Platform.PlatformMaker;
import inf112.ppbros.view.TilePositionInPixels;

public class PlatformGrid {
    private static final int GRID_WIDTH = 24; //24x80 = 1920
    private static final int GRID_HEIGHT = 12; //12x80 = 960 (nesten opp til 1080)
    private static final int TILE_SIZE = 80; //80x80 piksler

    private int[][] tileGrid = new int[GRID_WIDTH][GRID_HEIGHT]; //I piksler
    private Rectangle[] hitboxes; //Mest effektivt? O(1)?
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
     * Builds a platform grid and updates the tileGrid 2D array with the values. 
     * Also updates the hitboxes simple array to include the new platforms
     */
    public void buildGrid() {
        Coordinate platformStart = getPlatformStart();
        Platform platform = platformMaker.getNext();
        int[][] pattern = platform.getPlatform();
        for (int x = 0; x < pattern.length; x++) {
            for (int y = 0; y < pattern[x].length; y++) {
                if (pattern[y][x] == 1) {
                    int gridX = platformStart.x() + x;
                    int gridY = platformStart.y() + y;
                    tileGrid[gridX][gridY] = 1;
                    updateOccupiedCoordinates(gridX, gridY);
                    Coordinate tilePosInPixels = TilePositionInPixels.getTilePosInPixels(gridX, gridY, TILE_SIZE);
                    new Rectangle(tilePosInPixels.x(), tilePosInPixels.y(), TILE_SIZE, TILE_SIZE);
                }
            }
        }
        System.out.println("Occupied coordinates: " + "\n" + occupiedCoordinates + "\n"); //Debugging
    }

    /**
     * Generates a random coordinate on a vacant part of the grid and within the bounds of the grid
     * @return Coordinate
     */
    private Coordinate getPlatformStart() {
        Boolean occupiedPosition = true;
        Random random = new Random();
        Coordinate coordinate = null;
        int x;
        int y;
        while (occupiedPosition) { //TODO: Fix this method/make a helper method. Right now it only checks the lower left position, so if the rest of the 3x3 is occupied it will still draw the platform.
            x = random.nextInt(GRID_WIDTH - 3);
            y = random.nextInt(GRID_HEIGHT - 3);
            coordinate = new Coordinate(x, y);
            if (!occupiedCoordinates.contains(coordinate)) {
                occupiedPosition = false;
            }
        }
        return coordinate;
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

    // public void buildGrid() {
    //     Coordinate platformStart = getPlatformStart();
    //     Platform platform = platformMaker.getNext();
    //     int[][] pattern = platform.getPlatform();
    //     for (int y = 0; y < pattern.length; y++) {
    //         for (int x = 0; x < pattern[y].length; x++) {
    //             if (y == 0 && x == 0) {
    //                 continue;
    //             } else {
    //                 Coordinate platformGrid = getTilePosInPixels(x, y, TILE_SIZE);
    //                 tileGrid[platformStart.x() + platformGrid.x()][platformStart.y() + platformGrid.y()] = 1;
    //             }
    //         }
    //     }
    // }
}
