package inf112.ppbros.model.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.platform.PlatformGrid;
import inf112.ppbros.model.platform.TileConfig;

public class EnemyModel implements Entity {
  private float x, y;
  private int health;
  private final float speed;
  private final float attackRange;
  private final int attackDmg;
  private float width;
  private float height;
  private Rectangle collisionBox;
  // Regarding movement:
  private boolean chasingPlayer = false; // Start not chasing player
  private boolean movingLeft = true; // Start moving left by default
  private boolean movingRight = false;
  private boolean isJumping = false;
  private final float fallSpeed = 50.0f; // Speed for falling
  private final float jumpHeight = 50.0f; // Maximum height for a jump
  private final float moveSpeed = 5.0f; // Speed for horizontal movement
  
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
   * @param grid The platform grid the enemy is on.
   * @param player The player model to path towards.
   */
  public void updateMovement(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    // if (player.getY() >= this.y) {
        // pathTowardsPlayer(player, hitboxes, deltaTime); // Start chasing the player
    // } else {
    // }
    patrolPlatform(player, hitboxes, deltaTime); // Start patroling the platform
    
  }

  /**
   * Patrols the platform by moving left or right and jumping/falling if needed.
   * @param grid The platform grid the enemy is on.
   */
  private void patrolPlatform(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    this.chasingPlayer = false;
    moveEnemy(player, hitboxes, deltaTime);

    // // Handle falling or jumping
    // if (!isOnSolidGround(grid, tileX, tileY)) {
    //     move(0, -fallSpeed * Gdx.graphics.getDeltaTime());
    // }
  }

  /**
   * Paths towards the player by moving horizontally (and jumping/falling if needed.)
   * @param grid The platform grid the enemy is on.
   * @param player The player model to path towards.
   */
  private void pathTowardsPlayer(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    this.chasingPlayer = true;
    if (player.getX() < x) {
      if (isMovingLeft()) {
        moveEnemy(player, hitboxes, deltaTime);
      }
    } else if (player.getX() > x) {
      if (isMovingRight()) {
        changeDirection();
        moveEnemy(player, hitboxes, deltaTime);
      }
    } else {
      // Add funcitonality for jumping
    }
  }

  private void moveEnemy(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
    float prevX = x;
    float prevY = y;
    if (movingLeft) {
      deltaTime = -deltaTime;
    }
    move(deltaTime * moveSpeed,  0 * moveSpeed); // Move the enemy in the direction of the player
    if (platformCollision(hitboxes)) {
      x = prevX;
      y = prevY;
      changeDirection();
    }
    if (!hasTile(hitboxes)) {
      x = prevX;
      y = prevY;
      changeDirection();
    }
  }

  private boolean platformCollision(List<Rectangle> hitboxes) {
    for (Rectangle rec : hitboxes) {
      if (collisionBox.overlaps(rec)) {
        return true;
      }
    }
    return false;
  }

  private boolean hasTile(List<Rectangle> hitboxes) {
    float checkX = x;
    float checkY = y;
    if (movingLeft) {
      move(checkX - width, checkY - height);
      if (platformCollision(hitboxes)) {
        return true;
      }
      return false;
    }
    move(checkX + width, checkY - height);
    if (platformCollision(hitboxes)) {
      return true;
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

  public boolean isMovingLeft() {
    return movingLeft;
  }


  public boolean isMovingRight() {
    return movingRight;
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

  public boolean isChasingPlayer() {
    return chasingPlayer;
  }
  
}
