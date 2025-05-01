package inf112.ppbros.model.platform;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Rectangle;

import inf112.mockutil.GdxTestMock;
import inf112.ppbros.model.Coordinate;

import java.util.List;

class PlatformGridTest {

    private PlatformMaker platformMakerMock;
    private PlatformGrid platformGrid;

    @BeforeAll
    static void setupGdx() {
        GdxTestMock.init();
    }

    @BeforeEach
    void setup() {
        platformMakerMock = mock(PlatformMaker.class);
        platformGrid = new PlatformGrid(platformMakerMock, 0); // iteration 0
    }

    @Test
    void testGetYPos() {
        PlatformGrid grid = new PlatformGrid(platformMakerMock, 2);
        int expectedYPos = 2 * TileConfig.PLATFORM_GRIDHEIGHT_PIXELS;
        assertEquals(expectedYPos, grid.getYPos());
    }

    @Test
    void testGetHitboxes() {
        List<Rectangle> hitboxes = platformGrid.getHitboxes();
        assertNotNull(hitboxes);
        assertTrue(hitboxes.isEmpty(), "Hitboxes should be empty initially.");
    }

    @Test
    void testBuildGrid() {
        // Mock a simple platform
        int[][] platformPattern = {
            {0, 1, 0},
            {1, 1, 1}
        };
        Platform mockPlatform = mock(Platform.class);
        when(mockPlatform.getPlatform()).thenReturn(platformPattern);

        when(platformMakerMock.getBasePlatform(anyInt())).thenReturn(mockPlatform);
        when(platformMakerMock.getNext()).thenReturn(mockPlatform);
        when(platformMakerMock.getPlatformWidth()).thenReturn(3);
        when(platformMakerMock.getPlatformHeight()).thenReturn(2);

        platformGrid.buildGrid(1);
        boolean hasTile = false;
        for (int x = 0; x < TileConfig.GRID_WIDTH; x++) {
            for (int y = 0; y < TileConfig.GRID_HEIGHT; y++) {
                if (platformGrid.get(x, y) == 1) {
                    hasTile = true;
                    break;
                }
            }
        }
        assertTrue(hasTile, "There should be platform tiles after running buildGrid()");
    }

    @Test
    void testGetValidEnemySpawnPos() {
        PlatformMaker mockMaker = mock(PlatformMaker.class);
        when(mockMaker.getBasePlatform(anyInt())).thenReturn(new Platform(new int[][]{
            {1, 1, 1, 1, 1}
        }));
        when(mockMaker.getNext()).thenReturn(new Platform(new int[][]{
            {1, 1, 1}
        }));
        when(mockMaker.getPlatformHeight()).thenReturn(1);
        when(mockMaker.getPlatformWidth()).thenReturn(3);

        PlatformGrid platformGridBuilt = new PlatformGrid(mockMaker, 0);
        platformGridBuilt.buildGrid(1);

        Coordinate spawnPos = platformGridBuilt.getValidEnemySpawnPos();

        int x = spawnPos.x();
        int y = spawnPos.y();

        assertEquals(0, platformGridBuilt.get(x, y), "Spawn position should be free (0)");
        assertNotEquals(0,platformGridBuilt.get(x, y - 1), "Tile below spawn position should be occupied (non-zero)");
    }

    @Test
    void testTileGridHeight() {
        assertEquals(TileConfig.GRID_HEIGHT, platformGrid.tileGridHeight());
    }

    @Test
    void testTileGridWidth() {
        assertEquals(TileConfig.GRID_WIDTH, platformGrid.tileGridWidth());
    }
}
