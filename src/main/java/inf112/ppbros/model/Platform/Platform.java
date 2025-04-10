package inf112.ppbros.model.platform;

public class Platform {
    int[][] pattern;

    public Platform(int[][] pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns a 2D integer array containing the pattern for the platform
     * @return int[][]
     */
    public int[][] getPlatform() {
        return pattern;
    }
}
