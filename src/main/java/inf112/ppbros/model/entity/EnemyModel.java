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
    private boolean movingRight = false;
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
    }

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

    @Override
    public EntityType getType() {
        return EntityType.ENEMY;
    }

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

    public void changeDirection() {
        movingLeft = !movingLeft;
        movingRight = !movingRight;
    }

    public boolean facesLeft() {
        return movingLeft;
    }
}
