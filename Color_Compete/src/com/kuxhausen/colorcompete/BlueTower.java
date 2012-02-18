package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * 
 * @author Eric Kuxhausen
 */
public class BlueTower extends GamePiece {

	static Paint p;
	private static final int cost = 200;

	public BlueTower(float xCenter, float yCenter, GameEngine gEngine) {
		if (p == null) {
			p = new Paint();
			p.setColor(Color.BLUE);
		}
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.towerMap;
		gb.register(this);
		health = 3*cost/2;
	}

	@Override
	public void update() {
	}
	
	@Override
	public void die() {
		gb.unregister(this);
		gEng.towers.remove(this);		
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, health / 9f, p);
	}

}
