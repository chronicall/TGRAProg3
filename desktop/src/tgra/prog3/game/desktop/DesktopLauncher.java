package tgra.prog3.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tgra.prog3.game.Prog3Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Programming Assignment 3 - The Maze"; // or whatever you like
		config.height = 1000;
		config.width = 1900;
		config.x = 150;
		config.y = 50;
		//config.fullscreen = true;
		new LwjglApplication(new Prog3Game(), config);
	}
}
