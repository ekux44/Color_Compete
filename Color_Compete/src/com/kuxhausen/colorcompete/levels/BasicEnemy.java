package com.kuxhausen.colorcompete.levels;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Basic enemy that chargers as player spanners
 * 
 * @author Eric Kuxhausen
 */
public class BasicEnemy extends GamePiece {

	static Paint p;
	float speed=2f;
	public static final int cost = 180;
	private float health;
	private static final float sizeingFactor = 2, healthCostRatio = .5f;

	public BasicEnemy(float xCenter, float yCenter, GameEngine gEngine) {
		if (p == null) {
			p = new Paint();
			p.setColor(Color.BLACK);
		}
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.enemyMap;
		gb.register(this);
		health = cost * healthCostRatio;
		radius = sizeingFactor * (float) Math.sqrt(health);
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		// check to see if reach resource spawner
		if ((xc - speed) < (gEng.width * gEng.spawningRightEdgeFactor)) {
			gEng.spawns[gEng.whichResourceSpawner(yc)].takeDamage((int) health);
			die();
			return false;
		}

		// check for collisions
		GamePiece maybeCollision = gEng.towerMap.getNearestNeighbor(xc, yc);
		if (maybeCollision != null
				&& (maybeCollision.radius + this.radius) > GameBoard.distanceBetween(xc, yc, maybeCollision.xc,
						maybeCollision.yc)) {
			float damage = Math.min(health, maybeCollision.getHealth());
			maybeCollision.reduceHealth(damage);
			if (this.reduceHealth(damage))
				return false;
		}

		// update location
		if (gb.willMoveZones(xc, yc, xc - speed, yc)) {
			gb.unregister(this);
			xc -= speed;
			gb.register(this);
		} else
			xc -= speed;

		return true;
	}

	@Override
	public void die() {
		health = 0;
		gb.unregister(this);
		gEng.enemies.remove(this);
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, radius, p);
	}

	@Override
	/** @return true still alive*/
	public boolean reduceHealth(float damage) {
		health -= damage;
		if (health <= 0) {
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
