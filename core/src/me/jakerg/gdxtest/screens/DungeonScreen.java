package me.jakerg.gdxtest.screens;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.jakerg.gdxtest.DungeonGame;
import me.jakerg.gdxtest.creature.Player;
import me.jakerg.gdxtest.object.DungeonRoom;
import me.jakerg.gdxtest.object.GameDungeon;
import me.jakerg.gdxtest.object.ListenerClass;
import me.jakerg.gdxtest.object.utils.Box2DUtils;
import me.jakerg.rougelike.Tile;

public class DungeonScreen implements Screen {
	static float SPEED = 120;
	float x, y;
	
	DungeonGame game;
	GameDungeon dungeon;
	Player player;
	World world;
	Box2DDebugRenderer b2dr;
	public TextureAtlas atlas;
	private OrthographicCamera cam;
	private Viewport vp;
	
	public DungeonScreen(DungeonGame game, GameDungeon dungeon) {
		this.game = game;
		this.dungeon = dungeon;
	}

	@Override
	public void show() {
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		vp = new ExtendViewport(1280 / 4, 720 / 4, cam);
		
		world = new World(new Vector2(0, 0), false);
		world.setContactListener(new ListenerClass());
		dungeon.setUpBox2d(world);
		b2dr = new Box2DDebugRenderer();
		atlas = new TextureAtlas(Gdx.files.internal("zelda-sheet.txt"));
		Tile.loadTextures(atlas);
		Point center = dungeon.getCurrentRoom().getCenter(); 
		player = new Player(this, world, dungeon.getCurrentRoom().getSpawnPoint());
		cam.position.set(center.x, center.y, 0);
		cam.update();
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
			for(DungeonRoom room : dungeon.getRooms().values())
				room.draw(game.batch);
			player.draw(game.batch);
			player.sword.draw(game.batch);
		game.batch.end();
		b2dr.render(world, cam.combined.cpy().scl(Box2DUtils.PPM));
		world.step(delta, 6, 2);
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
