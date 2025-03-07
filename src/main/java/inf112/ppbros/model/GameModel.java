package inf112.ppbros.model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import inf112.ppbros.view.ScreenView;

public class GameModel extends Game {
    private static int brickSize = 100;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer mapRenderer;

    @Override
    public void create() {
        this.setScreen(new ScreenView(this));
        // tiledMap = new TmxMapLoader().load("TileMap.tmx");
        // mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    // public OrthogonalTiledMapRenderer getMapRenderer() {
    //     return mapRenderer;
    // }
}
