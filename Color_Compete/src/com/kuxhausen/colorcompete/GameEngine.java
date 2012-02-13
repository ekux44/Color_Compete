package com.kuxhausen.colorcompete;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class GameEngine {

	
	private Paint backgroundPaint;
	
	//TODO update game, update physics, etc
	
	public void Init(Resources resources) {
		
		// black painter below to clear the screen before the game is rendered
		backgroundPaint = new Paint();
		backgroundPaint.setARGB(255, 0, 64, 0);
		}
	
	public void SampleTouchInput(MotionEvent event)
	{
		// TODO Auto-generated method stub
	}
	
	public void Draw(Canvas c) {
		
		// clear the screen with the background painter.
		c.drawRect(0, 0, c.getWidth(), c.getHeight(), backgroundPaint);
		
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setTextSize(30);
		c.drawText("testing", 50, 50, p);
	}

	

}
