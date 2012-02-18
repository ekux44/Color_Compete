package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * (c) 2012 Eric Kuxhausen
 * 
 * @author Eric Kuxhausen
 */
public class BasicEnemy extends GamePiece {

	static Paint p;
	float speed;
	public static final int cost = 180;
	private float health;

	public BasicEnemy(float xCenter, float yCenter, GameEngine gEngine) {
		 if(p==null){
		p = new Paint();
		p.setColor(Color.BLACK);
		p.setShadowLayer(health / 2f, 0, 0, Color.BLACK);
		}
		xc = xCenter;
		yc = yCenter;
		gEng = gEngine;
		gb = gEng.enemyMap;
		gb.register(this);
		health = cost/2;
		speed = 2f;
	}

	@Override
	/** returns false if the piece dies */
	public boolean update() {
		if((xc - speed )< (gEng.width*gEng.spawningRightEdgeFactor)){
			gEng.spawns[gEng.whichResourceSpawner(yc)].takeDamage((int)health);
			die();
			return false;
		}
		if (gb.willMoveZones(xc, yc, xc - speed, yc)) {
			gb.unregister(this);
			xc -= speed;
			gb.register(this);
		} else
			xc -= speed;
		
		return true;
	}
	
	@Override
	public void die() {
		gb.unregister(this);
		gEng.enemies.remove(this);
	}
	
	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, health / 3f, p);
	}

	@Override
	public void reduceHealth(float damage) {
		health-=damage;
		if(health<0)
			die();
	}

	@Override
	public float getHealth() {
		return health;
	}
}
