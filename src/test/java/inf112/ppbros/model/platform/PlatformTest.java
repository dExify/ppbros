package inf112.ppbros.model.platform;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlatformTest {

    @Test
    void testGetPlatform() {
        int[][] pattern = {
            {0, 1, 0},
            {1, 1, 1}
        };

        Platform platform = new Platform(pattern);

        int[][] returnedPattern = platform.getPlatform();
        assertEquals(2, returnedPattern.length, "Pattern shoud have 2 rows");
        assertEquals(3, returnedPattern[0].length, "Row should have 3 columns");

        assertEquals(0, returnedPattern[0][0]);
        assertEquals(1, returnedPattern[0][1]);
        assertEquals(0, returnedPattern[0][2]);
        assertEquals(1, returnedPattern[1][0]);
        assertEquals(1, returnedPattern[1][1]);
        assertEquals(1, returnedPattern[1][2]);
    }
}
