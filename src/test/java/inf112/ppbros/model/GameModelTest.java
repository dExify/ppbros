package inf112.ppbros.model;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Rectangle;

import inf112.mockutil.GdxTestMock;
import inf112.ppbros.model.entity.EnemyModel;
import inf112.ppbros.model.entity.PlayerModel;
import inf112.ppbros.testutils.TestApplicationListener; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
  private GameModel gameModel;
  
  @BeforeEach
  void setUp() {
    if (!isGdxInitialized()) {
      // Start LibGDX in headless mode with the dummy listener
      HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
      new HeadlessApplication(new TestApplicationListener(), config);
    }
    GdxTestMock.init();

    gameModel = new GameModel(false, true);
    gameModel.makePlayer(0, 150);
  }
  
  private boolean isGdxInitialized() {
    return Gdx.graphics != null;
  }
  
  @Test
  void testPlayerInitialPosition() {
    PlayerModel player = gameModel.getPlayer();
    
    assertNotNull(player, "Player should not be null.");
    assertEquals(0, player.getX(), "Player X position should be 0.");
    assertEquals(150, player.getY(), "Player Y position should be 150.");
  }
  
  @Test
  void testAddToScore() {
    gameModel.addToScore();
    assertEquals(1, gameModel.getScore(), "Score should be 1 after increment.");
  }
  
  @Test
  void testMovePlayer() {
    PlayerModel player = gameModel.getPlayer();
    float initialX = player.getX();
    float initialY = player.getY();
    
    gameModel.movePlayer(10, 0);
    
    assertEquals(initialX + 10 * player.getSpeed(), player.getX(), "Player's X position should have increased by 10.");
    assertEquals(initialY, player.getY(), "Player's Y position should remain the same.");
  }
  
  @Test
  void testMovePlayerWithoutCollision() {
    // No platforms or enemies nearby
    gameModel.getPlatformHitboxes().clear();
    gameModel.getEnemyHitboxes().clear();
    
    PlayerModel player = gameModel.getPlayer();
    float initialX = player.getX();
    float initialY = player.getY();
    
    gameModel.movePlayer(1, 0); // Move right
    
    float expectedX = initialX + (1 * player.getSpeed());
    float expectedY = initialY;
    
    assertEquals(expectedX, player.getX(), 0.001f, "Player X should move by speed units.");
    assertEquals(expectedY, player.getY(), 0.001f, "Player Y should stay the same.");
  }
  
  @Test
  void testMovePlayerBlockedByPlatform() {
    // Add a platform directly in front of player
    gameModel.getPlatformHitboxes().clear();
    PlayerModel player = gameModel.getPlayer();
    float initialX = player.getX();
    float initialY = player.getY();
    player.setSize(50, 100); // Set player size so overlapping can happen
    
    // Simulate a blocking platform that is 1 pixel closer than player speed
    Rectangle blockingPlatform = new Rectangle(initialX + player.getSpeed()-1, initialY, player.getWidth(), player.getHeight());
    gameModel.getPlatformHitboxes().add(blockingPlatform);
    
    gameModel.movePlayer(1, 0); // Try to move right into platform
    
    assertEquals(initialX, player.getX(), 0.001f, "Player should stay in same X when colliding with platform.");
    assertEquals(initialY, player.getY(), 0.001f, "Player should stay in same Y when colliding with platform.");
  }
  
  @Test
  void testMovePlayerHitsEnemy() {
    PlayerModel player = gameModel.getPlayer();
    float initialX = player.getX();
    float initialY = player.getY();
    player.setSize(50, 100);
    
    // Create enemy model and set size/position
    EnemyModel enemy = new EnemyModel(new Coordinate((int) (initialX + player.getSpeed()), (int) initialY), 0);
    enemy.setSize(50, 100); 
    enemy.setX(initialX + player.getSpeed());
    enemy.setY(initialY);
    enemy.updateCollisionBox(enemy.getX(), enemy.getY());
    
    // Clear enemies and insert our test enemy
    gameModel.getEnemies().clear();
    gameModel.getEnemies().add(enemy);
    
    int initialHealth = player.getHealth();
    
    gameModel.movePlayer(1, 0); // Move right into enemy
    
    assertEquals(initialHealth - 10, player.getHealth(),
    "Player should lose 10 health after hitting enemy");
  }
  
  
  @Test
  void testAttackableEnemyFound() {
    // Create a mock enemy
    Coordinate start = new Coordinate(100, 150);
    EnemyModel enemy = new EnemyModel(start, 0);
    gameModel.getEnemies().add(enemy);
    
    // Player at (0, 150), move closer
    PlayerModel player = gameModel.getPlayer();
    player.setX(100);
    
    // Should be able to attack
    assertTrue(player.canAttack(enemy), "The player should be able to attack the enemy within the attack range.");
    
    // Move enemy further away
    enemy.setX(player.getX() + player.getAttackRange() + 1);
    enemy.getCollisionBox().x = enemy.getX(); // collision box has to be updated because can attack uses hitboxes only
    
    // Should NOT be able to attack
    assertFalse(player.canAttack(enemy), "The player should not be able to attack the enemy if it is out of range.");
  }
  
  @Test
  void testPlayerIsHit() {
    PlayerModel player = gameModel.getPlayer();
    int initialHealth = player.getHealth();
    
    gameModel.playerIsHit();
    
    assertEquals(initialHealth - 10, player.getHealth(), "Player's health should decrease by 10 after being hit.");
  }
  
  @Test
  void testIsOutOfBounds() {
    PlayerModel player = gameModel.getPlayer();
    
    // Move player out of bounds
    player.setX(Gdx.graphics.getWidth() + 1);
    assertTrue(gameModel.isOutOfBounds(), "Player should be out of bounds when moving beyond the screen.");
    
    // Move player back into bounds
    player.setX(0);
    assertFalse(gameModel.isOutOfBounds(), "Player should not be out of bounds when within the screen.");
  }
  
  @Test
  void testUpdateEnemiesPosition() {
    // Add a mock enemy
    Coordinate start = new Coordinate(50, 50);
    EnemyModel enemy = new EnemyModel(start, 0);
    gameModel.getEnemies().add(enemy);
    
    // Set player position
    PlayerModel player = gameModel.getPlayer();
    player.setX(100);
    player.setY(100);
    
    gameModel.updateEnemiesPos(0.1f);
    
    assertNotNull(gameModel.getEnemies(), "Enemies list should not be null after update.");
  }
  
  @Test
  void testGetCameraYCoordinateReturnsInt() {
    int camY = gameModel.getCameraYCoordinate();
    assertTrue(camY >= 0, "Camera Y coordinate should be positive");
  }
  
  
  @Test
  void testGetPlatformHitboxesNotNull() {
    assertNotNull(gameModel.getPlatformHitboxes(), "Platform hitbox list should not be null.");
  }
  
  @Test
  void testGetEnemyHitboxesNotNull() {
    assertNotNull(gameModel.getEnemyHitboxes(), "Enemy hitbox list should not be null.");
  }

  @Test
  void testStopTimerDoesNotThrow() {
    assertDoesNotThrow(() -> gameModel.stopTimer(), "Stopping timer should not throw");
  }
  
  @Test
  void testDisposeCancelsTimer() {
    assertDoesNotThrow(() -> gameModel.dispose(), "Dispose should cancel the timer");
  }
  
  @Test
