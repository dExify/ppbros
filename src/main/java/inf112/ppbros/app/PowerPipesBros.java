package inf112.ppbros.app;

import com.badlogic.gdx.Game;
import inf112.ppbros.view.StartMenuView;

// Main Game Class
public class PowerPipesBros extends Game {
  @Override
  public void create() {
    this.setScreen(new StartMenuView(this));
  }
}
