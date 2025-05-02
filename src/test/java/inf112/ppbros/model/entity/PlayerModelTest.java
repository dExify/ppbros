package inf112.ppbros.model.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.math.Rectangle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PlayerModelTest {

    private PlayerModel player;

    @BeforeEach
    void setUp() {
        player = new PlayerModel(100, 200);
        player.setSize(50, 100);
    }

    @Test
    void testInitialHealth() {
        assertEquals(100, player.getHealth());
    }

    @Test
    void testTakeDamageReducesHealth() {
        player.takeDamage(30);
        assertEquals(70, player.getHealth());
    }

    @Test
    void testTakeDamageToZeroHealth() {
        player.takeDamage(120);
        assertEquals(0, player.getHealth());
    }

    @Test
    void testCanAttackWithinRange() {
        EnemyModel enemy = mock(EnemyModel.class);
        when(enemy.getCollisionBox()).thenReturn(new Rectangle(90, 150, 40, 100));
        assertTrue(player.canAttack(enemy));
    }

    @Test
    void testCannotAttackOutsideRange() {
        EnemyModel enemy = mock(EnemyModel.class);
        when(enemy.getCollisionBox()).thenReturn(new Rectangle(300, 200, 40, 100));
        assertFalse(player.canAttack(enemy));
    }

    @Test
    void testCannotAttackIfTooFarUp() {
        EnemyModel enemy = mock(EnemyModel.class);
        when(enemy.getCollisionBox()).thenReturn(new Rectangle(100, 350, 40, 100));
        assertFalse(player.canAttack(enemy));
    }

    @Test
    void testMoveUpdatesPos() {
        float dx = 10f;
        float dy = 5f;
        float initialX = player.getX();
        float initialY = player.getY();
        player.move(dx, dy);
        assertEquals(initialX + dx, player.getX());
        assertEquals(initialY + dy, player.getY());
    }

    @Test
    void testCollidesWithOverlappingRectangle() {
        Rectangle platform = new Rectangle(100, 200, 50, 100);
        assertTrue(player.collidesWith(platform));
    }

    @Test
    void testNotCollidesWithNonOverlappingRectangle() {
        Rectangle platform = new Rectangle(500, 500, 50, 50);
        assertFalse(player.collidesWith(platform));
    }

    @Test
    void testJumpSetsVelocityWhenOnGround() {
        List<Rectangle> platforms = new ArrayList<>();
        platforms.add(new Rectangle(100, 100, 100, 20));
        player.setY(120); // Position just above platform
        player.update(1 / 60f, platforms);
        player.jump();
        player.update(1 / 60f, platforms);
        assertTrue(player.getY() > 120);
    }

    @Test
    void testUpdateGravityWhenFalling() {
        float initialY = player.getY();
        player.update(1.0f, new ArrayList<>()); // no platforms
        assertTrue(player.getY() < initialY);
    }

    @Test
    void testLandingOnPlatformStopsFalling() {
        List<Rectangle> platforms = new ArrayList<>();
        platforms.add(new Rectangle(100, 50, 100, 20));
        player.setY(60);
        player.update(1.0f, platforms);
        float afterY = player.getY();
        assertEquals(70, afterY, 0.01); // platform.y + height
    }

    @Test
    void testSetAndGetFacesLeft() {
        player.setFacesLeft(true);
        assertTrue(player.facesLeft());
        player.setFacesLeft(false);
        assertFalse(player.facesLeft());
    }

    @Test
    void testGetTypeReturnsMainCharacter() {
        assertEquals(EntityType.MAIN_CHARACTER, player.getType(), "getType should return MAIN_CHARACTER");
    }
}
