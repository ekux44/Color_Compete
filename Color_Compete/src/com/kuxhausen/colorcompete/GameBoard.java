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
	private static float ACCEPTABLE_NEARNESS = .8f;
	
	/* Temporary helper until getNeighbors is properly implemented */

	public GameBoard(float xMin, float xMax, float y) {
		xOffset = xMin;
		xSpan = xMax - xMin;
		ySpan = y;
		neighbors = new ArrayList<GamePiece>();
		all = new ArrayList<GamePiece>();
	}

	public boolean conflicts(float x, float y, float radius, GamePiece gp)
	{
		GamePiece near = getNearestNeighbor(x,y, gp);
		if(near!=null && distanceBetween(x,y,near.xc,near.yc) < ACCEPTABLE_NEARNESS*(radius+near.radius))
			return true;
		return false;
	}
	
	public void move(GamePiece gp, float dx, float dy) {
		//if(!conflicts(gp.xc+dx, gp.yc+dy, gp.radius))
		{	
			gp.xc += dx;
			gp.yc += dy;
		}
	}
	public boolean move(GamePiece gp, Pair delta) {
		if(!conflicts(gp.xc+delta.x, gp.yc+delta.y, gp.radius, gp))
		{	
			gp.xc += delta.x;
			gp.yc += delta.y;
			return true;
		}
		return false;
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

	/** @param exclude
	 *            the gamepice to search around; it will be excluded from the considered pieces
	 */
	public GamePiece getNearestNeighbor(float x, float y, GamePiece exclude) {
		GamePiece nearest = null;
		float nearness = Float.MAX_VALUE;
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i)!=exclude && i == 0) {
				nearest = all.get(i);
				nearness = distanceBetween(nearest.xc, nearest.yc, x, y) - nearest.radius;
			} else if (all.get(i)!=exclude && distanceBetween(all.get(i).xc, all.get(i).yc, x, y) - all.get(i).radius < nearness) {
				nearest = all.get(i);
				nearness = distanceBetween(nearest.xc, nearest.yc, x, y) - nearest.radius;
			}
		}
		return nearest;
	}
	
	public GamePiece getNearest(float x, float y) {
		return getNearestNeighbor(x,y,null);
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
