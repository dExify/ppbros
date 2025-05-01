package inf112.ppbros.model.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * An abstract base class that defines common behavior and functionality
 * shared by all entity types in the system.
 * <p>
 * This class provides default implementations for methods that are
 * common across entities.
 */
public abstract class AbstractEntity implements Entity {
    protected float x, y;
    protected float width, height;
    protected int health;
    protected float speed;
    protected float attackRange;
    protected int attackDmg;
    protected Rectangle collisionBox;
    protected static TextureRegion currentFrame;
    protected float animationTime = 0f;

    public AbstractEntity() {
        // Empty constructor for subclasses
    }

    /**
     * Set new x value and update collision box.
     * @param x new x value.
     */
    public void setX(float x) {
        this.x = x;
        collisionBox.setX(x);
    }

    /**
     * Set new y value and update collision box.
     * @param y new y value.
     */
    public void setY(float y) {
        this.y = y;
        collisionBox.setY(y);
    }

    /**
     * Update collision box with new x and y values.
     * @param x new x value.
     * @param y new y value.
     */
    public void updateCollisionBox(float x, float y) {
        this.collisionBox.setPosition(x, y);
    }

    @Override
    public boolean collidesWith(Rectangle rectangle) {
        return collisionBox.overlaps(rectangle);
    }

    /**
     * Get the current frame.
     * @return A TextureRegion as the current frame. 
     */
    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }
    
    /**
     * Update animation time with delta time
     * @param delta time elapsed since last frame update 
     */
    public void updateAnimation(float delta) {
        animationTime += delta;
        // Let subclasses override if needed
    }

    @Override
    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
        collisionBox.setPosition(x, y);
    }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        collisionBox.setSize(width, height);
    }

    @Override
    public void takeDamage(int damage) {
        if (health > damage) {
            health -= damage;
        } else {
            health = 0;
            // Optionally: trigger death behavior in subclass
        }
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

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public Rectangle getCollisionBox() {
        return collisionBox;
    }
}
