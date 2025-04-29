package inf112.ppbros.model.entity;

import inf112.mockutil.GdxTestMock;
import inf112.ppbros.model.platform.PlatformGrid;
import inf112.ppbros.model.platform.TileConfig;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RandomEnemyMakerTest {

    private RandomEnemyMaker randomEnemyMaker;
    private PlatformGrid platformGridMock;

    @BeforeAll
    static void setupGdx() {
        GdxTestMock.init();
    }

    @BeforeEach
    void setup() {
        platformGridMock = mock(PlatformGrid.class);
        randomEnemyMaker = new RandomEnemyMaker();
    }

    @Test
    void testGetNextReturnsValid() {
        // Set up a fake platform grid
        int[][] fakeGrid = new int[TileConfig.GRID_WIDTH][TileConfig.GRID_HEIGHT];

        // Make a valid spawn at (5, 5): empty above, platform below
        fakeGrid[5][5] = 0;
        fakeGrid[5][4] = 1;

        when(platformGridMock.returnGrid()).thenReturn(fakeGrid);
        when(platformGridMock.getYPos()).thenReturn(0);

        EnemyModel enemy = randomEnemyMaker.getNext(platformGridMock);

        assertNotNull(enemy, "Enemy should not be null.");
        assertTrue(enemy.getX() >= 0 && enemy.getX() < TileConfig.GRID_WIDTH, "Enemy X should be inside grid.");
        assertTrue(enemy.getY() >= 0, "Enemy Y should be positive.");
    }
}
