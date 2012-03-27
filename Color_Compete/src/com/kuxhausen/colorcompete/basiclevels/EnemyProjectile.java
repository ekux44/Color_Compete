package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Enemy projectile that acts as a homing missile against the player towers
 * 
 * @author Eric Kuxhausen
 */
public class EnemyProjectile extends GamePiece {

	float speed = 3f;
	public static final int COST = 60;
	private static final float RADIUS_HEALTH_RATIO = .5f, HEALTH_COST_RATIO = .5f;
	GamePiece target;

	public EnemyProjectile(float xCenter, float yCenter, GameEngine gEngine, GamePiece theTarget) {

		p = LevelLoader.enemyProjectileP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.projectileMap;
		gList = gEng.projectiles;
		gb.register(this);
		health = COST * HEALTH_COST_RATIO;
		radius = radiusHealthRatio * (float) Math.sqrt(health);

		target = theTarget;
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {

		// check for collisions
		GamePiece maybeCollision = gEng.towerMap.getNearest(xc, yc);
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
		gb.move(this, dx, dy);

		return true;
	}
}
