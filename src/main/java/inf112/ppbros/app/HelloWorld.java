package inf112.ppbros.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;


import com.badlogic.gdx.graphics.g2d.TextureRegion; // to use only parts of an image


public class HelloWorld implements ApplicationListener {
	private SpriteBatch batch;
	private BitmapFont font;
	private Texture spriteImage;
	private Texture spriteImage2;

	
	private Texture background;
	private Texture characterPoses;
	private TextureRegion character;
	private Sound jumpSound;
	private float speed = 50.0f;

	private Sound bellSound;
	private Rectangle spriteRect;
	private Rectangle screenRect = new Rectangle();
	private float dx = 1, dy = 1;

	@Override
	public void create() {
		// Called at startup

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.PINK);
		spriteImage = new Texture(Gdx.files.internal("obligator.png"));
		spriteImage2 = new Texture(Gdx.files.internal("ubligator.png"));
		background = new Texture(Gdx.files.internal("sky.png"));
		spriteRect = new Rectangle(1, 1, spriteImage.getWidth() / 2, spriteImage.getHeight() / 2);
		characterPoses = new Texture(Gdx.files.internal("character.png"));
		character = new TextureRegion(characterPoses,0,0, 50,50);
		bellSound = Gdx.audio.newSound(Gdx.files.internal(	"slash.ogg"));
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		Gdx.graphics.setForegroundFPS(60);
		Gdx.graphics.setWindowedMode(320, 256); // set window to the size of background
	}

	@Override
	public void dispose() {
		// Called at shutdown

		// Graphics and sound resources aren't managed by Java's garbage collector, so
		// they must generally be disposed of manually when no longer needed. But,
		// any remaining resources are typically cleaned up automatically when the
		// application exits, so these aren't strictly necessary here.
		// (We might need to do something like this when loading a new game level in
		// a large game, for instance, or if the user switches to another application
		// temporarily (e.g., incoming phone call on a phone, or something).
		batch.dispose();
		font.dispose();
		spriteImage.dispose();
		bellSound.dispose();
	}

	@Override
	public void render() {
		// Called when the application should draw a new frame (many times per second).

		// This is a minimal example – don't write your application this way!

		// Start with a blank screen
		//ScreenUtils.clear(Color.WHITE);

		// Draw calls should be wrapped in batch.begin() ... batch.end()
		batch.begin();
		//font.draw(batch, "Sample group's game", 10, Gdx.graphics.getHeight() -10);
		//batch.draw(spriteImage2, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);
		batch.draw(background, 0, 0);
		batch.draw(character,spriteRect.x,spriteRect.y, spriteRect.width, spriteRect.height);
		batch.end();

		// Move the alligator a bit. You normally shouldn't mix rendering with logic in
		// this way. (Also, movement should probably be based on *time*, not on how
		// often we update the graphics!)
		// change: move side to side
		Rectangle.tmp.set(spriteRect);
		Rectangle.tmp.x += dx;
		Rectangle.tmp2.set(spriteRect);
		
		if (screenRect.contains(Rectangle.tmp))
			spriteRect.x += dx;
		else
			dx = -dx;
		// add to go vertical
		//Rectangle.tmp2.y += dy; 
		/**if (screenRect.contains(Rectangle.tmp2))
			spriteRect.y += dy;
		else
			dy = -dy;
		*/
		// Don't handle input this way – use event handlers!
		//if (Gdx.input.justTouched()) { // check for mouse click
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			//bellSound.play();
			jumpSound.play();
			spriteRect.y += dy; // jumps up slightly (still moves side to side)
			
			//spriteRect.x = Gdx.input.getX() - spriteRect.width/2;
			//spriteRect.y = Gdx.graphics.getHeight() - Gdx.input.getY() - spriteRect.height/2;
			// System.out.println("X=" + Gdx.input.getX() + "| Y=" + Gdx.input.getY());
			// System.out.println("POSX=" + spriteRect.x + "| POSY=" + spriteRect.y);

		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { // check for key press
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		// Called whenever the window is resized (including with its original site at
		// startup)

		screenRect.width = width;
		screenRect.height = height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
