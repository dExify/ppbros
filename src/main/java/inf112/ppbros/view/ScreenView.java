package inf112.ppbros.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import inf112.ppbros.model.GameModel;

public class ScreenView implements Screen {
    private GameModel gameModel;
    private ShapeRenderer shapeRenderer;
    private Rectangle screenRect;
    // private SpriteBatch batch;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    Vector2 vector;

    public ScreenView(GameModel model) {
        this.gameModel = model;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        screenRect = new Rectangle();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //Kamera er på samme størrelse som skjermen
        vector = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2); //Kamera?
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        camera.position.set(vector, 0);
        camera.update();

        mapRenderer = gameModel.getMapRenderer();
        mapRenderer.setView(camera);
        mapRenderer.render();
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
