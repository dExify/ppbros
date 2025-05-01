package inf112.ppbros.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import inf112.mockutil.GdxTestMock;
import inf112.ppbros.model.GameModel;
import inf112.ppbros.model.entity.PlayerModel;

public class PlayerControllerTest {

    private GameModel model;
    private PlayerController controller;
    private PlayerModel player;


    @BeforeEach
    void setUp() {
        // Mock Gdx.input
        Gdx.input = Mockito.mock(Input.class);
        GdxTestMock.init();
        model = new GameModel(false);
        model.makePlayer(0, 150);
        player = model.getPlayer();
        player.setSize(50, 100); // Ensure player has size so collision checks behave as expected

        controller = new PlayerController(model);

    }
    @Test
    void testUpdateMovesRight(){
        controller.keyDown(Input.Keys.D);
        controller.update(1f);

        assertTrue(controller.isMoving(), "Player should be marked as moving");
        assertFalse(player.facesLeft(), "Player should be facing right");
    }
    @Test
    void testUpdateStopsMovingRight() {
        controller.keyDown(Input.Keys.D);
        controller.update(1f);
        controller.keyUp(Input.Keys.D);
        controller.update(1f);

        assertFalse(controller.isMoving(), "Player should not be marked as moving when key is released");
        assertFalse(player.facesLeft(), "Player should be facing right");
    }
    
    @Test
    void testUpdateMovesLeft() {
        controller.keyDown(Input.Keys.A);
        controller.update(1f);

        assertTrue(controller.isMoving(), "Player should be marked as moving");
        assertTrue(player.facesLeft(), "Player should be facing left");
    }

    @Test
    void testUpdateStopsMovingLeft() {
        controller.keyDown(Input.Keys.A);
        controller.update(1f);
        controller.keyUp(Input.Keys.A);
        controller.update(1f);

        assertFalse(controller.isMoving(), "Player should not be marked as moving when key is released");
        assertTrue(player.facesLeft(), "Player should be facing left");
    }

    @Test
    void testUpdateNotMoving() {
        controller.update(1f);
        assertFalse(controller.isMoving(), "Player should not be moving when no keys are pressed");
    }

    @Test
    void testUpdateAttackingTrue() {
        controller.keyDown(Input.Keys.F);
        controller.update(1f);

        assertTrue(controller.isAttacking(), "Player should be marked as attacking");
    }

    @Test
    void testAttackingFalse() {
        controller.keyDown(Input.Keys.F);
        controller.update(1f);
        controller.keyUp(Input.Keys.F);
        controller.update(1f);

        assertFalse(controller.isAttacking(), "Player should not be attacking after releasing F");
    }
    
}
