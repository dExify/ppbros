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
  private Texture platformBlackTexture;
  private Texture barrelTexture;
  private Texture debuggingTexture;
  private Texture skullsTexture;
  private Texture sewer1;
  private Texture sewer2;
  private Texture sewer3;
  
  private PlayerModel player;

  private TextureRegion currentFrame;
  private float animationTime = 0;
  
  private Texture resizedEnemyTexture;
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
    platformTexture = new Texture(Gdx.files.internal("Platforms/GraystoneBrickTileWithOutline80.png"));
    skullsTexture = new Texture(Gdx.files.internal("Platforms/Skulls.png"));
    barrelTexture = new Texture(Gdx.files.internal("Platforms/Barrel.png"));
    debuggingTexture = new Texture(Gdx.files.internal("Red_X.png"));
    mapTexture = new Texture(Gdx.files.internal("SewerMap.png"));
    sewer1 = new Texture(Gdx.files.internal("sewer_1.png"));
    sewer2 = new Texture(Gdx.files.internal("sewer_2.png"));
    sewer3 = new Texture(Gdx.files.internal("sewer_3.png"));
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
    
    // update score and health bar
    scoreLabel.setText("Score: " + gameModel.getScore());
    healthLabel.setText("Health: " + player.getHealth());
    
    // camera movement
    //gameModel.stopTimer();
    camera.position.y = gameModel.getCameraYCoordinate();
    camera.update();
    
    // draw player and update controller for input
    //drawPlayerAttack();
    
    enemies = gameModel.getEnemies();
    
    // temp for texture
    drawEnemies(delta);
    gameModel.updateEnemiesPos(delta);
    
    playerController.update(delta);
    animationTime += delta;
    
    drawPlayer(delta);
    
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
    stage.draw();
    playerController.update(delta);
    gameModel.updatePlayer();
    
    if (gameModel.isOutOfBounds()) {
      //System.out.println("Player is out of bounds!");
    }
  }
  
  private void drawPlayer(float delta) {
    player.updateAnimation(delta);
    
    batch.begin();
    TextureRegion frame = player.getCurrentFrame();
    batch.draw(frame, player.getX(), player.getY(), frame.getRegionWidth() / 3, frame.getRegionHeight() / 3);
    batch.end();
    
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
    stage.draw();
    
    playerController.update(delta);
    
    
    // drawHitboxes(); //debugging
    // drawPlayerHitbox(); //debugging
    // drawEnemiesHitbox(); //debugging
    
    if (gameModel.isOutOfBounds()) {
      // System.out.println("Player is out of bounds!");
    }
  }
  
  // === Debugging Methods ===

  private void drawHitboxes() {
    batch.begin();
    for (Rectangle rec : gameModel.getPlatformHitboxes()) {
      batch.draw(debuggingTexture, rec.getX(), rec.getY(), TILE_SIZE, TILE_SIZE);
    }
    batch.end();
  }
  
  private void drawPlayerHitbox() {
    Rectangle playerCollisionBox = player.getCollisionBox();
    batch.begin();
    batch.draw(debuggingTexture, playerCollisionBox.getX(), playerCollisionBox.getY(), playerCollisionBox.getWidth(), playerCollisionBox.getHeight());
    batch.end();
  }

  private void drawEnemiesHitbox() {
    enemies = gameModel.getEnemies();
    batch.begin();
    for (EnemyModel enemy : enemies){
      batch.draw(debuggingTexture, enemy.getCollisionBox().getX(), enemy.getCollisionBox().getY(), enemy.getCollisionBox().getWidth(), enemy.getCollisionBox().getHeight());
      // System.out.println("Enemy at: " + enemy.getCollisionBox().getX() + ", " + enemy.getCollisionBox().getY());
      // System.out.println("Size: " + enemy.getCollisionBox().getWidth() + "x" + enemy.getCollisionBox().getHeight());
    }
    batch.end();
  }

  private void drawEnemies(float delta) {
    enemies = gameModel.getEnemies();
    batch.begin();
    
    for (EnemyModel enemy : enemies) {
      enemy.updateAnimation(delta); 
      TextureRegion frame = enemy.getCurrentFrame();
      batch.draw(frame, enemy.getX(), enemy.getY(), frame.getRegionWidth() / 3, frame.getRegionHeight() / 3);
    }
    
    batch.end();
  }

  private void drawPlayerAttack() {
    batch.begin();
    batch.draw(currentFrame, player.getX(), player.getY(), currentFrame.getRegionWidth()/3, currentFrame.getRegionHeight()/3);
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
    if (backgroundHeight < camera.position.y + Gdx.graphics.getHeight() / 2) {
      drawInfiniteBackground = true;
    }
  }
  
  private void drawInfiniteBackground() {
    // double backgroundHeight = Gdx.graphics.getWidth() * 0.56;
    double backgroundHeight = TileConfig.PLATFORM_GRIDHEIGHT_PIXELS; 
    Texture bg1 = sewer1;
    Texture bg2 = sewer2;
    batch.begin();
    batch.setColor(0.7F, 0.7F, 0.7F, 1F);
    batch.draw(bg1, 0, platformGridObject1.getYPos(), Gdx.graphics.getWidth(), (int) backgroundHeight);
    batch.draw(bg2, 0, platformGridObject2.getYPos(), Gdx.graphics.getWidth(), (int) backgroundHeight);
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
          batch.draw(platformTexture, platformPixelPos.x(), yPos + platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
          break;
          case 2:
          batch.draw(platformBlackTexture, platformPixelPos.x(), yPos + platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
          break;
          case 5:
          batch.draw(skullsTexture, platformPixelPos.x(), yPos + platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
          break;
          case 7:
          batch.draw(barrelTexture, platformPixelPos.x(), yPos + platformPixelPos.y(), TILE_SIZE, TILE_SIZE);
          break;
          case -1:
          //Placeholder X
          break;
          default:
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
