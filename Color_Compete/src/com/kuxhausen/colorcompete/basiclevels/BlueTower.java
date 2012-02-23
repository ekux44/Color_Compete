package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameEngine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Player tower that serves as a passive barrier
 * 
 * @author Eric Kuxhausen
 */
public class BlueTower extends GamePiece {

	private static final int COST = 200;
	private static final float RADIUS_HEALTH_RATIO = 2, HEALTH_COST_RATIO = 1.5f;

	public BlueTower(float xCenter, float yCenter, GameEngine gEngine) {

		p = LevelLoader.blueTowerP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.towerMap;
		gList = gEng.towers;
		gb.register(this);
		health = COST * HEALTH_COST_RATIO;
		radius = radiusHealthRatio * (float) Math.sqrt(health);
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		return true;
	}
}
