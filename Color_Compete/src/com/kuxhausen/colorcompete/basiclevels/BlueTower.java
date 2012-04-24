package com.kuxhausen.colorcompete.basiclevels;

import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;
import com.kuxhausen.colorcompete.ResourceSpawner;
import com.kuxhausen.colorcompete.Route;

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
	private static int numberOfMe;

	public BlueTower(float xCenter, float yCenter, GameEngine gEngine, Route route, ResourceSpawner rspwn) {
		super(xCenter, yCenter, gEngine, route, rspwn);
		p = LevelLoader.blueTowerP;
		radiusHealthRatio = RADIUS_HEALTH_RATIO;
		gb = gEng.towerMap;
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
