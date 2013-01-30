package com.diamondgfx.ogam.threeprism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Gui {
	private Texture _background;
	
	public Gui() {
		_background = new Texture(Gdx.files.internal("data/scorebg.png"));
	}
	
	public void draw(SpriteBatch batch, int score) {
		BitmapFont font = new BitmapFont();
		batch.draw(_background, 0, 0);
		font.draw(batch, "Score: " + Integer.toString(score), 20, 39);
	}
}
