package inf112.ppbros.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.ppbros.model.GameModel;
import inf112.ppbros.model.entity.PlayerModel;

import java.util.HashSet;

/**
 * The controller for Power Pipes Bros characters.
 * This class handles input for the player.
 */
public class PlayerController extends InputAdapter {
    private final GameModel gameModel;
    private final AudioController audioController;
    private final HashSet<Integer> keysPressed = new HashSet<>();
    private boolean isAttacking = false;

    public PlayerController(GameModel gameModel, boolean loadAudio) {
        this.gameModel = gameModel;
        Gdx.input.setInputProcessor(this);

        if (loadAudio) {
        this.audioController = new AudioController();
        audioController.playBackgroundMusic(true);
        } else {
          audioController = null;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keysPressed.add(keycode);
        switch (keycode) {
            case Input.Keys.F:
                isAttacking = true;
                // Check attack on F press
                if (gameModel.attackableEnemy() != null) {
                    gameModel.playerAttacksEnemy(gameModel.attackableEnemy());
                    audioController.playSoundEffect("enemyAttacked");
                    System.out.println("Hit registered!");
                    System.out.println("Enemy health: " + gameModel.attackableEnemy().getHealth());
                } else {
                    System.out.println("No hit");
                }
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keysPressed.remove(keycode);
        if (keycode == Input.Keys.F) {
            isAttacking = false; // stop attack animation when key released
        }
        return true;
    }

    /**
     * Detetcs and handles any keypresses for movement and attack.
     * Updates animation states for player.
     * @param deltaTime all input is based on delta time 
     */
    public void update(float deltaTime) {
        PlayerModel player = gameModel.getPlayer(); // get PlayerModel

        boolean moving = false;
        boolean facingLeft = player.facesLeft(); // default to current

        if (keysPressed.contains(Input.Keys.D)) {
            gameModel.movePlayer(deltaTime, 0);
            moving = true;
            facingLeft = false;
        }
        if (keysPressed.contains(Input.Keys.A)) {
            gameModel.movePlayer(-deltaTime, 0);
            moving = true;
            facingLeft = true;
        }
        if (keysPressed.contains(Input.Keys.SPACE)) {
            gameModel.jump();
            if (audioController != null) {
              audioController.playSoundEffect("jump");
            }
        } 
        if (keysPressed.contains(Input.Keys.F) && isAttacking && audioController != null) {
            audioController.playSoundEffect("attack");
        }

        // Update animation states on player model
        player.setMoving(moving);
        player.setAttacking(isAttacking);
        player.setFacesLeft(facingLeft);
    }

    /**
     * Check if player is in movement: if any of the movement keys are being pressed
     * @return true or false depending on is player is in movement
     */
    public boolean isMoving() {
        return keysPressed.contains(Input.Keys.D) || keysPressed.contains(Input.Keys.A);
    }

    /**
     * Check if player is facing left
     * @return true if player is facing left, false if they are not
     */
    public boolean facesLeft() {
        return gameModel.getPlayer().facesLeft(); // delegate to model now
    }

    /**
     * Check if player is attacking
     * @return true if player is attacking, false if not
     */
    public boolean isAttacking() {
        return isAttacking;
    }

}
