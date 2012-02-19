package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.util.Log;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * GameBoard manages the location of all GamePieces in it. Future revisions will be used to optimizing which GamePieces
 * to check when calculating neighbors for collision detection and other area interaction
 * 
 * @author Eric Kuxhausen
 */
public class GameBoard {

	// may switch data structures in the future, need to minimize cost of
	// combining neighboring cells
	ArrayList<GamePiece>[][] grid;
	ArrayList<GamePiece> neighbors;
	float xOffset, xSpan, ySpan;
	final static int xTiles = 4, yTiles = 4;

	/* Temporary helper until getNeighbors is properly implemented */
	ArrayList<GamePiece> all; // temp

	public GameBoard(float xMin, float xMax, float y) {
		xOffset = xMin;
		xSpan = xMax - xMin;
		ySpan = y;
		neighbors = new ArrayList<GamePiece>();
		grid = new ArrayList[xTiles][yTiles];
		for (int r = 0; r < xTiles; r++)
			for (int c = 0; c < yTiles; c++)
				grid[r][c] = new ArrayList<GamePiece>();

		all = new ArrayList<GamePiece>();
	}

	public GamePiece register(GamePiece o) {
		all.add(o);

		Log.i("grid)", "registered in" + (int) (grid.length * (o.xc - xOffset) / xSpan) + " , "
				+ (int) (grid[0].length * (o.yc) / ySpan));
		grid[(int) (grid.length * (o.xc - xOffset) / xSpan)][(int) (grid[0].length * (o.yc) / ySpan)].add(o);
		return o;
	}

	public boolean willMoveZones(float x1, float y1, float x2, float y2) {
		return ((int) (grid.length * (x1 - xOffset) / xSpan) != (int) (grid.length * (x2 - xOffset) / xSpan) || ((int) (grid[0].length
				* (y1) / ySpan) != (int) (grid[0].length * (y2) / ySpan)));
	}

	public void unregister(GamePiece o) {
		all.remove(o);

		grid[(int) (grid.length * (o.xc - xOffset) / xSpan)][(int) (grid[0].length * (o.yc) / ySpan)].remove(o);
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
		// neighbors.clear();
		// neighbors.addAll(grid[(int) (grid.length * (y - xOffset) / xSpan)][(int) (grid[0].length * (y) / ySpan)]);
		// TODO check next level neighboring
		// return neighbors;
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
}
