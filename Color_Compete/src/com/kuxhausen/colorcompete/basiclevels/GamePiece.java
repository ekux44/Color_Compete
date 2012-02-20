package com.kuxhausen.colorcompete.basiclevels;

import java.util.ArrayList;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;

import android.graphics.Canvas;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Abstract Class defining common methods and fields all towers, enemies, and projectiles inherit.
 * 
 * @author Eric Kuxhausen
 */
public abstract class GamePiece {

	public float xc, yc, radius;
	protected GameBoard gb;
	protected GameEngine gEng;
	protected ArrayList<GamePiece> gList;
	protected float health;

	/** @return true if still alive */
	public abstract boolean update();

	/**
	 * Unregisters GamePiece from GameBoard and GameEngine; sets health to 0 so any other objects holding references to
	 * this GamePiece can figure out when to deallocate
	 */
	public void die() {
		health = 0;
		gb.unregister(this);
		gList.remove(this);
	}

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
	public float getHealth() {
		return health;
	}

}
