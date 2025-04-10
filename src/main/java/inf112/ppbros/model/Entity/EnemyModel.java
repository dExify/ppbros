package inf112.ppbros.model.entity;

import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Coordinate;

public class EnemyModel implements Entity {
    private float x, y;
    private int health;
    private final float speed;
    private final float attackRange;
    private final int attackDmg;
    private float width, height;
    private Rectangle collisionBox;

    /**
     * An enemy model contains the attributes and functions for enemies in the game
     * @param startX start x position for enemy
     * @param startY start y position for enemy
     */
    public EnemyModel(Coordinate startPos, int yPos) {
        this.x = startPos.x();
        this.y = startPos.y() + yPos;
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
    
    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.collisionBox.setSize(width, height);
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
    
}
