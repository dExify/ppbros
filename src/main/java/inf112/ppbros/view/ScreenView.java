package inf112.ppbros.view;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import inf112.ppbros.app.PowerPipesBros;
import inf112.ppbros.controller.PlayerController;
import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.GameModel;
import inf112.ppbros.model.entity.EnemyModel;
import inf112.ppbros.model.entity.PlayerModel;
import inf112.ppbros.model.platform.PlatformGrid;
import inf112.ppbros.model.platform.TileConfig;

/**
* The ScreenView class represents the main game screen where the gameplay occurs.
* It is responsible for rendering the game world, managing the camera, UI overlays,
* drawing the player, enemies, platform tiles, and handling scene transitions such as
* game over.
*
* <p>This class serves as the visual representation of the game state defined by {@link GameModel},
* where {@link PlayerController} handles input for character movement.
* It handles rendering background, platforms, entities, UI elements like score and health,
* and orchestrates the update loop in sync with LibGDX's rendering cycle.
*/
public class ScreenView implements Screen {
  
  private GameModel gameModel;
  private PlayerController playerController;
  private ShapeRenderer shapeRenderer;
  private Rectangle screenRect;
  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Stage stage;
  private Label scoreLabel;
  private Label healthLabel;
  
  private static final int TILE_SIZE = TileConfig.TILE_SIZE;
  private int yPos;
  private PlatformGrid platformGridObject1;
  private PlatformGrid platformGridObject2;
  private Texture mapTexture;
  private Texture platformTexture;
  private Texture barrelTexture;
  private Texture debuggingTexture;
  private Texture skullsTexture;
  private Texture sewerLoop;
  private PlayerModel player;
  

  @SuppressWarnings("unused")
  private float animationTime = 0;
  
  private List<EnemyModel> enemies;
  
  private boolean drawInfiniteBackground;
  
  /**
  * Constructs the main gameplay screen with a reference to the game's logic.
  *
  * @param model the GameModel containing the logic and state of the game
  */
  public ScreenView(GameModel model) {
    this.gameModel = model;
    this.playerController = new PlayerController(model, true);
    
  }
  
  @Override
  public void show() {
    Table healthTable;
    Table scoreTable;
    Skin skin;
    player = gameModel.getPlayer();
    gameModel.loadPlayerAnimations();
    
    
    // Make UI overlay
    stage = new Stage();
    // Make UI overlay for score and health bar
    skin = new Skin(Gdx.files.internal("skin/uiskin.json")); // Placeholderskin til vi er ferdig med å lage vårt eget
    scoreTable = new Table();
    healthTable = new Table();
    scoreLabel = new Label("", skin);
    healthLabel = new Label("", skin);
    healthTable.top().left();
    scoreTable.top().right();
    scoreTable.setFillParent(true);
    healthTable.setFillParent(true);
    scoreTable.add(scoreLabel).pad(10);
    healthTable.add(healthLabel).pad(10);
    stage.addActor(scoreTable);
    stage.addActor(healthTable);
    
    // Initiate a camera and shaperenderer
    shapeRenderer = new ShapeRenderer();
    screenRect = new Rectangle();
    camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    
    // Initiate the platform texture and platformGrid object
    batch = new SpriteBatch();
    platformTexture = new Texture(Gdx.files.internal("world/platform/GraystoneBrickTileWithOutline80.png"));
    skullsTexture = new Texture(Gdx.files.internal("world/platform/Skulls.png"));
    barrelTexture = new Texture(Gdx.files.internal("world/platform/Barrel.png"));
    debuggingTexture = new Texture(Gdx.files.internal("debug_hitbox.png"));
    mapTexture = new Texture(Gdx.files.internal("world/background/sewer_full.png"));
    sewerLoop = new Texture(Gdx.files.internal("world/background/sewer_repeat.png"));
    platformGridObject1 = gameModel.getNextPlatformGrid(); 
    platformGridObject2 = gameModel.getNextPlatformGrid();
    this.yPos = 0;
    
    
    
    gameModel.startTimer();
    
    drawInfiniteBackground = false;
  }
  
  @Override
  public void render(float delta) {
    // check if player is at 0 health, initiate game over screen if they are 
    if (player.getHealth() <= 0) {
      ((PowerPipesBros) Gdx.app.getApplicationListener()).transitionTo(new GameOverScreen(gameModel));
      return; // exits render() call
    }
    
    batch.setProjectionMatrix(camera.combined);
    
    if (drawInfiniteBackground) {
      drawInfiniteBackground();
    } 
    drawBackground();
    
    drawPlatformGrid(platformGridObject1);
    drawPlatformGrid(platformGridObject2);
    
    if (platformGridObject1.getYPos() < camera.position.y - (double) 3 * TileConfig.PLATFORM_GRIDHEIGHT_PIXELS/2) {
      platformGridObject1 = platformGridObject2;
      platformGridObject2 = gameModel.getNextPlatformGrid();
    }
    
    // Update score and health bar
    scoreLabel.setText("Score: " + gameModel.getScore());
    healthLabel.setText("Health: " + player.getHealth());
    
    // Camera movement
    camera.position.y = gameModel.getCameraYCoordinate();
    camera.update();
    
    
    enemies = gameModel.getEnemies();
    
    // temp for texture
    drawEnemies(delta);
    gameModel.updateEnemiesPos(delta);
    
    playerController.update(delta);
    animationTime += delta;
    
    gameModel.checkOutOfBounds();
    drawPlayer(delta);
    
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
    stage.draw();
    playerController.update(delta);
    gameModel.updatePlayer();
    
    //drawHitboxes(); //debugging
    //drawPlayerHitbox(); //debugging
  }
  
