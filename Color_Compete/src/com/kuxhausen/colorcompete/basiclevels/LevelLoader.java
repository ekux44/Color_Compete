package com.kuxhausen.colorcompete.basiclevels;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;

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

	public static ResourceSpawner[] loadSpawners(int level, GameEngine gEng) {
		switch (level) {
		case 0:
			return buildLevel0(gEng);
		case 1:
			return buildLevel1(gEng);
		case 2:
			return buildLevel2(gEng);
		default:
			return buildLevel0(gEng);
		}
	}

	private static ResourceSpawner[] buildLevel0(GameEngine gEng) {
		ResourceSpawner[] spawns = new ResourceSpawner[2];

		Paint temp1 = new Paint();
		temp1.setColor(Color.BLUE);
		spawns[0] = new ResourceSpawner(gEng, temp1, 3, 200, 900) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new BlueTower(x, y, gEng);
			}
		};
		Paint temp2 = new Paint();
		temp2.setColor(Color.RED);
		spawns[1] = new ResourceSpawner(gEng, temp2, 1, 300, 900) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new RedTower(x, y, gEng);
			}
		};
		return spawns;
	}

	private static ResourceSpawner[] buildLevel1(GameEngine gEng) {
		ResourceSpawner[] spawns = new ResourceSpawner[1];

		Paint temp1 = new Paint();
		temp1.setColor(Color.BLUE);
		spawns[0] = new ResourceSpawner(gEng, temp1, 3, 200, 400) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new BlueTower(x, y, gEng);
			}
		};
		return spawns;
	}
	private static ResourceSpawner[] buildLevel2(GameEngine gEng) {
		ResourceSpawner[] spawns = new ResourceSpawner[1];

		Paint temp2 = new Paint();
		temp2.setColor(Color.RED);
		spawns[0] = new ResourceSpawner(gEng, temp2, 1, 300, 600) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new RedTower(x, y, gEng);
			}
		};
		return spawns;
	}
}
