package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Simple projectile that acts as a homing missile against an enemy
 * 
 * @author Eric Kuxhausen
 */
public class SimpleProjectile extends GamePiece {

	static Paint p;
	float speed;
	public static final int cost = 60;
	private float health;
	private static final float sizeingFactor = .5f;
	GamePiece target;

	public SimpleProjectile(float xCenter, float yCenter, GameEngine gEngine, GamePiece theTarget) {
		if (p == null) {
			p = new Paint();
			p.setColor(Color.RED);
		}
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.projectileMap;
		gb.register(this);
		health = cost / 2;
		speed = 3f;
		radius = sizeingFactor * (float) Math.sqrt(health);

		target = theTarget;
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {

		// check for collisions
		GamePiece maybeCollision = gEng.enemyMap.getNearestNeighbor(xc, yc);
		if (maybeCollision != null
				&& (maybeCollision.radius + this.radius) > GameBoard.distanceBetween(xc, yc, maybeCollision.xc,
						maybeCollision.yc)) {
			float damage = Math.min(health, maybeCollision.getHealth());
			maybeCollision.reduceHealth(damage);
			if (this.reduceHealth(damage))
				return false;
		}

		/* die if target is dead */
		if (target.getHealth() <= 0) {
			die();
			return false;
		}

		// update location tracking target
		float dx = target.xc - xc;
		float dy = target.yc - yc;
		dx = (float) (dx * Math.min(1f, speed / Math.sqrt(dx * dx + dy * dy)));
		dy = (float) (dy * Math.min(1f, speed / Math.sqrt(dx * dx + dy * dy)));
		if (gb.willMoveZones(xc, yc, xc + dx, yc + dy)) {
			gb.unregister(this);
			xc += dx;
			yc += dy;
			gb.register(this);
		} else {
			xc += dx;
			yc += dy;
		}
		return true;
	}

	@Override
	public void die() {
		health = 0;
		gb.unregister(this);
		gEng.projectiles.remove(this);
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
