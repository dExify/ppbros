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
}
