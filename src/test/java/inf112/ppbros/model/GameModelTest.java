package inf112.ppbros.model;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Rectangle;

import inf112.mockutil.GdxTestMock;
import inf112.ppbros.model.entity.EnemyModel;
import inf112.ppbros.model.entity.PlayerModel;
import inf112.ppbros.model.platform.PlatformGrid;
import inf112.ppbros.model.platform.PlatformGridMaker;
import inf112.ppbros.testutils.TestApplicationListener; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

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
    
    assertEquals(initialHealth - 8, player.getHealth(),
    "Player should lose 8 health after hitting enemy");
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
    
    gameModel.playerIsHit(10);
    
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
void testUpdateEnemiesRemoveDead() {
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

    // Act
    gameModel.updateEnemiesPos(0.1f);
    
    // Assert
    assertFalse(gameModel.getEnemies().contains(deadEnemy), "Dead enemy should be removed");
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
  
  @Test
  void testShouldShowPowerUpMessage_initiallyFalse() {
      assertFalse(gameModel.shouldShowPowerUpMessage());
  }

  @Test
  void testShouldPowerUpAfterFifthKill() {
    PlayerModel player = gameModel.getPlayer();
    int initialDmg = player.getAttackDmg();

    for (int i = 0; i < 5; i++) {
      gameModel.addToScore();
    }
    assertEquals(5, gameModel.getScore());
    assertTrue(gameModel.shouldShowPowerUpMessage(), "Power-up message should be shown after 5 kills.");
    assertTrue(player.getAttackDmg() > initialDmg, "Player should have increased attack damage after 5 kills.");
  }  
  
  void testPlayerTakesDamageOutOfBounds() {
    PlayerModel player = gameModel.getPlayer();
    
    int initialHealth = player.getHealth();
    
    // Move player out of screen
    player.setX(Gdx.graphics.getWidth() + 100);
    
    gameModel.checkOutOfBounds();
    
    assertEquals(initialHealth - 10, player.getHealth(), "Player should take damage when out of bounds.");
  }
  
  @Test
  void testPlayerNotDamagedIfInBounds() {
    PlayerModel player = gameModel.getPlayer();
    
    int initialHealth = player.getHealth();
    
    // Ensure player is in bounds
    player.setX(100);
    
    gameModel.checkOutOfBounds();
    
    assertEquals(initialHealth, player.getHealth(), "Player should not take damage if inside bounds.");
  }
  
  
  @Test
  void testGetNextPlatformGridAddsEnemiesAndReturnsGrid() {
    PlatformGrid dummyGrid = mock(PlatformGrid.class);
    when(dummyGrid.getHitboxes()).thenReturn(List.of(new Rectangle(0, 0, 10, 10)));
    
    // Inject mock PlatformGridMaker
    try {
      Field field = GameModel.class.getDeclaredField("platformGridMaker");
      field.setAccessible(true);
      PlatformGridMaker makerMock = mock(PlatformGridMaker.class);
      when(makerMock.getNextPlatformGrid()).thenReturn(dummyGrid);
      when(dummyGrid.getValidEnemySpawnPos()).thenReturn(new Coordinate(0, 0));
      field.set(gameModel, makerMock);
    } catch (Exception e) {
      fail("Reflection injection failed: " + e.getMessage());
    }
    
    // Clear enemies and hitboxes before test
    gameModel.getEnemies().clear();
    gameModel.getPlatformHitboxes().clear();
    
    PlatformGrid result = gameModel.getNextPlatformGrid();
    
    assertNotNull(result, "Returned PlatformGrid should not be null.");
    assertEquals(5, gameModel.getEnemies().size(), "Exactly 5 enemies should be added.");
    assertEquals(1, gameModel.getPlatformHitboxes().size(), "Platform hitboxes should include the dummy hitbox.");
  }
  
  @Test
  void testJumpTriggersPlayerJump() {
    PlayerModel player = spy(gameModel.getPlayer());
    
    // Inject the spy back into the GameModel
    try {
      Field playerField = GameModel.class.getDeclaredField("player");
      playerField.setAccessible(true);
      playerField.set(gameModel, player);
    } catch (Exception e) {
      fail("Failed, error: " + e.getMessage());
    }
    
    gameModel.jump();
    
    verify(player, times(1)).jump();
  }
  
  @Test
  void testUpdatePlayerCallsPlayerUpdate() throws Exception {
    PlayerModel playerSpy = spy(gameModel.getPlayer());
    
    // Inject the spy into gamemodel
    try {
      Field playerField = GameModel.class.getDeclaredField("player");
      playerField.setAccessible(true);
      playerField.set(gameModel, playerSpy);
    } catch (Exception e) {
      fail("Failed, error: " + e.getMessage());
    }
    
    // Call the method under test
    gameModel.updatePlayer();
    
    // Verify update is called
    verify(playerSpy, times(1)).update(anyFloat(), anyList());
  }
  
}
