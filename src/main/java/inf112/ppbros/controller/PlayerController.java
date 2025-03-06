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

public class PlayerController extends InputAdapter implements Screen{
    Stage stage;
    SpriteBatch batch;
    Texture player;
    Texture brick;
    float Speed = 50.0f; // each time moved, moves 50 pixels

    // map size (should probably be moved somewhere else later)
    final float MAP_WIDTH = 480;
    final float MAP_HEIGHT = 320;
    final float ATTACK_RANGE = 10; // change if attack range can be changed during game

    // starting positions
    float playerx = 0;
    float playery = 0;
    float brickx = 100;
    float bricky = 100;

    boolean wasOutside = false;
    // collision rectangles
    Rectangle player_rect;
    Rectangle brick_rect;

    // previous positions
    float prevx;
    float prevy;

    @Override
    public void show() {

        player = new Texture(Gdx.files.internal("character.png"));
        brick = new Texture(Gdx.files.internal("brick.png"));
        player_rect = new Rectangle(playerx, playery, player.getWidth(), player.getHeight());
        brick_rect = new Rectangle(brickx, bricky, brick.getWidth(), brick.getHeight());
        prevx = 0;
        prevy = 0;

        stage = new Stage();

        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1, 1, 0);
        ScreenUtils.clear(1,1, 1, 0); // to avoid flickering

        // draw player on screen
        batch.begin();
            stage.draw();
            batch.draw(player,playerx,playery);
            batch.draw(brick, brickx, bricky);
        batch.end();

        // check for collision
        if (brick_rect.overlaps(player_rect)) {
            System.out.println("collided");
            playery = prevy;
            playerx = prevx;
        }

        // check for input
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            System.out.println("D");
            prevx = playerx;
            playerx += Gdx.graphics.getDeltaTime()*Speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            System.out.println("A");
            prevx = playerx;
            playerx -= Gdx.graphics.getDeltaTime()*Speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            System.out.println("W");
            prevy = playery;
            playery += Gdx.graphics.getDeltaTime()*Speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            System.out.println("S");
            prevy = playery;
            playery -= Gdx.graphics.getDeltaTime()*Speed;
        }

        player_rect = new Rectangle(playerx, playery, player.getWidth(), player.getHeight());
        brick_rect = new Rectangle(brickx, bricky, brick.getWidth(), brick.getHeight()); // only really needed if the object can be moved

        
        //check if player touchess edge of map boundries
        if (playerx < 0 || playerx + player.getWidth() > MAP_WIDTH ||
            playery < 0 || playery + player.getHeight() > MAP_HEIGHT){
                System.out.println("Player touched the edge of map!"); // Change later
            }
        
        
        // Get player width
        float playerWidth = player.getWidth();
        // Check if completely outside on the left or right
        boolean isNowOutside = (playerx + playerWidth < 0) || (playerx > MAP_WIDTH);

        if (isNowOutside && !wasOutside) {
            System.out.println("Player went out of map!"); // change later
        }
        wasOutside = isNowOutside; // Update state

        // Check for attack (F key)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (canPlayerReach(brick_rect)) {
                System.out.println("Hit registered!"); // change later
            } else {
                System.out.println("No hit, too far away."); 
            }
        }

        // close program with escape button
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { 
			Gdx.app.exit();
		}
        
    }

    // Function to check if the player is close enough to the enemy
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
    
        // Check if the smallest gap is within the attack range
        return (horizontalDistance <= ATTACK_RANGE && verticalDistance <= ATTACK_RANGE);
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    
    }

    @Override
    public void dispose() {
       
    }
}