package inf112.ppbros.model.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformMaker {
    private Random random;
    private final int platformWidth;
    private final int platformHeight;
    private List<int[][]> patterns;

    public PlatformMaker() {
        platformWidth = 5;
        platformHeight = 3;
        patterns = new ArrayList<>();
        random = new Random();
        addDefaultPatterns();
        // patterns.add(new int[][] {
        //     { 1, 1, 1, 1, 1 },
        //     { 1, 0, 0, 0, 1 },
        //     { 1, 1, 1, 1, 1 },
        // });
        // patterns.add(new int[][] {
        //         { 1, 1, 1, 1 },
        //         { 1, 0, 0, 1 },
        //         { 1, 1, 1, 1 },
        //     });
    }

    private void addDefaultPatterns() {
        patterns.add(new int[][] {
            { 0, 0, 0, 0, 0 },
            { 1, 5, 0, 1, 1 },
            { 1, 1, 1, 0, 0 },
        });
        patterns.add(new int[][] {
            { 0, 0, 0, 0, 0 },
            { 7, 0, 0, 1, 1 },
            { 1, 1, 1, 0, 0 },
        });
        patterns.add(new int[][] {
            { 1, 0, 0, 0, 0 },
            { 0, 0, 5, 0, 0 },
            { 0, 1, 1, 1, 0 },
        });
        patterns.add(new int[][] {
            { 0, 0, 0, 7, 0 },
            { 7, 5, 0, 1, 1 },
            { 1, 1, 0, 0, 0 },
        });
        patterns.add(new int[][] {
            { 0, 0, 0, 0, 0 },
            { 1, 1, 7, 0, 0 },
            { 0, 0, 1, 1, 1 },
        });
        patterns.add(new int[][] {
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 1, 1, 1 },
        });
        patterns.add(new int[][] {
            { 0, 0, 0, 0, 0 },
            { 0, 5, 0, 0, 7 },
            { 1, 1, 1, 1, 1 },
        });
    }

    // private void addDefaultPatterns() {
    //     patterns.add(new int[][] {
    //         { 1, 0, 0, 0 },
    //         { 1, 5, 0, 1 },
    //         { 1, 1, 1, 0 },
    //     });
    //     patterns.add(new int[][] {
    //         { 0, 0, 0, 0 },
    //         { 7, 0, 0, 0 },
    //         { 1, 1, 1, 1 },
    //     });
    //     patterns.add(new int[][] {
    //         { 1, 0, 0, 0 },
    //         { 0, 0, 5, 0 },
    //         { 0, 1, 1, 1 },
    //     });
    //     patterns.add(new int[][] {
    //         { 0, 0, 0, 7 },
    //         { 7, 5, 0, 1 },
    //         { 1, 1, 0, 0 },
    //     });
    //     patterns.add(new int[][] {
    //         { 0, 0, 0, 0 },
    //         { 1, 1, 7, 0 },
    //         { 0, 0, 1, 1 },
    //     });
    //     patterns.add(new int[][] {
    //         { 0, 0, 0, 0 },
    //         { 0, 0, 0, 0 },
    //         { 0, 0, 1, 1 },
    //     });
    //     patterns.add(new int[][] {
    //         { 0, 0, 0, 0 },
    //         { 0, 5, 0, 0 },
    //         { 1, 1, 1, 0 },
    //     });
    // }

    public void addPattern(int[][] pattern) {
        if (pattern.length == platformWidth && pattern[0].length == platformHeight) {
            patterns.add(pattern);
        } else {
            throw new IllegalStateException("No patterns available.");
        }
    }

    public void removePattern(int[][] pattern) {
        if (patterns.contains(pattern)) {
            patterns.remove(pattern);
        } else {
            throw new IllegalStateException("This pattern does not exist.");
        }
    }

    public Platform getNext() {
        if (patterns.isEmpty()) {
            throw new IllegalStateException("No patterns available.");
        }
        int index = random.nextInt(patterns.size());
        return new Platform(patterns.get(index));
    }

    public int getPlatformWidth() {
        return platformWidth;
    }

    public int getPlatformHeight() {
        return platformHeight;
    }
}
