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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GuideView implements Screen {
  
  @SuppressWarnings("unused")
  private Game game;
  private StartMenuView view;
  private Stage stage;
  private Skin skin;
  private Texture backgroundTexture;
  
  GuideView(Game game, StartMenuView startMenuView) {
    this.game = game;
    this.view = startMenuView;
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);
    skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    
    backgroundTexture = new Texture(Gdx.files.internal("background/menu_bg.png"));
    Image backgroundImage = new Image(backgroundTexture);
    backgroundImage.setFillParent(true);
    stage.addActor(backgroundImage);
    
    Table table = new Table();
    table.setFillParent(true);
    table.top().padTop(50); // Optional: move content downward a bit
    table.center();
    stage.addActor(table);
    
    
    Label instructions = new Label("""
    CONTROLS:
    • Move Right: D
    • Move Left: A
    • Jump: SPACE
    • Attack: F
    
    OBJECTIVE:
    Attack slimes and gain points! But don't fall below the map—
    your health is limited!
    For each slime killed, your health will slightly increase!
    Damage will slightly increase for every 5 points gathered
    
    Press ESC at any time to exit.
""", skin);
    
    instructions.setAlignment(Align.center);
    instructions.setWrap(true);
    
    table.add(instructions).center().width(500).padTop(100).row();
    TextButton backButton = new TextButton("Back", skin);
    backButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(view);
      }
    });
    
    // Add your button below
    table.add(backButton).center().width(150).height(50).padTop(20).row();
    
    
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
