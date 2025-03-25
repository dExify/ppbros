package inf112.ppbros.model.Entity;

import com.badlogic.gdx.math.Rectangle;

public class PlayerModel implements Entity {
    private float x, y; // starting positions
    private int health;
    private final float speed; // remove final if player can change speed
    private final float attackRange;
    private final int attackDmg;
    private final Rectangle collisionBox;
    private float width, height;

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
        this.speed = 70.0f; // in pixels
        this.attackRange = 10;
        this.attackDmg = 20;
        this.width = 0;
        this.height = 0;
        this.collisionBox = new Rectangle(x, y, width, height);
    }

    /**
     * Character looses health based on how much damage taken
     * and it is game over if player looses all health
     * @param damage damage character take
     */
    public void takeDamage(int damage) {
        if (health >= damage) {
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
        this.collisionBox.setSize(width, height);
    }

    /**
     * Checks to see if player collides with another collision rectangle
     * @param other collision box to check if player collides with
     * @return true or false based on if they collide or not
     */
    public boolean collidesWith(Rectangle other) {
        return collisionBox.overlaps(other);
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

    @Override
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
        collisionBox.setPosition(x,y);
    }
    
    @Override
    public Rectangle getCollisionBox() {
        return collisionBox;
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
    
}