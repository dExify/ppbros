package inf112.ppbros.model;

import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Platform.Platform;
import inf112.ppbros.model.Platform.PlatformMaker;

public class PlatformGrid {
    private static final int GRID_WIDTH = 24; //24x80 = 1920
    private static final int GRID_HEIGHT = 12; //12x80 = 960 (nesten opp til 1080)
    private static final int TILE_SIZE = 80; //80x80 piksler
    private static final int PLATFORM_SIZE = 240; //3x3 grid av tiles (240x240 piksler)

    private Texture platformTexture;
    private int[][] tileGrid = new int[GRID_WIDTH][GRID_HEIGHT]; //I piksler
    private Rectangle[] hitboxes;
    private PlatformMaker platformMaker;
    //private HashSet occupiedCoordinates;

    public PlatformGrid(PlatformMaker maker) {
        this.platformMaker = maker;
        platformTexture = new Texture(Gdx.files.internal("GraystoneBrickTile80.png"));
    }

    public void buildGrid() {
        Coordinate platformStart = getPlatformStart();
        Platform platform = platformMaker.getNext();
        int[][] pattern = platform.getPlatform();
        for (int x = 0; x < pattern.length; x++) {
            for (int y = 0; y < pattern[x].length; y++) {
                if (x == 0 && y == 0) {
                    continue;
                } else {
                    Coordinate platformGrid = getTilePosInPixels(x, y, TILE_SIZE);
                    tileGrid[platformStart.x() + platformGrid.x()][platformStart.y() + platformGrid.y()] = 1;
                }
            }
        }
    }

    public void printArray() {
        for (int x = 0; x < tileGrid.length; x++) {
            for (int y = 0; y < tileGrid[x].length; y++) {
                System.out.print(tileGrid[x][y] + " ");
            }
            System.out.println();
        }
    }

    private Coordinate getTilePosInPixels(int x, int y, int tileSize) {
        int tileX = tileSize * x;
        int tileY = tileSize * y;
        return new Coordinate(x, y);
    }

    private void hitboxMaker() {

    }

    private Coordinate getPlatformStart() { //Fra venstre nede i 3x3 plattformen
        Random random = new Random();
        int x = random.nextInt(GRID_WIDTH - 3);
        int y = random.nextInt(GRID_HEIGHT - 3);
        Coordinate coordinates = new Coordinate(x, y);
        return coordinates;
    }
}
