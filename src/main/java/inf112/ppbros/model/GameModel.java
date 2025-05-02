package inf112.ppbros.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.controller.AudioController;
import inf112.ppbros.model.entity.EnemyModel;
import inf112.ppbros.model.entity.PlayerModel;
import inf112.ppbros.model.platform.PlatformGrid;
import inf112.ppbros.model.platform.PlatformGridMaker;
import inf112.ppbros.model.platform.TileConfig;
import inf112.ppbros.view.ScreenView;
import inf112.ppbros.view.StartMenuView;
import inf112.ppbros.view.TilePositionInPixels;

/**
 * The {@code GameModel} class represents the core logic and state of the game.
 * It manages the player, enemy entities, platform generation, collisions,
 * score tracking, camera movement, and overall game progression.
 * <p>
 * This class also handles initialization of the game view, entity updates,
 * and timer-based camera control. It is responsible for ensuring gameplay
 * state updates are handled properly per frame.
 */
public class GameModel extends Game {
  private boolean createView;
  private AudioController audioController;
  private PlayerModel player;
  private int score;
  private List<EnemyModel> enemies;
  private int enemyAmount = 5; // Number of enemies to spawn on each platform grid
  private int cameraPos;
  private Timer timer;
  private CameraYPos timerTask;
  private long lastExecution = 0;
  private PlatformGridMaker platformGridMaker;
  private List<Rectangle> platformHitboxes;
  private List<Rectangle> enemyHitboxes;

  private boolean showPowerUpMessage = false;
  private float messageTimer = 0f;
  private static final float MESSAGE_DURATION = 1.55f;
  private static final int OUT_OF_BOUNDS_DAMAGE = 10;

  
  private static final long COOLDOWNTIME = 1000; // 1 second, can be changed
  private static final int TILESIZE = TileConfig.TILE_SIZE;
  
  /**
   * Constructs the GameModel. Optionally initializes the game view and assets.
   *
   * @param createsView Whether to create and show the view immediately.
   */
  public GameModel(boolean createsView, boolean loadAudio) {
    this.createView = createsView;
    if (createView) { // To allow unit tests
      this.setScreen(new StartMenuView(this));
      EnemyModel.loadAnimations();
      // Load animations a little after initializing game, avoids placing loading in contructors which interferes with tests
    }
    if (loadAudio) { // To allow unit tests
      this.audioController = new AudioController();
    } else {
      this.audioController = null;
    }
    this.score = 0;
    this.cameraPos = 0;
    this.platformGridMaker = new PlatformGridMaker();
    enemies = new ArrayList<>();
    this.timer = new Timer();
    this.timerTask = new CameraYPos();
    this.platformHitboxes = new ArrayList<>();
    this.enemyHitboxes = new ArrayList<>();
  
    
    // Sets player start position
    makePlayer(0, 150);
  }
  
  @Override
  public void create() {
    this.setScreen(new ScreenView(this));
  }
  
  /**
  * Returns the player instance.
  * @return the player.
  */
  public PlayerModel getPlayer() {
    return player;
  }
  
  /**
  * Returns the current list of enemies.
  * @return list of enemies. 
  */
  public List<EnemyModel> getEnemies() {
    return enemies;
  }
  
  /**
  * Returns the current score.
  * @return the score.
  */
  public int getScore(){
    return score;
  }
  
  /**
  * Increments the score by 1.
  */
  public void addToScore(){
    audioController.playSoundEffect("pointAcquired");
    score ++;
    onEnemyKilled();
  }
  
  /**
  * Creates an instance of player at given start position
  * @param startX start x value
  * @param startY start y value
  */
  public void makePlayer(int startX, int startY) {
    this.player = new PlayerModel(startX, startY);
  }
  
  /** Moves player based on its speed.
  * Checks for collision with platforms and enemies.
  * Collision with platforms puts the player back to previous position.
  * @param deltaX horizontal movement
  * @param deltaY vertical movement
  */
  public void movePlayer(float dx, float dy) {
    float prevX = player.getX();
    float prevY = player.getY();

    player.move(dx * player.getSpeed(), dy * player.getSpeed());

    if (collisionCheck(platformHitboxes)) {
        player.setX(prevX);
        player.setY(prevY);
    }
    collisionWithAnyEnemy();
  }

