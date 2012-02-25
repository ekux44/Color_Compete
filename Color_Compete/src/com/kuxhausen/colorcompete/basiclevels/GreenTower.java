package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Player tower that serves as a passive barrier
 * 
 * @author Eric Kuxhausen
 */
public class GreenTower extends GamePiece {

	float speed = 3f;
	private static final int COST = 200;
	private static final float RADIUS_HEALTH_RATIO = 2, HEALTH_COST_RATIO = 1f;

	public GreenTower(float xCenter, float yCenter, GameEngine gEngine) {

		p = LevelLoader.greenTowerP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.towerMap;
		gList = gEng.towers;
		gb.register(this);
		health = COST * HEALTH_COST_RATIO;
		radius = radiusHealthRatio * (float) Math.sqrt(health);
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
			//check to see if reached enemy spawner
			if ((xc + speed) >= (gEng.width * gEng.enemyLeftEdgeFactor)) {
					gEng.enemyBase.takeDamage((int)health);
					die();
					return false;
				}
		
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

			// update location
			gb.move(this, speed, 0);

			return true;
	}
}