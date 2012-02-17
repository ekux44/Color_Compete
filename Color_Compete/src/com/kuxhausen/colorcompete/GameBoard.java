package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.util.Log;

/**(c) 2012 Eric Kuxhausen
 * @author Eric Kuxhausen
 */
public class GameBoard {
	
	ArrayList<GamePiece>[][] grid;
	float xOffset, xSpan, ySpan;
	final static int xTiles=4,yTiles=4;
	
	public GameBoard(float xMin, float xMax, float y){
		xOffset = xMin;
		xSpan = xMax-xMin;
		ySpan = y;
		grid = new ArrayList[xTiles][yTiles];
		for(int r = 0; r<xTiles; r++)
			for(int c= 0; c<yTiles; c++)
				grid[r][c] = new ArrayList<GamePiece>();
	}
	public GamePiece register(GamePiece o){
		Log.i("grid)", "registered in"+ (int)(grid.length*(o.xc-xOffset)/xSpan)+" , "+(int)(grid[0].length*(o.yc)/ySpan));
		grid[(int)(grid.length*(o.xc-xOffset)/xSpan)][(int)(grid[0].length*(o.yc)/ySpan)].add(o);
		return o;
	}
	public void update(GamePiece o, float xOld, float yOld){
		unregister(o,xOld,yOld);
		register(o);
	}
	public void unregister(GamePiece o, float xOld, float yOld){
		//TODO
	}
	public ArrayList<GamePiece> getNeighbors(float x, float y){
		//TODO
		return null;
	}
}
