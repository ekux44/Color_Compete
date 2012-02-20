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

	static Paint basicEnemyP, blueTowerP, redTowerP, simpleProjectileP;

	public LevelLoader() {
		basicEnemyP = new Paint();
		basicEnemyP.setColor(Color.BLACK);

		blueTowerP = new Paint();
		blueTowerP.setColor(Color.BLUE);

		redTowerP = new Paint();
		redTowerP.setColor(Color.RED);

		simpleProjectileP = new Paint();
		simpleProjectileP.setColor(Color.RED);
	}

	public static ResourceSpawner[] loadSpawners(int level, GameEngine gEngine) {
		ResourceSpawner[] spawns;
		// TODO impliment switching systemm to support mutliple levels
		spawns = new ResourceSpawner[2];

		/* Resource Spawners */
		Paint temp1 = new Paint();
		temp1.setColor(Color.BLUE);
		spawns[0] = new ResourceSpawner(gEngine, temp1, 3, 200, 500) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new BlueTower(x, y, gEng);
			}
		};
		Paint temp2 = new Paint();
		temp2.setColor(Color.RED);
		spawns[1] = new ResourceSpawner(gEngine, temp2, 1, 300, 600) {
			public GamePiece spawnResource(float x, float y) {
				fill -= respawnCost;
				return new RedTower(x, y, gEng);
			}
		};
		return spawns;
	}
}
