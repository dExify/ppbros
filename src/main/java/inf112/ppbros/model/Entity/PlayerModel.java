package inf112.ppbros.model.Entity;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

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
    private final float MAX_FALL_SPEED = -150f;
    private final float JUMP_VELOCITY = 700f;

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
    }

    /**
     * Checks to see if player collides with another collision rectangle
     * @param rectangle collision box to check if player collides with
     * @return true or false based on if they collide or not
     */
    public boolean collidesWith(Rectangle rectangle) {
        return collisionBox.overlaps(rectangle);
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
        // Apply gravity
        if (!isOnGround) {
            velocityY += GRAVITY;
            velocityY = Math.max(velocityY, MAX_FALL_SPEED);
        }

        // Apply velocity
        y += velocityY * deltaTime;

        // Update hitbox
        hitbox.setPosition(x, y);

        // Check for collisions
        isOnGround = false;
        for (Rectangle platform : platformHitboxes) {
            if (hitbox.overlaps(platform)) {
                // Snap the player on top of platform
                y = platform.y + platform.height;
                velocityY = 0;
                isOnGround = true;
                hitbox.setPosition(x, y);
                break;
            }
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