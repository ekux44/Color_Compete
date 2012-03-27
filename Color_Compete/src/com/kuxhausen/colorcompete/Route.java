package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

public class Route {

	public ArrayList<Pair> points;
	private Path visualPath;
	private Paint p;

	public Route(Paint paint) {

		points = new ArrayList<Pair>();

		visualPath = new Path();

		p = paint;

	}

	public void addPoint(float x, float y) {
		if (points.size() < 3)
			visualPath.moveTo(x, y);
		else
			visualPath.lineTo(x, y);

		Pair point = new Pair(x,y);
		points.add(point);
	}
	
	public Pair moveAlongRouter(float speed, boolean foward){
		return new Pair(speed,0);
	}

	public void clear() {
		points.clear();
		visualPath.reset();
	}

	/**
	 * @param c
	 *            Canvas onto which GamePiece will draw itself
	 */
	public void draw(Canvas c, float xOffset) {
		c.drawPath(visualPath, p);
		// TODO incorporate xOffset
	}
}
