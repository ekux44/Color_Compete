package com.kuxhausen.colorcompete;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Route {

	private Pair first, last, current;
	private Path visualPath;
	private Paint p;
	public boolean loopMode;
	public boolean foward;

	public Route(Paint paint) {

		visualPath = new Path();
		p = paint;
		current = first;

	}

	public void addPoint(float x, float y) {
		Pair toAdd = new Pair(x,y);
		if(first==null){
			first = toAdd;
			last = toAdd;
			current = toAdd;
			visualPath.moveTo(x, y);
		}
		else {
			toAdd.previous = last;
			last.next = toAdd;
			last = toAdd;
			visualPath.lineTo(x, y);
		}
	}
	/** returns the new front Pair */
	public Pair removeFront(){
		if(first!=null){
			if(current == first)
				current = first.next;
			first = first.next;
			
			//rebuild the visualization
			visualPath.reset();
			Pair iterate = first;
			visualPath.moveTo(iterate.x, iterate.y);
			while(iterate.next!=null){
				iterate = iterate.next;
				visualPath.lineTo(iterate.x, iterate.y);		
			}
			
		}
		return first;
	}
	
	public void moveAlongRoute(float xc, float yc, float speed, GamePiece gp){
		
		if(current == null)
			return;
		
		// calculate distance to current target
		Pair delta = new Pair(current.x-xc,current.y-yc);
		
		
		//normalize and scale to speed
		Pair movement = delta;
		if(delta.getMagnitude()>speed){
			float scale = speed/delta.getMagnitude();
			if(Float.isNaN(scale) || Float.isInfinite(scale))
				scale = 0;
			movement = new Pair(delta.x*scale,delta.y*scale);
		}
		
		//if succeeded in moving, 
		if(gp.gb.move(gp, movement)){
			//if target reached, re-target to next
			if(movement.getMagnitude()<.9*speed){//TODO move more this turn
				Pair target = foward ? current.next : current.previous;
				//if nothing in that direction, reverse direction
				if(target==null){
					foward = !foward;
					target = (foward) ? current.next : current.previous;
					
					//if still nothing, return to original direction
					if(target==null){
						foward = !foward;
					}
				}
				//update current target
				if(target!=null)
					current = target;
			}
		}
	}
	
	public void moveToEnd(){
		current = last;
	}
	public void moveToStart(){
		current = first;
	}

	public void clear() {
		first = last = current = null;
		visualPath.reset();
		
	}

	/**
	 * @param c
	 *            Canvas onto which GamePiece will draw itself
	 */
	public void draw(Canvas c, float xOffset) {
		visualPath.offset(xOffset, 0);
		c.drawPath(visualPath, p);
		visualPath.offset(-xOffset, 0);
	}
}
