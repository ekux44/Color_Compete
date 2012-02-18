package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * 
 * @author Eric Kuxhausen
 */
public class RedTower extends GamePiece {

	static Paint p;
	private static final int cost = 300;
	private float health;

	public RedTower(float xCenter, float yCenter, GameEngine gEngine) {
		if (p == null) {
			p = new Paint();
			p.setColor(Color.RED);
		}
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.towerMap;
		gb.register(this);
		health = cost/2;
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		return true;
	}

	@Override
	public void die() {
		gb.unregister(this);
		gEng.towers.remove(this);		
	}
	
	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, health / 3f, p);
	}

	@Override
	public void reduceHealth(float damage) {
		health-=damage;
		if(health<0)
			die();
	}

	@Override
	public float getHealth() {
		return health;
	}

}
