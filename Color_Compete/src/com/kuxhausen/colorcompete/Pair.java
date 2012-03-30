package com.kuxhausen.colorcompete;

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
