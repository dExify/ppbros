package inf112.ppbros.model.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import inf112.mockutil.GdxTestMock;
import inf112.ppbros.controller.PlayerController;
import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.GameModel;
import inf112.ppbros.model.platform.TileConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class EnemyModelTest {

  private EnemyModel enemy;
  // private PlayerModel player;
  // private GameModel model;

  @BeforeEach
  void setUp() {
    Coordinate start = new Coordinate(100, 100);
    enemy = new EnemyModel(start, 0);

    // // Mock Gdx.input
    // Gdx.input = Mockito.mock(Input.class);
    GdxTestMock.init();
    // model = new GameModel(false, false);
  }

  @Test
  void testInitialization() {
    assertEquals(100, enemy.getHealth());
    assertEquals(50.0f, enemy.getSpeed());
    assertEquals(10, enemy.getAttackDmg());
    assertEquals(EntityType.ENEMY, enemy.getType());
  }

  @Test
  void testTakeDamage() {
    enemy.takeDamage(30);
    assertEquals(70, enemy.getHealth());

    enemy.takeDamage(100); // Overkill
    assertEquals(0, enemy.getHealth());
  }

  @Test
  void testMoveUpdatesPositionAndCollisionBox() {
    float initialX = enemy.getX();
    float initialY = enemy.getY();

    enemy.move(10, 5);

    assertEquals(initialX + 10, enemy.getX());
    assertEquals(initialY + 5, enemy.getY());

    Rectangle box = enemy.getCollisionBox();
    assertEquals(enemy.getX(), box.getX());
    assertEquals(enemy.getY(), box.getY());
  }

  @Test
  void testUpdateCollisionBox() {
    enemy.updateCollisionBox(200, 150);
    Rectangle box = enemy.getCollisionBox();
    assertEquals(200, box.getX());
    assertEquals(150, box.getY());
  }

  @Test
  void testFacesLeft() {
    assertTrue(enemy.facesLeft()); // default is left
    enemy.changeDirection();
    assertFalse(enemy.facesLeft());
  }

  @Test
  void testEnemyDirectionSwitch() {
    boolean initialDirection = enemy.facesLeft();
    enemy.changeDirection();
    assertNotEquals(initialDirection, enemy.facesLeft(), "Enemy direction should be flipped");
  }

  @Test
  void testPatrolChangesDirectionWhenNoTileBelow() {
      enemy.setSize(10, 10); // Ensure dimensions are set
      List<Rectangle> hitboxes = List.of(); // No tile below
      PlayerModel player = mock(PlayerModel.class);
      when(player.getX()).thenReturn(1000f);
      when(player.getY()).thenReturn(1000f);

      boolean initialDirection = enemy.facesLeft();
      enemy.updateMovement(player, hitboxes, 1f);
      assertNotEquals(initialDirection, enemy.facesLeft(), "Enemy should change direction at edge");
  }

  @Test
  void testNoTileBelowEnemyDoesNotMove() {
      enemy.setSize(10, 10);
      float startX = enemy.getX();
      PlayerModel player = mock(PlayerModel.class);
      when(player.getX()).thenReturn(startX);
      when(player.getY()).thenReturn(enemy.getY());

      enemy.updateMovement(player, List.of(), 0.1f); // No tiles
      assertEquals(startX, enemy.getX(), "Enemy should not move without platform below");
  }

  @Test
  void testPlatformCollisionChangesDirection() {
      enemy.setSize(10, 10);
      Rectangle wall = new Rectangle(enemy.getX() + 5, enemy.getY(), 10, 10); // Just in front
      List<Rectangle> platforms = List.of(new Rectangle(enemy.getX(), enemy.getY() - 10, 50, 10), wall);

      PlayerModel player = mock(PlayerModel.class);
      when(player.getX()).thenReturn(enemy.getX() - 100); // Still in patrol mode
      when(player.getY()).thenReturn(enemy.getY());

      boolean initialDirection = enemy.facesLeft();
      enemy.updateMovement(player, platforms, 0.1f);
      assertNotEquals(initialDirection, enemy.facesLeft(), "Enemy should reverse on collision");
  }



}