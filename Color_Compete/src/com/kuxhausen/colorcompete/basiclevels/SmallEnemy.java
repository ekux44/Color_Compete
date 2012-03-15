package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Basic enemy that chargers as player spanners
 * 
 * @author Eric Kuxhausen
 */
public class SmallEnemy extends GamePiece {

	float speed = 2f;
	public static final int COST = 150;
	private static final float RADIUS_HEALTH_RATIO = 2, HEALTH_COST_RATIO = .5f;

	public SmallEnemy(float xCenter, float yCenter, GameEngine gEngine) {

		p = LevelLoader.smallEnemyP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.enemyMap;
		gList = gEng.enemies;
		gb.register(this);
		health = COST * HEALTH_COST_RATIO;
		radius = radiusHealthRatio * (float) Math.sqrt(health);
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		// check to see if reach resource spawner
		if ((xc - speed) <= gEng.cameraOffset) {
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
		gb.move(this, -speed, 0);

		return true;
	}

	@Override
	/** @return true still alive*/
	public boolean reduceHealth(float damage) {
		gEng.playerScore += Math.min(health, damage);
		return super.reduceHealth(damage);
	}
}
