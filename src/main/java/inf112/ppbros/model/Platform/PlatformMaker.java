package inf112.ppbros.model.Platform;

import java.util.Random;

public class PlatformMaker { //Abstrakt klasse?
    Random random;

    public PlatformMaker() {
        random = new Random();
    }

    /**
     * Returns a new platform
     * @return Platform
     */
    public Platform getNext() {
        int randomInt = random.nextInt(5);
        // System.out.println(randomInt); // Debugging
        Platform platform = newPlatform(randomInt);
        return platform;
    }

    /**
     * Returns a new Platform object based on a random integer
     * @param randInt
     * @return Platform
     */
    private Platform newPlatform(int randInt) {
        int[][] pattern;
    
        // switch (randInt) {
        //     case 1:
        //         pattern = new int[][] {
        //             { 0, 0, 1, 1},
        //             { 0, 0, 0, 0},
        //             { 1, 1, 0, 0},
        //         };
        //         break;
        //     case 2:
        //         pattern = new int[][] {
        //             { 0, 0, 0, 0},
        //             { 1, 1, 0, 0},
        //             { 0, 0, 1, 1},
        //         };
        //         break;
        //     case 3:
        //         pattern = new int[][] {
        //             { 0, 0, 0, 0},
        //             { 1, 1, 1, 1},
        //             { 0, 0, 0, 0},
        //     };
        //     break;
        //     case 4:
        //         pattern = new int[][] {
        //             { 0, 1, 1, 1},
        //             { 0, 0, 0, 0},
        //             { 1, 1, 0, 1},
        //     };
        //     break;
        //     default:
        //         pattern = new int[][] {
        //             { 1, 0, 0, 0},
        //             { 1, 1, 0, 0},
        //             { 0, 1, 1, 1},
        //         };
        //         break;
        // } 
            pattern = new int[][] {
                { 1, 0, 0, 0},
                { 1, 0, 0, 0},
                { 1, 1, 1, 1},
            };
        return new Platform(pattern);
    }

}
