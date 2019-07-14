package me.jakerg.gdxtest.creature;

import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import me.jakerg.gdxtest.DungeonGame;
import me.jakerg.gdxtest.screens.DungeonScreen;
import me.jakerg.rougelike.Move;

public class Player extends Sprite implements InputProcessor{
	enum State { WALK, ATTACK };
	
	
	private final int SPEED = 200;
	private Stack<Move> inputStack;
	private Move lastMove;
	private State state;
	public Animation<TextureRegion> runningAnimation;
	public HashMap<Move, Animation<TextureRegion>> animations;
	public HashMap<Move, Animation<TextureRegion>> attackRegions;
	private float elapsedTime;
	
	public Player(DungeonScreen screen) {
		super(screen.atlas.findRegion("walkup_1"));
		this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
		this.setPosition(0, 0);
		this.setScale(4);
		this.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		inputStack = new Stack<>();
		inputStack.push(Move.NONE);
		lastMove = Move.UP;
		state = State.WALK;
		
		setUpAnimations(screen);
		
		elapsedTime = 0;
	}
	
	private void setUpAnimations(DungeonScreen screen) {
		animations = new HashMap<>();
		float s = 1/10f;
		
		Array<AtlasRegion> regions = new Array<>();
		regions.add(screen.atlas.findRegion("walkup_1"));
		regions.add(screen.atlas.findRegion("walkup_2"));
		Animation<TextureRegion> a = new Animation<>(s, regions, PlayMode.LOOP);
		animations.put(Move.UP, a);
		regions.clear();
		
		regions.add(screen.atlas.findRegion("walkdown_1"));
		regions.add(screen.atlas.findRegion("walkdown_2"));
		a = new Animation<>(s, regions, PlayMode.LOOP);
		animations.put(Move.DOWN, a);
		regions.clear();
		
		regions.add(screen.atlas.findRegion("walkleft_1"));
		regions.add(screen.atlas.findRegion("walkleft_2"));
		a = new Animation<>(s, regions, PlayMode.LOOP);
		animations.put(Move.LEFT, a);
		regions.clear();
		
		regions.add(screen.atlas.findRegion("walkright_1"));
		regions.add(screen.atlas.findRegion("walkright_2"));
		a = new Animation<>(s, regions, PlayMode.LOOP);
		animations.put(Move.RIGHT, a);
		regions.clear();
		
		
		s = 1/5f;
		attackRegions = new HashMap<>();
		regions.add(screen.atlas.findRegion("attackup"));
		a = new Animation<>(s, regions, PlayMode.NORMAL);
		attackRegions.put(Move.UP, a);
		regions.clear();
		
		regions.add(screen.atlas.findRegion("attackdown"));
		a = new Animation<>(s, regions, PlayMode.NORMAL);
		attackRegions.put(Move.DOWN, a);
		regions.clear();
		
		regions.add(screen.atlas.findRegion("attackleft"));
		a = new Animation<>(s, regions, PlayMode.NORMAL);
		attackRegions.put(Move.LEFT, a);
		regions.clear();
		
		regions.add(screen.atlas.findRegion("attackright"));
		a = new Animation<>(s, regions, PlayMode.NORMAL);
		attackRegions.put(Move.RIGHT, a);
		regions.clear();
	}

	public void update(float delta) {
		Move move = inputStack.peek();
		if(move == null)
			move = Move.NONE;
		
		if(state.equals(State.ATTACK)) move = Move.NONE;
		
		if(!move.equals(Move.NONE)) {
			lastMove = move;
			elapsedTime += delta;
		}
		
		if(state.equals(State.WALK))
			this.setRegion(animations.get(lastMove).getKeyFrame(elapsedTime, true));
		else if(state.equals(State.ATTACK)) {
			elapsedTime += delta;
			this.setRegion(attackRegions.get(lastMove).getKeyFrame(elapsedTime, true));

			if(attackRegions.get(lastMove).isAnimationFinished(elapsedTime)) {
				state = State.WALK;
			}
		}
		this.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
//		this.setScale(1, 1);
		float dX = move.x() * SPEED * delta;
		float dY = move.y() * SPEED * delta;
		
		this.translate(dX, dY);
	}

	@Override
	public boolean keyDown(int keycode) {
		inputStack.push(Move.getByKey(keycode));
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		inputStack.remove(Move.getByKey(keycode));
		return true;
	}

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { 
		if(button == Buttons.LEFT) {
			state = State.ATTACK;
			elapsedTime = 0;
			return true;
		}
		return false; 
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false; }

	@Override
	public boolean scrolled(int amount) { return false; }
}
