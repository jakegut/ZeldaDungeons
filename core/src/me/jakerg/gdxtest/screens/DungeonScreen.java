package me.jakerg.gdxtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.jakerg.gdxtest.DungeonGame;
import me.jakerg.gdxtest.creature.Player;

public class DungeonScreen implements Screen {
	static float SPEED = 120;
	float x, y;
	
	DungeonGame game;
	Player player;
	public TextureAtlas atlas;
	private OrthographicCamera cam;
	private Viewport vp;
	
	public DungeonScreen(DungeonGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 0);
		cam.update();
		
		vp = new ExtendViewport(1280, 720, cam);
		
		atlas = new TextureAtlas(Gdx.files.internal("zelda-sheet.txt"));
		player = new Player(this);
		Gdx.input.setInputProcessor(player);
	}

	@Override
	public void render(float delta) {		
		update(delta);
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		player.draw(game.batch);
		player.sword.draw(game.batch);
		game.batch.end();
		
	}

	private void update(float delta) {
		player.update(delta);
		
	}

	@Override
	public void resize(int width, int height) {
		vp.update(width, height);
		
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
		player.getTexture().dispose();
	}

}
