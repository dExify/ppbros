package inf112.ppbros.model.platform;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Rectangle;

import inf112.mockutil.GdxTestMock;

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
}
