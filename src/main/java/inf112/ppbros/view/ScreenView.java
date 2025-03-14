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

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.GameModel;
import inf112.ppbros.model.PlatformGrid;

public class ScreenView implements Screen {
    private GameModel gameModel;
    private ShapeRenderer shapeRenderer;
    private Rectangle screenRect;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    Vector2 vector;
    PlatformGrid platformGridObject;
    private Texture platformTexture;
    private SpriteBatch batch;
    private static final int TILE_SIZE = 80;

    public ScreenView(GameModel model) {
        this.gameModel = model;
    }

    @Override
    public void show() {
        //Initiate a camera and shaperenderer
        shapeRenderer = new ShapeRenderer();
        screenRect = new Rectangle();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //Kamera er på samme størrelse som skjermen
        vector = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2); //Kamera?

        //Initiate the platform texture and platformGrid object
        batch = new SpriteBatch();
        platformTexture = new Texture(Gdx.files.internal("GraystoneBrickTile80.png"));
        platformGridObject = gameModel.getPlatformGrid();
    }

    @Override
    public void render(float delta) {
        camera.position.set(vector, 0);
        camera.update();

        mapRenderer = gameModel.getMapRenderer();
        mapRenderer.setView(camera);
        mapRenderer.render();

        drawPlatformGrid(platformGridObject);
    }

    /**
     * Renders the platform grid
     * @param platformGrid
     */
    private void drawPlatformGrid(PlatformGrid platformGrid) {
        int[][] grid = platformGrid.returnGrid();
        batch.begin();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == 0) { //The most effective approach?
                    continue;
                } else if (grid[x][y] == 1) {
                    Coordinate platformPixelPos = TilePositionInPixels.getTilePosInPixels(x, y, TILE_SIZE);
                    batch.draw(platformTexture, platformPixelPos.x(), platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
                } else { //Here we can choose what type of tiles to draw based on the integer in the 2D array
                    continue;
                }
            }
        }
        batch.end();
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
