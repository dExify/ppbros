package inf112.ppbros.app;

import org.lwjgl.system.Configuration;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import inf112.ppbros.model.GameModel;

public class Main {
	public static void main(String[] args) {
		if (SharedLibraryLoader.os == Os.MacOsX) {
			Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
		}
		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("Power Pipes Bros");
		Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		cfg.setFullscreenMode(displayMode);
		cfg.setForegroundFPS(60);

		new Lwjgl3Application(new GameModel(), cfg);
	}
}
