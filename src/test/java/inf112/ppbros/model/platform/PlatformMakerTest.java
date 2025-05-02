package inf112.ppbros.model.platform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlatformMakerTest {

    private PlatformMaker platformMaker;

    @BeforeEach
    void setUp() {
        platformMaker = new PlatformMaker();
    }

    @Test
    void testAddPatternWithValidDimensionsAddsPattern() {
        int[][] validPattern = new int[][] {
            {1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1}
        };

        assertDoesNotThrow(() -> platformMaker.addPattern(validPattern));
    }

    @Test
    void testAddPatternWithInvalidDimensionsThrowsException() {
        int[][] invalidPattern = new int[][] {
            {1, 1, 1},
            {0, 0, 0}
        };

        Exception exception = assertThrows(IllegalStateException.class, () -> platformMaker.addPattern(invalidPattern));
        assertEquals("Platform is not of the right length", exception.getMessage());
    }

    @Test
    void testRemoveExistingPatternRemovesSuccessfully() {
        int[][] pattern = new int[][] {
            {1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1}
        };

        platformMaker.addPattern(pattern);
        assertDoesNotThrow(() -> platformMaker.removePattern(pattern));
    }

    @Test
    void testRemoveNonExistingPatternThrowsException() {
        int[][] notAddedPattern = new int[][] {
            {1, 0, 0, 0, 1},
            {0, 0, 5, 0, 0},
            {1, 1, 1, 1, 1}
        };

        Exception exception = assertThrows(IllegalStateException.class, () -> platformMaker.removePattern(notAddedPattern));
        assertEquals("This pattern does not exist.", exception.getMessage());
    }

    @Test
    void testGetNextReturnsValidPlatform() {
        Platform platform = platformMaker.getNext();
        assertNotNull(platform);
        assertNotNull(platform.getPlatform());
        assertEquals(3, platform.getPlatform().length); // height
        assertEquals(5, platform.getPlatform()[0].length); // width
    }

    @Test
    void testGetNextThrowsWhenNoPatternsAvailable() {
        PlatformMaker emptyMaker = new PlatformMaker();

        // Remove all default patterns
        for (int i = 0; i < 10; i++) {
            try {
                emptyMaker.removePattern(emptyMaker.getNext().getPlatform());
            } catch (IllegalStateException ignored) {
                break;
            }
        }

        Exception exception = assertThrows(IllegalStateException.class, emptyMaker::getNext);
        assertEquals("No patterns available.", exception.getMessage());
    }

    @Test
    void testGetBasePlatformGeneratesCorrectPattern() {
        int width = 5;

        Platform basePlatform = platformMaker.getBasePlatform(width);

        assertNotNull(basePlatform, "Platform should not be null");

        int[][] pattern = basePlatform.getPlatform();
        assertEquals(1, pattern.length, "Base platform should have 1 row");
        assertEquals(width, pattern[0].length, "Base platform should have correct number of columns");

        for (int i = 0; i < width; i++) {
            assertEquals(1, pattern[0][i], "Each tile should be set to 1");
        }
    }
}

