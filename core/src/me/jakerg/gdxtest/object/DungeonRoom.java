package me.jakerg.gdxtest.object;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import me.jakerg.gdxtest.object.utils.Box2DUtils;
import me.jakerg.rougelike.Tile;

public class DungeonRoom {
	private int offsetX;
	private int offsetY;
	private int width;
	private int height;
	private Tile[][] tiles;
	
	public DungeonRoom(int h, int w) {
		width = w;
		height = h;
		tiles = new Tile[h][w];
	}
	
	public void draw(SpriteBatch batch) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				batch.draw(tile(x, y).getTexture(), offsetX + x * GameDungeon.TILE_SIZE, offsetY + (height - y - 1) * GameDungeon.TILE_SIZE);
			}
		}
	}
	
	public Point getSpawnPoint() {
		return new Point(offsetX + 5 * GameDungeon.TILE_SIZE, 
						 offsetY + 5 * GameDungeon.TILE_SIZE);
	}
	
	public Tile getTileByCoordinates(float x, float y) {
		int tX = (int) ((x / GameDungeon.TILE_SIZE) - offsetX);
		int tY = (int) ((y / GameDungeon.TILE_SIZE) - offsetY);
		return tile(tX, tY);
	}
	
	public Tile tile(int x, int y) {
		if(x < 0 || x >= tiles[0].length || y < 0 || y > tiles.length)
			return Tile.FLOOR;
		return tiles[y][x];
	}
	
	public Point getCenter() {
		int x = offsetX + (width * GameDungeon.TILE_SIZE / 2);
		int y = offsetY + (height * GameDungeon.TILE_SIZE / 2);
		return new Point(x, y);
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public void setUpBox2d(World world) {
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(tile(x, y).playerPassable()) continue;
				float realY = offsetY + (height - y - 1) * (GameDungeon.TILE_SIZE) + ((GameDungeon.TILE_SIZE) / 2);
				float realX = offsetX + x * (GameDungeon.TILE_SIZE) + ((GameDungeon.TILE_SIZE) / 2);
				Body b = Box2DUtils.createStaticRectBody(world, realX, realY, GameDungeon.TILE_SIZE, GameDungeon.TILE_SIZE);
				b.getFixtureList().get(0).setUserData(tile(x, y));
			}
		}
	}
}
