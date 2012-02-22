package com.kuxhausen.colorcompete;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Thread that contains the game loop
 * 
 * @author Eric Kuxhausen
 */
public class GameLoopThread extends Thread {

	private SurfaceHolder mSurfaceHolder;
	private Handler mHandler;
	private Context mContext;
	GameEngine gEngine;

	// used to control rate at which game loop runs
	private long sleepTime;
	private long delay = 16;

	// used by updateFPS() to approximate rate of frames per second, every 10
	// frames
	private long FPS_intervalStartTime;
	private int FPS_framesCounted = 0; // number of frames since was this
										// tracking interval started
	private final static int FPS_framesPerInterval = 10; // number of frames in each
	// FPS tracking interval
	private int FPS = -1; // frames per second

	// state of game
	int state = 1;
	public final static int RUNNING = 1;
	public final static int PAUSED = 2;

	public GameLoopThread(SurfaceHolder surfaceHolder, Context context, GameEngine gEngineS, Handler handler) {

		// data about the screen
		mSurfaceHolder = surfaceHolder;
		this.mHandler = handler;
		this.mContext = context;
		gEngine = gEngineS;
	}

	/**
	 * invoked when the call to start() is made from the GameView. It loops continuously until the game is finished or
	 * the application is suspended.
	 */
	@Override
	public void run() {
		while (state == RUNNING) {
			// time before update
			long beforeTime = System.nanoTime();

			/** SAMPLE INPUT **/
			gEngine.processInput();

			/** UPDATE **/
			gEngine.update();

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
			this.sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);

			try {
				// sleep
				if (sleepTime > 0) {
					this.sleep(sleepTime);
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(GameLoopThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/** Marks game paused to shut down game loop. Also, passes message to make the results TextView visible */
	public void endGame(boolean playerWon) {
		state = GameLoopThread.PAUSED;
		/*
		 * Message msg = mHandler.obtainMessage(); Bundle b = new Bundle(); if (playerWon) b.putString("text",
		 * "You Won!"); else b.putString("text", "You Lost!"); b.putInt("viz", View.VISIBLE); b.putBoolean("clickable",
		 * true); msg.setData(b); mHandler.sendMessage(msg);
		 */
	}

	private void updateFPS() {
		FPS_framesCounted++;
		if (FPS_framesCounted >= FPS_framesPerInterval) {
			FPS = (int) ((FPS_framesCounted * 1000000000L) / (System.nanoTime() - FPS_intervalStartTime));
			FPS_framesCounted = 0;
			FPS_intervalStartTime = System.nanoTime();
		}
	}

	/** Override to use */
	public void handleMessage(Message m) {
	}
}