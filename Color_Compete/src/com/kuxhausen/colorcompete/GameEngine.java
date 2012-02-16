package com.kuxhausen.colorcompete;

import java.util.ArrayList;

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
	int width, height; //TODO create scaling factor
	
	/*Gamestate*/
	ResourceSpawner[] spawns;
	EnemySpawner enemyBase;
	ArrayList<Tower> towers;
	
	//TODO update game, update physics, etc
	
	public void Init(Resources resource) {
		
		height=resource.getDisplayMetrics().heightPixels;
		width=resource.getDisplayMetrics().widthPixels;
		
		// black painter below to clear the screen before the game is rendered
		backgroundP = new Paint();
		backgroundP.setARGB(255, 255, 255, 255);
		
		int userInterfaceColor = Color.parseColor("#FF603311");
		
		textP = new Paint();
		textP.setColor(userInterfaceColor);
		textP.setTextSize(20);
		
		userInterfaceP = new Paint();
		userInterfaceP.setColor(userInterfaceColor);
		
		spawns = new ResourceSpawner[4];
		Paint temp1 = new Paint();
		temp1.setColor(Color.BLUE);
		spawns[0] = new ResourceSpawner(temp1,5,20);
		Paint temp2 = new Paint();
		temp2.setColor(Color.GREEN);
		spawns[1] = new ResourceSpawner(temp2,1,0);
		Paint temp3 = new Paint();
		temp3.setColor(Color.RED);
		spawns[2] = new ResourceSpawner(temp3,4,0);
		Paint temp4 = new Paint();
		temp4.setColor(Color.YELLOW);
		spawns[3] = new ResourceSpawner(temp4,10,0);
		
		enemyBase = new EnemySpawner(1000);
		towers = new ArrayList<Tower>();
		
	}
	
	public void SampleTouchInput(MotionEvent event)
	{
		// TODO Auto-generated method stub
	}
	
	public void update()
	{
		for(ResourceSpawner rs: spawns){
			rs.update();
			rs.fill%=rs.maxFill;//temp for testing 
		}
		enemyBase.update();
		if(enemyBase.spawnsRemaining<100) //temp for testing 
			enemyBase.spawnsRemaining+=100; //temp for testing 
		if(Math.random()<.3)	//temp for testing
			towers.add(new RedTower(((float)Math.random()*.88f+.12f)*width, (float)Math.random()*height));	//temp for testing
		for(int i=0; i<towers.size();i++){
			towers.get(i).update();
			towers.get(i).health--;		//temp for testing
			if(towers.get(i).health<=0){
				towers.remove(i);
				i--;
			}
		}
	}
	
	public void Draw(Canvas c, int FPS) {
		float maxX = c.getWidth()-1;
		float maxY = c.getHeight()-1;
		
		// clear the screen with the background painter.
		c.drawRect(0, 0, maxX, maxY, backgroundP);
		
		//user interface & resource spawns
		c.drawRect(0, 0, maxX*.12f, maxY, userInterfaceP);
		for(int i = 0; i<spawns.length; i++)
			spawns[i].draw(c,backgroundP,10, i*maxY/spawns.length + (5 +(i==0?5:0)), maxX*.12f-10, (1+i)*maxY/spawns.length - (5+(i==(spawns.length-1)?5:0)));
		
		//GameBoard
		enemyBase.draw(c);
		for(Tower t: towers)
			t.draw(c);
		
		
		
		//draw FPS counter
		c.drawText("FPS:"+FPS, c.getWidth()-180, 40, textP);
	}
}
