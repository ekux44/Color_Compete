package com.kuxhausen.colorcompete;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * GameActivity is the Android Activity within which individual game levels run
 * 
 * @author Eric Kuxhausen
 */
public class GameActivity extends Activity {

	private GameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// disable title bar, ask for fullscreen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// tell system to use the layout defined in this XML file
		setContentView(R.layout.game);

		// get handle to the GameView from XML
		gameView = (GameView) findViewById(R.id.gView);

		// give the GameView a handle to the TextView used for messages
		gameView.setTextView((TextView) findViewById(R.id.text));
		
		// give the GameView a handle to this(used to end the game)
		gameView.setGameActivity(this);
		
		// give the GameView a the level it should be loading
		gameView.initView(this.getIntent().getExtras().getInt("level"));

		// TODO allow for actually resuming from previous game
		if (savedInstanceState == null) {
			// we were just launched: set up a new game
			Log.w(this.getClass().getName(), "Saved Instance State is null");
		} else {
			// we are being restored: resume a previous game
			Log.w(this.getClass().getName(), "Saved Instance State is nonnull");
		}
	}

	public void die(){
		onDestroy();
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
