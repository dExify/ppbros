package inf112.ppbros.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class GameView implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;

    public GameView(Game game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("clean-crispy-ui.json")); // Placeholderskin til vi er ferdig med å lage vårt eget

        Table healthTable = new Table();
        Table scoreTable = new Table();
        healthTable.top().left();
        scoreTable.top().right();
        scoreTable.setFillParent(true);
        healthTable.setFillParent(true);
        stage.addActor(healthTable);
        stage.addActor(scoreTable);

        Label scoreLabel = new Label("Score: 0", skin);
        Label healthLabel = new Label("Health: 100", skin);
        
        scoreTable.add(scoreLabel).pad(10);
        healthTable.add(healthLabel).pad(10);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() { stage.dispose(); skin.dispose(); }
}
