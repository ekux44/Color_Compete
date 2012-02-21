package com.kuxhausen.colorcompete;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * GameView is the dedicated view on which the game is drawn.
 * 
 * @author Eric Kuxhausen
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	/** handler to the display surface **/
	private SurfaceHolder surfaceHolder;

	private GameEngine gEngine;
	private Context context;
	private GameLoopThread gThread;
	private ArrayList<MotionEvent> touches = new ArrayList<MotionEvent>();

	/** Pointer to the text view to display */
	private TextView statusText;

	/** initialization code */
	public void initView(int level) {
		// initialize screen holder
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// initialize game engine
		gEngine = new GameEngine();
		gEngine.Init(this, context.getResources(), level);

		// initialize game loop thread, start will be called later
		gThread = new GameLoopThread(holder, context, gEngine, new Handler() {
			@Override
			public void handleMessage(Message m) {
				statusText.setVisibility(m.getData().getInt("viz"));
				statusText.setText(m.getData().getString("text"));
			}
		});
		setFocusable(true);
	}

	public void endGame(boolean playerWon) {
		gThread.endGame(playerWon);
	}

	/** constructor **/
	public GameView(Context cont) {
		super(cont);
		context = cont;
		
		//InitView();
	}

	/** constructor **/
	public GameView(Context cont, AttributeSet attrs) {
		super(cont, attrs);
		context = cont;
		//InitView();
	}

	/** constructor **/
	public GameView(Context cont, AttributeSet attrs, int defStyle) {
		super(cont, attrs, defStyle);
		context = cont;
		//InitView();
	}

	@Override
	/** Handles touchScreen events by recording them */
	public boolean onTouchEvent(MotionEvent event) {
		touches.add(event);
		return true;
	}

	/**
	 * Provides all touch inputs recorded since last time this was called
	 * 
	 * @param copy
	 *            ArrayList the recorded inputs will be copied into
	 */
	protected void getInputs(ArrayList<MotionEvent> copy) {
		copy.addAll(touches);
		touches.clear();
	}

	public void setTextView(TextView textView) {
		statusText = textView;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		// code to end game loop
		gThread.state = GameLoopThread.PAUSED;
		while (retry) {
			try {
				// code to kill game loop thread
				gThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		if (gThread.state == GameLoopThread.PAUSED) {
			// When game is opened again in the Android OS
			gThread = new GameLoopThread(getHolder(), context, gEngine, new Handler() {
				@Override
				public void handleMessage(Message m) {
					statusText.setVisibility(m.getData().getInt("viz"));
					statusText.setText(m.getData().getString("text"));
				}
			});
			gThread.start();
		} else {
			// create game loop thread for the first time
			gThread.start();
		}
	}
}
