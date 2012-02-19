package com.kuxhausen.colorcompete;

import android.graphics.Canvas;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Abstract Class defining common methods and fields all towers, enemies, and projectiles inherit.
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

	/**
	 * Unregisters GamePiece from GameBoard and GameEngine; sets health to 0 so any other objects holding references to
	 * this GamePiece can figure out when to deallocate
	 */
	public abstract void die();

	/**
	 * @param c
	 *            Canvas onto which GamePiece will draw itself
	 */
	public abstract void draw(Canvas c);

	/**
	 * @return true if still alive
	 * @param damage
	 *            amount to reduce health by
	 */
	public abstract boolean reduceHealth(float damage);

	/** @return health */
	public abstract float getHealth();

}
