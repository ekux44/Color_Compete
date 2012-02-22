package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import com.kuxhausen.colorcompete.basiclevels.BlueTower;
import com.kuxhausen.colorcompete.basiclevels.GamePiece;
import com.kuxhausen.colorcompete.basiclevels.LevelLoader;
import com.kuxhausen.colorcompete.basiclevels.RedTower;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
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
	boolean fingerOnBoard= false;
	float tx, ty;
	
	
	/* Graphics */
	Paint backgroundP;
	public Paint blackP, textP, userInterfaceP, pathPaint;
	private Path selectedPath;
	public int width, height; // TODO create scaling factor
	public final static float spawningRightEdgeFactor = .12f; // leftmost bounds of the play field
	final static float enemyLeftEdgeFactor = .92f; // rightmost bounds of the play field
	DashPathEffect[] pathEffects;
	int phase;
	

	/* Gamestate */
	LevelLoader load;
	public ResourceSpawner[] spawns;
	private int selectedSpawner = 0;
	EnemySpawner enemyBase;
	public ArrayList<GamePiece> towers, enemies, projectiles;
	public GameBoard towerMap, enemyMap, projectileMap;
	public GameEngine gEngine = this;
	private int gameEndDelayer = 0;
	
	/* Stats */
	public float playerScore;

	public void Init(GameView g, Resources resource, int level) {

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
		towers = new ArrayList<GamePiece>();
		enemies = new ArrayList<GamePiece>();
		projectiles = new ArrayList<GamePiece>();
		towerMap = new GameBoard(width * spawningRightEdgeFactor, width * enemyLeftEdgeFactor, height);
		enemyMap = new GameBoard(width * spawningRightEdgeFactor, width * enemyLeftEdgeFactor, height);
		projectileMap = new GameBoard(width * spawningRightEdgeFactor, width * enemyLeftEdgeFactor, height);

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
		selectedPath.lineTo(width * spawningRightEdgeFactor - 5, 5);
		selectedPath.lineTo(width * spawningRightEdgeFactor - 5, 5 + (height - 10) / spawns.length);
		selectedPath.lineTo(5, 5 + (height - 10) / spawns.length);
		selectedPath.lineTo(5, 5);

	}

	public void processInput() {
		gView.getInputs(touches);
		for (MotionEvent e : touches) {
			if (e.getHistorySize() > 0 && e.getHistoricalX(0) < (width * spawningRightEdgeFactor)) {
				selectedPath.offset(0, -selectedSpawner * (height - 10) / spawns.length);
				selectedSpawner = whichResourceSpawner(e.getHistoricalY(0));
				selectedPath.offset(0, selectedSpawner * (height - 10) / spawns.length);
			} 
			else if (e.getAction() == MotionEvent.ACTION_DOWN && e.getX() < (width * spawningRightEdgeFactor)) {
				selectedPath.offset(0, -selectedSpawner * (height - 10) / spawns.length);
				selectedSpawner = whichResourceSpawner(e.getY());
				selectedPath.offset(0, selectedSpawner * (height - 10) / spawns.length);
			}
			
			if(spawns[selectedSpawner].canSpawn() && (width * spawningRightEdgeFactor < e.getX() && e.getX() < width * enemyLeftEdgeFactor)){
				if (e.getAction() == MotionEvent.ACTION_UP
						){
					towers.add(spawns[selectedSpawner].spawnResource(e.getX(), e.getY()));
				}
				else{
					tx = e.getX();
					ty = e.getY();
					fingerOnBoard = true;
				}
				
			}
			if (e.getAction() == MotionEvent.ACTION_UP)
				fingerOnBoard = false;
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

		//finger tracking hover
		if(fingerOnBoard)
			spawns[selectedSpawner].drawTouch(c, tx, ty);
		
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
		gView.endGame(playerWon, (int)playerScore);
	}

}
