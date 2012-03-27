package com.kuxhausen.colorcompete;

import java.util.ArrayList;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * GameBoard manages the location of all GamePieces in it. Future revisions will be used to optimizing which GamePieces
 * to check when calculating neighbors for collision detection and other area interaction
 * 
 * @author Eric Kuxhausen
 */
public class GameBoard {

	// may switch data structures in the future, need to minimize cost of interacting with neighbors and drawing
	// everthing in view
	ArrayList<GamePiece> all; // temp
	ArrayList<GamePiece> neighbors;
	float xOffset, xSpan, ySpan;
	final static int xTiles = 4, yTiles = 4;

	/* Temporary helper until getNeighbors is properly implemented */

	public GameBoard(float xMin, float xMax, float y) {
		xOffset = xMin;
		xSpan = xMax - xMin;
		ySpan = y;
		neighbors = new ArrayList<GamePiece>();
		all = new ArrayList<GamePiece>();
	}

	public void move(GamePiece gp, float dx, float dy) {
		gp.xc += dx;
		gp.yc += dy;
	}
	public void move(GamePiece gp, Pair p) {
		gp.xc += p.x;
		gp.yc += p.y;
	}

	/**
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param radius
	 *            search radius
	 * @return ArrayList containing at least all GamePices on this board in that radius; other pieces may be returned as
	 *         well
	 */
	public ArrayList<GamePiece> getNeighbors(float x, float y, float radius) {
		// TODO
		return all;
	}

	public GamePiece getNearestNeighbor(float x, float y) {
		GamePiece nearest = null;
		float nearness = Float.MAX_VALUE;
		for (int i = 0; i < all.size(); i++) {
			if (i == 0) {
				nearest = all.get(i);
				nearness = distanceBetween(nearest.xc, nearest.yc, x, y) - nearest.radius;
			} else if (distanceBetween(all.get(i).xc, all.get(i).yc, x, y) - all.get(i).radius < nearness) {
				nearest = all.get(i);
				nearness = distanceBetween(nearest.xc, nearest.yc, x, y) - nearest.radius;
			}
		}
		return nearest;
	}

	/** performs simple calculation of geometric distance between two points */
	public static float distanceBetween(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

	public void unregister(GamePiece thing) {
		all.remove(thing);

	}

	public void register(GamePiece thing) {
		all.add(thing);

	}
}
