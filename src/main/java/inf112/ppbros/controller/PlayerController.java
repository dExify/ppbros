package inf112.ppbros.controller;

import com.badlogic.gdx.InputAdapter;

import inf112.ppbros.model.GameModel;
import inf112.ppbros.view.ScreenView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/** 
 * The controller for Power Pipes Bros characters.
 * This class handles input for any playable characters. 
 */
public class PlayerController extends InputAdapter {
    private GameModel gameModel;
    
    public PlayerController(GameModel gameModel, ScreenView gameView) {
        this.gameModel = gameModel;
        Gdx.input.setInputProcessor(this);
    }
    
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            // inputs for movement
            case Input.Keys.D:
            System.out.println("D PRESSED");
            gameModel.movePlayerRight(1);
            break;
            case Input.Keys.A:
            System.out.println("A PRESSED");
            gameModel.movePlayerLeft(1);
            break;
            case Input.Keys.W:
            System.out.println("W PRESSED");
            gameModel.movePlayerUp(1);
            break;
            case Input.Keys.S:
            System.out.println("S PRESSED");
            gameModel.movePlayerDown(1);
            break;
            case Input.Keys.F:
            // when F is pressed, checks to see if player can attack
            if (gameModel.canPlayerAttack()) {
                System.out.println("Hit registered!");
                gameModel.playerAttacksEnemy();
            } else {
                System.out.println("No hit");
            }
            break;
            // close program with escape button
            case Input.Keys.ESCAPE:
            Gdx.app.exit();
        }
        return true; // Return true to indicate input was handled
    }
    
}