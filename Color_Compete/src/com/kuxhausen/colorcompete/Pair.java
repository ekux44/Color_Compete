package com.kuxhausen.colorcompete;

public class Pair {
	float x;
	float y;
	
	public Pair(float X, float Y){
		x=X;
		y=Y;
	}
	public float getMagnitude(){
		return (float) Math.sqrt(x*x + y*y);
	}
}
