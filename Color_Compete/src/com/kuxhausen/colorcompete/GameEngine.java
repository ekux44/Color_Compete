package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
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
	RedTower draggingTower = null;
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
	private int selectedSpawner = -1;//
	public EnemySpawner enemyBase;
	public GameBoard towerMap, enemyMap, projectileMap;
	public ArrayList<Route> activeRoutes;
	public GameEngine gEngine = this;
	private int gameEndDelayer = 0;

	/* Stats */
	public static StatisticsEngine statEng;
	

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
		towerMap = new GameBoard(width * RIGHT_EDGE_OF_SPAWNER_FACTOR, width, height);
		enemyMap = new GameBoard(width * RIGHT_EDGE_OF_SPAWNER_FACTOR, width, height);
		projectileMap = new GameBoard(width * RIGHT_EDGE_OF_SPAWNER_FACTOR, width, height);
		activeRoutes = new ArrayList<Route>();

		statEng = new StatisticsEngine();
		
		load = new LevelLoader();
		load.prepBoards(level, gEngine);
		spawns = LevelLoader.loadSpawners(level, gEngine);
		enemyBase = LevelLoader.loadEnemySpawner(level, gEngine);

		/* More UI */
		pathEffects = new DashPathEffect[15];
		for (int i = 0; i < pathEffects.length; i++)
			pathEffects[i] = new DashPathEffect(new float[] { 10, 5 }, i);
		pathPaint = new Paint();
		pathPaint.setColor(Color.WHITE);
		pathPaint.setPathEffect(pathEffects[0]);
		pathPaint.setStrokeWidth(5);
		pathPaint.setStyle(Paint.Style.STROKE);
		selectedPath = new Path();
		selectedPath.moveTo(5, 5);
		selectedPath.lineTo(width * RIGHT_EDGE_OF_SPAWNER_FACTOR - 5, 5);
		selectedPath.lineTo(width * RIGHT_EDGE_OF_SPAWNER_FACTOR - 5, 5 + (height - 10) / spawns.length);
		selectedPath.lineTo(5, 5 + (height - 10) / spawns.length);
		selectedPath.lineTo(5, 5);

		inProgress = new Route(enemyP);// spawns[selectedSpawner].spawnRoute();

	}

	public void processInput() {
		gView.getInputs(touches);
		for (MotionEvent e : touches) {
			if (e == null)
				return;
			// update spawner selection if touch was in that area
			if (e.getHistorySize() > 0 && e.getHistoricalX(0) < (width * RIGHT_EDGE_OF_SPAWNER_FACTOR)) {
				selectedSpawner = whichResourceSpawner(e.getHistoricalY(0));
				inProgress = spawns[selectedSpawner].spawnRoute();
				if (draggingTower != null)
					draggingTower.selected = false;
			} else if (e.getAction() == MotionEvent.ACTION_DOWN && e.getX() < (width * RIGHT_EDGE_OF_SPAWNER_FACTOR)) {
				selectedSpawner = whichResourceSpawner(e.getY());
				inProgress = spawns[selectedSpawner].spawnRoute();
				if (draggingTower != null)
					draggingTower.selected = false;
			}
			// if red tower on the board is in the middle of being interacted with
			else if (!fingerOnBoard
					&& draggingTower != null
					&& e.getAction() != MotionEvent.ACTION_UP
					&& (width * RIGHT_EDGE_OF_SPAWNER_FACTOR < e.getX() && e.getX() < width
							* LEFT_EDGE_OF_ENEMY_SPAWNER_FACTOR)) {
				draggingTower.r.addPoint(e.getX() + cameraOffset, e.getY());
			}
			// check if a red tower on the board was newly interacted with
			else if (!fingerOnBoard
					&& towerMap.getNearest(e.getX() + cameraOffset, e.getY(), 25/* TODO change */) instanceof RedTower) {
				if (draggingTower != null)
					draggingTower.selected = false;
				draggingTower = (RedTower) towerMap.getNearest(e.getX() + cameraOffset, e.getY(), 25/* TODO change */);
				draggingTower.selected = true;
				draggingTower.r.clear();
				selectedSpawner = -1;
			}
			// otherwise
			else if (selectedSpawner > -1
					&& spawns[selectedSpawner].canSpawn()
					&& (width * RIGHT_EDGE_OF_SPAWNER_FACTOR < e.getX() && e.getX() < width
							* LEFT_EDGE_OF_ENEMY_SPAWNER_FACTOR)
					&& !towerMap.conflicts(e.getX() + cameraOffset, e.getY(), 25/* TODO change */, null)) {
				if (e.getAction() == MotionEvent.ACTION_UP) {
					spawns[selectedSpawner].spawnResource(e.getX() + cameraOffset, e.getY(), inProgress);
					inProgress = spawns[selectedSpawner].spawnRoute();
					fingerOnBoard = false;
					selectedSpawner = -1;
				} else {
					tx = e.getX();
					ty = e.getY();
					fingerOnBoard = true;
					inProgress.addPoint(tx + cameraOffset, ty);
				}

			} else /* if (e.getAction() == MotionEvent.ACTION_UP) */{
				fingerOnBoard = false;
				inProgress.clear();
				if (draggingTower != null)
					draggingTower.selected = false;
				draggingTower = null;
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
		for (int i = 0; i < towerMap.getAll().size(); i++) {

			if (!towerMap.getAll().get(i).update())
				i--;
		}
		for (int i = 0; i < enemyMap.getAll().size(); i++) {
			if (!enemyMap.getAll().get(i).update())
				i--;
		}
		for (int i = 0; i < projectileMap.getAll().size(); i++) {
			if (!projectileMap.getAll().get(i).update())
				i--;
		}

		if (enemyBase.spawnsRemaining <= 0 && enemyMap.getAll().size() <= 0) {
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
		for (GamePiece twr : towerMap.getAll())
			twr.draw(c, -cameraOffset);
		for (GamePiece enm : enemyMap.getAll())
			enm.draw(c, -cameraOffset);
		for (GamePiece prj : projectileMap.getAll())
			prj.draw(c, -cameraOffset);

		// finger tracking hover
		if (fingerOnBoard && selectedSpawner > -1)
			spawns[selectedSpawner].drawTouch(c, tx, ty);

		// user interface & resource spawns
		c.drawRect(0, 0, maxX * RIGHT_EDGE_OF_SPAWNER_FACTOR, maxY, userInterfaceP);

		for (int i = 0; i < spawns.length; i++)
			spawns[i].draw(c, backgroundP, enemyP, 10, i * maxY / spawns.length + (5 + (i == 0 ? 5 : 0)), maxX
					* RIGHT_EDGE_OF_SPAWNER_FACTOR - 10, (1 + i) * maxY / spawns.length
					- (5 + (i == (spawns.length - 1) ? 5 : 0)));

		// resource spawn selected indicator
		if (selectedSpawner > -1) {
			selectedPath.offset(0, selectedSpawner * (height - 10) / spawns.length);
			phase++;
			pathPaint.setPathEffect(pathEffects[phase % pathEffects.length]);
			c.drawPath(selectedPath, pathPaint);
			selectedPath.offset(0, -selectedSpawner * (height - 10) / spawns.length);
		}
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
		gView.endGame(playerWon, (int) statEng.getDarknessEliminated());
	}

}
