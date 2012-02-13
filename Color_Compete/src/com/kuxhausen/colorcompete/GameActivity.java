package com.kuxhausen.colorcompete;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

	private GameView mGameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// tell system to use the layout defined in our XML file
		setContentView(R.layout.game);

		// get handle to the ColorCompeteView from XML
		mGameView = (GameView) findViewById(R.id.gView);

		if (savedInstanceState == null) {
			// we were just launched: set up a new game
			Log.w(this.getClass().getName(), "Saved Instance State is null");
		} else {
			// we are being restored: resume a previous game
			Log.w(this.getClass().getName(), "Saved Instance State is nonnull");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("state", "onPaused");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("state", "onResumed");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("state", "onStopped");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("state", "onRestarted");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("state", "onStarted");
	}
}
