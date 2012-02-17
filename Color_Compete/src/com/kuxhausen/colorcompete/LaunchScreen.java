package com.kuxhausen.colorcompete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**(c) 2012 Eric Kuxhausen
 * @author Eric Kuxhausen
 */
public class LaunchScreen extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		View LaunchButton = this.findViewById(R.id.launchButton);
		LaunchButton.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.launchButton:
			Intent singlePlayerGame = new Intent(this, GameActivity.class);
			startActivity(singlePlayerGame);
			break;

		}
	}
}