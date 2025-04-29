package inf112.ppbros.model.entity;

import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import inf112.ppbros.controller.AudioController;


public class PlayerModel implements Entity {
  private float x;
  private float y;
  private int health;
  private final float speed; // remove final if player can change speed
  private final float attackRange;
  private final int attackDmg;
  private Rectangle hitbox;
  private float width;
  private float height;
  private float velocityY = 0;
  private TextureRegion playerTextureRight;
  private TextureRegion playerTextureLeft;
  private Animation<TextureRegion> playerRunAnimR;
  private Animation<TextureRegion> playerRunAnimL;
  private Animation<TextureRegion> playerAttackAnimR;
  private Animation<TextureRegion> playerAttackAnimL;
  private TextureRegion currentFrame;
  private boolean facesLeft = false;
  private boolean isMoving = false;
  private boolean isAttacking = false;
  private float animationTime = 0f;
  private AudioController audioController;
  
  private final float GRAVITY = -20f;
  private final float MAX_FALL_SPEED = -550f;
  private final float JUMP_VELOCITY = 900f;
  
  private boolean isOnGround = false;
  
  /**
  * A player model contains the attributes and functions for any controllable character in the game
  * @param startX start x position  for character
  * @param startY start y position for character
  */
  public PlayerModel(float startX, float startY) {
    this.x = startX;
    this.y = startY;
    // sets default values
    this.health = 100; 
    this.speed = 250.0f; // in pixels
    this.attackRange = 140;
    this.attackDmg = 20;
    this.width = 0;
    this.height = 0;
    this.hitbox = new Rectangle(x, y, width, height);
    this.audioController = new AudioController();
    loadAnimations();
  }
  
  
  /**
  * Checks if player can attack given enemy
  * @param enemy enemy to attack
  * @return true if they can attack, false if they do not meet requirements
  */
  public boolean canAttack(Entity enemy) {
    if (!(enemy instanceof EnemyModel)) return false; // change if different enemytypes exists
    Rectangle enemyBox = ((EnemyModel) enemy).getCollisionBox();
    
    float horizontalDistance = Math.abs(enemyBox.x - x);
    float verticalDistance = Math.abs(enemyBox.y - y);
    
    boolean isNotBelow = y >= enemyBox.y;
    
    return horizontalDistance <= attackRange && verticalDistance <= attackRange && isNotBelow;
  }
  
  @Override
  public void takeDamage(int damage) {
    if (health > damage) {
      health -= damage;
    } else {
      health = 0;
      // TODO: initiate GAMEE_OVER state/screen
    }
    if (health == 0) {
      // TODO: initiate GAMEE_OVER state/screen
    }
  }
  
  @Override
  public void setSize(float width, float height) {
    this.width = width;
    this.height = height;
    this.hitbox.setSize(width, height);
  }
  
  /**
  * Checks to see if player collides with another collision rectangle
  * @param other collision box to check if player collides with
  * @return true or false based on if they collide or not
  */
  public boolean collidesWith(Rectangle rectangle) {
    return hitbox.overlaps(rectangle);
  }
  
  public void update(float deltaTime, List<Rectangle> platformHitboxes) {
    platformHitboxes.sort(Comparator.comparingDouble(platform -> Math.abs(platform.y - this.getY())));
    // Apply gravity if not on the ground
    if (!isOnGround) {
      velocityY += GRAVITY;
      velocityY = Math.max(velocityY, MAX_FALL_SPEED);
    }
    
    // Apply vertical movement
    y += velocityY * deltaTime;
    
    // Update hitbox position
    hitbox.setPosition(x, y);
    
    isOnGround = false;
    
    Rectangle bestCollision = null;
    float bestDistance = Float.MAX_VALUE;
    
    for (Rectangle platform : platformHitboxes) {
      if (hitbox.overlaps(platform)) {
        float playerBottom = y;
        float playerTop = y + height;
        float platformTop = platform.y + platform.height;
        float platformBottom = platform.y;
        
        
        y = platformTop;
        isOnGround = true;
        velocityY = 0;
        hitbox.setPosition(x, y);
      }
      // OLD IMPLEMENTATION, NEW BUGGY IMPLEMENTATION BELOW
    }
    
    // // Falling: landing on platform
    // if (velocityY <= 0 && playerBottom >= platformTop - 5) {
    //     float distance = playerBottom - platformTop;
    //     if (distance < bestDistance) {
    //         bestDistance = distance;
    //         bestCollision = platform;
    //     }
    // }
    
    // // Jumping: hitting head on platform
    // else if (velocityY > 0 && playerTop <= platformBottom + 5) {
    //     float distance = platformBottom - playerTop;
    //     if (distance < bestDistance) {
    //         bestDistance = distance;
    //         bestCollision = platform;
    //             }
    //         }
    //     }
    // }
    
    // if (bestCollision != null) {
    //     float platformTop = bestCollision.y + bestCollision.height;
    //     float platformBottom = bestCollision.y;
    
    //     if (velocityY <= 0) { // Landing
    //         y = platformTop;
    //         isOnGround = true;
    //     } else { // Hitting ceiling
    //         y = platformBottom - height;
    //     }
    
    //     velocityY = 0;
    //     hitbox.setPosition(x, y);
    // }
  }
  
  
  
  
  
  
  public void jump() {
    if (isOnGround) {
      velocityY = JUMP_VELOCITY;
      isOnGround = false;
    }
  }
  