  /**
   * Returns the enemies the player can currently attack
   * @return {@code List<EnemyModel>} of attackable enemies
   */
  public List<EnemyModel> attackableEnemies() {
    List<EnemyModel> attackableEnemies = new ArrayList<>();
    for (EnemyModel enemy : enemies){
      if (player.canAttack(enemy)) {
        attackableEnemies.add(enemy);
      }
    }
    return attackableEnemies;
  }

    /**
   * Returns an enemy the player can currently attack
   * @return attackable enemy or {@code null}
   */
  public EnemyModel attackableEnemy() {
    for (EnemyModel enemy : enemies){ // if mutiple enemies on same platform, this will not work well
      if (player.canAttack(enemy)) {
        return enemy;
      }
    }
    return null;
  }

  
  /**
   * Player attacks enemy and enemy takes damage.
   * When enemy no longer has more health the score increments by 1.
   * @param enemy enemy that is hit
   */
  public void playerAttacksEnemy(EnemyModel enemy) {
    enemy.takeDamage(player.getAttackDmg());
    if (enemy.getHealth() == 0) {
      addToScore();
    }
  }

  private void onEnemyKilled() {
    player.getHpOnKill();
    if (score % 5 == 0) {
      player.gainPowerUp();
      messageTimer = 0f;
      showPowerUpMessage = true;
    }
  }
  

  /**
   * Returns the current state of the power-up message.
   * @return {@code true} if the power-up message should be shown, {@code false} otherwise.
   */
  public boolean shouldShowPowerUpMessage() {
    return showPowerUpMessage;
  }

  /**
   * Updates the message timer for the power-up message.
   * If the message duration is exceeded, the message is hidden.
   * @param deltaTime the time since the last frame
   */
  public void updateMessageTimer(float deltaTime) {
    if (showPowerUpMessage) {
      messageTimer += deltaTime;
      if (messageTimer >= MESSAGE_DURATION) {
        showPowerUpMessage = false;
        messageTimer = 0f;
      }
    }
  }
  
  /**
  * Player is hit by enemy and takes damage.
  * Has a cooldown to prevent player from continously taking damage.
  * When player no longe has more health they die/its game over.
  */
  public void playerIsHit(int damage) {
    long now = System.currentTimeMillis();
    if (now - lastExecution >= COOLDOWNTIME){
      lastExecution = now;
      audioController.playSoundEffect("takeDamage");
      player.takeDamage(damage); 
      if (player.getHealth() == 0) {
        audioController.playSoundEffect("gameOver");
      }
    }
  }
  
  /**
   * When player is out of bounds they take damage.
   */
  public void checkOutOfBounds() {
    if (isOutOfBounds()) {
      playerIsHit(OUT_OF_BOUNDS_DAMAGE); // Alternativly, execute instantly game over screen
    }
  }

