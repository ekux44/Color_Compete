package com.kuxhausen.colorcompete.basiclevels;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import com.kuxhausen.colorcompete.EnemySpawner;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;
import com.kuxhausen.colorcompete.ResourceSpawner;
import com.kuxhausen.colorcompete.Route;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Holds all of the game piece paints, and generates the appropriate EnemySpawner and ResourceSpawner for a level
 * 
 * @author Eric Kuxhausen
 */
public class LevelLoader {

	static Paint smallEnemyP, mediumEnemyP, largeEnemyP, redEnemyP, greenEnemyP, enemyProjectileP, blueTowerP,
			greenTowerP, redTowerP, simpleProjectileP, redRouteP, greenRouteP, blueRouteP, selectedP;

	public LevelLoader() {
		smallEnemyP = new Paint();
		smallEnemyP.setColor(Color.BLACK);
		smallEnemyP.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.NORMAL));

		mediumEnemyP = new Paint();
		mediumEnemyP.setColor(Color.BLACK);
		mediumEnemyP.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.NORMAL));

		largeEnemyP = new Paint();
		largeEnemyP.setColor(Color.BLACK);
		largeEnemyP.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.NORMAL));

		redEnemyP = new Paint();
		redEnemyP.setColor(Color.BLACK);
		redEnemyP.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.NORMAL));

		greenEnemyP = new Paint();
		greenEnemyP.setColor(Color.BLACK);
		greenEnemyP.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.NORMAL));

		enemyProjectileP = new Paint();
		enemyProjectileP.setColor(Color.BLACK);

		blueTowerP = new Paint();
		blueTowerP.setColor(Color.BLUE);

		greenTowerP = new Paint();
		greenTowerP.setColor(Color.GREEN);

		redTowerP = new Paint();
		redTowerP.setColor(Color.RED);

		simpleProjectileP = new Paint();
		simpleProjectileP.setColor(Color.RED);

		redRouteP = new Paint();
		redRouteP.setColor(0x5FFF0000);
		redRouteP.setPathEffect(new DashPathEffect(new float[] { 8, 5 }, 0));
		redRouteP.setStyle(Paint.Style.STROKE);
		redRouteP.setStrokeWidth(20);

		greenRouteP = new Paint();
		greenRouteP.setColor(0x5F00FF00);
		greenRouteP.setPathEffect(new DashPathEffect(new float[] { 8, 5 }, 0));
		greenRouteP.setStyle(Paint.Style.STROKE);
		greenRouteP.setStrokeWidth(20);

		blueRouteP = new Paint();
		blueRouteP.setColor(0x5F0000FF);
		blueRouteP.setPathEffect(new DashPathEffect(new float[] { 8, 5 }, 0));
		blueRouteP.setStyle(Paint.Style.STROKE);
		blueRouteP.setStrokeWidth(20);
		
		selectedP = new Paint();
		selectedP.setColor(Color.WHITE);

		selectedP.setStyle(Paint.Style.STROKE);
	}

	public void prepBoards(int level, GameEngine gEng) {
		switch (level) {
		case 1:
			RedTower starter = new RedTower(gEng.width/2,gEng.height/2, gEng, new Route(redRouteP));
			gEng.towers.add(starter);
			gEng.towerMap.register(starter);
			break;
		default:
			return;
		}
	}

	public static float loadCameraVelocity(int level, GameEngine gEng) {
		switch (level) {
		}
		return 0f;
	}

	/** returns the appropriately configured EnemySpawner for that level */
	public static EnemySpawner loadEnemySpawner(int level, GameEngine gEng) {
		Class<?>[] enemyTypes = new Class[5];

		try {
			enemyTypes[0] = SmallEnemy.class;
			enemyTypes[1] = MediumEnemy.class;
			enemyTypes[2] = LargeEnemy.class;
			enemyTypes[3] = RedEnemy.class;
			enemyTypes[4] = GreenEnemy.class;
		} catch (LinkageError e) {
		}
		float[] probabilities = { .5f, .65f, .75f, .9f, 1f };// cumulative mass function
		EnemySpawner enemy = null;
		Class<?>[] enemies;
		switch (level) {
		case 1: 
			enemies = new Class[2];
			enemies[0] = enemyTypes[0];
			enemies[1] = enemyTypes[1];
			float[] probs = {.75f,1f};
			enemy = new EnemySpawner(gEng, enemies, probs, 5000, 1, 4000); 
			break;
		case 2:
			enemies = new Class[3];
			enemies[0] = enemyTypes[0];
			enemies[1] = enemyTypes[1];
			enemies[2] = enemyTypes[2];
			float[] probs2 = {.75f,.9f,1f};
			enemy = new EnemySpawner(gEng, enemies, probs2, 10000, 1, 1000); 
			break;
		case 3:
			enemies = new Class[3];
			enemies[0] = enemyTypes[0];
			enemies[1] = enemyTypes[1];
			enemies[2] = enemyTypes[2];
			float[] probs3 = {.75f,.9f,1f};
			enemy = new EnemySpawner(gEng, enemies, probs3, 20000, 2, 1000); 
			break;
		default: enemy = new EnemySpawner(gEng, enemyTypes, probabilities, 100000, 2, 2000);
		}
		return enemy;
	}

	/** returns the appropriately configured ResourceSpawner[] for that level */
	public static ResourceSpawner[] loadSpawners(int level, GameEngine gEng) {
		ResourceSpawner[] spawns;
		switch (level) {
		case 1:
			spawns = new ResourceSpawner[1];
			spawns[0] = getRed(gEng, 0, 200, 0);
			break;
		case 2:
			spawns = new ResourceSpawner[1];
			spawns[0] = getRed(gEng, 2, 500, 1000);
			break;
		case 3:
			spawns = new ResourceSpawner[2];
			spawns[0] = getRed(gEng, 1, 500, 200);
			spawns[1] = getGreen(gEng, 2, 300, 600);
			break;
		default:
			spawns = new ResourceSpawner[3];
			spawns[0] = getBlue(gEng, 1, 500, 500);
			spawns[1] = getRed(gEng, 1, 500, 500);
			spawns[2] = getGreen(gEng, 1, 500, 500);

		}
		return spawns;
	}

	/** Returns a RedTower generating ResourceSpawner */
	private static ResourceSpawner getRed(GameEngine gEng, int spawnRate, int spawnCost, int startingFill) {
		Paint ready = new Paint();
		ready.setColor(Color.RED);
		Paint charging = new Paint();
		charging.setARGB(0xFF, 0xFF, 0x80, 0x80);
		return new ResourceSpawner(gEng, ready, charging, spawnRate, spawnCost, startingFill) {
			@Override
			public GamePiece spawnResource(float x, float y, Route r) {
				fill -= respawnCost;
				return new RedTower(x, y, gEng, r);
			}

			// TODO add firing radius visualization
			/*
			 * public void drawTouch(Canvas c, float tX, float tY) { c.drawCircle(tX, tY, 50f, charging); }
			 */

			@Override
			public Route spawnRoute() {
				return new Route(redRouteP);
			}
		};
	}

	/** Returns a GreenTower generating ResourceSpawner */
	private static ResourceSpawner getGreen(GameEngine gEng, int spawnRate, int spawnCost, int startingFill) {
		Paint ready = new Paint();
		ready.setColor(Color.GREEN);
		Paint charging = new Paint();
		charging.setARGB(0xFF, 0x80, 0xFF, 0x80);
		return new ResourceSpawner(gEng, ready, charging, spawnRate, spawnCost, startingFill) {
			@Override
			public GamePiece spawnResource(float x, float y, Route r) {
				fill -= respawnCost;
				return new GreenTower(x, y, gEng, r);
			}

			@Override
			public Route spawnRoute() {
				return new Route(greenRouteP);
			}
		};
	}

	/** Returns a BlueTower generating ResourceSpawner */
	private static ResourceSpawner getBlue(GameEngine gEng, int spawnRate, int spawnCost, int startingFill) {
		Paint ready = new Paint();
		ready.setColor(Color.BLUE);
		Paint charging = new Paint();
		charging.setARGB(0xFF, 0x80, 0x80, 0xFF);
		return new ResourceSpawner(gEng, ready, charging, spawnRate, spawnCost, startingFill) {
			@Override
			public GamePiece spawnResource(float x, float y, Route r) {
				fill -= respawnCost;
				return new BlueTower(x, y, gEng, r);
			}

			@Override
			public Route spawnRoute() {
				return new Route(blueRouteP);
			}
		};
	}
}
