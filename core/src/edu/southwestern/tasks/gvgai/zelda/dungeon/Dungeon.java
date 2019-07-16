package edu.southwestern.tasks.gvgai.zelda.dungeon;

import com.google.gson.Gson;
import edu.southwestern.tasks.gvgai.zelda.dungeon.ZeldaDungeon.Level;
import edu.southwestern.tasks.gvgai.zelda.level.ZeldaGrammar;
import edu.southwestern.util.datastructures.Pair;
import me.jakerg.rougelike.Tile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class Dungeon {

    private HashMap<String, Node> levels;
    private String currentLevel;
    private String[][] levelThere;
    private String goal;
    private Point goalPoint;
    private int levelWidth = -1;
    private int levelHeight = -1;

    public Dungeon() {
        levels = new HashMap<>();
        levelThere = null;
    }

    /**
     * Helper function to return a dungeon instance from a json file
     *
     * @param filePath Path to JSON file
     * @return Dungeon filled with info from JSON file
     */
    public static Dungeon loadFromJson(String filePath) {
        Gson gson = new Gson();
        try {
            FileInputStream stream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Dungeon d = gson.fromJson(reader, Dungeon.class);
            reader.close();
            stream.close();
            return d;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Node> getLevels() {
        return this.levels;
    }

    public Node newNode(String name, Level level) throws Exception {
        Node node = new Node(name, level);
        if (levels.get(name) != null)
            throw new Exception("Unable to place new node : " + name);
        levels.put(name, node);
        return node;
    }

    public Node getNode(String name) {
        return levels.get(name);
    }

    public void setCurrentLevel(String name) {
        this.currentLevel = name;
    }

    public String[][] getLevelThere() {
        return this.levelThere;
    }

    public void setLevelThere(String[][] levelThere) {
        this.levelThere = levelThere;
    }

    /**
     * Set the next node based on the exit point
     *
     * @param exitPoint Exit Point of level based on string
     * @return Point of where to start the new level
     */
    @SuppressWarnings("unused")
    public Point getNextNode(String exitPoint) {
        System.out.println("Exit point   " + exitPoint);
        Node n = getCurrentlevel();
        System.out.println("Node : " + n);
        HashMap<String, Pair<String, Point>> adjacency = n.adjacency;
        Pair<String, Point> next = adjacency.get(exitPoint);
        if (next == null) {
            System.out.println("No next, returning null");
            return null;
        }
        System.out.println("Next thingy : " + next.t1);
        setCurrentLevel(next.t1);
        return next.t2;
    }

    public Point getCoords(String name) {
        if (!levels.containsKey(name)) {
            System.out.println("Name isn't in list : " + name);
            return null;
        }

        for (int y = 0; y < levelThere.length; y++)
            for (int x = 0; x < levelThere[y].length; x++)
                if (name == levelThere[y][x])
                    return new Point(x, y);

        System.out.println("Couldnt find name : " + name);
        return null;
    }

    public Pair<String, Point> getNextLevel(Node node, String exitPoint) {
        HashMap<String, Pair<String, Point>> adjacency = node.adjacency;
        Pair<String, Point> r = adjacency.get(exitPoint);
        return r;
    }

    public Point getCoords(Node node) {
        return getCoords(node.name);
    }

    public Node getCurrentlevel() {
        return levels.get(currentLevel);
    }

    /**
     * Helper function to get a 2D array of levels based on the strings in levelThere
     *
     * @return 2D array of levels
     */
    public Level[][] getLevelArrays() {
        Level[][] r = new Level[levelThere.length][levelThere[0].length];

        for (int y = 0; y < levelThere.length; y++)
            for (int x = 0; x < levelThere[y].length; x++)
                if (levelThere[y][x] != null)
                    r[y][x] = levels.get(levelThere[y][x]).level;
                else
                    r[y][x] = null;

        return r;
    }

    public Node getNodeAt(int x, int y) {
        if (x < 0 || x >= levelThere[0].length || y < 0 || y >= levelThere.length) return null;

        return getNode(levelThere[y][x]);
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String g) {
        this.goal = g;
    }

    public Point getGoalPoint() {
        return goalPoint;
    }

    public void setGoalPoint(Point goalPoint) {
        this.goalPoint = goalPoint;
    }

    public int getLevelWidth() {
        if (levelWidth == -1)
            levelWidth = getCurrentlevel().level.intLevel.get(0).size();

        return levelWidth;
    }

    public int getLevelHeight() {
        if (levelHeight == -1)
            levelHeight = getCurrentlevel().level.intLevel.size();

        return levelHeight;
    }

    public void removeNode(String name) {
        Node n = getNode(name);
        // Remove adjacencies from the node adjacencies
        n.adjacency.values().forEach(p -> getNode(p.t1).adjacency.entrySet().removeIf(e -> e.getValue().t1 == name));

        levels.remove(name);
    }

    public void printLevelThere() {
        for (int y = 0; y < levelThere.length; y++) {
            for (int x = 0; x < levelThere[y].length; x++) {
                String n = levelThere[y][x];
                System.out.print(n + " ");
            }
            System.out.println();
        }
    }

    public void flipLevelThere() {
        String[][] temp = new String[levelThere.length][levelThere[0].length];
        for (int y = 0; y < levelThere.length; y++) {
            for (int x = 0; x < levelThere[y].length; x++) {
                temp[levelThere.length - 1 - y][x] = levelThere[y][x];
            }
        }
        levelThere = temp;
    }

    public class Node {
        public Level level;
        public String name;
        public HashMap<String, Pair<String, Point>> adjacency;
        // Exit point   Node    Starting point
        public ZeldaGrammar grammar;

        public Node(String name, Level level) {
            this.name = name;
            this.level = level;
            adjacency = new HashMap<>();
        }

        public void setAdjacency(String exitPoint, String whereTo, Point startPoint) {
            adjacency.put(exitPoint, new Pair<String, Point>(whereTo, startPoint));
        }

        public String toString() {
            return this.name;
        }

        public boolean hasLock() {
            ArrayList<ArrayList<Integer>> ints = level.intLevel;
            for (int y = 0; y < ints.size(); y++)
                for (int x = 0; x < ints.get(y).size(); x++)
                    if (ints.get(y).get(x).equals(Tile.LOCKED_DOOR.getNum()))
                        return true;


            return false;
        }
    }

}