  /**
  * Checks if player is out of bounds left, right and bottom of screeen.
  * @return {@code true} if player is out of bounds, {@code false} if they are within bounds
  */
  protected boolean isOutOfBounds() {
    float playerX = player.getX();
    float playerY = player.getY();
    return (playerX + player.getWidth() < 0) || (playerX > Gdx.graphics.getWidth() || 
    playerY + player.getHeight() < getCameraYCoordinate() - Gdx.graphics.getHeight()/2);
  }
  
  
  /**
   * Checks if player collides with an array containing collision boxes as rectangles.
   * @param collisionBox an array of collision boxes to check collision with
   * @return {@code true} if player collides with a rectangle, {@code false} if they don't
   */
  private boolean collisionCheck(List<Rectangle> collisionBox) {
    for (Rectangle rec : collisionBox) {
      if (player.collidesWith(rec)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the player is colliding with any spawned enemy.
   * At collision player takes a hit.
   * @return true if player collides
   */
  private boolean collisionWithAnyEnemy() {
    for (EnemyModel e : enemies) {
        if (player.collidesWith(e.getCollisionBox())){
          playerIsHit(e.getAttackDmg());
          return true;
        }
    }
    return false;
  }
  
  /**
  * Builds a platform grid and returns the platformGrid object and corresponding enemy on grid.
  * @return PlatformGrid with enemies
  */
  public PlatformGrid getNextPlatformGrid() {
    PlatformGrid platformGrid;
    platformGrid = platformGridMaker.getNextPlatformGrid();
    for (int i = 0; i < enemyAmount; i++) {
      updateEnemies(platformGrid);
    }
    platformHitboxes.addAll(platformGrid.getHitboxes());
    return platformGrid;
  }
  
  /**
  * Create new enemy based on platform grid.
  * Converts enemy's position from tiles to pixels.
  * Updates and adds collision box for enemy.
  * Adds enemy to ArrayList of enemies.
  * @param platformGrid grid to check valid position for enemy
  */
  private void updateEnemies(PlatformGrid platformGrid) {
    EnemyModel newEnemy = getNext(platformGrid);
    // Convert enemy positions from tiles to pixels 
    Coordinate enemyPosInPixels = TilePositionInPixels.getTilePosInPixels((int)newEnemy.getX(), (int)newEnemy.getY(), TILESIZE);
    // Update and add collisionbox to a list of all enemy collision boxes 
    newEnemy.updateCollisionBox(enemyPosInPixels.x(), enemyPosInPixels.y());
    newEnemy.setX(enemyPosInPixels.x());
    newEnemy.setY(enemyPosInPixels.y());
    enemyHitboxes.add(newEnemy.getCollisionBox());
    
    enemies.add(newEnemy);
  }
  
  /**
  * Returns an integer that represents the x coordiante of the viewport
  * @return camera Y coordinate
  */
  public int getCameraYCoordinate() {
    cameraPos = timerTask.getCameraPos();
    return cameraPos;
  }
  
  /**
  * Start timer with fixed rate execution
  */
  public void startTimer() {
    timer.scheduleAtFixedRate(timerTask, 0, 25);
  }
  
  /**
  * Stop camera movement.
  */
  public void stopTimer() {
    timerTask.cancel();
  }
  
  @Override
  public void dispose() {
    timer.cancel();
  }
  /**
   * Returns a list of all platform collision hitboxes.
   *
   * @return list of platform hitboxes
   */
  public List<Rectangle> getPlatformHitboxes() {
    return platformHitboxes;
  }

  /**
   * Returns a list of all enemy collision hitboxes.
   *
   * @return list of enemy hitboxes
   */
  public List<Rectangle> getEnemyHitboxes() {
    return enemyHitboxes;
  }

  /**
   * Triggers the player's jump action.
   */
  public void jump() {
    player.jump();
  }

  /**
   * Updates the player’s position and animation state.
   */
  public void updatePlayer() {
    player.update(Gdx.graphics.getDeltaTime(), platformHitboxes);
  }

  /**
   * Updates the enemies' positions based on the player's position and the platform hitboxes.
   * This method is called every frame to ensure that the enemies move towards the player, and move correctly.
   * Also checks for collision with player.
   * @param deltaTime The time since the last frame, used to update the enemies' positions.
   * This is used to ensure that the enemies move at a consistent speed, regardless of the frame rate.
   */
  public void updateEnemiesPos(float deltaTime) {
    for (Iterator<EnemyModel> it = enemies.iterator(); it.hasNext();) {
      EnemyModel enemy = it.next();

      if (enemy.getHealth() <= 0) {
        it.remove();
        continue;
      }

      enemy.updateMovement(player, platformHitboxes, deltaTime);

      collisionWithAnyEnemy();
    }

  }

  /**
   * Loads animations for the player character.
   */
  public void loadPlayerAnimations() {
    player.loadAnimations();
  }

  /**
   * Returns a new enemy, in a valid position on the platform grid
  * @param grid the platform grid to check for valid spawn coordinates
  * @return EnemyModel
  */
  private EnemyModel getNext(PlatformGrid grid) {
    Coordinate spawnPos = grid.getValidEnemySpawnPos();
    EnemyModel enemy = new EnemyModel(spawnPos, (grid.getYPos() / TileConfig.PLATFORM_GRIDHEIGHT_PIXELS) * TileConfig.GRID_HEIGHT);
    return enemy;
  }

}
