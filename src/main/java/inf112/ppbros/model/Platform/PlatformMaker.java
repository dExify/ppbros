package inf112.ppbros.model.Platform;

public class PlatformMaker {

    public PlatformMaker() {
        
    }

    public Platform newPlatform(int randomNumberUpTo4) {
        boolean[][] pattern;
    
        switch (randomNumberUpTo4) {
            case 1:
                pattern = new boolean[][] {
                    { true, false, false },
                    { true,  true,  true },
                    { false, false, true }
                };
                break;
            case 2:
                pattern = new boolean[][] {
                    { false, false, false },
                    { true, true, true },
                    { false, false, false }
                };
                break;
            case 3:
            pattern = new boolean[][] {
                { false, false, false },
                { false, true, true },
                { true, false, false }
            };
            break;
            case 4:
            pattern = new boolean[][] {
                { true, false, false },
                { true, false, false },
                { true, true, false }
            };
            break;
            default:
                pattern = new boolean[][] {
                    { false, false, false },
                    { false, false, false },
                    { false, false, false }
                };
                break;
        }
    
        return new Platform(pattern);
    }

}
