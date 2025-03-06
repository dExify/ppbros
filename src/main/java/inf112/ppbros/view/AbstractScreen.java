package inf112.ppbros.view;

import com.badlogic.gdx.Screen;
import inf112.ppbros.app.PPBros;

public class AbstractScreen implements Screen {

    private PPBros game;
    

    public void AbstracScreen(PPBros game) {
        this.game = game;
    }

    @Override
    public void show() {
       
    }

    @Override
    public void render(float delta) {
        
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


    public PPBros getGame() {
        return game;
    }
    
}
