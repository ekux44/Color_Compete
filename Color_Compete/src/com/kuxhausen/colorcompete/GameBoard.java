package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.util.Log;

/**
 * (c) 2012 Eric Kuxhausen
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

	public GameBoard(float xMin, float xMax, float y) {
		xOffset = xMin;
		xSpan = xMax - xMin;
		ySpan = y;
		neighbors = new ArrayList<GamePiece>();
		grid = new ArrayList[xTiles][yTiles];
		for (int r = 0; r < xTiles; r++)
			for (int c = 0; c < yTiles; c++)
				grid[r][c] = new ArrayList<GamePiece>();
	}

	public GamePiece register(GamePiece o) {
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
		grid[(int) (grid.length * (o.xc - xOffset) / xSpan)][(int) (grid[0].length * (o.yc) / ySpan)].remove(o);
	}

	public ArrayList<GamePiece> getNeighbors(float x, float y) {
		neighbors.clear();
		neighbors.addAll(grid[(int) (grid.length * (y - xOffset) / xSpan)][(int) (grid[0].length * (y) / ySpan)]);
		// TODO check next level neighboring
		return neighbors;
	}
}
