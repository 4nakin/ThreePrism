/**
 * 
 */
package com.diamondgfx.ogam.threeprism;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;

/**
 * @author brichey
 *
 */
public class Gem {
	protected Texture   _img;
	protected Rectangle _rect;
	
	public Gem() {
		_img = new Texture(64, 64, Format.Alpha);
		_rect = new Rectangle(0, 0, 64, 64);
	}
	
	public Gem(String imgFilename) {
		_img = new Texture(Gdx.files.internal("data/" + imgFilename + ".png"));
		_rect = new Rectangle(0, 0, _img.getWidth(), _img.getHeight());
	}
	
	public void draw(SpriteBatch batch, int x, int y)
	{
		goTo(x, y);
		draw(batch);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(_img, _rect.x, _rect.y);
	}
	
	public void goTo(int x, int y)
	{
		_rect.x = x;
		_rect.y = y;
	}
}