  private void drawPlayer(float delta) {
    player.updateAnimation(delta);
    
    batch.begin();
    TextureRegion frame = player.getCurrentFrame();
    batch.draw(frame, player.getX(), player.getY(), frame.getRegionWidth() / (float) 3, frame.getRegionHeight() / (float) 3);
    batch.end();
    
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
    stage.draw();
    
    playerController.update(delta);
    
    
    // drawHitboxes(); //debugging
    // drawPlayerHitbox(); //debugging
    // drawEnemiesHitbox(); //debugging
  }
  
  // === Debugging Methods ===
  
  @SuppressWarnings("unused")
  private void drawHitboxes() {
    batch.begin();
    for (Rectangle rec : gameModel.getPlatformHitboxes()) {
      batch.draw(debuggingTexture, rec.getX(), rec.getY(), TILE_SIZE, TILE_SIZE);
    }
    batch.end();
  }
  
  @SuppressWarnings("unused")
  private void drawPlayerHitbox() {
    Rectangle playerCollisionBox = player.getCollisionBox();
    batch.begin();
    batch.draw(debuggingTexture, playerCollisionBox.getX(), playerCollisionBox.getY(), playerCollisionBox.getWidth(), playerCollisionBox.getHeight());
    batch.end();
  }
  
  @SuppressWarnings("unused")
  private void drawEnemiesHitbox() {
    enemies = gameModel.getEnemies();
    batch.begin();
    for (EnemyModel enemy : enemies){
      batch.draw(debuggingTexture, enemy.getCollisionBox().getX(), enemy.getCollisionBox().getY(), enemy.getCollisionBox().getWidth(), enemy.getCollisionBox().getHeight());
    }
    batch.end();
  }
  
  private void drawEnemies(float delta) {
    enemies = gameModel.getEnemies();
    batch.begin();
    
    for (EnemyModel enemy : enemies) {
      enemy.updateAnimation(delta); 
      TextureRegion frame = enemy.getCurrentFrame();
      batch.draw(frame, enemy.getX(), enemy.getY(), frame.getRegionWidth() / (float) 3, frame.getRegionHeight() / (float) 3);
    }
    
    batch.end();
  }
  
  private void drawBackground() {
    batch.begin();
    batch.setColor(0.7F, 0.7F, 0.7F, 1F); //Set brightness to 70%
    // batch.setColor(0F, 0F, 0F, 1F); //Set brightness to 0% (debugging)
    double backgroundHeight = Gdx.graphics.getWidth() * 2.5;
    batch.draw(mapTexture, 0, 0, Gdx.graphics.getWidth(), (int) backgroundHeight);
    batch.draw(mapTexture, 0, 0, Gdx.graphics.getWidth(), (int) backgroundHeight);
    batch.setColor(1F, 1F, 1F, 1F);
    batch.end();
    if (backgroundHeight < camera.position.y + Gdx.graphics.getHeight() / (double) 2) {
      drawInfiniteBackground = true;
    }
  }
  
  private void drawInfiniteBackground() {
    double backgroundHeight = TileConfig.PLATFORM_GRIDHEIGHT_PIXELS; 
    batch.begin();
    batch.setColor(0.7F, 0.7F, 0.7F, 1F);
    batch.draw(sewerLoop, 0, platformGridObject1.getYPos(), Gdx.graphics.getWidth(), (int) backgroundHeight);
    batch.draw(sewerLoop, 0, platformGridObject2.getYPos(), Gdx.graphics.getWidth(), (int) backgroundHeight);
    batch.setColor(1F, 1F, 1F, 1F);
    batch.end();
  }
  
  /**
  * Renders the platform grid
  * @param platformGrid to be rendered
  */
  private void drawPlatformGrid(PlatformGrid platformGrid) {
    yPos = platformGrid.getYPos();
    batch.begin();
    for (int x = 0; x < platformGrid.tileGridWidth(); x++) {
      for (int y = 0; y < platformGrid.tileGridHeight(); y++) {
        Coordinate platformPixelPos = TilePositionInPixels.getTilePosInPixels(x, y, TILE_SIZE);
        switch(platformGrid.get(x, y)) {
          case 1:
            drawPlatformTexture(platformTexture, platformPixelPos);
          break;
          case 5:
            drawPlatformTexture(skullsTexture, platformPixelPos);
          break;
          case 7:
            drawPlatformTexture(barrelTexture, platformPixelPos);
          break;
          default:
          continue;
        }
      }
    }
    batch.end();
  }
  
  private void drawPlatformTexture(Texture texture, Coordinate coordinate) {
    batch.draw(texture, coordinate.x(), yPos + coordinate.y(), TILE_SIZE, TILE_SIZE);
  }
  
  @Override
  public void resize(int width, int height) {
    screenRect.width = width;
    screenRect.height = height;
  }
  
  @Override
  public void pause() {
    // Not implemented
  }
  
  @Override
  public void resume() {
    // Not implemented
  }
  
  @Override
  public void hide() {
    // Not implemented
  }
  
  @Override
  public void dispose() {
    shapeRenderer.dispose(); 
  }
  
}
