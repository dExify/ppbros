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

        // close program with escape button
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { 
			Gdx.app.exit();
		}
        
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