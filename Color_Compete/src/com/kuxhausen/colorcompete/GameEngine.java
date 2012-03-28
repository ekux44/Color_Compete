package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.kuxhausen.colorcompete.basiclevels.LevelLoader;
import com.kuxhausen.colorcompete.basiclevels.RedTower;

/**
 * (c) 2012 Eric Kuxhausen
 * 
 * @author Eric Kuxhausen
 */
public class GameEngine {

	/* Input */
	GameView gView;
	ArrayList<MotionEvent> touches = new ArrayList<MotionEvent>();
	boolean fingerOnBoard = false;
	float tx, ty;
	Route inProgress;

	/* Graphics */
	Paint backgroundP;
	public Paint enemyP, textP, userInterfaceP, pathPaint;
	private Path selectedPath;
	public int width, height; // TODO create scaling factor
	public final static float RIGHT_EDGE_OF_SPAWNER_FACTOR = .12f; // leftmost bounds of the play field
	public final static float LEFT_EDGE_OF_ENEMY_SPAWNER_FACTOR = .92f; // rightmost bounds of the play field
	DashPathEffect[] pathEffects;
	int phase;

	/* Camera */
	public float cameraOffset;
	float cameraVelocity;

	/* Gamestate */
	int level;
	LevelLoader load;
	public ResourceSpawner[] spawns;
	private int selectedSpawner = 0;
	public EnemySpawner enemyBase;
	public ArrayList<GamePiece> towers, enemies, projectiles;
	public GameBoard towerMap, enemyMap, projectileMap;
	public ArrayList<Route> activeRoutes;
	public GameEngine gEngine = this;
	private int gameEndDelayer = 0;

	/* Stats */
	public float playerScore;

