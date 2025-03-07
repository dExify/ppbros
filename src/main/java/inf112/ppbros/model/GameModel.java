package inf112.ppbros.model;

import com.badlogic.gdx.Game;

import inf112.ppbros.view.ScreenView;

public class GameModel extends Game {

    @Override
    public void create() {
        this.setScreen(new ScreenView(this));
    } 



}
