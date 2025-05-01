package inf112.ppbros.model;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Rectangle;

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
        // if (Gdx.gl == null) {
        // Gdx.gl = mock(GL20.class);
        // }
        // if (Gdx.gl20 == null) {
        //     Gdx.gl20 = mock(GL20.class);
        // }
        // if (Gdx.gl30 == null) {
        //     Gdx.gl30 = mock(GL30.class);
        // }

        // // mock Gdx.graphics
        // if (Gdx.graphics == null) {
        //     Gdx.graphics = mock(Graphics.class);
        //     when(Gdx.graphics.getWidth()).thenReturn(800);
        //     when(Gdx.graphics.getHeight()).thenReturn(600);
        // }
        gameModel = new GameModel(false);
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
}
