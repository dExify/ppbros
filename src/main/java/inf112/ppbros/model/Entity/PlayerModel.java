package inf112.ppbros.model.Entity;

import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Platform.TileConfig;

public class PlayerModel implements Entity {
    private float x, y; // player position
    private int health;
    private final float speed; // remove final if player can change speed
    private final float attackRange;
    private final int attackDmg;
    private Rectangle hitbox;
    private float width;
    private float height;
    private float velocityY = 0;
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
        this.attackRange = 10;
        this.attackDmg = 20;
        this.width = 0;
        this.height = 0;
        this.hitbox = new Rectangle(x, y, width, height);
    }
    
    /**
    * Character looses health based on how much damage taken
    * and it is game over if player looses all health
    * @param damage damage character take
    */
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
    
    /**
    * Checks if player can attack given enemy
    * @param enemy enemy to attack
    * @return true if they can attack, false if they do not meet requirements
    */
    public boolean canAttack(Entity enemy) {
        if (!(enemy instanceof EnemyModel)) return false;
        Rectangle enemyBox = ((EnemyModel) enemy).getCollisionBox();
        
        float horizontalDistance = Math.abs(enemyBox.x - x);
        float verticalDistance = Math.abs(enemyBox.y - y);
        
        boolean isNotBelow = y >= enemyBox.y;
        
        return horizontalDistance <= attackRange && verticalDistance <= attackRange && isNotBelow;
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
    
                // Falling: landing on platform
                if (velocityY <= 0 && playerBottom >= platformTop - 5) {
                    float distance = playerBottom - platformTop;
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestCollision = platform;
                    }
                }
    
                // Jumping: hitting head on platform
                else if (velocityY > 0 && playerTop <= platformBottom + 5) {
                    float distance = platformBottom - playerTop;
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestCollision = platform;
                    }
                }
            }
        }
    
        if (bestCollision != null) {
            float platformTop = bestCollision.y + bestCollision.height;
            float platformBottom = bestCollision.y;
    
            if (velocityY <= 0) { // Landing
                y = platformTop;
                isOnGround = true;
            } else { // Hitting ceiling
                y = platformBottom - height;
            }
    
            velocityY = 0;
            hitbox.setPosition(x, y);
        }
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
    
}