package inf112.ppbros.model.entity;

import java.util.Comparator;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Represents the player in the game.
 * <p>
 * The {@code PlayerModel} handles player-specific attributes such as movement,
 * jumping, attacking, collision with platforms, and animation states.
 * <p>
 * It extends {@link AbstractEntity} and provides player-specific logic on top of
 * shared entity functionality.
 */
public class PlayerModel extends AbstractEntity {
    private float velocityY = 0;
    private TextureRegion playerTextureRight;
    private TextureRegion playerTextureLeft;
    private Animation<TextureRegion> playerRunAnimR;
    private Animation<TextureRegion> playerRunAnimL;
    private Animation<TextureRegion> playerAttackAnimR;
    private Animation<TextureRegion> playerAttackAnimL;
    private boolean facesLeft = false;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private final float GRAVITY = -20f;
    private final float MAX_FALL_SPEED = -550f;
    private final float JUMP_VELOCITY = 900f;
    private boolean isOnGround = false;

    /**
     * Constructs a new player instance at the given starting position.
     * 
     * @param startX the initial horizontal position of the player.
     * @param startY the initial vertical position of the player.
     */
    public PlayerModel(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.health = 100;
        this.speed = 250.0f;
        this.attackRange = 140;
        this.attackDmg = 20;
        this.width = 0;
        this.height = 0;
        this.collisionBox = new Rectangle(x, y, width, height);
    }

    /**
     * Determines whether the player can attack the specified enemy.
     * <p>
     * A valid attack requires that the enemy is within both horizontal and vertical
     * attack range and is not positioned below the player.
     * @param enemy the enemy entity to check.
     * @return {@code true} if enemy is within attack range, {code false} otherwise.
     */
    public boolean canAttack(Entity enemy) {
        Rectangle enemyBox = enemy.getCollisionBox();
        float horizontalDistance = Math.abs(enemyBox.x - x);
        float verticalDistance = Math.abs(enemyBox.y - y);
        boolean isNotBelow = y >= enemyBox.y;
        return horizontalDistance <= attackRange && verticalDistance <= attackRange && isNotBelow;
    }

    /**
     * Updates the player's vertical position and collision state.
     * <p>
     * Applies gravity if the player is not on the ground, and checks for collisions
     * with nearby platform hitboxes. Adjusts the player's position once collisions
     * occur to not let player go through platform.
     * @param deltaTime         time elapsed since the last update
     * @param platformHitboxes  list of platform rectangles to check collisions against
     */
    public void update(float deltaTime, List<Rectangle> platformHitboxes) {
        // Apply gravity
        velocityY += GRAVITY;
        velocityY = Math.max(velocityY, MAX_FALL_SPEED);
    
        // Predict next Y position
        float newY = y + velocityY * deltaTime;
    
        // Move collision box to predicted position
        collisionBox.setPosition(x, newY);
        isOnGround = false;
    
        for (Rectangle platform : platformHitboxes) {
            if (collisionBox.overlaps(platform)) {
                float platformTop = platform.y + platform.height;
                float platformBottom = platform.y;
    
                if (velocityY < 0) {
                    // Falling - landed on platform
                    newY = platformTop;
                    velocityY = 0;
                    isOnGround = true;
                } else if (velocityY > 0) {
                    // Jumping - hit bottom of platform
                    newY = platformBottom - collisionBox.height;
                    velocityY = 0;
                }
    
                collisionBox.setPosition(x, newY);
                break; // Stop after resolving one collision
            }
        }
    
        // Apply resolved position
        y = newY;
    }


    /**
     * Causes the player to jump if they are currently on the ground.
     * <p>
     * Sets the vertical velocity to a jump speed and disables ground status.
     */
    public void jump() {
        if (isOnGround) {
            velocityY = JUMP_VELOCITY;
            isOnGround = false;
        }
    }

    /**
     * Loads and initializes textures and animations for the player.
     * <p>
     * This includes idle, running, and attack animations for both
     * left- and right-facing directions.
     */
    public void loadAnimations() {
        playerTextureRight = new TextureRegion(new Texture(Gdx.files.internal("entity/player/player_r.png")));
        playerTextureLeft = new TextureRegion(new Texture(Gdx.files.internal("entity/player/player_l.png")));

        Array<TextureRegion> runFramesRight = new Array<>();
        Array<TextureRegion> runFramesLeft = new Array<>();
        Array<TextureRegion> attackFramesRight = new Array<>();
        Array<TextureRegion> attackFramesLeft = new Array<>();

        for (int i = 1; i <= 3; i++) {
            runFramesRight.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/movement/run_r" + i + ".png"))));
            runFramesLeft.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/movement/run_l" + i + ".png"))));
        }
        for (int i = 1; i <= 4; i++) {
            attackFramesRight.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/attack/r" + i + ".png"))));
            attackFramesLeft.add(new TextureRegion(new Texture(Gdx.files.internal("entity/player/attack/l" + i + ".png"))));
        }

        playerRunAnimR = new Animation<>(0.1f, runFramesRight, Animation.PlayMode.LOOP);
        playerRunAnimL = new Animation<>(0.1f, runFramesLeft, Animation.PlayMode.LOOP);
        playerAttackAnimR = new Animation<>(0.1f, attackFramesRight, Animation.PlayMode.LOOP);
        playerAttackAnimL = new Animation<>(0.1f, attackFramesLeft, Animation.PlayMode.LOOP);

        currentFrame = playerTextureRight;
        setSize(playerTextureRight.getRegionWidth() / 3, playerTextureRight.getRegionHeight() / 3);
    }

    /**
     * Sets whether the player is currently moving.
     *
     * @param moving {@code true} if the player is moving; {@code false} otherwise
     */
    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    /**
     * Sets whether the player is currently attacking.
     *
     * @param attacking {@code true} if the player is attacking; {@code false} otherwise
     */
    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    /**
     * Sets the direction the player is facing.
     *
     * @param facesLeft {@code true} if the player should face left; {@code false} to face right
     */
    public void setFacesLeft(boolean facesLeft) {
        this.facesLeft = facesLeft;
    }

    /**
     * Checks if the player is currently facing left.
     *
     * @return {@code true} if the player is facing left; {@code false} otherwise
     */
    public boolean facesLeft() {
        return facesLeft;
    }

    @Override
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
    
    @Override
    public EntityType getType() {
        return EntityType.MAIN_CHARACTER;
    }

}
