package me.jakerg.rougelike;

import asciiPanel.AsciiPanel;

import java.awt.*;

public class Ladder extends Item {

    private static char glpyh = '#';
    private static Color color = AsciiPanel.brightCyan;

    public Ladder(World world, int x, int y) {
        super(world, glpyh, color, x, y);
        this.pickupable = true;
    }

    @Override
    public void onPickup(Creature creature) {
        if (creature.isPlayer())
            creature.addItem(this);
    }

}
