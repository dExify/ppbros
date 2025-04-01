package inf112.ppbros.model.Entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.badlogic.gdx.math.Rectangle;

public class PlayerModel implements Entity {
    private float x, y; // player position
    private int health;
    private final float speed; // remove final if player can change speed
    private final float attackRange;
    private final int attackDmg;
    private final Rectangle collisionBox;
    private float width, height;
    private Body playerBody;
    private World world;
    private Vec2 playerVector;

    /**
     * A player model contains the attributes and functions for any controllable character in the game
     * @param startX start x position  for character
     * @param startY start y position for character
     */
    public PlayerModel(float startX, float startY, World world) {
        x = startX;
        y = startY;
        // sets default values
        health = 100; 
        speed = 250.0f; // in pixels
        attackRange = 10;
        attackDmg = 20;
        width = 0;
        height = 0;
        collisionBox = new Rectangle(x, y, width, height);
        this.world = world;
        initialiseBody(x, y);
        playerVector = new Vec2(x, y);
    }

    public Body getPlayerBody() {
        return playerBody;
    }

    public void movePlayerRight() {
        Vec2 vector = playerBody.getPosition();
        playerBody.applyForceToCenter(new Vec2(speed, 0));
        move(10f, vector.y);
    }

    public void movePlayerLeft() {
        Vec2 vector = playerBody.getPosition();
        playerBody.applyForceToCenter(new Vec2(-speed, 0));
        move(-10.0f, vector.y);
    }

    public void movePlayerDown() {
        Vec2 vector = playerBody.getPosition();
        move(vector.x, vector.y);
    }

    private void initialiseBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);
        playerBody = world.createBody(bodyDef);

        // Create collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.0f, 1.0f);

        // Define fixture (properties of the shape)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f; // Weight
        fixtureDef.friction = 0.3f; // Sliding resistance
        fixtureDef.restitution = 0.0f; // Bounciness

        // Attach shape to the body
        playerBody.createFixture(fixtureDef);
        // shape.dispose();
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
        this.collisionBox.setSize(width, height);
    }

    /**
     * Checks to see if player collides with another collision rectangle
     * @param other collision box to check if player collides with
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
        if (!(enemy instanceof EnemyModel)) return false;
        Rectangle enemyBox = ((EnemyModel) enemy).getCollisionBox();
        
        float horizontalDistance = Math.abs(enemyBox.x - x);
        float verticalDistance = Math.abs(enemyBox.y - y);

        boolean isNotBelow = y >= enemyBox.y;

        return horizontalDistance <= attackRange && verticalDistance <= attackRange && isNotBelow;
    }

    @Override
    public void move(float dx, float dy) {
        // x += dx;
        // y += dy;
        playerVector = new Vec2(dx, dy);
        // collisionBox.setPosition(x,y);
    }
    
    public Vec2 getPlayerPos() {
        return playerVector;
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
    
}