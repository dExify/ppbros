package inf112.ppbros.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.GameModel;
import inf112.ppbros.model.Entity.PlayerModel;
import inf112.ppbros.model.Platform.PlatformGrid;
import inf112.ppbros.model.Platform.TileConfig;

public class ScreenView implements Screen {
    private GameModel gameModel;
    private ShapeRenderer shapeRenderer;
    private Rectangle screenRect;
    private OrthographicCamera camera;
    // private PlatformGrid platformGridObject;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private final int TILE_SIZE = TileConfig.TILE_SIZE; //Should we initialise TILE_SIZE in the show function?
    private PlayerModel player;
    private Texture playerTexture, mapTexture, platformTexture, platformRustyTexture, redX;
    private final int startX, startY;
    private int yPos;
    PlatformGrid platformGridObject1, platformGridObject2;

    public ScreenView(GameModel model) {
        this.gameModel = model;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("clean-crispy-ui.json")); // Placeholderskin til vi er ferdig med å lage vårt eget
        playerTexture = new Texture(Gdx.files.internal("character.png"));

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

        startX = -Gdx.graphics.getWidth()/2;
        startY = -Gdx.graphics.getHeight()/2;

        platformGridObject1 = gameModel.getNextPlatformGrid(); 
        platformGridObject2 = gameModel.getNextPlatformGrid();
    }

    @Override
    public void show() {
        //Initiate a camera and shaperenderer
        shapeRenderer = new ShapeRenderer();
        screenRect = new Rectangle();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Initiate the platform texture and platformGrid object
        batch = new SpriteBatch();
        platformTexture = new Texture(Gdx.files.internal("GraystoneBrickTile80.png"));
        platformRustyTexture = new Texture(Gdx.files.internal("RustyGraystoneBrickTile80.png"));
        mapTexture = new Texture(Gdx.files.internal("SewerMap.png"));
        redX = new Texture(Gdx.files.internal("redX.png"));
        // platformGridObject = gameModel.getPlatformGrid();

        player = gameModel.getPlayer();
        this.yPos = 0;
    }

    @Override
    public void render(float delta) {
        drawBackground(); //Should only run once? -- batch.draw(mapTexture, 0, 0, 1920, 4800);

        drawPlatformGrid(platformGridObject1);
        drawPlatformGrid(platformGridObject2);

        if (platformGridObject1.getYPos() < camera.position.y - TileConfig.platformGridHeightInPixels) {
            platformGridObject1 = platformGridObject2;
            platformGridObject2 = gameModel.getNextPlatformGrid();
        }

        camera.position.y = gameModel.getCameraYCoordinate();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Health
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

        drawPlayer();
    }

    private void drawBackground() {
        batch.begin();
        batch.setColor(0.7F, 0.7F, 0.7F, 1F); //Set brightness to 70%
        // batch.setColor(0F, 0F, 0F, 1F); //Set brightness to 0% (debugging)
        batch.draw(mapTexture, startX, startY, 1920, 4800); //Maybe use screenwidth?
        batch.setColor(1F, 1F, 1F, 1F);
        batch.end();
    }

    /**
     * Renders the platform grid
     * @param platformGrid
     */
    private void drawPlatformGrid(PlatformGrid platformGrid) {
        yPos = platformGrid.getYPos();
        int[][] grid = platformGrid.returnGrid();
        batch.begin();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == 0) { //The most effective approach?
                    continue;
                } else if (grid[x][y] == 1) {
                    Coordinate platformPixelPos = TilePositionInPixels.getTilePosInPixels(x, y, TILE_SIZE);
                    batch.draw(platformTexture, startX + platformPixelPos.x(), startY + yPos + platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
                } else if (grid[x][y] == 2) {
                    Coordinate platformPixelPos = TilePositionInPixels.getTilePosInPixels(x, y, TILE_SIZE);
                    batch.draw(platformRustyTexture, startX + platformPixelPos.x(), startY + yPos + platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
                } else if (grid[x][y] == -1) {
                    Coordinate platformPixelPos = TilePositionInPixels.getTilePosInPixels(x, y + yPos, TILE_SIZE);
                    batch.draw(redX, startX + platformPixelPos.x(), startY + yPos + platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
                } else { //Here we can choose what type of tiles to draw based on the integer in the 2D array
                    continue;
                }
            }
        }
        batch.end();
    }

    private void drawPlayer() {
        batch.begin();
        batch.draw(playerTexture, startX + player.getX(), startY + player.getY(), 46, 68);
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
