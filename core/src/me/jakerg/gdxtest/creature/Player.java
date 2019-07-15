package me.jakerg.gdxtest.creature;

import java.awt.Point;
import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import me.jakerg.gdxtest.DungeonGame;
import me.jakerg.gdxtest.object.utils.Box2DUtils;
import me.jakerg.gdxtest.screens.DungeonScreen;
import me.jakerg.rougelike.Move;

public class Player extends Sprite implements InputProcessor{
	enum State { WALK, ATTACK };
	
	
	private final int SPEED = 75;
	private Stack<Move> inputStack;
	private Move lastMove;
	private State state;
	private Body body;
	public Animation<TextureRegion> runningAnimation;
	public HashMap<Move, Animation<TextureRegion>> animations;
	public HashMap<Move, Animation<TextureRegion>> attackRegions;
	public Sprite sword;
	private Body swordBody;
	private float elapsedTime;
	
	public Player(DungeonScreen screen, World world, Point center) {
		super(screen.atlas.findRegions("walkup").get(0));
		this.setOriginCenter();
		this.setPosition(0, 0);
		this.setOriginBasedPosition(center.x, center.y);
		
		body = Box2DUtils.createCircBody(world, this.getX(), this.getY(), (this.getHeight() - 4) / 2);
		body.getFixtureList().get(0).setUserData("Player");
		
		sword = new Sprite(screen.atlas.findRegion("swordattack"));
		sword.setOriginCenter();
		sword.setAlpha(0);
		
		swordBody = Box2DUtils.createDynRectBody(world, sword);
		
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

		Animation<TextureRegion> a = new Animation<>(s, screen.atlas.findRegions("walkup"), PlayMode.LOOP);
		animations.put(Move.UP, a);
		
		a = new Animation<>(s, screen.atlas.findRegions("walkdown"), PlayMode.LOOP);
		animations.put(Move.DOWN, a);

		a = new Animation<>(s, screen.atlas.findRegions("walkleft"), PlayMode.LOOP);
		animations.put(Move.LEFT, a);

		a = new Animation<>(s, screen.atlas.findRegions("walkright"), PlayMode.LOOP);
		animations.put(Move.RIGHT, a);
		
		
		s = 1/5f;
		attackRegions = new HashMap<>();

		a = new Animation<>(s, screen.atlas.findRegions("attackup"), PlayMode.NORMAL);
		attackRegions.put(Move.UP, a);

		a = new Animation<>(s, screen.atlas.findRegions("attackdown"), PlayMode.NORMAL);
		attackRegions.put(Move.DOWN, a);

		a = new Animation<>(s, screen.atlas.findRegions("attackleft"), PlayMode.NORMAL);
		attackRegions.put(Move.LEFT, a);

		a = new Animation<>(s, screen.atlas.findRegions("attackright"), PlayMode.NORMAL);
		attackRegions.put(Move.RIGHT, a);

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
			drawSword();
			if(attackRegions.get(lastMove).isAnimationFinished(elapsedTime)) {
				state = State.WALK;
				sword.setAlpha(0);
				swordBody.setActive(false);
			}
		}
//		this.setScale(1, 1);
		float dX = move.x() * SPEED / Box2DUtils.PPM;
		float dY = move.y() * SPEED / Box2DUtils.PPM;
		
		body.setLinearVelocity(dX, dY);
		Box2DUtils.setSpritePosition(this, body);
	}

	private void drawSword() {
		float sX, sY, deg;
		switch(lastMove) {
		case UP:
			sX = this.getX() + this.getWidth() / 3 + sword.getWidth() / 2;
			sY = this.getY() + this.getHeight() + sword.getHeight() / 2;
			deg = 0;
			break;
		case DOWN:
			sX = this.getX() + this.getWidth() / 2 + sword.getWidth() / 2;
			sY = this.getY() - this.getHeight() + 5 + sword.getHeight() / 2;
			deg = 180;
			break;
		case LEFT:
			sX = this.getX() - this.getWidth() / 2 + 2;
			sY = this.getY() + this.getHeight() / 2;
			deg = 90;
			break;
		case RIGHT:
			sX = this.getX() + this.getWidth() * 3 / 2 - 2;
			sY = this.getY() + this.getHeight() / 2;
			deg = 270;
			break;
		default:
			sX = 0;
			sY = 0;
			deg = 0;
			break;
		}
		
		deg = (float) (deg * (Math.PI / 180));
		swordBody.setActive(true);
		swordBody.setTransform(sX / 16, sY / 16, deg);
		Box2DUtils.setSpritePosition(sword, swordBody);
		
		sword.setAlpha(1);
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
