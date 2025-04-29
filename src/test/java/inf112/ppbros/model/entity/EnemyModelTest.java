package inf112.ppbros.model.entity;

import com.badlogic.gdx.math.Rectangle;
import inf112.ppbros.model.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyModelTest {

  private EnemyModel enemy;

  @BeforeEach
  void setUp() {
    Coordinate start = new Coordinate(100, 100);
    enemy = new EnemyModel(start, 0);
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
  
}
