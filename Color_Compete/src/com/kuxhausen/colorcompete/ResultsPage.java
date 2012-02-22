package com.kuxhausen.colorcompete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * (c) 2012 Eric Kuxhausen
 * <p>
 * Results page shown after a level completes
 * 
 * @author Eric Kuxhausen
 */
public class ResultsPage extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);

		TextView ResultsText = (TextView) this.findViewById(R.id.resultsText);
		if (this.getIntent().getExtras().getBoolean("PLAYER_WON"))
			ResultsText.setText("You Won");
		else
			ResultsText.setText("You Lost!");

		TextView ScoreText = (TextView) this.findViewById(R.id.scoreText);
		ScoreText.setText("Score:"+this.getIntent().getExtras().getInt("SCORE"));
		
		Button NextButton = (Button) this.findViewById(R.id.nextButton);
		NextButton.setOnClickListener(this);

		Button MainButton = (Button) this.findViewById(R.id.mainButton);
		MainButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextButton:
			Intent iNextGame = new Intent(this, GameActivity.class);
			iNextGame.putExtra("level", 1);
			startActivity(iNextGame);
			break;
		case R.id.mainButton:
			Intent iMenu = new Intent(this, LaunchScreen.class);
			startActivity(iMenu);
			break;
		}
	}
}