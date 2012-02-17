package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**(c) 2012 Eric Kuxhausen
 * @author Eric Kuxhausen
 */
public abstract class ResourceSpawner {

	Paint p;
	final int maxFill = 1000;// can't be changed without rewriting draw
	int fill,respawnRate,respawnCost;
	int xIncrements;
	float xIncrementCoefficient;// the inverse of the number of steps the
								// horizontal progress bar should be divided
								// into
	final float yIncrementCoefficient = .1f;

	public ResourceSpawner(Paint paint, int spawnRate, int spawnCost, int startingFill) {
		p = paint;
		respawnRate = spawnRate;
		respawnCost= spawnCost;
		fill = startingFill;
		xIncrementCoefficient = respawnRate / (yIncrementCoefficient * maxFill);

	}

	public void update() {
		fill += respawnRate;
		fill = Math.min(fill, maxFill);
	}

	public void draw(Canvas c, Paint backgroundP, float startX, float startY,
			float stopX, float stopY) {
		float incrementX = xIncrementCoefficient * (stopX - startX);
		float incrementY = yIncrementCoefficient * (stopY - startY);

		c.drawRect(startX, startY, stopX, stopY, backgroundP);
		c.drawRect(startX, startY + incrementY * (10 - fill / 100), stopX,
				stopY, p);
		if (fill % maxFill != 0)
			c.drawRect(startX, startY + incrementY * (9 - fill / 100), startX
					+ incrementX * ((fill % 100) / respawnRate), startY
					+ incrementY * (10 - fill / 100), p);
	}

	public boolean canSpawn(){
		return fill>=respawnCost;
	}
	
	/*any implimentation should decrement fill by respawnCost*/
	public abstract GamePiece spawnResource(float xCenter, float yCenter);

}
