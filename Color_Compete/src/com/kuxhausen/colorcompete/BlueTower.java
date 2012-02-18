package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * 
 * @author Eric Kuxhausen
 */
public class BlueTower extends GamePiece {

	static Paint p;
	private static final int cost = 200;
	private float health;
	private static final float sizeingFactor = 2;

	public BlueTower(float xCenter, float yCenter, GameEngine gEngine) {
		if (p == null) {
			p = new Paint();
			p.setColor(Color.BLUE);
		}
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.towerMap;
		gb.register(this);
		health = 3 * cost / 2;
		radius = sizeingFactor * (float) Math.sqrt(health);
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		return true;
	}

	@Override
	public void die() {
		health=0;
		gb.unregister(this);
		gEng.towers.remove(this);
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, radius, p);
	}

	@Override
	public boolean reduceHealth(float damage) {
		health -= damage;
		if (health <= 0){
			die();
			return false;
		}
		radius = sizeingFactor * (float) Math.sqrt(health);
		return true;
	}

	@Override
	public float getHealth() {
		return health;
	}

}
