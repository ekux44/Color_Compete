package com.kuxhausen.colorcompete;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameLoopThread extends Thread {
	
	private SurfaceHolder mSurfaceHolder;
	private Handler mHandler;
	private Context mContext;
	GameEngine gEngine;

	// used to control rate at which game loop runs
	private long sleepTime;
	private long delay = 32;
	
	// used by updateFPS() to approximate rate of frames per second, every 10 frames
	private long FPS_intervalStartTime;
	private int FPS_framesCounted = 0; // number of frames since was this tracking interval started
	private final int FPS_framesPerInterval = 10; // number of frames in each FPS tracking interval
	private int FPS = -1; //frames per second
	
	// use to control how often touch events are processed
	private long lastTouchSample;
	private long touchSamplingInterval = 20;
	
	
	// state of game
	int state = 1;
	public final static int RUNNING = 1;
	public final static int PAUSED = 2;

	public GameLoopThread(SurfaceHolder surfaceHolder, Context context,
			Handler handler, GameEngine gEngineS) {

		// data about the screen
		mSurfaceHolder = surfaceHolder;
		this.mHandler = handler;
		this.mContext = context;
		gEngine = gEngineS;
	}

	/** invoked when the call to start() is made from the SurfaceView class. 
	 * It loops continuously until the game is finished or the application is suspended.
	 */
	@Override
	public void run() {

		while (state == RUNNING) {
			// time before update
			long beforeTime = System.nanoTime();

			/** DRAW **/
			updateFPS();
			Canvas c = null;
			try {
				// lock canvas so nothing else can use it
				c = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {
					// draw the next frame game engine.
					gEngine.Draw(c, FPS);
				}
			} finally {
				// done in finally so that if an exception is thrown
				// during the above, Surface isn't left in an inconsistent state
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}

			/** SLEEP **/
			// recalculate sleep delay 
			this.sleepTime = delay
					- ((System.nanoTime() - beforeTime) / 1000000L);

			try {
				//sleep
				if (sleepTime > 0) {
					this.sleep(sleepTime);
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(GameLoopThread.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	//TODO revaluate approach to touch handling
	/** passes touch events to game engine, rate limited by touchSamplingInterval **/
	public boolean doTouchEvent(MotionEvent e) {
		if((System.nanoTime() - lastTouchSample)/1000000L > touchSamplingInterval){
			lastTouchSample = System.nanoTime();
			gEngine.SampleTouchInput(e);
		}
		return true;
	}
	
	private void updateFPS()
	{
		FPS_framesCounted++;
		if(FPS_framesCounted>=FPS_framesPerInterval)
		{
			FPS = (int)((FPS_framesCounted*1000000000L)/(System.nanoTime()-FPS_intervalStartTime));
			FPS_framesCounted = 0;
			FPS_intervalStartTime = System.nanoTime();
		}
		Log.i("FPS",""+FPS); 
	}
}