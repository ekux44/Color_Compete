package com.kuxhausen.colorcompete;

import android.graphics.Canvas;

public abstract class Tower {

	float xc;
	float yc;
	float health;
	
	public abstract void update();
	public abstract void draw(Canvas c);
	
}
