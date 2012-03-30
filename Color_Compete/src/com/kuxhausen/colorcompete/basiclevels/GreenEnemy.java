package com.kuxhausen.colorcompete.basiclevels;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Medium sized enemy that chargers at player spanners
 * 
 * @author Eric Kuxhausen
 */
public class GreenEnemy extends GamePiece {

	float speed = 4f;
	public static final int COST = 250;
	private static final float RADIUS_HEALTH_RATIO = 2, HEALTH_COST_RATIO = .25f;
	Paint pInner;

	public GreenEnemy(float xCenter, float yCenter, GameEngine gEngine) {

		p = LevelLoader.greenEnemyP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.enemyMap;
		gb.register(this);
		health = COST * HEALTH_COST_RATIO;
		radius = radiusHealthRatio * (float) Math.sqrt(health);
		pInner = LevelLoader.greenTowerP;
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		// check to see if reach resource spawner
		if ((xc - speed) <= gEng.cameraOffset + GameEngine.RIGHT_EDGE_OF_SPAWNER_FACTOR * gEng.width) {
			gEng.spawns[gEng.whichResourceSpawner(yc)].takeDamage((int) health);
			die();
			return false;
		}

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

	public static int getCost() {
		return COST;
	}

	@Override
	public void draw(Canvas c, float xOffset) {
		super.draw(c, xOffset);
		c.drawCircle(xc + xOffset, yc, radius / 3, pInner);
	}
}