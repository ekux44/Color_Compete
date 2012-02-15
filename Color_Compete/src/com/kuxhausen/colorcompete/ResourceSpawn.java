package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class ResourceSpawn {
	
	Paint p;
	int respawnRate =10;
	final int maxFill = 1000;//can't be changed without rewriting draw
	int fill = 0;
	
	public ResourceSpawn(Paint paint){
		p = paint;
	}
	public void update(){
		fill+=respawnRate;
		fill= Math.min(fill, maxFill);
	}
	public void draw(Canvas c, Paint backgroundP, float startX, float startY, float stopX, float stopY){
		Log.i("testing",startX+" "+startY+" "+" "+stopX+" "+stopY);
		
		float incrementY = .1f * (stopY-startY);
		float incrementX = .1f * (stopX-startX);
		
		c.drawRect(startX, startY, stopX, stopY, backgroundP);
		c.drawRect(startX, startY+incrementY*(10-fill/100), stopX, stopY, p);
		c.drawRect(startX, startY+incrementY*(9-fill/100), startX+incrementX*((fill/10)%10), startY+incrementY*(10-fill/100), p);
	}
}