  @Override
  public void move(float dx, float dy) {
    x += dx;
    y += dy;
    hitbox.setPosition(x,y);
  }
  
  @Override
  public Rectangle getCollisionBox() {
    return hitbox;
  }
  
  @Override
  public EntityType getType() {
    return EntityType.MAIN_CHARACTER;
  }
  
  @Override
  public int getHealth() {
    return health;
  }
  
  @Override
  public float getX() {
    return x;
  }
  
  @Override
  public float getY() {
    return y;
  }
  
  public void setX(float x) {
    this.x = x;
  }
  
  public void setY(float y) {
    this.y = y;
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
  public int getAttackDmg() {
    return attackDmg;
  }
  
  @Override
  public float getHeight() {
    return height;
  }
  
  @Override
  public float getWidth() {
    return width;
  }
  
  public Rectangle getHitbox() {
    return hitbox;
  }
  
  public void loadAnimations() {
    playerTextureRight = new TextureRegion(new Texture(Gdx.files.internal("entity/player/player_r.png")));
    playerTextureLeft = new TextureRegion(new Texture(Gdx.files.internal("entity/player/player_l.png")));
    
    Array<TextureRegion> runFramesRight = new Array<>();
    for (int i = 1; i <= 3; i++) {
      runFramesRight.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/movement/run_r" + i + ".png"))));
    }
    Array<TextureRegion> runFramesLeft = new Array<>();
    for (int i = 1; i <= 3; i++) {
      runFramesLeft.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/movement/run_l" + i + ".png"))));
    }
    Array<TextureRegion> attackFramesRight = new Array<>();
    for (int i = 1; i <= 4; i++) {
      attackFramesRight.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/attack/r" + i + ".png"))));
    }
    Array<TextureRegion> attackFramesLeft = new Array<>();
    for (int i = 1; i <= 4; i++) {
      attackFramesLeft.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/attack/l" + i + ".png"))));
    }
    
    playerRunAnimR = new Animation<>(0.1f, runFramesRight, Animation.PlayMode.LOOP);
    playerRunAnimL = new Animation<>(0.1f, runFramesLeft, Animation.PlayMode.LOOP);
    playerAttackAnimR = new Animation<>(0.1f, attackFramesRight, Animation.PlayMode.LOOP);
    playerAttackAnimL = new Animation<>(0.1f, attackFramesLeft, Animation.PlayMode.LOOP);
    
    currentFrame = playerTextureRight;
    
    // Adjust player size according to idle texture
    setSize(playerTextureRight.getRegionWidth() / 3, playerTextureRight.getRegionHeight() / 3);
  }
  
  
  public void updateAnimation(float delta) {
    animationTime += delta;
    
    if (isAttacking) {
      currentFrame = facesLeft ? playerAttackAnimL.getKeyFrame(animationTime) : playerAttackAnimR.getKeyFrame(animationTime);
    } else if (!isMoving) {
      currentFrame = facesLeft ? playerTextureLeft : playerTextureRight;
    } else {
      currentFrame = facesLeft ? playerRunAnimL.getKeyFrame(animationTime) : playerRunAnimR.getKeyFrame(animationTime);
    }
  }
  
  public void setMoving(boolean moving) {
    this.isMoving = moving;
  }
  
  public void setAttacking(boolean attacking) {
    this.isAttacking = attacking;
  }
  
  public void setFacesLeft(boolean facesLeft) {
    this.facesLeft = facesLeft;
  }
  
  public TextureRegion getCurrentFrame() {
    return currentFrame;
  }
  
  public boolean facesLeft() {
    return facesLeft;
}

  
  
}