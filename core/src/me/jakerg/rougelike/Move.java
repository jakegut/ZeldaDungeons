package me.jakerg.rougelike;

import com.badlogic.gdx.Input.Keys;

import java.awt.*;

/**
 * Enumerator to keep track of the latest direction of the player
 *
 * @author gutierr8
 */
public enum Move {
    UP("UP", new Point(0, -1), Keys.W),
    DOWN("DOWN", new Point(0, 1), Keys.S),
    LEFT("LEFT", new Point(-1, 0), Keys.A),
    RIGHT("RIGHT", new Point(1, 0), Keys.D),
    NONE("NONE", new Point(0, 0), Keys.UNKNOWN);

    private String direction;
    private Point point;

    ;
    private int key;

    Move(String direction, Point p, int key) {
        this.direction = direction;
        this.point = p;
        this.key = key;
    }

    public static Move getByString(String s) {
        switch (s) {
            case "UP":
                return UP;
            case "DOWN":
                return DOWN;
            case "LEFT":
                return LEFT;
            case "RIGHT":
                return RIGHT;
            default:
                return NONE;
        }
    }

    public static Move getByKey(int key) {
        for (Move m : Move.values()) {
            if (key == m.key)
                return m;
        }
        return null;
    }

    public String direction() {
        return this.direction;
    }

    public Point point() {
        return point;
    }

    public int x() {
        return point.x;
    }

    public int y() {
        return -point.y;
    }

    public int key() {
        return key;
    }

    public Point getPoint() {
        return point;
    }

    public Move opposite() {
        if (this == UP)
            return DOWN;
        else if (this == DOWN)
            return UP;
        else if (this == LEFT)
            return RIGHT;
        else if (this == RIGHT)
            return LEFT;

        return null;
    }
}
