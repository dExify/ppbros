package inf112.ppbros.model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import inf112.ppbros.model.Platform.PlatformMaker;
import inf112.ppbros.view.ScreenView;

public class GameModel extends Game {
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer mapRenderer;
    PlatformGrid platformGrid;
    PlatformMaker platformMaker;

    @Override
    public void create() {
        this.setScreen(new ScreenView(this));
        getPlatformGrid();
    }

    public PlatformGrid getPlatformGrid() {
        platformMaker = new PlatformMaker();
        platformGrid = new PlatformGrid(platformMaker);
        platformGrid.buildGrid();
        platformGrid.buildGrid();
        platformGrid.printArray(); //Debugging
        return platformGrid;
    }

    /**
     * Returns the tile background
     * @return OrthogonalTiledMapRenderer
     */
    public OrthogonalTiledMapRenderer getMapRenderer() {
        TiledMap tiledMap = new TmxMapLoader().load("TileMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        return mapRenderer;
    }
}
