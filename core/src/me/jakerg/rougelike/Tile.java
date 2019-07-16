package me.jakerg.rougelike;

import asciiPanel.AsciiPanel;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.*;

/**
 * Enumerator to model our tiles with a character representation and a color
 *
 * @author gutierr8
 */
public enum Tile {
    // Refer to Code Page 437 for the number representation of the char
    FLOOR((char) 250, AsciiPanel.yellow, 0, "dungeon-floor"),
    VISITED('x', AsciiPanel.white, 101, null),
    UNVISITED('x', AsciiPanel.red, 110, null),
    WALL((char) 219, AsciiPanel.yellow, 1, "dungeon-wall"),
    CURRENT((char) 219, AsciiPanel.brightYellow, -99, null),
    EXIT((char) 239, AsciiPanel.green, 4, null),
    DOOR((char) 239, AsciiPanel.green, 3, "dungeon-unlocked"),
    BLOCK((char) 177, AsciiPanel.cyan, 5, "dungeon-water"), // this is the 'P' water block thing
    LOCKED_DOOR((char) 239, AsciiPanel.red, -5, "dungeon-locked"),
    SOFT_LOCK_DOOR((char) 239, AsciiPanel.brightBlue, -55, "dungeon-softlocked"),
    HIDDEN((char) 178, AsciiPanel.yellow, -7, "dungeon-hidden"),
    BOUNDS('x', AsciiPanel.brightBlack, -99, null),
    KEY('k', AsciiPanel.brightYellow, 6, "item-key"),
    TRIFORCE((char) 30, AsciiPanel.brightYellow, 8, "item-triforce"),
    MOVABLE_BLOCK_UP((char) 219, AsciiPanel.yellow, 10, "dungeon-wall"),
    MOVABLE_BLOCK_DOWN((char) 219, AsciiPanel.yellow, 100, "dungeon-wall"),
    MOVABLE_BLOCK_LEFT((char) 219, AsciiPanel.yellow, 1000, "dungeon-wall"),
    MOVABLE_BLOCK_RIGHT((char) 219, AsciiPanel.yellow, 10000, "dungeon-wall"),
    PUZZLE_LOCKED((char) 239, Color.ORANGE, -10, "dungeon-puzzle");

    private char glyph;
    private Color color;
    private int number;
    private String region;
    private TextureRegion texture;

    Tile(char glyph, Color color, int number, String region) {
        this.glyph = glyph;
        this.color = color;
        this.number = number;
        this.region = region;
    }

    public static Tile findNum(int num) {
        for (Tile tile : Tile.values()) {
            if (num == tile.getNum())
                return tile;
        }
        return Tile.FLOOR;
    }

    public static Tile findByMove(Move d) {
        switch (d) {
            case UP:
                return MOVABLE_BLOCK_UP;
            case DOWN:
                return MOVABLE_BLOCK_DOWN;
            case LEFT:
                return MOVABLE_BLOCK_LEFT;
            case RIGHT:
                return MOVABLE_BLOCK_RIGHT;
            default:
                return null;
        }
    }

    public static void loadTextures(TextureAtlas atlas) {
        for (Tile t : Tile.values()) {
            if (t.region != null)
                t.setTexture(atlas.findRegion(t.region));
            else
                t.setTexture(null);
        }
    }

    public char getGlyph() {
        if (this == HIDDEN) {
            if (RougelikeApp.DEBUG)
                return glyph;
            else
                return WALL.glyph;
        }
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public int getNum() {
        return number;
    }

    /**
     * Only diggable walls
     *
     * @return True if the tile is a wall
     */
    public boolean isDiggable() {
        return this == WALL || this == KEY || this.isMovable();
    }

    /**
     * If a creature can walk on
     *
     * @return True if it's not a wall and not a bound
     */
    public boolean isGround() {
        return this != WALL && this != BOUNDS && !this.isDoor();
    }

    public boolean isBlock() {
        return this == BLOCK || this.isMovable();
    }

    public boolean playerPassable() {
        return this.isGround() && !this.isBlock() || this.isInterest();
    }

    public boolean isStatePassable() {
        return this == FLOOR || this.isInterest();
    }

    public boolean isDoor() {
        return this == DOOR || this == HIDDEN || this == SOFT_LOCK_DOOR || this == LOCKED_DOOR || this == Tile.PUZZLE_LOCKED;
    }

    public boolean isInterest() {
        return this == KEY || this == TRIFORCE;
    }

    /**
     * If the tile is an exit
     *
     * @return True of the tile is EXIT
     */
    public boolean isExit() {
        return this == EXIT;
    }

    public boolean isBombable() {
        return this == FLOOR || this == HIDDEN || this == WALL;
    }

    public boolean isKey() {
        return this == KEY;
    }

    public boolean isMovable() {
        return this == MOVABLE_BLOCK_UP || this == MOVABLE_BLOCK_DOWN || this == MOVABLE_BLOCK_LEFT || this == MOVABLE_BLOCK_RIGHT;
    }

    public Move getDirection() {
        if (this == MOVABLE_BLOCK_UP)
            return Move.UP;
        else if (this == MOVABLE_BLOCK_DOWN)
            return Move.DOWN;
        else if (this == MOVABLE_BLOCK_LEFT)
            return Move.LEFT;
        else if (this == Tile.MOVABLE_BLOCK_RIGHT)
            return Move.RIGHT;
        else
            return Move.NONE;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

}
