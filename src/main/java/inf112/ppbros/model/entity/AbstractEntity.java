package inf112.ppbros.model.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class AbstractEntity implements Entity {

    protected float x, y;
    protected float width, height;
    protected int health;
    protected float speed;
    protected float attackRange;
    protected int attackDmg;
    protected Rectangle collisionBox;
    protected TextureRegion currentFrame;
    protected float animationTime = 0f;

    public AbstractEntity() {
        // Empty constructor for subclasses
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
        collisionBox.setX(x);
    }

    public void setY(float y) {
        this.y = y;
        collisionBox.setY(y);
    }

    public void updateCollisionBox(float x, float y) {
        this.collisionBox.setPosition(x, y);
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

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public abstract void loadAnimations();
    
    public void updateAnimation(float delta) {
        animationTime += delta;
        // Let subclasses override if needed
    }
}
