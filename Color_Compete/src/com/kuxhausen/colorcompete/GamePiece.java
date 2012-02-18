package com.kuxhausen.colorcompete;

import android.graphics.Canvas;

/**
 * (c) 2012 Eric Kuxhausen
 * 
 * @author Eric Kuxhausen
 */
public abstract class GamePiece {

	float xc;
	float yc;
	GameBoard gb;
	GameEngine gEng;
	float radius;

	/** @return true if still alive */
	public abstract boolean update();

	public abstract void die();

	public abstract void draw(Canvas c);

	/** @return true if still alive */
	public abstract boolean reduceHealth(float damage);

	public abstract float getHealth();

}
