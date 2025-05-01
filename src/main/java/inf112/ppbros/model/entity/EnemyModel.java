package inf112.ppbros.model.entity;

import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.ppbros.model.Coordinate;
import inf112.ppbros.model.platform.TileConfig;

/**
 * Represents an enemy in the game world.
 * <p>
 * The {@code EnemyModel} class defines the behavior, movement logic, and animations
 * of a basic enemy. It handles patrolling behavior when the player is far and pathfinding
 * behavior when the player is within a certain vertical range. Enemies can also load and
 * update their animations and change direction when hitting platform edges or obstacles.
 * <p>
 * This class extends {@link AbstractEntity}, inheriting shared entity properties
 * such as position, health, and collision handling.
 */
public class EnemyModel extends AbstractEntity {
    private boolean movingLeft = true;
    private boolean movingRight = false;
    private static Animation<TextureRegion> enemyRunAnimR;
    private static Animation<TextureRegion> enemyRunAnimL;

    /**
     * Creates a new enemy instance at a specified starting position
     * @param startPos  the initial starting position
     * @param yPos      the vertical offset to be added to the initial y position
     */
    public EnemyModel(Coordinate startPos, int yPos) {
        this.x = startPos.x();
        this.y = startPos.y() + yPos;
        this.health = 100;
        this.speed = 50.0f;
        this.attackRange = 0;
        this.attackDmg = 10;
        this.width = 0;
        this.height = 0;
        this.collisionBox = new Rectangle(x, y, width, height);
    }

    /**
     * Updates the enemy's movement based on the player's position.
     * <p>
     * If the player is within vertical range, the enemy will path towards the player.
     * Otherwise it will continue patrolling the platform moving left and right switching 
     * direction once at the end of platform or collision detected.
     * @param player    the player to path towards
     * @param hitboxes  list of platform hitboxes
     * @param deltaTime time elapsed since last frame update 
     */
    public void updateMovement(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
        if ((player.getY() >= this.y - TileConfig.TILE_SIZE * 2) && (player.getY() <= this.y + TileConfig.TILE_SIZE * 2) && (player.getX() >= this.x - TileConfig.TILE_SIZE * 3) && (player.getX() <= this.x + TileConfig.TILE_SIZE * 3)) {
            pathTowardsPlayer(player, hitboxes, deltaTime);
        } else {
            patrolPlatform(hitboxes, deltaTime);
        }
    }

    private void patrolPlatform(List<Rectangle> hitboxes, float deltaTime) {
        moveEnemy(hitboxes, deltaTime);
    }

    private void pathTowardsPlayer(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
        if ((player.getX() < x && !movingLeft) || (player.getX() > x && !movingRight)) {
            changeDirection();
        }
        moveEnemy(hitboxes, deltaTime);
    }

    private void moveEnemy(List<Rectangle> hitboxes, float deltaTime) {
        float prevX = x;
        int direction = movingLeft ? -1 : 1;

        if (!hasTileBelow(hitboxes)) {
            changeDirection();
            return;
        }

        move(direction * speed * deltaTime, 0);

        if (platformCollision(hitboxes)) {
            x = prevX;
            collisionBox.setPosition(x, y);
            changeDirection();
        }
    }

    private boolean hasTileBelow(List<Rectangle> hitboxes) {
        float checkX = movingLeft ? x - width : x + width;
        float checkY = y - height;
        Rectangle checkBox = new Rectangle(checkX, checkY, width, height / 2);
        for (Rectangle rec : hitboxes) {
            if (checkBox.overlaps(rec)) {
                return true;
            }
        }
        return false;
    }

    private boolean platformCollision(List<Rectangle> hitboxes) {
        for (Rectangle rec : hitboxes) {
            if (collisionBox.overlaps(rec)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Loads and initializes the enemy's running animation frames.
     */
    public static void loadAnimations() {
        Array<TextureRegion> runFramesRight = new Array<>();
        Array<TextureRegion> runFramesLeft = new Array<>();

        for (int i = 0; i <= 22; i++) {
            String filenameR = String.format("idle%02dr.png", i);
            String filenameL = String.format("idle%02dl.png", i);
            runFramesRight.add(new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/idle/" + filenameR))));
            runFramesLeft.add(new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/idle/" + filenameL))));
        }

        enemyRunAnimR = new Animation<>(0.1f, runFramesRight, Animation.PlayMode.LOOP);
        enemyRunAnimL = new Animation<>(0.1f, runFramesLeft, Animation.PlayMode.LOOP);
    }

    /**
     * Reverse the enemy's horizontal movement direction.
     * <p>
     * Used when the enemy hits a wall or edge of a platform. 
     */
    public void changeDirection() {
        movingLeft = !movingLeft;
        movingRight = !movingRight;
    }

    /**
     * Checks if enemy is currently facing left.
     * @return true if the enemy is facing left, false otherwise.
     */
    public boolean facesLeft() {
        return movingLeft;
    }

    @Override
    public void updateAnimation(float delta) {
        animationTime += delta;
        currentFrame = movingLeft ? enemyRunAnimL.getKeyFrame(animationTime) : enemyRunAnimR.getKeyFrame(animationTime);
    }

    @Override
    public EntityType getType() {
        return EntityType.ENEMY;
    }
}
