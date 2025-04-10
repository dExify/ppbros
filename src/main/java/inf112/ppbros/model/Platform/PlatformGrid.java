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
    private Random random;
    
    public PlatformGrid(PlatformMaker maker, int iteration) {
        this.platformMaker = maker;
        this.occupiedCoordinates = new HashSet<>();
        this.hitboxes = new ArrayList<>();
        this.yPos = iteration * platformGridHeight;
        this.random = new Random();
    }
    
    /**
    * Returns the 2D array containing the platform tile positions in a grid
    * @return int[][]
    */
    public int[][] returnGrid() {
        return tileGrid;
    }
    
    public int getYPos() {
        return yPos;
    }
    
    /**
    * Returns a simple array of rectangles representing the hitboxes of the platforms
    * @return Rectangle[]
    */
    public ArrayList<Rectangle> getHitboxes() {
        return hitboxes;
    }
    
    public void buildGrid(int platformCount) {
        // Add base platform at the bottom (e.g., y = 0)
        Platform basePlatform;
        if (yPos == 0) {
            basePlatform = platformMaker.getBasePlatform(GRID_WIDTH);
        } else {
            basePlatform = platformMaker.getNext();
        }
        Coordinate baseStart = new Coordinate(0, 0); // left-bottom corner
        
        int[][] basePattern = basePlatform.getPlatform();
        for (int y = 0; y < basePattern.length; y++) {
            for (int x = 0; x < basePattern[y].length; x++) {
                if (basePattern[y][x] != 0) {
                    insertTile(basePattern[y][x], baseStart, x, y);
                }
            }
        }
        
        for (int i = 0; i < platformCount; i++) {
            Coordinate start = getPlatformStart();
            if (start == null) {
                // System.out.println("Fant ingen plass for plattform nr. " + i); Debugging
                continue;
            }
            Platform platform = platformMaker.getNext();
            int[][] pattern = platform.getPlatform();
            for (int y = 0; y < pattern.length; y++) {
                for (int x = 0; x < pattern[y].length; x++) {
                    if (pattern[y][x] != 0) {
                        insertTile(pattern[y][x], start, x, y);
                    }
                }   
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
    private void insertTile(int tileType, Coordinate platformStart, int x, int y) { //Todo: Update this method so that it is affected by yPos
        int gridX = platformStart.x() + x;
        int gridY = platformStart.y() + y;
        tileGrid[gridX][gridY] = tileType;
        updateOccupiedCoordinates(gridX, gridY);
        Coordinate tilePosInPixels = TilePositionInPixels.getTilePosInPixels(gridX, gridY, TILE_SIZE);
        hitboxes.add(new Rectangle(tilePosInPixels.x(), tilePosInPixels.y() + yPos, TILE_SIZE, TILE_SIZE));
    }
    
    /**
    * Generates a random coordinate on a vacant part of the grid and within the bounds of the grid
    * @return Coordinate
    */
    private Coordinate getPlatformStart() {
        boolean occupiedPosition = true;
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
            for (int y = randomY; y < randomY + 5; y++) {
                for (int x = randomX - 1; x < randomX + 5; x++) {
                    coordinate = new Coordinate(x, y);
                    if (!occupiedCoordinates.contains(coordinate)) {
                        vacantPosCount += 1;
                    } else {
                        break;
                    }
                }
            }
            if (vacantPosCount == 30) {
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
    
    /**
    * Prints a text representation of the array
    */
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
