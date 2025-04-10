package inf112.ppbros.model.entity;

import java.util.Random;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.platform.PlatformGrid;
import inf112.ppbros.model.platform.TileConfig;

public class RandomEnemyMaker {
  
  private Random random;
  private PlatformGrid grid;
  
  public RandomEnemyMaker() {
    this.random = new Random();
    // this.hitboxes = new ArrayList<>();
  }
  
  /**
  * Returns a new enemy, in a valid position on the platform grid
  * @param grid the platform grid to check for valid spawn coordinates
  * @return EnemyModel
  */
  public EnemyModel getNext(PlatformGrid grid) {
    this.grid = grid;
    Coordinate spawnPos = getValidSpawnPos(grid); // get a valid spawn position for the enemy
    // EnemyModel enemy = newRandomEnemy();
    return new EnemyModel(spawnPos, (grid.getYPos()/TileConfig.PLATFORM_GRIDHEIGHT_PIXELS)*TileConfig.GRID_HEIGHT);
    // TODO: GOSH DARN FIX THE Y POSITION CALCUALATION, IT CANT BE CALCULATED AS IS
  }
  
  /**
  * Finds a valid spawn coordinate for the enemy in the platform grid
  * @param grid the platform grid to check for valid spawn coordinates
  */
  private Coordinate getValidSpawnPos(PlatformGrid grid) {
    int[][] platformGrid = grid.returnGrid();
    Coordinate coordinate;
    
    // Loop until a valid spawn coordinate is found
    // The enemy can only spawn on a platform, so we check if the tile at (x, y) is occupied (1) and the tile below it is not occupied (0)
    // This ensures that the enemy spawns on top of a platform and not in the air or below it
    while (true) {
      int x = random.nextInt(TileConfig.GRID_WIDTH); // random x coordinate for enemy
      int y = random.nextInt(TileConfig.GRID_HEIGHT - 1) + 1; // random y coordinate for enemy
      coordinate = new Coordinate(x, y); // random coordinate for enemy
      if (platformGrid[x][y] == 0 && platformGrid[x][y-1] != 0) {
        // TODO: MAKE A CHECK THAT TAKES IN CONSIDERATION OF ENEMY'S SURROUNDING AREA (DON'T LET IT SPAWN WHEN ANOTHER ENEMY IS NEARBY)
        return coordinate; // returns the valid spawn coordnate for the enemy
      } else {
        continue; // if the coordinate is not valid, continue to search for a new one
      }
    }
  }
  
  
  // This is not currently needed because of singular enemytype, but adding so it is easier to include different types at a later date
  // private EnemyModel newRandomEnemy() {
  //   int randInt = random.nextInt(1);
  //   EntityType type;
  //   Coordinate spawnPos = getValidSpawnPos(grid); // get a valid spawn position for the enemy
  
  //   switch (randInt) {
  //     case 0:
  //       type = EntityType.ENEMY;
  //       break;
  
  //     default:
  //       type = EntityType.ENEMY;
  //       break;
  //   }
  
  //   return new EnemyModel(spawnPos);
  // }
  
}
