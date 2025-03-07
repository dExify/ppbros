package inf112.ppbros.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.ppbros.view.GameView;

public class PlayerController extends InputAdapter implements Screen {
    private Stage stage;
    private SpriteBatch batch;
    private Texture player;
    private Texture brick;
    private GameView gameView;
    
    private static final float SPEED = 50.0f;
    private static final float MAP_WIDTH = 480;
    private static final float MAP_HEIGHT = 320;
    private static final float ATTACK_RANGE = 10;

    private float playerx = 50;
    private float playery = 0;
    private float brickx = 100;
    private float bricky = 100;

    private boolean wasOutside = false;

    private Rectangle player_rect;
    private Rectangle brick_rect;

    private float prevx;
    private float prevy;

    public PlayerController(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void show() {
        player = new Texture(Gdx.files.internal("character.png"));
        brick = new Texture(Gdx.files.internal("brick.png"));

        player_rect = new Rectangle(playerx, playery, player.getWidth(), player.getHeight());
        brick_rect = new Rectangle(brickx, bricky, brick.getWidth(), brick.getHeight());

        prevx = playerx;
        prevy = playery;

        stage = new Stage();
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();

        gameView.show();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 0);

        handleInput();
        checkCollisions();
        drawObjects();
        
        gameView.render(delta);
    }

    private void drawObjects() {
        batch.begin();
        stage.draw();
        batch.draw(player, playerx, playery);
        batch.draw(brick, brickx, bricky);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && canPlayerReach(brick_rect)) {
            System.out.println("Hit registered!");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            System.out.println("No hit");
        }

        prevx = playerx;
        prevy = playery;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) playerx += Gdx.graphics.getDeltaTime() * SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) playerx -= Gdx.graphics.getDeltaTime() * SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) playery += Gdx.graphics.getDeltaTime() * SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) playery -= Gdx.graphics.getDeltaTime() * SPEED;
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void checkCollisions() {
        player_rect.set(playerx, playery, player.getWidth(), player.getHeight());
        brick_rect.set(brickx, bricky, brick.getWidth(), brick.getHeight());

        if (playerx < 0 || playerx + player.getWidth() > MAP_WIDTH ||
            playery < 0 || playery + player.getHeight() > MAP_HEIGHT) {
            System.out.println("Player touched the edge of the map!");
        }

        boolean isNowOutside = (playerx + player.getWidth() < 0) || (playerx > MAP_WIDTH);
        if (isNowOutside && !wasOutside) {
            System.out.println("Player went out of map!");
        }
        wasOutside = isNowOutside;

        if (brick_rect.overlaps(player_rect)) {
            playerx = prevx;
            playery = prevy;
        }
    }

    private boolean canPlayerReach(Rectangle enemy) { // change type later when enemytype is made
        float playerWidth = player.getWidth();
        float playerHeight = player.getHeight();

        // Find the edges of the player
        float playerLeft = playerx;
        float playerRight = playerx + playerWidth;
        float playerTop = playery + playerHeight;
        float playerBottom = playery;

        // Find the edges of the enemy
        float enemyLeft = enemy.getX();
        float enemyRight = enemy.getX() + enemy.getWidth();
        float enemyTop = enemy.getY() + enemy.getHeight();
        float enemyBottom = enemy.getY();

        // Calculate the closest horizontal and vertical distances between player and brick
        float horizontalDistance = Math.max(0, Math.max(enemyLeft - playerRight, playerLeft - enemyRight));
        float verticalDistance = Math.max(0, Math.max(enemyBottom - playerTop, playerBottom - enemyTop));
    
        // Prevent hits from below (playerBottom must be above enemyBottom)
        boolean isNotBelow = playerBottom >= enemyBottom;
        
        // Check if the smallest gap is within the attack range and player is not hitting from below 
        return (horizontalDistance <= ATTACK_RANGE && verticalDistance <= ATTACK_RANGE && isNotBelow);
    }



    @Override
    public void resize(int width, int height) {
        gameView.resize(width, height);
    }

    @Override
    public void pause() {
        gameView.pause();
    }

    @Override
    public void resume() {
        gameView.resume();
    }

    @Override
    public void hide() {
        gameView.hide();
    }

    @Override
    public void dispose() {
        gameView.dispose();
        batch.dispose();
        player.dispose();
        brick.dispose();
        stage.dispose();
    }
}
