package com.kuxhausen.colorcompete;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class GameEngine {

	
	private Paint backgroundP;
	private Paint textP;
	
	//TODO update game, update physics, etc
	
	public void Init(Resources resources) {
		
		// black painter below to clear the screen before the game is rendered
		backgroundP = new Paint();
		backgroundP.setARGB(255, 0, 64, 0);
		
		textP = new Paint();
		textP.setARGB(255,255, 0, 255);
		textP.setTextSize(20);
		
	}
	
	public void SampleTouchInput(MotionEvent event)
	{
		// TODO Auto-generated method stub
	}
	
	public void Draw(Canvas c, int FPS) {
		
		// clear the screen with the background painter.
		c.drawRect(0, 0, c.getWidth(), c.getHeight(), backgroundP);
		c.drawText("FPS:"+FPS, c.getWidth()-80, 40, textP);
		
		
		
	}

	

}
