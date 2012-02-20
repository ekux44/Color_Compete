package com.kuxhausen.colorcompete;

import java.util.Random;

import com.kuxhausen.colorcompete.levels.BasicEnemy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Spawner that generates enemies based on spawn rate until all of the spawn points have been spent
 * 
 * @author Eric Kuxhausen
 */
public class EnemySpawner {
	Paint p;
	int totalSpawns, spawnsRemaining, damage, spawnRate, pendingSpawnUnits, spawnAccelInterval, spawnAccelerationCount;
	Random r = new Random();
	GameEngine gEng;

	public EnemySpawner(GameEngine gEngine, int toSpawn, int rateToSpawn, int spawnAccelerationInterval) {
		p = new Paint();
		p.setShadowLayer(10, 0, 0, Color.GRAY);
		gEng = gEngine;
		totalSpawns = toSpawn;
		spawnsRemaining = totalSpawns;
		spawnRate = rateToSpawn;
		spawnAccelInterval += spawnAccelerationInterval;
	}

	public void update() {
		pendingSpawnUnits += spawnRate;
		spawnsRemaining -= spawnRate;
		spawnAccelerationCount++;
		if (spawnAccelerationCount >= spawnAccelInterval) {
			spawnAccelerationCount = 0;
			spawnRate += (10 * spawnRate) / 9;
		}
		if (spawnsRemaining <= BasicEnemy.cost)
			gEng.endGame(true);
		int maybeSpawn = r.nextInt(pendingSpawnUnits);

		if (maybeSpawn > BasicEnemy.cost) {
			gEng.enemies.add(new BasicEnemy(gEng.width * gEng.enemyLeftEdgeFactor - 1, r.nextInt(gEng.height), gEng));
			maybeSpawn -= BasicEnemy.cost;
			pendingSpawnUnits -= BasicEnemy.cost;
		}
	}

	public void draw(Canvas c) {

		c.drawRect(c.getWidth() * (1f - .08f * spawnsRemaining / (float) totalSpawns), 0, c.getWidth(), c.getHeight(),
				p);
	}
}
