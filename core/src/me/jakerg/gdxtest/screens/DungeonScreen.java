package me.jakerg.gdxtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.jakerg.gdxtest.DungeonGame;
import me.jakerg.gdxtest.creature.Player;
import me.jakerg.gdxtest.object.GameDungeon;
import me.jakerg.gdxtest.object.ListenerClass;
import me.jakerg.gdxtest.object.utils.Box2DUtils;
import me.jakerg.gdxtest.object.utils.CameraUtils;
import me.jakerg.rougelike.Tile;

import java.awt.*;

public class DungeonScreen implements Screen {

    public TextureAtlas atlas;
    DungeonGame game;
    GameDungeon dungeon;
    Player player;
    World world;
    Box2DDebugRenderer b2dr;
    private OrthographicCamera cam;
    private Viewport vp;
    private Viewport stageVP;
    private HudStage hud;
    private OrthographicCamera stageCam;

    public DungeonScreen(DungeonGame game, GameDungeon dungeon) {
        this.game = game;
        this.dungeon = dungeon;
    }

    @Override
    public void show() {
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stageCam = new OrthographicCamera();

        vp = new FitViewport(dungeon.getRoomWidth() * Box2DUtils.PPM, dungeon.getRoomHeight() * Box2DUtils.PPM, cam);
        vp.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4);

        stageVP = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), stageCam);
        stageVP.setScreenBounds(0, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4,  dungeon.getRoomWidth() * (int) Box2DUtils.PPM, Gdx.graphics.getHeight() / 4);
        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new ListenerClass(dungeon));

        dungeon.setUpBox2d(world);
        b2dr = new Box2DDebugRenderer();

        atlas = new TextureAtlas(Gdx.files.internal("zelda-sheet.txt"));
        Tile.loadTextures(atlas);

        Point center = dungeon.getCurrentRoom().getCenter();

        player = new Player(this, world, dungeon.getCurrentRoom().getSpawnPoint());
        hud = new HudStage(player, stageVP, game.batch);

        cam.position.set(center.x, center.y, 0);
        cam.update();
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float delta) {
        Point c = dungeon.getCurrentRoom().getCenter();
        boolean reachedTarget = lightEquals(cam.position, c);

        if (!reachedTarget)
            CameraUtils.lerpToTarget(cam, c);
        else
            update(delta);
//		cam.position.set(player.getX(), player.getY(), 0);
//		cam.update();
        game.batch.setProjectionMatrix(cam.combined);


        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        vp.apply();
//			for(DungeonRoom room : dungeon.getRooms().values())
//				room.draw(game.batch);
        dungeon.drawSurronding(game.batch);
        if (reachedTarget) {
            player.draw(game.batch);
            player.sword.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.getViewport().apply(true);
        hud.stage.draw();

//		b2dr.render(world, cam.combined.cpy().scl(Box2DUtils.PPM));
        world.step(delta, 6, 2);
    }

    private boolean lightEquals(Vector3 position, Point c) {
        float threshold = 0.5f;
        float x = Math.abs(position.x - c.x);
        float y = Math.abs(position.y - c.y);
        return (x <= threshold && y <= threshold);
    }

    private void update(float delta) {
        player.update(delta);
        hud.stage.act(delta);

    }

    @Override
    public void resize(int width, int height) {
        vp.update(width, height - height / 4);
        hud.stage.getViewport().update(width, height);
        hud.update();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        player.getTexture().dispose();
        player.sword.getTexture().dispose();
        hud.stage.dispose();
        b2dr.dispose();
        world.dispose();
    }

}
