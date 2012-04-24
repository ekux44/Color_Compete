package com.kuxhausen.colorcompete;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Records gameplay statistics and calculates score; Abstracted into separate class to easy implementing persistent
 * and/or network storage
 * 
 * @author Eric Kuxhausen
 */
public class StatisticsEngine {

	private float darknessEliminated;
	private int enemiesEliminated;

	public StatisticsEngine() {
		darknessEliminated = 0;
		enemiesEliminated = 0;
	}

	public float getDarknessEliminated() {
		return darknessEliminated;
	}

	public void enemeyDamaged(float damage) {
		darknessEliminated += damage;
	}

	public void pieceDied(GamePiece g) {
	}

	public void destroyEnemy() {
		enemiesEliminated++;
	}

}
