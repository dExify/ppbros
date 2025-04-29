package inf112.ppbros.model.entity;

import java.util.Comparator;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.ppbros.controller.AudioController;

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
    private AudioController audioController;
    private final float GRAVITY = -20f;
    private final float MAX_FALL_SPEED = -550f;
    private final float JUMP_VELOCITY = 900f;
    private boolean isOnGround = false;

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

    public boolean canAttack(Entity enemy) {
        if (!(enemy instanceof EnemyModel)) return false;
        Rectangle enemyBox = enemy.getCollisionBox();
        float horizontalDistance = Math.abs(enemyBox.x - x);
        float verticalDistance = Math.abs(enemyBox.y - y);
        boolean isNotBelow = y >= enemyBox.y;
        return horizontalDistance <= attackRange && verticalDistance <= attackRange && isNotBelow;
    }

    public void update(float deltaTime, List<Rectangle> platformHitboxes) {
        platformHitboxes.sort(Comparator.comparingDouble(platform -> Math.abs(platform.y - this.getY())));
        
        if (!isOnGround) {
            velocityY += GRAVITY;
            velocityY = Math.max(velocityY, MAX_FALL_SPEED);
        }

        y += velocityY * deltaTime;
        collisionBox.setPosition(x, y);
        isOnGround = false;

        for (Rectangle platform : platformHitboxes) {
            if (collisionBox.overlaps(platform)) {
                float platformTop = platform.y + platform.height;
                y = platformTop;
                isOnGround = true;
                velocityY = 0;
                collisionBox.setPosition(x, y);
            }
        }
    }

    public void jump() {
        if (isOnGround) {
            velocityY = JUMP_VELOCITY;
            isOnGround = false;
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.MAIN_CHARACTER;
    }

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

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    public void setFacesLeft(boolean facesLeft) {
        this.facesLeft = facesLeft;
    }

    public boolean facesLeft() {
        return facesLeft;
    }
}
