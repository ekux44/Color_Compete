package com.kuxhausen.colorcompete;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Route {

	private Pair first, last, current;
	private Path visualPath;
	private Paint p;
	public int mode;
	public boolean foward;
	public final static int PATROL_MODE = 0, CHASE_MODE = 1;
	public GamePiece gp;
	
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
	///** returns the new front Pair */
	/*public Pair removeFront(){
		if(first!=null){
			if(current == first)
				current = first.next;
			first = first.next;
			
			rebuildVisualization(front.x,front.y);		
		}
		return first;
	}*/
	
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
				// if in chase mode, remove node 
				if(mode == CHASE_MODE){
					remove(current);
					current = null;
					rebuildVisualization(xc,yc);
				}					
				//update current target
				if(target!=null)
					current = target;
			}
		}else{//movement failed, probably because of a collision
			if(mode==PATROL_MODE)
				foward = !foward;
				current = foward ? current.next : current.previous;
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
	
	public void remove(Pair c){
		if(c==null)
			return;
		Pair left = c.previous;
		Pair right = c.next;
		if(first==c)
			first = right;
		if(last==c)
			last = left;
		if(left!=null)
			left.next=right;
		if(right!=null)
			right.previous=left;
	}
	public void rebuildVisualization(float xc, float yc){
		visualPath.reset();
		Pair iterate = first;
		//visualPath.moveTo(xc, yc);
		if(first==null)
			return;
		//visualPath.lineTo(iterate.x, iterate.y);
		visualPath.moveTo(iterate.x, iterate.y);
		
		while(iterate.next!=null){
			iterate = iterate.next;
			visualPath.lineTo(iterate.x, iterate.y);		
		}
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
