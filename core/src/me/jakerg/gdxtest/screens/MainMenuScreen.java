package me.jakerg.gdxtest.screens;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

import edu.southwestern.parameters.Parameters;
import edu.southwestern.tasks.gvgai.zelda.dungeon.Dungeon;
import edu.southwestern.tasks.gvgai.zelda.dungeon.DungeonUtil;
import edu.southwestern.tasks.gvgai.zelda.level.LevelLoader;
import edu.southwestern.tasks.gvgai.zelda.level.ZeldaGrammar;
import edu.southwestern.tasks.gvgai.zelda.level.ZeldaGraphGrammar;
import edu.southwestern.util.ClassCreation;
import edu.southwestern.util.datastructures.Graph;
import edu.southwestern.util.datastructures.GraphUtil;
import me.jakerg.gdxtest.DungeonGame;
import me.jakerg.gdxtest.object.utils.DungeonUtils;

public class MainMenuScreen implements Screen{

	DungeonGame game;
	
	public MainMenuScreen(DungeonGame game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.font.draw(game.batch, "Welcome to Zelda Dungeons", 100, 150);
		game.font.draw(game.batch, "Press enter to begin", 100, 100);
		game.batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			Dungeon d = generateDungeon();
			game.setScreen(new DungeonScreen(game, DungeonUtils.loadDungeon(d)));
			dispose();
		}
		
	}
	
	public Dungeon generateDungeon() {
		List<ZeldaGrammar> initialList = new LinkedList<>();
		Parameters.initializeParameterCollections(new String[] {"zeldaGANUsesOriginalEncoding:false", "zeldaLevelLoader:edu.southwestern.tasks.gvgai.zelda.level.GANLoader"});
		initialList.add(ZeldaGrammar.START_S);
		initialList.add(ZeldaGrammar.ENEMY_S);
		initialList.add(ZeldaGrammar.KEY_S);
		initialList.add(ZeldaGrammar.LOCK_S);
		initialList.add(ZeldaGrammar.ENEMY_S);
		initialList.add(ZeldaGrammar.KEY_S);
		initialList.add(ZeldaGrammar.PUZZLE_S);
		initialList.add(ZeldaGrammar.LOCK_S);
		initialList.add(ZeldaGrammar.ENEMY_S);
		initialList.add(ZeldaGrammar.TREASURE);
		
		Graph<ZeldaGrammar> graph = new Graph<>(initialList);
		
		
		ZeldaGraphGrammar grammar = new ZeldaGraphGrammar();
//		ZeldaGraphGrammar grammar = new ZeldaGraphGrammar(new File("data/VGLC/Zelda/rules/1"));
		try {
			grammar.applyRules(graph);
			Dungeon d = null;
			d = DungeonUtil.recursiveGenerateDungeon(graph, (LevelLoader) ClassCreation.createObject("zeldaLevelLoader"));
			DungeonUtil.makeDungeonPlayable(d);
			DungeonUtil.viewDungeon(d);
			return d;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}
		
		return null;
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
