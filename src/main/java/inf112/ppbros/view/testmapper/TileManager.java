package inf112.ppbros.view.testmapper;

import inf112.ppbros.view.testmapper.Tile;

public class TileManager {

    GamePanel gp;
    Tile[] tile;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];

        getTileImage();
    }

    @Override
	public void create() {
		// Called at startup

		batch = new SpriteBatch();

    public void getTileImage(){

    }
    
}
