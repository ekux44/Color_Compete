package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RedTower extends Tower {

	static Paint p;
	
	public RedTower(float xCenter, float yCenter){
		//if(p!=null){
			p = new Paint();
			p.setColor(Color.RED);
			p.setShadowLayer(health/2f, 0, 0, Color.RED);
		//}
		xc=xCenter;
		yc=yCenter;
		health=100;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, health/3f, p);
	}

}
