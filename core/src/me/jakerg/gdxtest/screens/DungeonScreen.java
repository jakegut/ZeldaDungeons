package me.jakerg.gdxtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import me.jakerg.gdxtest.DungeonGame;
import me.jakerg.gdxtest.creature.Player;

public class DungeonScreen implements Screen {
	static float SPEED = 120;
	Texture img;
	float x, y;
	
	DungeonGame game;
	Player player;
	
	public DungeonScreen(DungeonGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		img = new Texture(Gdx.files.internal("badlogic.jpg"));
		player = new Player();
	}

	@Override
	public void render(float delta) {		
		update(delta);
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();
		
	}

	private void update(float delta) {
		player.update(delta);
		
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
