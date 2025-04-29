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
    Coordinate spawnPos = grid.getValidSpawnPos(); // get a valid spawn position for the enemy
    EnemyModel enemy = new EnemyModel(spawnPos, (grid.getYPos()/TileConfig.PLATFORM_GRIDHEIGHT_PIXELS)*TileConfig.GRID_HEIGHT);
    enemy.loadAnimations();
    return enemy;
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
