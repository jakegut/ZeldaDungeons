package me.jakerg.gdxtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import me.jakerg.gdxtest.DungeonGame;

public class DungeonScreen implements Screen {
	static float SPEED = 120;
	Texture img;
	float x, y;
	
	DungeonGame game;
	
	public DungeonScreen(DungeonGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		img = new Texture(Gdx.files.internal("badlogic.jpg"));
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyPressed(Keys.UP))
			y += SPEED * delta;
		
		if(Gdx.input.isKeyPressed(Keys.DOWN))
			y += -SPEED * delta;
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			x += SPEED * delta;
		
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			x += -SPEED * delta;
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.draw(img, x, y);
		game.batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		img.dispose();
	}

}
