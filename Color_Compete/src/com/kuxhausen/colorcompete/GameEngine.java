package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**(c) 2012 Eric Kuxhausen
 * @author Eric Kuxhausen
 */
public class GameEngine {

	/* Input */
	GameView gView;
	ArrayList<MotionEvent> touches = new ArrayList<MotionEvent>();

	/* Graphics */
	private Paint backgroundP;
	private Paint textP;
	private Paint userInterfaceP;
	int width, height; // TODO create scaling factor
	final static float spawningRightEdgeFactor = .12f; //leftmost bounds of the play field
	final static float enemyLeftEdgeFactor = .92f;	//rightmost bounds of the play field
	
	/* Gamestate */
	ResourceSpawner[] spawns;
	private int selectedSpawner = 0;
	EnemySpawner enemyBase;
	ArrayList<GamePiece> towers;
	GameBoard towerGrid, enemyGrid;

	// TODO update game, update physics, etc

	public void Init(GameView g, Resources resource) {

		gView = g;

		height = resource.getDisplayMetrics().heightPixels;
		width = resource.getDisplayMetrics().widthPixels;

		// black painter below to clear the screen before the game is rendered
		backgroundP = new Paint();
		backgroundP.setARGB(255, 255, 255, 255);

		int userInterfaceColor = Color.parseColor("#FF603311");

		textP = new Paint();
		textP.setColor(userInterfaceColor);
		textP.setTextSize(20);

		userInterfaceP = new Paint();
		userInterfaceP.setColor(userInterfaceColor);

		spawns = new ResourceSpawner[2];
		Paint temp1 = new Paint();
		temp1.setColor(Color.BLUE);
		spawns[0] = new ResourceSpawner(temp1, 4, 400, 20) {
			public GamePiece spawnResource(float x, float y) {
				fill-=respawnCost;
				return new BlueTower(x, y);
			}
		};
		Paint temp2 = new Paint();
		temp2.setColor(Color.RED);
		spawns[1] = new ResourceSpawner(temp2, 1, 30, 0) {
			public GamePiece spawnResource(float x, float y) {
				fill-=respawnCost;
				return new RedTower(x, y);
			}
		};

		enemyBase = new EnemySpawner(1000);
		towers = new ArrayList<GamePiece>();
		towerGrid = new GameBoard(width*spawningRightEdgeFactor, width*enemyLeftEdgeFactor, height);
		enemyGrid = new GameBoard(width*spawningRightEdgeFactor, width*enemyLeftEdgeFactor, height);
	}

	public void processInput() {
		gView.getInputs(touches);
		for (MotionEvent e : touches) {
			if(e.getHistorySize()>0 && e.getHistoricalX(0)<(width*spawningRightEdgeFactor))
				selectedSpawner= (int)(spawns.length*e.getHistoricalY(0)/height); 
			else if(e.getAction()==MotionEvent.ACTION_DOWN && e.getX()<(width*spawningRightEdgeFactor))
				selectedSpawner= (int)(spawns.length*e.getY()/height);
			if (e.getAction() == MotionEvent.ACTION_UP && (width*spawningRightEdgeFactor < e.getX() && e.getX()< width*enemyLeftEdgeFactor) && spawns[selectedSpawner].canSpawn())
				towers.add(towerGrid.register(spawns[selectedSpawner].spawnResource(e.getX(),e.getY())));
			
		}

		/** IMPORTANT **/
		touches.clear();
	}

	public void update() {
		for (ResourceSpawner rs : spawns) {
			rs.update();
			rs.fill %= rs.maxFill;// temp for testing
		}
		enemyBase.update();
		if (enemyBase.spawnsRemaining < 100) // temp for testing
			enemyBase.spawnsRemaining += 100; // temp for testing
		for (int i = 0; i < towers.size(); i++) {
			towers.get(i).update();
			towers.get(i).health--; // temp for testing
			if (towers.get(i).health <= 0) {
				towers.remove(i);
				i--;
			}
		}
	}

	public void Draw(Canvas c, int FPS) {
		float maxX = c.getWidth();
		float maxY = c.getHeight();

		// clear the screen with the background painter.
		c.drawRect(0, 0, maxX, maxY, backgroundP);

		// user interface & resource spawns
		c.drawRect(0, 0, maxX * spawningRightEdgeFactor, maxY, userInterfaceP);
		for (int i = 0; i < spawns.length; i++)
			spawns[i].draw(c, backgroundP, 10, i * maxY / spawns.length
					+ (5 + (i == 0 ? 5 : 0)), maxX * spawningRightEdgeFactor - 10, (1 + i) * maxY
					/ spawns.length - (5 + (i == (spawns.length - 1) ? 5 : 0)));

		// GameBoard
		enemyBase.draw(c);
		for (GamePiece t : towers)
			t.draw(c);

		// draw FPS counter
		c.drawText("FPS:" + FPS, c.getWidth() - 180, 40, textP);
	}
}
