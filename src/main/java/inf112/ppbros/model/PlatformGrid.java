package inf112.ppbros.model;

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

    private Texture paltformTexture;
    private int[][] tileGrid = new int[GRID_WIDTH][GRID_HEIGHT];
    private Rectangle[] hitboxes;
    private PlatformMaker platformMaker;

    public PlatformGrid(PlatformMaker maker) {
        this.platformMaker = maker;
        paltformTexture = new Texture(Gdx.files.internal("GraystoneBrickTile80.png"));
    }

    private Coordinate getRandomPlatformStart() { //Fra venstre nede i 3x3 plattformen
        Random random = new Random();
        int x = random.nextInt(GRID_WIDTH);
        int y = random.nextInt(GRID_HEIGHT);
        Coordinate coordinates = new Coordinate(x, y);
        return coordinates;
    }
}
