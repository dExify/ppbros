package inf112.ppbros.model.entity;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.platform.TileConfig;

public class EnemyModel implements Entity {
  private float x, y;
  private int health;
  private final float speed;
  private final float attackRange;
  private final int attackDmg;
  private float width;
  private float height;
  private float moveSpeed = 50.0f; // Default move speed
  private Rectangle collisionBox;
  // Regarding movement:
  private boolean movingLeft = true; // Start moving left by default
  private boolean movingRight = false;
  private TextureRegion enemyIdleTextureRight;
  private TextureRegion enemyIdleTextureLeft;
  private Animation<TextureRegion> enemyRunAnimR;
  private Animation<TextureRegion> enemyRunAnimL;
  private Animation<TextureRegion> enemyAttackAnimR;
  private Animation<TextureRegion> enemyAttackAnimL;
  private TextureRegion currentFrame;
  private boolean isAttacking = false;
  private boolean isMoving = false;
  private boolean facesLeft = true;
  private float animationTime = 0f;
  
  
  /**
  * An enemy model contains the attributes and functions for enemies in the game
  * @param startPos start x and y position for enemy
  * @param yPos start grid-iteration for enemy
  */
  public EnemyModel(Coordinate startPos, int yPos) {
    this.x = startPos.x();
    this.y = startPos.y() + (float) yPos;
    this.health = 100;
    this.speed = 50.0f;
    this.width = 0;
    this.height = 0;
    this.collisionBox = new Rectangle(x, y, width, height);
    this.attackRange = 0;
    this.attackDmg = 10;
    loadAnimations();
  }
  
  /**
  * Update collision box with new x and y values
  * @param x new x value
  * @param y new y value
  */
  public void updateCollisionBox(float x, float y) {
    this.collisionBox.setPosition(x, y);
  }
  
  @Override
  public void takeDamage(int damage) {
    if (health > damage) {
      health -= damage;
    } else {
      health = 0;
      // TODO: enemy dies and has to be removed
    }
    if (health == 0) {
      // TODO: enemy dies and has to be removed
    }
  }
  
  @Override
  public void move(float dx, float dy) {
    x += dx;
    y += dy;
    collisionBox.setPosition(x, y);
  }
  
  public void setX(float x) {
    this.x = x;
  }
  
  public void setY(float y) {
    this.y = y;
  }
  
  @Override
  public void setSize(float width, float height) {
    this.width = width;
    this.height = height;
    this.collisionBox.setSize(width, height);
  }
  
  // if platforms has patterns with elevation we should make method to check for collision
  // and check if they can go up or down (e.g. depending on if there is a platform instance x amount of height below)
  
  /**
  * Updates the enemy's movement based on platform and player position.
  * @param player The player model to path towards.
  * @param hitboxes The list of hitboxes to check for collisions.
  * @param deltaTime The time since the last frame.
  */
  public void updateMovement(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    if ((player.getY() >= this.y - TileConfig.TILE_SIZE * 2) && (player.getY() <= this.y + TileConfig.TILE_SIZE * 2)) {
      pathTowardsPlayer(player, hitboxes, deltaTime); // Start chasing the player
    } else {
      patrolPlatform(player, hitboxes, deltaTime); // Start patroling the platform
    }
  }
  
  /**
  * Patrols the platform by moving left or right and jumping/falling if needed.
  */
  private void patrolPlatform(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    this.moveSpeed = 50.0f;
    moveEnemy(player, hitboxes, deltaTime);
  }
  
  /**
  * Paths towards the player by moving horizontally (and jumping/falling if needed.)
  * @param player The player model to path towards.
  */
  private void pathTowardsPlayer(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    this.moveSpeed = 75.0f; // Increase speed when chasing player
    if (player.getX() < x) {
      if (movingLeft) {
        moveEnemy(player, hitboxes, deltaTime);
      } else {
        changeDirection();
        moveEnemy(player, hitboxes, deltaTime);
      }
    } else {
      if (movingRight) {
        moveEnemy(player, hitboxes, deltaTime);
      } else {
        changeDirection();
        moveEnemy(player, hitboxes, deltaTime);
      }
    }
  }
  
