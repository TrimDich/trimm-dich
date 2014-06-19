package de.lmu.msp.trimmdich.run;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.layout;
import de.lmu.msp.trimmdich.exercise.ExerciseReviewActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ExerciseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
	}
	
	public void finishExercise(View view) {
		Intent newIntent = new Intent(this, ExerciseReviewActivity.class);
		startActivity(newIntent);
	}
}
