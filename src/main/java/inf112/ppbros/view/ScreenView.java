package inf112.ppbros.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import inf112.ppbros.controller.PlayerController;
import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.GameModel;
import inf112.ppbros.model.Entity.PlayerModel;
import inf112.ppbros.model.Platform.PlatformGrid;
import inf112.ppbros.model.Platform.TileConfig;

public class ScreenView implements Screen {
    private GameModel gameModel;
    private PlayerController playerController;
    private ShapeRenderer shapeRenderer;
    private Rectangle screenRect;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    Vector2 vector;
    PlatformGrid platformGridObject;
    private Texture platformTexture;
    private Texture platformRustyTexture;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private static final int TILE_SIZE = TileConfig.TILE_SIZE; //Should we initialise TILE_SIZE in the show function?
    private PlayerModel player;
    private Texture playerTexture, resizedPlayerTexture;
    private String playerRight, playerLeft;

    public ScreenView(GameModel model) {
        this.gameModel = model;
        this.playerController = new PlayerController(model, this);
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("clean-crispy-ui.json")); // Placeholderskin til vi er ferdig med å lage vårt eget

        playerRight = "character.png";
        playerLeft = "charFlipped.png";
        playerTexture = new Texture(Gdx.files.internal(playerRight));
        this.resizedPlayerTexture = TextureUtils.resizeTexture(playerTexture, playerTexture.getWidth()/3, playerTexture.getHeight()/3);

        // set player size based on texture size
        gameModel.getPlayer().setSize(resizedPlayerTexture.getWidth(), resizedPlayerTexture.getHeight());
        
        Table healthTable = new Table();
        Table scoreTable = new Table();
        healthTable.top().left();
        scoreTable.top().right();
        scoreTable.setFillParent(true);
        healthTable.setFillParent(true);
        stage.addActor(healthTable);
        stage.addActor(scoreTable);

        Label scoreLabel = new Label("Score: 0", skin);
        Label healthLabel = new Label("Health: 100", skin);
        
        scoreTable.add(scoreLabel).pad(10);
        healthTable.add(healthLabel).pad(10);
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
        platformRustyTexture = new Texture(Gdx.files.internal("RustyGraystoneBrickTile80.png"));
        platformGridObject = gameModel.getPlatformGrid();

        player = gameModel.getPlayer();
    }

    @Override
    public void render(float delta) {
        camera.position.set(vector, 0);
        camera.update();

        mapRenderer = gameModel.getMapRenderer();
        mapRenderer.setView(camera);
        mapRenderer.render();

        drawPlatformGrid(platformGridObject);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

        // draw player and update controller for input
        drawPlayer();
        // show hitbox delete later
        shapeRenderer.setColor(1, 0, 0, 0);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);    
        shapeRenderer.rect(2*gameModel.getPlayer().getWidth(), 50, gameModel.getPlayer().getWidth(), gameModel.getPlayer().getHeight());
        shapeRenderer.end();
        
        playerController.update(delta);
        // player faces the direction it is walking
        if (playerController.facesLeft){
            loadNewTexture(playerLeft);
        } 
        if (playerController.facesLeft == false) {
            loadNewTexture(playerRight);
        }
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
                } else if (grid[x][y] == 2) {
                    Coordinate platformPixelPos = TilePositionInPixels.getTilePosInPixels(x, y, TILE_SIZE);
                    batch.draw(platformRustyTexture, platformPixelPos.x(), platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
                } else { //Here we can choose what type of tiles to draw based on the integer in the 2D array
                    continue;
                }
            }
        }
        batch.end();
    }

    private void drawPlayer() {
        batch.begin();
        batch.draw(resizedPlayerTexture, player.getX(), player.getY(), resizedPlayerTexture.getWidth(), resizedPlayerTexture.getHeight());
        batch.end();
    }

    public void loadNewTexture(String path) {
        // Dispose of the previous texture if it exists
        if (playerTexture != null) {
            playerTexture.dispose();
        }
        
        // Load the new texture
        playerTexture = new Texture(Gdx.files.internal(path));
        this.resizedPlayerTexture = TextureUtils.resizeTexture(playerTexture, playerTexture.getWidth()/3, playerTexture.getHeight()/3);
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
