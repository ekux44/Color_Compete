package com.kuxhausen.colorcompete.basiclevels;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.kuxhausen.colorcompete.EnemySpawner;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.GamePiece;
import com.kuxhausen.colorcompete.ResourceSpawner;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Holds all of the game piece paints, and generates the appropriate EnemySpawner and ResourceSpawner for a level
 * 
 * @author Eric Kuxhausen
 */
public class LevelLoader {

	static Paint smallEnemyP, mediumEnemyP, largeEnemyP, redEnemyP, greenEnemyP, enemyProjectileP, blueTowerP, greenTowerP, redTowerP, simpleProjectileP;

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
	}

	public static float loadCameraVelocity(int level, GameEngine gEng) {
		switch (level) {
		case 4:
			return .2f;
		case 5:
			return -.1f;
		}
		return 0f;
	}

	/** returns the appropriately configured EnemySpawner for that level */
	public static EnemySpawner loadEnemySpawner(int level, GameEngine gEng) {
		Class[] enemeyTypes = new Class[5];

		try {
			enemeyTypes[0] = SmallEnemy.class;
			enemeyTypes[1] = MediumEnemy.class;
			enemeyTypes[2] = LargeEnemy.class;
			enemeyTypes[3] = RedEnemy.class;
			enemeyTypes[4] = RedEnemy.class;
		} catch (LinkageError e) {
		}
		float[] probabilities = { .5f, .7f, .8f, .9f, 1f};// cumulative mass function
		EnemySpawner enemy = new EnemySpawner(gEng, enemeyTypes, probabilities, 10000, 2, 1000);
		switch (level) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 4:
			break;
		case 5:
			break;
		default:
		}
		return enemy;
	}

	/** returns the appropriately configured ResourceSpawner[] for that level */
	public static ResourceSpawner[] loadSpawners(int level, GameEngine gEng) {
		ResourceSpawner[] spawns;
		switch (level) {
		case 1:
			spawns = new ResourceSpawner[1];
			spawns[0] = getBlue(gEng, 3, 200, 400);
			break;
		case 2:
			spawns = new ResourceSpawner[1];
			spawns[0] = getRed(gEng, 1, 300, 600);
			break;
		case 3:
			spawns = new ResourceSpawner[1];
			spawns[0] = getGreen(gEng, 2, 200, 200);
			break;
		default:
			spawns = new ResourceSpawner[3];
			spawns[0] = getBlue(gEng, 1, 200, 500);
			spawns[1] = getRed(gEng, 1, 250, 500);
			spawns[2] = getGreen(gEng, 2, 200, 500);

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
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new RedTower(x, y, gEng);
			}
			// TODO add firing radius visualization
			/*
			 * public void drawTouch(Canvas c, float tX, float tY) { c.drawCircle(tX, tY, 50f, charging); }
			 */
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
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new GreenTower(x, y, gEng);
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
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new BlueTower(x, y, gEng);
			}
		};
	}
}
