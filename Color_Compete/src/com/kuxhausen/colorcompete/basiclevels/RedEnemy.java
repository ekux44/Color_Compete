package com.kuxhausen.colorcompete.basiclevels;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Enemy that chargers at player spawners and fires at player towers
 * 
 * @author Eric Kuxhausen
 */
public class RedEnemy extends GamePiece {

	float speed = 1f;
	public static final int COST = 250, spawnRate = 2, spawnPoolMax = 65;
	private static final float RADIUS_HEALTH_RATIO = 2, firingRadius = 200f, HEALTH_COST_RATIO = .25f;
	private int spawnPool = 200;
	Paint pInner;

	public RedEnemy(float xCenter, float yCenter, GameEngine gEngine) {

		p = LevelLoader.redEnemyP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.enemyMap;
		gList = gEng.enemies;
		gb.register(this);
		health = COST * HEALTH_COST_RATIO;
		radius = radiusHealthRatio * (float) Math.sqrt(health);
		pInner = LevelLoader.redTowerP;
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		// check to see if reach resource spawner
		if ((xc - speed) <= gEng.cameraOffset+ GameEngine.RIGHT_EDGE_OF_SPAWNER_FACTOR*gEng.width) {
			gEng.spawns[gEng.whichResourceSpawner(yc)].takeDamage((int) health);
			die();
			return false;
		}

		// check if tower should fire
		GamePiece nearestTower = gEng.towerMap.getNearestNeighbor(xc, yc);
		if (nearestTower != null
				&& spawnPool >= EnemyProjectile.COST
				&& (nearestTower.radius + firingRadius) > GameBoard.distanceBetween(xc, yc, nearestTower.xc,
						nearestTower.yc)) {
			gEng.projectiles.add(new EnemyProjectile(xc, yc, gEng, nearestTower));
			spawnPool -= EnemyProjectile.COST;
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

	public static int getCost() {
		return COST;
	}

	@Override
	public void draw(Canvas c, float xOffset) {
		super.draw(c, xOffset);
		c.drawCircle(xc + xOffset, yc, radius / 3, pInner);
	}
}