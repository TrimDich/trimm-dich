package de.lmu.msp.trimmdich;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CompassActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
	}
	
	public void arriveAtExercise(View view) {
		Intent newIntent = new Intent(this, ExerciseActivity.class);
		startActivity(newIntent);
	}
}
