package com.kuxhausen.colorcompete.levels;

import com.kuxhausen.colorcompete.GameBoard;
import com.kuxhausen.colorcompete.GameEngine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Player tower that fires projectiles
 * 
 * @author Eric Kuxhausen
 */
public class RedTower extends GamePiece {

	static Paint p;
	private static final int cost = 300, spawnRate = 2, spawnPoolMax = 75;
	private float health;
	private static final float sizeingFactor = 2, firingRadius = 100f, healthCostRatio = .5f;
	private int spawnPool=200;

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
		health = cost * healthCostRatio;
		radius = sizeingFactor * (float) Math.sqrt(health);
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		// update spawnPool, don't go beyond max
		spawnPool = Math.min(spawnPool + spawnRate, spawnPoolMax);

		// check if tower should fire
		GamePiece nearestEnemy = gEng.enemyMap.getNearestNeighbor(xc, yc);
		if (nearestEnemy != null
				&& spawnPool >= SimpleProjectile.cost
				&& (nearestEnemy.radius + firingRadius) < GameBoard.distanceBetween(xc, yc, nearestEnemy.xc,
						nearestEnemy.yc)) {
			gEng.projectiles.add(new SimpleProjectile(xc, yc, gEng, nearestEnemy));
			spawnPool -= SimpleProjectile.cost;
		}
		return true;
	}

	@Override
	public void die() {
		health = 0;
		gb.unregister(this);
		gEng.towers.remove(this);
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, radius, p);
	}

	@Override
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
