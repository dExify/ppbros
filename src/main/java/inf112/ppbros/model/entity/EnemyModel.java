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

public class EnemyModel extends AbstractEntity {
    private boolean movingLeft = true;
    private List<Rectangle> hitboxes;
    private float deltaTime;
    private float playerX;
    private float playerY;
    private TextureRegion slimeTexture;
    private static Animation<TextureRegion> enemyRunAnimR;
    private static Animation<TextureRegion> enemyRunAnimL;

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
        this.slimeTexture = new TextureRegion(new Texture(Gdx.files.internal("entity/enemy/slime/slime_idle.png")));
        setSize(slimeTexture.getRegionWidth() / 3, slimeTexture.getRegionHeight() / 3);
    }

    /**
     * Updates the enemy movement based on the player's position and the hitboxes of the platforms.
     * If the player is within range, the enemy will move towards the player. If not, it will patrol its platform.
     * @param player the player model
     * @param hitboxes the list of hitboxes for the platforms
     * @param deltaTime the time since the last frame.
     * This is used to ensure that the enemy moves at a consistent speed, regardless of the frame rate.
     */
    public void updateMovement(PlayerModel player, List<Rectangle> hitboxes, float deltaTime) {
        this.hitboxes = hitboxes;
        this.deltaTime = deltaTime;
        this.playerX = player.getX();
        this.playerY = player.getY();

        if (playerInRange()) {
            pathTowardsPlayer(player);
        } else {
            patrolPlatform();
        }
    }

    private boolean playerInRange() {
        return (playerY >= y - TileConfig.TILE_SIZE * 2) && (playerY <= y + TileConfig.TILE_SIZE * 2) && 
                (playerX >= x - TileConfig.TILE_SIZE * 3) && (playerX <= x + TileConfig.TILE_SIZE * 3);
    }

    private void patrolPlatform() {
        moveEnemy();
    }

    private void pathTowardsPlayer(PlayerModel player) {
        if ((player.getX() < x && !movingLeft) || (player.getX() > x && movingLeft)) {
            changeDirection();
        }
        moveEnemy();
    }

    private void moveEnemy() {
        float prevX = x;
        int direction = movingLeft ? -1 : 1;

        if (!hasTileBelow()) {
            changeDirection();
            return;
        }

        move(direction * speed * deltaTime, 0);

        if (platformCollision()) {
            x = prevX;
            collisionBox.setPosition(x, y);
            changeDirection();
        }
    }

    private boolean hasTileBelow() {
        float checkX = movingLeft ? x - width : x + width;
        float checkY = y - height;
        Rectangle checkBox = new Rectangle(checkX, checkY, width, height);
        for (Rectangle rec : hitboxes) {
            if (checkBox.overlaps(rec)) {   
                return true;
            }
        }
        return false;
    }

    private boolean platformCollision() {
        for (Rectangle rec : hitboxes) {
            if (collisionBox.overlaps(rec)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EntityType getType() {
        return EntityType.ENEMY;
    }

    /**
     * Loads the enemy animations from the specified directory.
     * The animations are loaded into static variables for later use.
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

    @Override
    public void updateAnimation(float delta) {
        animationTime += delta;
        currentFrame = movingLeft ? enemyRunAnimL.getKeyFrame(animationTime) : enemyRunAnimR.getKeyFrame(animationTime);
    }

    private void changeDirection() {
        movingLeft = !movingLeft;
    }

}
