package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**(c) 2012 Eric Kuxhausen
 * @author Eric Kuxhausen
 */
public class BasicEnemy extends GamePiece {

	static Paint p;
	float speed;

	public BasicEnemy(float xCenter, float yCenter) {
		// if(p!=null){
		p = new Paint();
		p.setColor(Color.RED);
		p.setShadowLayer(health / 2f, 0, 0, Color.BLACK);
		// }
		xc = xCenter;
		yc = yCenter;
		health = 10;
		speed = 1f;
	}

	@Override
	public void update() {
		yc-=speed;
	}

	@Override
	public void draw(Canvas c) {
		c.drawCircle(xc, yc, health / 3f, p);
	}
}
