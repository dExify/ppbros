package inf112.ppbros.model.Platform;

public class Platform {
    int[][] pattern;

    private Platform(int[][] pattern) {
        this.pattern = pattern;
    }

    public static Platform newPlatform(int randInt) {
        int[][] pattern;
    
        switch (randInt) {
            case 1:
                pattern = new int[][] {
                    { 1, 0, 0 },
                    { 1,  1,  1 },
                    { 0, 0, 1 }
                };
                break;
            case 2:
                pattern = new int[][] {
                    { 0, 0, 0 },
                    { 1, 1, 1 },
                    { 0, 0, 0 }
                };
                break;
            case 3:
            pattern = new int[][] {
                { 0, 0, 1 },
                { 0, 1, 1 },
                { 1, 0, 0 }
            };
            break;
            case 4:
            pattern = new int[][] {
                { 1, 0, 0 },
                { 1, 0, 0 },
                { 1, 1, 1 }
            };
            break;
            default:
                pattern = new int[][] {
                    { 0, 0, 0 },
                    { 0, 0, 0 },
                    { 0, 0, 0 }
                };
                break;
        }
    
        return new Platform(pattern);
    }

    public int[][] getPlatform() {
        return pattern;
    }
}
