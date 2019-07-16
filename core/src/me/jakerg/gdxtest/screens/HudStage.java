package me.jakerg.gdxtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.jakerg.gdxtest.creature.Player;

public class HudStage {

    private Label health;
    private Table table;
    private BitmapFont nes16;
    private Player player;
    public Stage stage;

    public HudStage(Player player, Viewport stageVP, SpriteBatch batch) {
        stage = new Stage(stageVP, batch);
        this.player = player;
        nes16 = new BitmapFont(Gdx.files.internal("fonts/Pixel_NES-16.fnt"));
        table = new Table();
//		table.setColor(Color.BLACK);
        table.setX(0);
        table.setY(Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4);
        table.setWidth(stageVP.getWorldWidth());
        table.setHeight(Gdx.graphics.getHeight() / 4);
//		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.top();
//        table.setFillParent(true);

        LabelStyle style;
        style = new LabelStyle(nes16, Color.WHITE);
        health = new Label("Test of HUD : " + player.getStats().hp(), style);
        table.add(health).expandX().pad(10);
        stage.addActor(table);
        stage.setDebugAll(true);
    }

    public void update(){
        table.setX(0);
        table.setY(Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4);
        table.setWidth(stage.getViewport().getWorldWidth());
        table.setHeight(Gdx.graphics.getHeight() / 4);
    }

}
