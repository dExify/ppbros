package inf112.ppbros.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;

import inf112.ppbros.model.GameModel;
import inf112.ppbros.view.GameView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.ppbros.view.GameView;

public class PlayerController extends InputAdapter implements Screen{
    private GameModel gameModel;
    private Texture playerTexture, enemyTexture;
    private SpriteBatch batch;
    private GameView gameView;

    // map size (should probably be moved somewhere else later)
    final float MAP_WIDTH = 480;
    final float MAP_HEIGHT = 320;

    public void playerController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.batch = new SpriteBatch();
        this.playerTexture = new Texture(Gdx.files.internal("character.png"));
        this.enemyTexture = new Texture(Gdx.files.internal("enemy.png"));
        Gdx.input.setInputProcessor(this);
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            gameModel.movePlayerRight(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            gameModel.movePlayerLeft(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            gameModel.movePlayerUp(delta);    
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            gameModel.movePlayerDown(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (gameModel.canPlayerAttack()) {
                System.out.println("Hit registered!");
                gameModel.playerAttacksEnemy();
            } else {
                System.out.println("No hit");
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(1, 1, 1, 0);

        handleInput(1);
        //drawObjects(); method to be created in view
        
        gameView.render(delta);

    }



    @Override
    public void resize(int width, int height) {
        gameView.resize(width, height);
    }

    @Override
    public void pause() {
        gameView.pause();
    }

    @Override
    public void resume() {
        gameView.resume();
    }

    @Override
    public void hide() {
        gameView.hide();
    }

    @Override
    public void dispose() {
       batch.dispose();
       playerTexture.dispose();
       enemyTexture.dispose();
    }
}