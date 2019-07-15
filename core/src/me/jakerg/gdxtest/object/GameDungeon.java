package me.jakerg.gdxtest.object;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.World;

import edu.southwestern.tasks.gvgai.zelda.dungeon.Dungeon;

public class GameDungeon {
	private HashMap<String, DungeonRoom> rooms;
	private Dungeon dungeon;
	public static int TILE_SIZE = 16;

	public GameDungeon() {
		rooms = new HashMap<>();
	}

	public HashMap<String, DungeonRoom> getRooms() {
		return rooms;
	}

	public void setRooms(HashMap<String, DungeonRoom> rooms) {
		this.rooms = rooms;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	public DungeonRoom getCurrentRoom() {
		return rooms.get(dungeon.getCurrentlevel().name);
	}

	public void setUpBox2d(World world) {
		for(DungeonRoom room : rooms.values()) {
			room.setUpBox2d(world);
		}
	}
}
