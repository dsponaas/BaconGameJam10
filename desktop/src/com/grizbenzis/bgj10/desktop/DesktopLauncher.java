package com.grizbenzis.bgj10.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.grizbenzis.bgj10.bgj10;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "BACON GAME JAM 10";
		config.width = 1024;
		config.height = 768;
		config.resizable = false;
		new LwjglApplication(new bgj10(), config);
 	}
}
