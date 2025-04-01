package inf112.ppbros.controller;

import com.badlogic.gdx.InputAdapter;

import inf112.ppbros.model.GameModel;
import inf112.ppbros.view.ScreenView;

import java.util.HashSet;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/** 
 * The controller for Power Pipes Bros characters.
 * This class handles input for any playable characters. 
 */
public class PlayerController extends InputAdapter {
    private GameModel gameModel;
    private HashSet<Integer> keysPressed = new HashSet<>();
    public boolean facesLeft = false;
    
    public PlayerController(GameModel gameModel, ScreenView gameView) {
        this.gameModel = gameModel;
        Gdx.input.setInputProcessor(this);
        
    }
    
    @Override
    public boolean keyDown(int keycode) {
        keysPressed.add(keycode); // Track key presses
        switch (keycode) {
            case Input.Keys.F:
            // When F is pressed, checks to see if player can attack
            if (gameModel.canPlayerAttack()) {
                System.out.println("Hit registered!");
                gameModel.playerAttacksEnemy();
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

    // public void update(float deltaTime) {
    //     // Check held keys and move player continuously
    //     if (keysPressed.contains(Input.Keys.D)) {
    //         gameModel.movePlayerRight(deltaTime);
    //         facesLeft = false;
    //     }
    //     if (keysPressed.contains(Input.Keys.A)) {
    //         gameModel.movePlayerLeft(deltaTime);
    //         facesLeft = true;
    //     }
    //     if (keysPressed.contains(Input.Keys.W)) {
    //         gameModel.movePlayerUp(deltaTime);
    //     }
    //     if (keysPressed.contains(Input.Keys.S)) {
    //         gameModel.movePlayerDown(deltaTime);
    //     }
    // }
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gameModel.movePlayerRight();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gameModel.movePlayerLeft();
        }
        // if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        //     gameModel.getPlayerBody().applyLinearImpulse(new Vector2(0, 5f), gameModel.getPlayerBody().getWorldCenter(), true);
        // }
    }

}