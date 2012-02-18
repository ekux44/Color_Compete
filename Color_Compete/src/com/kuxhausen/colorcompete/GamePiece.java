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
	
	/** returns false if the piece dies */
	public abstract boolean update();
	
	public abstract void die();

	public abstract void draw(Canvas c);

	public abstract void reduceHealth(float damage);
	
	public abstract float getHealth();
	
}
