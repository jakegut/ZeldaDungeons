package me.jakerg.rougelike;

import java.awt.*;

public abstract class Item {
    public int x;
    public int y;
    protected World world;
    protected char glyph;
    protected Color color;
    protected boolean pickupable;
    protected boolean removable;

    public Item(World world) {
        this.world = world;
    }

    public Item(World world, char glpyh, Color color, int x, int y) {
        this.world = world;
        this.glyph = glpyh;
        this.color = color;
        this.x = x;
        this.y = y;
        this.removable = true;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World w) {
        world = w;
    }

    public char glyph() {
        return glyph;
    }

    public Color color() {
        return color;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void update() {
    }

    public abstract void onPickup(Creature creature);

}
