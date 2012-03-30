package com.kuxhausen.colorcompete;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Holds 2 float vectors and can be linked with other Pairs.
 * Usually used to store the coordinates of a point or offset.
 * 
 * @author Eric Kuxhausen
 */
public class Pair {
	float x;
	float y;
	Pair previous;
	Pair next;

	public Pair(float X, float Y, Pair Previous, Pair Next) {
		x = X;
		y = Y;
		previous = Previous;
		next = Next;
	}

	public Pair(float X, float Y) {
		x = X;
		y = Y;
		previous = null;
		next = null;
	}

	public float getMagnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}
}
