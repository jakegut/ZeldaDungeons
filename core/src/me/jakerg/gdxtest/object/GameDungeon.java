package me.jakerg.gdxtest.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import edu.southwestern.tasks.gvgai.zelda.dungeon.Dungeon;

import java.awt.*;
import java.util.HashMap;

public class GameDungeon {
    public static int TILE_SIZE = 16;
    private HashMap<String, DungeonRoom> rooms;
    private Dungeon dungeon;
    private Point currentCords;

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
        currentCords = dungeon.getCoords(dungeon.getCurrentlevel());
    }

    public DungeonRoom getCurrentRoom() {
        return rooms.get(dungeon.getCurrentlevel().name);
    }

    public Point getNextRoom(String adj) {
        Point p = dungeon.getNextNode(adj);
        if (p != null) {
            currentCords = dungeon.getCoords(dungeon.getCurrentlevel());
            getCurrentRoom().getCenter();
        }
        return p;
    }

    public void setUpBox2d(World world) {
        for (DungeonRoom room : rooms.values()) {
            room.setUpBox2d(world);
        }
    }

    public int getRoomWidth() {
        return dungeon.getCurrentlevel().level.intLevel.get(0).size();
    }

    public int getRoomHeight() {
        return dungeon.getCurrentlevel().level.intLevel.size();
    }

    public void drawSurronding(SpriteBatch batch) {
        String n = dungeon.getCurrentlevel().name;
        Point cor = currentCords;
        drawByPoint(cor.x, cor.y, batch);
        drawByPoint(cor.x - 1, cor.y, batch);
        drawByPoint(cor.x + 1, cor.y, batch);
        drawByPoint(cor.x, cor.y + 1, batch);
        drawByPoint(cor.x, cor.y - 1, batch);
    }

    private void drawByPoint(int x, int y, SpriteBatch batch) {
        String[][] levels = dungeon.getLevelThere();
        if (x < 0 || x >= levels[0].length || y < 0 || y >= levels.length)
            return;
        if (levels[y][x] == null) return;

        rooms.get(levels[y][x]).draw(batch);
    }
}
