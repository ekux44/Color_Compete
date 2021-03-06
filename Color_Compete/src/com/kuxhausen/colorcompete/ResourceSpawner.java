package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Spawner that generatates player towers on demand when available
 * 
 * @author Eric Kuxhausen
 */
public abstract class ResourceSpawner {

	Paint readyP, chargingP, drawingP;
	final static int maxFill = 1000;// can't be changed without rewriting draw
	protected int respawnCost;
	protected float respawnRate, fill;
	int xIncrements;
	float xIncrementCoefficient;// the inverse of the number of steps the horizontal progress bar should be divided into
	final static float yIncrementCoefficient = .1f;
	private int damage;
	boolean dead;
	protected GameEngine gEng;
	protected int numberSpawnedAndAlive;

	public ResourceSpawner(GameEngine gEngine, Paint readyPaint, Paint chargingPaint, float spawnRate, int spawnCost,
			int startingFill) {
		gEng = gEngine;
		readyP = readyPaint;
		chargingP = chargingPaint;
		respawnRate = spawnRate;
		respawnCost = spawnCost;
		fill = startingFill;
		xIncrementCoefficient = respawnRate / (yIncrementCoefficient * maxFill);
	}

	public void update() {
		fill += respawnRate / (Math.pow(1.5, numberSpawnedAndAlive));
		fill = Math.min(fill, maxFill - damage);
	}

	public void draw(Canvas c, Paint backgroundP, Paint enemyP, float startX, float startY, float stopX, float stopY) {
		float incrementX = xIncrementCoefficient * (stopX - startX);
		float incrementY = yIncrementCoefficient * (stopY - startY);

		if (canSpawn())
			drawingP = readyP;
		else
			drawingP = chargingP;

		c.drawRect(startX, startY, stopX, stopY, backgroundP);
		c.drawRect(startX, startY + incrementY * (10 - ((int) fill) / 100), stopX, stopY, drawingP);
		if (fill != 0)
			c.drawRect(startX, startY + incrementY * (9 - ((int) fill) / 100), startX + incrementX
					* ((((int) fill) % 100) / Math.max(1, respawnRate)), startY + incrementY
					* (10 - ((int) fill) / 100), drawingP);
		c.drawRect(startX, startY, stopX, startY + incrementY * (damage / 100), enemyP);
		if (damage != 0)
			c.drawRect(stopX, startY + incrementY * (1 + damage / 100),
					stopX - incrementX * ((damage % 100) / Math.max(1, respawnRate)), startY + incrementY
							* (damage / 100), enemyP);
	}

	public void drawTouch(Canvas c, float tX, float tY) {
		c.drawCircle(tX, tY, 50f, chargingP);
	}

	public boolean canSpawn() {
		return fill >= respawnCost;
	}

	public void takeDamage(int dam) {
		gEng.gView.parentActivity.vibrate();

		damage += dam;
		if (damage >= maxFill) {
			damage = maxFill;
			dead = true;
			gEng.checkPlayerAlive();
		}
		Log.i("damage", "damage " + damage + "  to spawner colored" + readyP.getColor());
	}

	public void offspringDied() {
		numberSpawnedAndAlive--;
	}

	public int remianingHealth() {
		if (dead)
			return 0;
		return (int) fill;
	}

	/** any implementation should decrement fill by respawnCost and increment numberSpawnedAndAlive */
	public abstract GamePiece spawnResource(float xCenter, float yCenter, Route r);

	/** */
	public abstract Route spawnRoute();

}
