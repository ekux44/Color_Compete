package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Hold an a barrier that cannot be crossed by game piece
 * 
 * @author Eric Kuxhausen
 */
public class Terrain {

	float x1, y1, x2, y2;
	static Paint p;
	protected GameEngine gEng;

	public Terrain(float startX, float startY, float stopX, float stopY, GameEngine gEngine) {
		if (p != null) {
			p = new Paint();
			p.setColor(Color.parseColor("#FF603311"));
			p.setStrokeWidth(4);
		}
		x1 = startX;
		y1 = startY;
		x2 = stopX;
		y2 = stopY;
		gEng = gEngine;
	}

	/**
	 * @param c
	 *            Canvas onto which GamePiece will draw itself
	 */
	public void draw(Canvas c) {
		c.drawLine(x1, y1, x2, y2, p);
	}
}
