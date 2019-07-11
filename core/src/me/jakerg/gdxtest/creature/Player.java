package me.jakerg.gdxtest.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite{
	
	private final int SPEED = 200;
	
	public Player() {
		super(new Texture(Gdx.files.internal("badlogic.jpg")), 100, 100);
	}
	
	public void update(float delta) {
		float dX = 0;
		float dY = 0;
		if(Gdx.input.isKeyPressed(Keys.UP))
			dY += SPEED * delta;
		
		if(Gdx.input.isKeyPressed(Keys.DOWN))
			dY += -SPEED * delta;
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			dX += SPEED * delta;
		
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			dX += -SPEED * delta;
		
		if(dX != 0)
			dY = 0;
		else if(dY != 0)
			dX = 0;
		
		this.translate(dX, dY);
	}
}