	public void Init(GameView g, Resources resource, int lvl) {

		height = resource.getDisplayMetrics().heightPixels;
		width = resource.getDisplayMetrics().widthPixels;

		level = lvl;

		/* Graphics */

		// painter to clear the screen before the game is rendered
		backgroundP = new Paint();
		backgroundP.setARGB(255, 255, 255, 255);

		enemyP = new Paint();
		enemyP.setColor(Color.BLACK);
		enemyP.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));

		int userInterfaceColor = Color.parseColor("#FF603311");

		textP = new Paint();
		textP.setColor(userInterfaceColor);
		textP.setTextSize(20);

		userInterfaceP = new Paint();
		userInterfaceP.setColor(userInterfaceColor);

		/* Camera */
		cameraOffset = width * RIGHT_EDGE_OF_SPAWNER_FACTOR;
		cameraVelocity = LevelLoader.loadCameraVelocity(level, gEngine);

		/* GameState */
		gView = g;
		towers = new ArrayList<GamePiece>();
		enemies = new ArrayList<GamePiece>();
		projectiles = new ArrayList<GamePiece>();
		towerMap = new GameBoard(width * RIGHT_EDGE_OF_SPAWNER_FACTOR, width, height);
		enemyMap = new GameBoard(width * RIGHT_EDGE_OF_SPAWNER_FACTOR, width, height);
		projectileMap = new GameBoard(width * RIGHT_EDGE_OF_SPAWNER_FACTOR, width, height);
		activeRoutes = new ArrayList<Route>();

		load = new LevelLoader();
		spawns = LevelLoader.loadSpawners(level, gEngine);
		enemyBase = LevelLoader.loadEnemySpawner(level, gEngine);

		/* More UI */
		pathEffects = new DashPathEffect[15];
		for (int i = 0; i < pathEffects.length; i++)
			pathEffects[i] = new DashPathEffect(new float[] { 10, 5 }, i);
		pathPaint = new Paint();
		pathPaint.setColor(Color.WHITE);
		pathPaint.setPathEffect(pathEffects[0]);
		pathPaint.setStyle(Paint.Style.STROKE);
		selectedPath = new Path();
		selectedPath.moveTo(5, 5);
		selectedPath.lineTo(width * RIGHT_EDGE_OF_SPAWNER_FACTOR - 5, 5);
		selectedPath.lineTo(width * RIGHT_EDGE_OF_SPAWNER_FACTOR - 5, 5 + (height - 10) / spawns.length);
		selectedPath.lineTo(5, 5 + (height - 10) / spawns.length);
		selectedPath.lineTo(5, 5);

		inProgress = spawns[selectedSpawner].spawnRoute();

	}

	public void processInput() {
		gView.getInputs(touches);
		for (MotionEvent e : touches) {
			if(e==null)
				return;
			// update spawner selection if touch was in that area
			if (e.getHistorySize() > 0 && e.getHistoricalX(0) < (width * RIGHT_EDGE_OF_SPAWNER_FACTOR)) {
				selectedPath.offset(0, -selectedSpawner * (height - 10) / spawns.length);
				selectedSpawner = whichResourceSpawner(e.getHistoricalY(0));
				selectedPath.offset(0, selectedSpawner * (height - 10) / spawns.length);
				inProgress = spawns[selectedSpawner].spawnRoute();
			} else if (e.getAction() == MotionEvent.ACTION_DOWN && e.getX() < (width * RIGHT_EDGE_OF_SPAWNER_FACTOR)) {
				selectedPath.offset(0, -selectedSpawner * (height - 10) / spawns.length);
				selectedSpawner = whichResourceSpawner(e.getY());
				selectedPath.offset(0, selectedSpawner * (height - 10) / spawns.length);
				inProgress = spawns[selectedSpawner].spawnRoute();
			}
			//check if a red tower on the board was interacted with
			else if(!fingerOnBoard&&towerMap.getNearest(e.getX(), e.getY(), .1f) instanceof RedTower){
				//TODO select red tower, make draggable
			}
			// otherwise
			else if (spawns[selectedSpawner].canSpawn()
					&& (width * RIGHT_EDGE_OF_SPAWNER_FACTOR < e.getX() && e.getX() < width
							* LEFT_EDGE_OF_ENEMY_SPAWNER_FACTOR) && !towerMap.conflicts(e.getX() + cameraOffset, e.getY(), 25/*TODO change*/, null)) {
				if (e.getAction() == MotionEvent.ACTION_UP) {
					towers.add(spawns[selectedSpawner].spawnResource(e.getX() + cameraOffset, e.getY(), inProgress));
					inProgress = spawns[selectedSpawner].spawnRoute();
					fingerOnBoard = false;
				} else {
					tx = e.getX();
					ty = e.getY();
					fingerOnBoard = true;
					inProgress.addPoint(tx + cameraOffset, ty);
				}

			}
			else /*if (e.getAction() == MotionEvent.ACTION_UP)*/{
				fingerOnBoard = false;
				inProgress.clear();
			}
		}

		/* IMPORTANT */
		touches.clear();
	}

	public void update() {

		for (ResourceSpawner rs : spawns) {
			rs.update();
		}
		enemyBase.update();
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

		if (enemyBase.spawnsRemaining <= 0 && enemies.size() <= 0) {
			gameEndDelayer++;
			if (gameEndDelayer >= 90)
				endGame(true);

		}

		cameraOffset += cameraVelocity;
	}

	public void Draw(Canvas c, int FPS) {
		float maxX = c.getWidth();
		float maxY = c.getHeight();

		// clear the screen with the background painter.
		c.drawRect(0, 0, maxX, maxY, backgroundP);

		// Routes
		inProgress.draw(c, -cameraOffset);
		for (Route rt : activeRoutes)
				rt.draw(c, -cameraOffset);

		// GameBoard
		enemyBase.draw(c);
		for (GamePiece twr : towers)
			twr.draw(c, -cameraOffset);
		for (GamePiece enm : enemies)
			enm.draw(c, -cameraOffset);
		for (GamePiece prj : projectiles)
			prj.draw(c, -cameraOffset);

		// finger tracking hover
		if (fingerOnBoard)
			spawns[selectedSpawner].drawTouch(c, tx, ty);

		// user interface & resource spawns
		c.drawRect(0, 0, maxX * RIGHT_EDGE_OF_SPAWNER_FACTOR, maxY, userInterfaceP);
		for (int i = 0; i < spawns.length; i++)
			spawns[i].draw(c, backgroundP, enemyP, 10, i * maxY / spawns.length + (5 + (i == 0 ? 5 : 0)), maxX
					* RIGHT_EDGE_OF_SPAWNER_FACTOR - 10, (1 + i) * maxY / spawns.length
					- (5 + (i == (spawns.length - 1) ? 5 : 0)));

		// resource spawn selected indicator
		phase++;
		pathPaint.setPathEffect(pathEffects[phase % pathEffects.length]);
		c.drawPath(selectedPath, pathPaint);

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
		gView.endGame(playerWon, (int) playerScore);
	}

}
