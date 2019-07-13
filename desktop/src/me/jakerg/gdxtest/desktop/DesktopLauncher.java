package me.jakerg.gdxtest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.jakerg.gdxtest.DungeonGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		config.width = 1280;
		config.height = 720;
		config.useHDPI = true;
		new LwjglApplication(new DungeonGame(), config);
	}
}
