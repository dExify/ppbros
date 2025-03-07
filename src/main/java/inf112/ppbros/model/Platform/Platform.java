package inf112.ppbros.model.Platform;

public class Platform {
    boolean[][] pattern;

    public Platform(boolean[][] doubleArray) {
        pattern = doubleArray;
    }

    public boolean[][] getPlatform() {
        return pattern;
    }
}
