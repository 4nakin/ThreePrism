package com.diamondgfx.ogam.threeprism;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ThreePrism";
		cfg.useGL20 = false;
		cfg.width = 640;
		cfg.height = 704;
		
		new LwjglApplication(new ThreePrism(), cfg);
	}
}