package com.kuxhausen.colorcompete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Activity that launches specific game levels
 * 
 * @author Eric Kuxhausen
 */
public class LaunchScreen extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		View LaunchButton0 = this.findViewById(R.id.launchButton0);
		LaunchButton0.setOnClickListener(this);

		View LaunchButton1 = this.findViewById(R.id.launchButton1);
		LaunchButton1.setOnClickListener(this);

		View LaunchButton2 = this.findViewById(R.id.launchButton2);
		LaunchButton2.setOnClickListener(this);

		View LaunchButton3 = this.findViewById(R.id.launchButton3);
		LaunchButton3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent singlePlayerGame = new Intent(this, GameActivity.class);
		switch (v.getId()) {
		case R.id.launchButton0:
			singlePlayerGame.putExtra("level", 0);
			startActivity(singlePlayerGame);
			break;
		case R.id.launchButton1:
			singlePlayerGame.putExtra("level", 1);
			startActivity(singlePlayerGame);
			break;
		case R.id.launchButton2:
			singlePlayerGame.putExtra("level", 2);
			startActivity(singlePlayerGame);
			break;
		case R.id.launchButton3:
			singlePlayerGame.putExtra("level", 3);
			startActivity(singlePlayerGame);
			break;

		}
	}
}