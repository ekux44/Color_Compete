package com.kuxhausen.colorcompete;

import java.util.Random;

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
	Class<?>[] enemyTypes;
	float[] probabilities;
	Class<?> nextType;
	int nextCost;

	public EnemySpawner(GameEngine gEngine, Class<?>[] enemies, float[] probWeights, int toSpawn, int rateToSpawn,
			int spawnAccelerationInterval) {
		p = new Paint();
		p.setShadowLayer(10, 0, 0, Color.GRAY);
		p.setColor(Color.DKGRAY);
		gEng = gEngine;
		enemyTypes = enemies;
		probabilities = probWeights;
		totalSpawns = toSpawn;
		spawnsRemaining = totalSpawns;
		spawnRate = rateToSpawn;
		spawnAccelInterval += spawnAccelerationInterval;
		updateNext();
	}

	private void updateNext() {
		try {
			float selectionValue = r.nextFloat();
			selector: for (int i = 0; i < enemyTypes.length; i++) {
				if (selectionValue < probabilities[i]) {
					nextType = enemyTypes[i];
					break selector;
				}
			}
			nextCost = (Integer) nextType.getDeclaredMethod("getCost").invoke(null);
			// nextType.getDeclaredField("COST").
		} catch (Exception e) {
		}
	}

	public void update() {

		if (spawnsRemaining <= 0) {

		} else {
			pendingSpawnUnits += spawnRate;
			spawnsRemaining -= spawnRate;
			spawnAccelerationCount++;
			if (spawnAccelerationCount >= spawnAccelInterval) {
				spawnAccelerationCount = 0;
				spawnRate += (10 * spawnRate) / 9;
			}

			int maybeSpawn = r.nextInt(pendingSpawnUnits);

			if (maybeSpawn > nextCost) {
				try {

					gEng.enemies.add((GamePiece) nextType.getConstructor(float.class, float.class, GameEngine.class)
							.newInstance(gEng.width + gEng.cameraOffset, r.nextInt(gEng.height), gEng));
					maybeSpawn -= nextCost;
					pendingSpawnUnits -= nextCost;
				} catch (Exception e) {
				}

				updateNext();
			}

		}
	}

	public void draw(Canvas c) {

		c.drawRect(c.getWidth() * (1f - .08f * spawnsRemaining / totalSpawns), 0, c.getWidth(), c.getHeight(), p);

	}

	public void takeDamage(int d) {
		damage += d;
		gEng.playerScore += d;
	}
}
