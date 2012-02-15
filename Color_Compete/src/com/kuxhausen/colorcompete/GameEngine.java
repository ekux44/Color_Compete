package com.kuxhausen.colorcompete;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class GameEngine {

	/*Graphics*/
	private Paint backgroundP;
	private Paint textP;
	private Paint userInterfaceP;
	
	/*Gamestate*/
	ResourceSpawn[] spawns;
	
	//TODO update game, update physics, etc
	
	public void Init(Resources resources) {
		
		// black painter below to clear the screen before the game is rendered
		backgroundP = new Paint();
		backgroundP.setARGB(255, 255, 255, 255);
		
		int userInterfaceColor = Color.parseColor("#FF603311");
		
		textP = new Paint();
		textP.setColor(userInterfaceColor);
		textP.setTextSize(20);
		
		userInterfaceP = new Paint();
		userInterfaceP.setColor(userInterfaceColor);
		
		spawns = new ResourceSpawn[3];
		Paint temp1 = new Paint();
		temp1.setColor(Color.BLUE);
		spawns[0] = new ResourceSpawn(temp1);
		Paint temp2 = new Paint();
		temp2.setColor(Color.GREEN);
		spawns[1] = new ResourceSpawn(temp2);
		Paint temp3 = new Paint();
		temp3.setColor(Color.RED);
		spawns[2] = new ResourceSpawn(temp3);
		
		
	}
	
	public void SampleTouchInput(MotionEvent event)
	{
		// TODO Auto-generated method stub
	}
	
	public void update()
	{
		for(ResourceSpawn rs: spawns){
			rs.update();
			rs.fill%=rs.maxFill;//temp for testing 
		}	
	}
	
	public void Draw(Canvas c, int FPS) {
		float maxX = c.getWidth()-1;
		float maxY = c.getHeight()-1;
		// clear the screen with the background painter.
		c.drawRect(0, 0, maxX, maxY, backgroundP);

		//draw FPS
		c.drawText("FPS:"+FPS, c.getWidth()-80, 40, textP);
		
		//user interface & resource spawns
		c.drawRect(0, 0, maxX*.11f, maxY, userInterfaceP);
		for(int i = 0; i<spawns.length; i++)
			spawns[i].draw(c,backgroundP,10, i*maxY/spawns.length + (5 +(i==0?5:0)), maxX*.11f-10, (1+i)*maxY/spawns.length - (5+(i==(spawns.length-1)?5:0)));
		
	}
}