  /**
  * Moves the enemy left or right based on its current direction.
  * If there's a gap below, it changes direction.
  * If there's a platform collision, it reverts the immediate movement, it changes direction.
  * @param player
  * @param hitboxes
  * @param deltaTime
  */
  private void moveEnemy(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    float prevX = x;
    float prevY = y;
    
    int direction = movingLeft ? -1 : 1;
    
    // Check for gaps
    if (!hasTileBelow(hitboxes)) {
      changeDirection();
      return; // Stop movement if there's a gap
    }
    
    // Move the enemy
    move(direction * moveSpeed * deltaTime, 0);
    
    if (platformCollision(hitboxes)) {
      x = prevX;
      y = prevY;
      changeDirection();
      // System.out.println("Collision detected, changing direction.");
    }
  }
  
  // Check if there is a tile in the direction the enemy is moving
  private boolean hasTileInDirection(List<Rectangle> hitboxes) {
    float checkX = movingLeft ? x - width : x + width;
    float checkY = y;
    Rectangle checkBox = new Rectangle(checkX, checkY, width, height);
    for (Rectangle rec : hitboxes) {
      if (checkBox.overlaps(rec)) {
        return true;
      }
    }
    return false;
  }
  
  // Check if there is a tile below the enemy in the direction it is moving
  private boolean hasTileBelow(List<Rectangle> hitboxes) {
    float checkX = movingLeft ? x - width / 2 : x + width / 2;
    float checkY = y - height;
    Rectangle checkBox = new Rectangle(checkX, checkY, width, height / 2); // Adjust height for better accuracy
    for (Rectangle rec : hitboxes) {
      if (checkBox.overlaps(rec)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean platformCollision(List<Rectangle> hitboxes) {
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
  
  @Override
  public int getHealth() {
    return health;
  }
  
  @Override
  public Rectangle getCollisionBox() {
    return collisionBox;
  }
  
  @Override
  public float getX() {
    return x;
  }
  
  @Override
  public float getY() {
    return y;
  }
  
  @Override
  public float getSpeed() {
    return speed;
  }
  
  @Override
  public float getAttackRange() {
    return attackRange;
  }
  
  @Override
  public float getHeight() {
    return height;
  }
  
  @Override
  public float getWidth() {
    return width;
  }
  
  @Override
  public int getAttackDmg() {
    return attackDmg;
  }
  
  public boolean facesLeft() {
    return movingLeft;
  }
  
  public void changeDirection() {
    if (movingLeft) {
      movingLeft = false;
      movingRight = true;
    } else {
      movingLeft = true;
      movingRight = false;
    }
  }
  public void loadAnimations() {
    // enemyIdleTextureRight = new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/enemy_idle_r.png")));
    // enemyIdleTextureLeft = new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/enemy_idle_l.png")));
    
    Array<TextureRegion> runFramesRight = new Array<>();
    for (int i = 0; i <= 9; i++) {
      runFramesRight.add(new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/patrol/jump_0" + i + "r.png"))));
    }
    Array<TextureRegion> runFramesLeft = new Array<>();
    for (int i = 0; i <= 9; i++) {
      runFramesLeft.add(new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/patrol/jump_0" + i + ".png"))));
    }
    enemyRunAnimR = new Animation<>(0.1f, runFramesRight, Animation.PlayMode.LOOP);
    enemyRunAnimL = new Animation<>(0.1f, runFramesLeft, Animation.PlayMode.LOOP);
    
    currentFrame = enemyIdleTextureRight;
    
  }
  public void updateAnimation(float delta) {
    animationTime += delta;
    currentFrame = movingLeft ? enemyRunAnimL.getKeyFrame(animationTime) : enemyRunAnimR.getKeyFrame(animationTime);
  }
  
  public TextureRegion getCurrentFrame() {
    return currentFrame;
  }
  
  
  
  
}
