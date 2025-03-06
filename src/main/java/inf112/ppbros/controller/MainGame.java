package inf112.ppbros.controller;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
public class MainGame extends Game{
 
    public Screen PlayerController, playableCharacter;

    
    @Override
    public void create() {
        PlayerController = new PlayerController();
        this.setScreen(PlayerController);
    }
    
}
