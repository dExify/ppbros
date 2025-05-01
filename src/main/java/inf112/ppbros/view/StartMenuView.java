package inf112.ppbros.view;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import inf112.ppbros.model.GameModel;

/**
 * The StartMenuView displays the game's main menu with options to start the game or exit.
 * It is the initial screen presented to the player on game launch.
 */
public class StartMenuView implements Screen {
  @SuppressWarnings("unused")
  private Game game;
  private Stage stage;
  private Skin skin;
  private Texture backgroundTexture;
  
  /**
   * Constructs the start menu with a title, start button, and exit button.
   * 
   * @param game The main game application instance used to transition screens.
   */
  public StartMenuView(Game game) {
    this.game = game;
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage); // Needed to activate usage of buttons
    skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
  
    backgroundTexture = new Texture(Gdx.files.internal("background/menu_bg.png"));
    Image backgroundImage = new Image(backgroundTexture);
    backgroundImage.setFillParent(true); // Cover full screen
    stage.addActor(backgroundImage);

    Table table = new Table();
    table.setFillParent(true);
    stage.addActor(table);

    Label title = new Label("Power Pipes Bros", skin);
    TextButton startButton = new TextButton("Start", skin);
    TextButton guideButton = new TextButton("How to play", skin);
    TextButton exitButton = new TextButton("Exit", skin);
    
    startButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        GameModel model = new GameModel(true, true);
        game.setScreen(new ScreenView(model));
      }
    });
    
    guideButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent even, float x, float y) {
        game.setScreen(new GuideView(game, StartMenuView.this));
      }
    });

    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.app.exit();
      }
    });
    
    table.add(title).padBottom(20).row();
    table.add(startButton).padBottom(10).row();
    table.add(guideButton).padBottom(10).row();
    table.add(exitButton).padBottom(10).row();
    
  }
  
  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage); // Needed to activate usage of buttons
  }
  
  @Override
  public void render(float delta) {
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
    stage.draw();
  }
  
  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }
  
  @Override
  public void pause() {
    // Not implemented
  }
  @Override
  public void resume() {
    // Not implemented
  }
  @Override
  public void hide() {
    // Not implemented
  }
  @Override
  public void dispose() { 
    stage.dispose(); 
    skin.dispose(); 
    backgroundTexture.dispose();

  }
}