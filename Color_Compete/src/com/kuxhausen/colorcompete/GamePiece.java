package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

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
	protected float health, radiusHealthRatio;
	protected int cost;
	protected Paint p;
	protected Route r;
	
	/** @return true if still alive */
	public abstract boolean update();

	/**
	 * Unregisters GamePiece from GameBoard and GameEngine; sets health to 0 so any other objects holding references to
	 * this GamePiece can figure out when to deallocate
	 */
	public void die() {
		health = 0;
		gb.unregister(this);
		gEng.activeRoutes.remove(r);
	}

	/**
	 * @param c
	 *            Canvas onto which GamePiece will draw itself
	 */
	public void draw(Canvas c, float xOffset) {
		c.drawCircle(xc + xOffset, yc, radius, p);
	}

	/**
	 * @return true if still alive
	 * @param damage
	 *            amount to reduce health by
	 */
	/** @return true still alive */
	public boolean reduceHealth(float damage) {
		health -= damage;
		if (health <= 0) {
			die();
			return false;
		}
		radius = radiusHealthRatio * (float) Math.sqrt(health);
		return true;
	}

	/** @return health */
	public float getHealth() {
		return health;
	}

}
