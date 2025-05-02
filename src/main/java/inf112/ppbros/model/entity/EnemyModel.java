package inf112.ppbros.model.entity;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.platform.TileConfig;

/**
* Represents an enemy in the game world.
* <p>
* The {@code EnemyModel} class defines the behavior, movement logic, and animations
* of a basic enemy. It handles patrolling behavior when the player is far and pathfinding
* behavior when the player is within a certain vertical range. Enemies can also load and
* update their animations and change direction when hitting platform edges or obstacles.
* <p>
* This class extends {@link AbstractEntity}, inheriting shared entity properties
* such as position, health, and collision handling.
*/
public class EnemyModel extends AbstractEntity {
  private boolean movingLeft = true;
  private Random random;
  private List<Rectangle> hitboxes;
  private float deltaTime;
  private float playerX;
  private float playerY;
  private static Animation<TextureRegion> enemyRunAnimR;
  private static Animation<TextureRegion> enemyRunAnimL;
  private static final float SIZE_RATIO = 3f;
  
  /**
  * Creates a new enemy instance at a specified starting position
  * @param startPos  the initial starting position
  * @param yPos      the vertical offset to be added to the initial y position
  */
  public EnemyModel(Coordinate startPos, int yPos) {
    this.x = startPos.x();
    this.y = startPos.y() + (float) yPos;
    this.health = 100;
    this.speed = 50.0f;
    this.attackRange = 0;
    this.attackDmg = 10;
    this.width = 0;
    this.height = 0;
    this.random = new Random();
    this.movingLeft = random.nextBoolean();
    this.collisionBox = new Rectangle(x, y, width, height);
  }
  
  /**
  * Updates the enemy's movement based on the player's position.
  * <p>
  * If the player is within vertical range, the enemy will path towards the player.
  * Otherwise it will continue patrolling the platform moving left and right switching 
  * direction once at the end of platform or collision detected.
  * @param player    the player to path towards
  * @param hitboxes  list of platform hitboxes
  * @param deltaTime time elapsed since last frame update 
  */
  public void updateMovement(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    this.hitboxes = hitboxes;
    this.deltaTime = deltaTime;
    this.playerX = player.getX();
    this.playerY = player.getY();
    
    if (playerInRange()) {
      pathTowardsPlayer(player);
    } else {
      patrolPlatform();
    }
  }
  
  
  
  private boolean playerInRange() {
    return (playerY >= y - TileConfig.TILE_SIZE * 2) && (playerY <= y + TileConfig.TILE_SIZE * 2) && 
    (playerX >= x - TileConfig.TILE_SIZE * 3) && (playerX <= x + TileConfig.TILE_SIZE * 3);
  }
  
  private void patrolPlatform() {
    moveEnemy();
  }
  
  private void pathTowardsPlayer(PlayerModel player) {
    if ((player.getX() < x && !movingLeft) || (player.getX() > x && movingLeft)) {
      changeDirection();
    }
    moveEnemy();
  }
  
  private void moveEnemy() {
    float prevX = x;
    int direction = movingLeft ? -1 : 1;
    
    if (!hasTileBelow()) {
      changeDirection();
      return;
    }
    
    move(direction * speed * deltaTime, 0);
    
    if (platformCollision()) {
      x = prevX;
      collisionBox.setPosition(x, y);
      changeDirection();
    }
  }
  
  private boolean hasTileBelow() {
    float checkX = movingLeft ? x - width : x + width;
    float checkY = y - height;
    Rectangle checkBox = new Rectangle(checkX, checkY, width, height);
    for (Rectangle rec : hitboxes) {
      if (checkBox.overlaps(rec)) {   
        return true;
      }
    }
    return false;
  }
  
  
  private boolean platformCollision() {
    for (Rectangle rec : hitboxes) {
      if (collisionBox.overlaps(rec)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public EntityType getType() {
    return EntityType.ENEMY;
  }
  
  /**
  * Loads the enemy animations from the specified directory.
  * The animations are loaded into static variables for later use.
  */
  /**
  * Loads the enemy animations from the specified directory.
  * The animations are loaded into static variables for later use.
  */
  public static void loadAnimations() {
    Array<TextureRegion> runFramesRight = new Array<>();
    Array<TextureRegion> runFramesLeft = new Array<>();
    
    for (int i = 0; i <= 22; i++) {
      String filenameR = String.format("idle%02dr.png", i);
      String filenameL = String.format("idle%02dl.png", i);
      runFramesRight.add(new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/idle/" + filenameR))));
      runFramesLeft.add(new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/idle/" + filenameL))));
    }
    
    enemyRunAnimR = new Animation<>(0.1f, runFramesRight, Animation.PlayMode.LOOP);
    enemyRunAnimL = new Animation<>(0.1f, runFramesLeft, Animation.PlayMode.LOOP); 
    enemyRunAnimL = new Animation<>(0.1f, runFramesLeft, Animation.PlayMode.LOOP); 
  }
  
  /**
  * Reverse the enemy's horizontal movement direction.
  * <p>
  * Used when the enemy hits a wall or edge of a platform. 
  */
  public void changeDirection() {
    movingLeft = !movingLeft;
  }
  
  /**
  * Checks if enemy is currently facing left.
  * @return true if the enemy is facing left, false otherwise.
  */
  public boolean facesLeft() {
    return movingLeft;
  }
  
  @Override
  public void updateAnimation(float delta) {
    animationTime += delta;
    currentFrame = movingLeft ? enemyRunAnimL.getKeyFrame(animationTime) : enemyRunAnimR.getKeyFrame(animationTime);
  }
  
  public void initViewSize() {
    TextureRegion slimeTexture;
    slimeTexture = new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/slime_idle.png")));
    setSize(slimeTexture.getRegionWidth() / SIZE_RATIO, slimeTexture.getRegionHeight() / SIZE_RATIO);
    
  }
}
