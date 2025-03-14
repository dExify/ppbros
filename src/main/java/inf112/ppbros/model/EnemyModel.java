package inf112.ppbros.model;

import com.badlogic.gdx.math.Rectangle;

public class EnemyModel implements Entity {
    private float x, y;
    private int health;
    private final float speed;
    private final float attackRange;
    private final Rectangle collisionBox;
    private final int attackDmg;

    /**
     * An enemy model contains the attributes and functions for enemies in the game
     * @param startX start x position for enemy
     * @param startY start y position for enemy
     */
    public EnemyModel(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.health = 100;
        this.speed = 50.0f;
        this.collisionBox = new Rectangle(x, y, getWidth(), getHeight());
        this.attackRange = 0;
        this.attackDmg = 10;
    }

    /**
     * Enemy looses health based on how much damage taken 
     * @param damage damage enemy takes
     */
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
        collisionBox.setPosition(x,y);
    }

    // if platforms has patterns with elevation we should make method to check for collision
    // and check if they can go up or down (e.g. depending on if there is a platform instance x amount of height below)

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
    public int getHeight() {
        return getHeight();
    }

    @Override
    public int getWidth() {
        return getWidth();
    }

    @Override
    public int getAttackDmg() {
        return attackDmg;
    }
    
}
