package de.lmu.msp.trimmdich.exercise;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ExerciseReviewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_review);
	}
	
	public void startNewRun(View view) {
		Intent newIntent = new Intent(this, ExerciseReviewActivity.class);
		startActivity(newIntent);
	}
}
