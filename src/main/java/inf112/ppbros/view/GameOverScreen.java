package inf112.ppbros.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import inf112.ppbros.app.PowerPipesBros;
import inf112.ppbros.model.GameModel;

/**
 * The {@code GameOverScreen} class represents the screen that appears
 * when the game has ended. It displays a "Game Over" message, the player's score,
 * and provides UI buttons to either retry the game or exit the application.
 * <p>
 * This screen is implemented using libGDX's Scene2D UI framework and includes:
 * <ul>
 *   <li>A label indicating the game is over</li>
 *   <li>The final score from the {@link GameModel}</li>
 *   <li>A "Retry" button to restart the game</li>
 *   <li>An "Exit" button to close the application</li>
 * </ul>
 *
 * This screen is designed to be shown after the player loses or finishes the game.
 */
public class GameOverScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private Texture backgroundTexture;
    
    /**
     * Constructs the game over with a title, score count, retry button, and exit button.
     * @param game game that just ended.
     * */
    public GameOverScreen(GameModel gameModel) {
        final GameModel game = gameModel;
        //this.game = game; // creates new game logic as creating a new ScreenView does not
        this.stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/uiskin.json")); 
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("background/gameover_bg.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label gameOverLabel = new Label("Game Over", skin);
        Label scoreLabel = new Label("Score: "+ game.getScore(), skin);
        gameOverLabel.setFontScale(2);
        scoreLabel.setFontScale(1.5f);
        TextButton retryButton = new TextButton("Retry", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((PowerPipesBros) Gdx.app.getApplicationListener()).transitionTo(new ScreenView(new GameModel(true, true))); // restarts game
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(gameOverLabel).padBottom(10).row();
        table.add(scoreLabel).padBottom(20).row();
        table.add(retryButton).padBottom(10).row();
        table.add(exitButton).padBottom(10).row();
        stage.addActor(table);
        
    }

    @Override
    public void show() {
      // Not used
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1,true);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { 
      // Not used
      }

    @Override
    public void resume() {
      // Not used
    }
    @Override
    public void hide() {
      // Not used
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}
