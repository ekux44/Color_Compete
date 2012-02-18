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

	public BlueTower(float xCenter, float yCenter, GameBoard gBoard) {
		// if(p!=null){
		p = new Paint();
		p.setColor(Color.BLUE);
		p.setShadowLayer(health / 2f, 0, 0, Color.RED);
		// }
		xc = xCenter;
		yc = yCenter;
		gb = gBoard;
		health = 300;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, health / 12f, p);
	}

}
