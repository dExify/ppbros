package inf112.ppbros.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import inf112.ppbros.view.StartMenuView;

// Main Game Class
public class PowerPipesBros extends Game {
    private Screen pendingScreen; 

    @Override
    public void create() {
        this.setScreen(new StartMenuView(this));
    }

    @Override
    public void render(){
        super.render();

        // Handle pending screen transition
        if (pendingScreen != null) {
            if (getScreen() != null) {
                getScreen().dispose();
            }
            setScreen(pendingScreen);
            pendingScreen = null;
        }
    }

    /**
     * Transitions from current screen to another.
     * @param nextScreen next screen to show
     */
    public void transitionTo(Screen nextScreen) {
        this.pendingScreen = nextScreen;
    }
}
