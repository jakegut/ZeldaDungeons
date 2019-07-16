package me.jakerg.gdxtest.object.utils;

import edu.southwestern.tasks.gvgai.zelda.dungeon.Dungeon;
import edu.southwestern.tasks.gvgai.zelda.dungeon.ZeldaDungeon.Level;
import me.jakerg.gdxtest.object.DungeonRoom;
import me.jakerg.gdxtest.object.GameDungeon;
import me.jakerg.rougelike.Tile;

import java.awt.*;
import java.util.ArrayList;

public class DungeonUtils {
    public static GameDungeon loadDungeon(Dungeon dungeon) {
        GameDungeon gd = new GameDungeon();
        dungeon.flipLevelThere();
        String[][] levels = dungeon.getLevelThere();
        gd.setDungeon(dungeon);
        for (int y = 0; y < levels.length; y++) {
            for (int x = 0; x < levels[y].length; x++) {
                if (levels[y][x] == null) continue;
                gd.getRooms().put(levels[y][x], loadRoom(dungeon, dungeon.getNode(levels[y][x])));
            }
        }
        return gd;
    }

    public static DungeonRoom loadRoom(Dungeon dungeon, Dungeon.Node node) {
        Level level = node.level;
        DungeonRoom room = new DungeonRoom(level.intLevel.size(), level.intLevel.get(0).size());

        room.setTiles(getTiles(level));
        room.setHeight(level.intLevel.size());
        Point p = dungeon.getCoords(node);
        room.setOffsetX(p.x * room.getWidth() * GameDungeon.TILE_SIZE);
        room.setOffsetY(p.y * room.getHeight() * GameDungeon.TILE_SIZE);

        return room;
    }

    private static Tile[][] getTiles(Level level) {
        ArrayList<ArrayList<Integer>> ints = level.intLevel;
        Tile[][] tiles = new Tile[ints.size()][ints.get(0).size()];

        for (int y = 0; y < ints.size(); y++) {
            for (int x = 0; x < ints.get(y).size(); x++) {
                Tile t = Tile.findNum(ints.get(y).get(x));
                tiles[y][x] = t;
            }
        }
        return tiles;
    }


}
