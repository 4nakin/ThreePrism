package com.diamondgfx.ogam.threeprism;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ThreePrism implements ApplicationListener {
	private OrthographicCamera _camera;
	private SpriteBatch _batch;
	
	private GemBoard _board;
	private GameInputProcessor _input;
	private Gui _gui;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		_camera = new OrthographicCamera(1, h/w);
		_camera.setToOrtho(false, 640, 704);
		_batch = new SpriteBatch();
		
		_board = new GemBoard();
		_input = new GameInputProcessor(_board);
		Gdx.input.setInputProcessor(_input);
		
		_gui = new Gui();
	}

	@Override
	public void dispose() {
		_batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		_camera.update();
		
		_batch.setProjectionMatrix(_camera.combined);
		_batch.begin();
		_board.draw(_batch);
		_gui.draw(_batch, _board.getScore());
		_batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
