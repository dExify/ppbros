package inf112.ppbros.controller;

import com.badlogic.gdx.InputAdapter;

import inf112.ppbros.model.GameModel;
import inf112.ppbros.view.ScreenView;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/** 
* The controller for Power Pipes Bros characters.
* This class handles input for any playable characters. 
*/
public class PlayerController extends InputAdapter {
    private GameModel gameModel;
    private ScreenView gameView;
    private AudioController audioController;
    private HashSet<Integer> keysPressed = new HashSet<>();
    private boolean facesLeft = false;
    private boolean isAttacking = false;
    
    
    public PlayerController(GameModel gameModel, ScreenView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        Gdx.input.setInputProcessor(this);
        this.audioController = new AudioController();
    }
    
    @Override
    public boolean keyDown(int keycode) {
        keysPressed.add(keycode); // Track key presses
        switch (keycode) {
            case Input.Keys.F:
            isAttacking = !isAttacking;
            // When F is pressed, checks to see if player can attack
            if (gameModel.attackableEnemy() != null) {
                gameModel.playerAttacksEnemy(gameModel.attackableEnemy());
                System.out.println("Hit registered!");
                System.out.println("Enemy health: "+ gameModel.attackableEnemy().getHealth());
            } else {
                System.out.println("No hit");
            }
            break;
            // Close program with escape button
            case Input.Keys.ESCAPE:
            Gdx.app.exit();
        }
        return true;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        keysPressed.remove(keycode); // Remove released keys
        return true;
    }
    
    public void update(float deltaTime) {

        // Check held keys and move player continuously
        if (keysPressed.contains(Input.Keys.D)) {
            // gameModel.movePlayerRight(deltaTime);
            gameModel.movePlayer(deltaTime, 0);
            facesLeft = false;
        }
        if (keysPressed.contains(Input.Keys.A)) {
            // gameModel.movePlayerLeft(deltaTime);
            gameModel.movePlayer(-deltaTime, 0);
            facesLeft = true;
        }
        // if (keysPressed.contains(Input.Keys.W)) {
        //     // gameModel.movePlayerUp(deltaTime);
        //     gameModel.movePlayer(0, deltaTime);
        // }
        // if (keysPressed.contains(Input.Keys.S)) {
        //     // gameModel.movePlayerDown(deltaTime);
        //     gameModel.movePlayer(0, -deltaTime);
        // }
        if (keysPressed.contains(Input.Keys.SPACE)) {
            gameModel.jump();
        }
        if (keysPressed.contains(Input.Keys.F) && isAttacking()) {
            audioController.playSoundEffect("attack");
        }
    }
    
    public boolean isMoving() {
        return keysPressed.contains(Input.Keys.D) || 
        keysPressed.contains(Input.Keys.A) || 
        keysPressed.contains(Input.Keys.W) || 
        keysPressed.contains(Input.Keys.S);
    }
    
    public boolean facesLeft() {
        return facesLeft;
    }

    public boolean isAttacking() {
        return isAttacking;
    }
    
}