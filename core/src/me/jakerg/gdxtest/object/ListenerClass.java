package me.jakerg.gdxtest.object;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import me.jakerg.gdxtest.creature.Player;
import me.jakerg.gdxtest.object.utils.Box2DUtils;
import me.jakerg.rougelike.Tile;

import java.awt.*;

public class ListenerClass implements ContactListener {

    GameDungeon dungeon;

    public ListenerClass(GameDungeon dungeon) {
        System.out.println("New listener");
        this.dungeon = dungeon;
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getUserData() instanceof Tile &&
                contact.getFixtureB().getUserData() instanceof Player) {
            Tile t = (Tile) contact.getFixtureA().getUserData();
            if (t.isDoor()) {
                Player player = (Player) contact.getFixtureB().getUserData();
                if (!ableToExit(t, player)) return;
                System.out.println(contact.getFixtureA().getBody().getPosition());
                int x = (int) (contact.getFixtureA().getBody().getPosition().x - dungeon.getCurrentRoom().getOffsetX() / GameDungeon.TILE_SIZE);
                int y = (int) (contact.getFixtureA().getBody().getPosition().y - dungeon.getCurrentRoom().getOffsetY() / GameDungeon.TILE_SIZE);
                y = dungeon.getCurrentRoom().getHeight() - y - 1;
                Point p = new Point(x, y);
                Point start = dungeon.getNextRoom(p.toString());
                if (start == null) return;
                System.out.println(dungeon.getCurrentRoom().getCenter());
                int newX = start.x * GameDungeon.TILE_SIZE + dungeon.getCurrentRoom().getOffsetX();
                int newY = dungeon.getCurrentRoom().getHeight() - start.y - 1;
                newY = newY * GameDungeon.TILE_SIZE + dungeon.getCurrentRoom().getOffsetY();
                player.setNewPosition((newX + player.getWidth() / 2) / Box2DUtils.PPM, (newY + player.getHeight() / 2) / Box2DUtils.PPM);
                System.out.println(p);

            }
        }
    }

    /**
     * Check if the player is able to go through the door based on requirements
     *
     * @param t
     * @param p
     */
    private boolean ableToExit(Tile t, Player p) {
        if (t.equals(Tile.DOOR))
            return true;
        else
            return false;
    }

    @Override
    public void endContact(Contact contact) {
        // TODO Auto-generated method stub

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }

}
