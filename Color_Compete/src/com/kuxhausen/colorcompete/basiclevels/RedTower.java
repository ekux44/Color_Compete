package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;
import com.kuxhausen.colorcompete.Route;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Player tower that fires projectiles
 * 
 * @author Eric Kuxhausen
 */
public class RedTower extends GamePiece {

	private static final int COST = 300, spawnRate = 2, spawnPoolMax = 65;
	private static final float RADIUS_HEALTH_RATIO = 2, firingRadius = 200f, HEALTH_COST_RATIO = .5f;
	public static final float RED_RADIUS = 300;//cost * healthcost * radiushealth
	private int spawnPool = 200;

	public RedTower(float xCenter, float yCenter, GameEngine gEngine, Route route) {

		p = LevelLoader.redTowerP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		xc = xCenter;
		yc = yCenter;
		r = route;
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
		// update spawnPool, don't go beyond max
		spawnPool = Math.min(spawnPool + spawnRate, spawnPoolMax);

		// check if tower should fire
		GamePiece nearestEnemy = gEng.enemyMap.getNearest(xc, yc);
		if (nearestEnemy != null
				&& spawnPool >= SimpleProjectile.COST
				&& (nearestEnemy.radius + firingRadius) > GameBoard.distanceBetween(xc, yc, nearestEnemy.xc,
						nearestEnemy.yc)) {
			gEng.projectiles.add(new SimpleProjectile(xc, yc, gEng, nearestEnemy));
			spawnPool -= SimpleProjectile.COST;
		}
		return true;
	}

}
