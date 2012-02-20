package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;

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

	float speed = 3f;
	public static final int cost = 60;
	private static final float sizeingFactor = .5f, healthCostRatio = .5f;
	GamePiece target;

	public SimpleProjectile(float xCenter, float yCenter, GameEngine gEngine, GamePiece theTarget) {
		
		p = LevelLoader.simpleProjectileP;
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.projectileMap;
		gList = gEng.projectiles;
		gb.register(this);
		health = cost * healthCostRatio;
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

}
