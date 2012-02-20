package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import com.kuxhausen.colorcompete.levels.BlueTower;
import com.kuxhausen.colorcompete.levels.GamePiece;
import com.kuxhausen.colorcompete.levels.RedTower;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * (c) 2012 Eric Kuxhausen
 * 
 * @author Eric Kuxhausen
 */
public class GameEngine {

	/* Input */
	GameView gView;
	ArrayList<MotionEvent> touches = new ArrayList<MotionEvent>();

	/* Graphics */
	Paint backgroundP;
	public Paint blackP;
	private Paint textP;
	private Paint userInterfaceP;
	public int width, height; // TODO create scaling factor
	public final static float spawningRightEdgeFactor = .12f; // leftmost bounds of the play field
	final static float enemyLeftEdgeFactor = .92f; // rightmost bounds of the play field

	/* Gamestate */
	public ResourceSpawner[] spawns;
	private int selectedSpawner = 0;
	EnemySpawner enemyBase;
	public ArrayList<GamePiece> towers, enemies, projectiles;
	public GameBoard towerMap, enemyMap, projectileMap;
	public GameEngine gEngine = this;

	public void Init(GameView g, Resources resource) {

		height = resource.getDisplayMetrics().heightPixels;
		width = resource.getDisplayMetrics().widthPixels;

		/* Graphics */

		// painter to clear the screen before the game is rendered
		backgroundP = new Paint();
		backgroundP.setARGB(255, 255, 255, 255);

		blackP = new Paint();
		blackP.setColor(Color.BLACK);

		int userInterfaceColor = Color.parseColor("#FF603311");

		textP = new Paint();
		textP.setColor(userInterfaceColor);
		textP.setTextSize(20);

		userInterfaceP = new Paint();
		userInterfaceP.setColor(userInterfaceColor);

		/* GameState */
		gView = g;
		enemyBase = new EnemySpawner(gEngine, 10000, 2, 1000);
		towers = new ArrayList<GamePiece>();
		enemies = new ArrayList<GamePiece>();
		projectiles = new ArrayList<GamePiece>();
		towerMap = new GameBoard(width * spawningRightEdgeFactor, width * enemyLeftEdgeFactor, height);
		enemyMap = new GameBoard(width * spawningRightEdgeFactor, width * enemyLeftEdgeFactor, height);
		projectileMap = new GameBoard(width * spawningRightEdgeFactor, width * enemyLeftEdgeFactor, height);
		spawns = new ResourceSpawner[2];

		/* Resource Spawners */
		Paint temp1 = new Paint();
		temp1.setColor(Color.BLUE);
		spawns[0] = new ResourceSpawner(gEngine, temp1, 3, 200, 500) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new BlueTower(x, y, gEngine);
			}
		};
		Paint temp2 = new Paint();
		temp2.setColor(Color.RED);
		spawns[1] = new ResourceSpawner(gEngine, temp2, 1, 300, 600) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new RedTower(x, y, gEngine);
			}
		};
	}

	public void processInput() {
		gView.getInputs(touches);
		for (MotionEvent e : touches) {
			if (e.getHistorySize() > 0 && e.getHistoricalX(0) < (width * spawningRightEdgeFactor))
				selectedSpawner = whichResourceSpawner(e.getHistoricalY(0));
			else if (e.getAction() == MotionEvent.ACTION_DOWN && e.getX() < (width * spawningRightEdgeFactor))
				selectedSpawner = whichResourceSpawner(e.getY());
			if (e.getAction() == MotionEvent.ACTION_UP
					&& (width * spawningRightEdgeFactor < e.getX() && e.getX() < width * enemyLeftEdgeFactor)
					&& spawns[selectedSpawner].canSpawn())
				towers.add(spawns[selectedSpawner].spawnResource(e.getX(), e.getY()));

		}

		/* IMPORTANT */
		touches.clear();
	}

	public void update() {
		for (ResourceSpawner rs : spawns) {
			rs.update();
		}
		enemyBase.update();
		if (enemyBase.spawnsRemaining < 10) // temp for testing
			enemyBase.spawnsRemaining += 10; // temp for testing
		for (int i = 0; i < towers.size(); i++) {

			if (!towers.get(i).update())
				i--;
		}
		for (int i = 0; i < enemies.size(); i++) {
			if (!enemies.get(i).update())
				i--;
		}
		for (int i = 0; i < projectiles.size(); i++) {
			if (!projectiles.get(i).update())
				i--;
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
			spawns[i].draw(c, backgroundP, blackP, 10, i * maxY / spawns.length + (5 + (i == 0 ? 5 : 0)), maxX
					* spawningRightEdgeFactor - 10, (1 + i) * maxY / spawns.length
					- (5 + (i == (spawns.length - 1) ? 5 : 0)));

		// GameBoard
		enemyBase.draw(c);
		for (GamePiece twr : towers)
			twr.draw(c);
		for (GamePiece enm : enemies)
			enm.draw(c);
		for (GamePiece prj : projectiles)
			prj.draw(c);

		// draw FPS counter
		c.drawText("FPS:" + FPS, c.getWidth() - 180, 40, textP);
	}

	/** Returns the index of the resource spawner that covers inputed y coordinate */
	public int whichResourceSpawner(float y) {
		return (int) (spawns.length * y / height);
	}

	/** if all of the ResouceSpawners are dead, player lost so end game */
	public void checkPlayerAlive() {
		boolean dead = true;
		for (ResourceSpawner rs : spawns)
			dead = dead && rs.dead;
		if (dead)
			endGame(false);
	}

	public void endGame(boolean playerWon) {
		gView.endGame(playerWon);
	}

}
