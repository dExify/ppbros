package inf112.ppbros.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import inf112.ppbros.model.GameModel;

public class ScreenView implements Screen {
    private GameModel gameModel;
    private ShapeRenderer shapeRenderer;
    private Rectangle screenRect;

    public ScreenView(GameModel model) {
        this.gameModel = model;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        screenRect = new Rectangle();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        screenRect.width = width;
		screenRect.height = height;
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

}
