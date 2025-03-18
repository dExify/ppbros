package inf112.ppbros.controller;

import com.badlogic.gdx.InputAdapter;

import inf112.ppbros.model.GameModel;
import inf112.ppbros.view.ScreenView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PlayerController extends InputAdapter {
    private GameModel gameModel;
    
    // map size (should probably be moved somewhere else later)
    final float MAP_WIDTH = 480;
    final float MAP_HEIGHT = 320;
    
    public PlayerController(GameModel gameModel, ScreenView gameView) {
        this.gameModel = gameModel;
        Gdx.input.setInputProcessor(this);
    }
    
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
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
            if (gameModel.canPlayerAttack()) {
                System.out.println("Hit registered!");
                gameModel.playerAttacksEnemy();
            } else {
                System.out.println("No hit");
            }
            break;
        }
        return true; // Return true to indicate input was handled
    }
    
}