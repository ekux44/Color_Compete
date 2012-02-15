package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class EnemySpawner {
	Paint p;
	int spawnRate = 2;
	int spawnsRemaining;
	final int totalSpawns;
	
	public EnemySpawner(int enemiesToSpawn){
		p = new Paint();
		p.setShadowLayer(20, 0, 0, Color.GRAY);
		totalSpawns = enemiesToSpawn;
		spawnsRemaining=totalSpawns;
	}
	public void update(){
		spawnsRemaining-=spawnRate;
	}
	public void draw(Canvas c){
		
		c.drawRect(c.getWidth()*(1f-.08f*spawnsRemaining/(float)totalSpawns) , 0, c.getWidth(), c.getHeight(), p);
	}
}
