package inf112.ppbros.controller;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.view.ScreenView;

public class GameController implements ApplicationListener {
    private ScreenView screenView;
    private Rectangle screenRect;

    public GameController(ScreenView view) {
        ScreenView screenView = view;
    }

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        
    }
    
}