void testPlayerAttacksEnemyReducesHealthAndIncreasesScore() {
    EnemyModel enemy = new EnemyModel(new Coordinate(0, 0), 0);
    enemy.setSize(50, 100);

    // Set enemy health low enough so it will die after one hit
    int initialHealth = enemy.getHealth();
    enemy.takeDamage(initialHealth - 1); // enemy now has 1 HP

    int initialScore = gameModel.getScore();

    // Attack: this should reduce health to 0 and increase score
    gameModel.playerAttacksEnemy(enemy);
    
    assertEquals(0, enemy.getHealth(), "Enemy health should be zero");
    assertEquals(initialScore + 1, gameModel.getScore(), "Score should increase by 1 after enemy dies.");
}

@Test
void testUpdateEnemiesRemoveDeadAndIncrementScore() {
    PlayerModel player = gameModel.getPlayer();
    player.setSize(50, 100); // to ensure valid hitbox comparisons

    // Create a dead enemy
    EnemyModel deadEnemy = new EnemyModel(new Coordinate(100, 100), 0);
    deadEnemy.setSize(50, 100);
    deadEnemy.setX(100);
    deadEnemy.setY(100);
    deadEnemy.updateCollisionBox(100, 100);

    // Set health to 0 to simulate dead enemy
    deadEnemy.takeDamage(deadEnemy.getHealth()); // now health == 0

    gameModel.getEnemies().add(deadEnemy);
    int initialScore = gameModel.getScore();

    // Act
    gameModel.updateEnemiesPos(0.1f);

    // Assert
    assertFalse(gameModel.getEnemies().contains(deadEnemy), "Dead enemy should be removed");
    assertEquals(initialScore + 1, gameModel.getScore(), "Score should increase when enemy is removed");
}

@Test
void testNoPlayerHitWhenNoCollision() {
    PlayerModel player = gameModel.getPlayer();
    player.setSize(50, 100);
    float initialHealth = player.getHealth();

    // Place player far from enemy
    player.setX(0);
    player.setY(0);

    // Create an enemy far away from player
    EnemyModel enemy = new EnemyModel(new Coordinate(1000, 1000), 0);
    enemy.setSize(50, 100);
    enemy.setX(1000);
    enemy.setY(1000);
    enemy.updateCollisionBox(1000, 1000);

    gameModel.getEnemies().add(enemy);
    gameModel.updateEnemiesPos(0.1f);

    assertEquals(initialHealth, player.getHealth(), "Player health should not change when there is no collision");
}



}
