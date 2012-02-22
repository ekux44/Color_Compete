package com.kuxhausen.colorcompete;

import com.kuxhausen.colorcompete.basiclevels.GamePiece;

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
	protected int fill, respawnRate, respawnCost;
	int xIncrements;
	float xIncrementCoefficient;// the inverse of the number of steps the horizontal progress bar should be divided into
	final static float yIncrementCoefficient = .1f;
	private int damage;
	boolean dead;
	protected GameEngine gEng;

	public ResourceSpawner(GameEngine gEngine, Paint readyPaint, Paint chargingPaint, int spawnRate, int spawnCost, int startingFill) {
		gEng = gEngine;
		readyP = readyPaint;
		chargingP = chargingPaint;
		respawnRate = spawnRate;
		respawnCost = spawnCost;
		fill = startingFill;
		xIncrementCoefficient = respawnRate / (yIncrementCoefficient * maxFill);
	}

	public void update() {
		fill += respawnRate;
		fill = Math.min(fill, maxFill - damage);
	}

	public void draw(Canvas c, Paint backgroundP, Paint blackP, float startX, float startY, float stopX, float stopY) {
		float incrementX = xIncrementCoefficient * (stopX - startX);
		float incrementY = yIncrementCoefficient * (stopY - startY);

		if(canSpawn())
			drawingP = readyP;
		else
			drawingP = chargingP;
		
		c.drawRect(startX, startY, stopX, stopY, backgroundP);
		c.drawRect(startX, startY + incrementY * (10 - fill / 100), stopX, stopY, drawingP);
		if (fill != 0)
			c.drawRect(startX, startY + incrementY * (9 - fill / 100), startX + incrementX
					* ((fill % 100) / respawnRate), startY + incrementY * (10 - fill / 100), drawingP);
		c.drawRect(startX, startY, stopX, startY + incrementY * (damage / 100), blackP);
		if (damage != 0)
			c.drawRect(stopX, startY + incrementY * (1 + damage / 100), stopX - incrementX
					* ((damage % 100) / respawnRate), startY + incrementY * (damage / 100), blackP);
	}

	public boolean canSpawn() {
		return fill >= respawnCost;
	}

	public void takeDamage(int dam) {
		damage += dam;
		if (damage >= maxFill) {
			damage = maxFill;
			dead = true;
			gEng.checkPlayerAlive();
		}
		Log.i("damage", "damage " + damage + "  to spawner colored" + readyP.getColor());
	}

	/** any implementation should decrement fill by respawnCost */
	public abstract GamePiece spawnResource(float xCenter, float yCenter);

}
