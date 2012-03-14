package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;

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

	/** Merge this blue tower with b if towers are too close together */
	public boolean merge(BlueTower b) {
		// TODO implement logic to merge or move towers that get dropped on eachother
		return false;
	}
}
