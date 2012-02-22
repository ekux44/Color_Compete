package com.kuxhausen.colorcompete.basiclevels;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;

import com.kuxhausen.colorcompete.EnemySpawner;
import com.kuxhausen.colorcompete.GameEngine;
import com.kuxhausen.colorcompete.ResourceSpawner;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Loads level
 * 
 * @author Eric Kuxhausen
 */
public class LevelLoader {

	static Paint smallEnemyP, mediumEnemyP, blueTowerP, redTowerP, simpleProjectileP;

	public LevelLoader() {
		smallEnemyP = new Paint();
		smallEnemyP.setColor(Color.BLACK);

		mediumEnemyP = new Paint();
		mediumEnemyP.setColor(Color.BLACK);

		blueTowerP = new Paint();
		blueTowerP.setColor(Color.BLUE);

		redTowerP = new Paint();
		redTowerP.setColor(Color.RED);

		simpleProjectileP = new Paint();
		simpleProjectileP.setColor(Color.RED);
	}

	public static EnemySpawner loadEnemySpawner(int level, GameEngine gEng) {
		EnemySpawner enemy = new EnemySpawner(gEng, 1000, 2, 1000);
		switch (level) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		default:
			}
		return enemy;
	}
	public static ResourceSpawner[] loadSpawners(int level, GameEngine gEng) {
		ResourceSpawner[] spawns;
		switch (level) {
		case 0:
			spawns = new ResourceSpawner[2];
			spawns[0] = getBlue(gEng, 3, 200, 900);
			spawns[1] = getRed(gEng, 1, 300, 900);
			break;
		case 1:
			spawns = new ResourceSpawner[1];
			spawns[0] = getBlue(gEng, 3, 200, 400);
			break;
		case 2:
			spawns = new ResourceSpawner[1];
			spawns[0] = getRed(gEng, 1, 300, 600);
			break;
		default:
			spawns = new ResourceSpawner[2];
			spawns[0] = getBlue(gEng, 1, 200, 1000);
			spawns[1] = getRed(gEng, 1, 250, 1000);
			}
		return spawns;
	}
	
	private static ResourceSpawner getRed(GameEngine gEng, int spawnRate, int spawnCost, int startingFill){
		Paint ready = new Paint();
		ready.setColor(Color.RED);
		Paint charging = new Paint();
		charging.setARGB(0xFF, 0xFF, 0x80, 0x80);
		return new ResourceSpawner(gEng, ready, charging, spawnRate, spawnCost, startingFill) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new RedTower(x, y, gEng);
			}
		};
	}
	private static ResourceSpawner getBlue(GameEngine gEng, int spawnRate, int spawnCost, int startingFill){
		Paint ready = new Paint();
		ready.setColor(Color.BLUE);
		Paint charging = new Paint();
		charging.setARGB(0xFF, 0x80, 0x80, 0xFF);
		return new ResourceSpawner(gEng, ready, charging, spawnRate, spawnCost, startingFill) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new BlueTower(x, y, gEng);
			}
		};
	}
}
