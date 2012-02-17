package com.kuxhausen.colorcompete;

import android.graphics.Canvas;

/**(c) 2012 Eric Kuxhausen
 * @author Eric Kuxhausen
 */
public abstract class Enemy {
	float xc;
	float yc;
	float health;

	public abstract void update();

	public abstract void draw(Canvas c);
}