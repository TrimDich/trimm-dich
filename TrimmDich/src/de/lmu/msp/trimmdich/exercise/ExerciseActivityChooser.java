package de.lmu.msp.trimmdich.exercise;

import de.lmu.msp.trimmdich.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ExerciseActivityChooser extends Activity {
	public static final String TAG = "TD-Exercise";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_done_layout);
		setTitle(R.string.exercise_title);
	}

	public void onClickSuccess(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result","0");
		setResult(RESULT_OK,returnIntent);
		finish();
	}

	public void onClickNearSuccess(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result","1");
		setResult(RESULT_OK,returnIntent);
		finish();
	}

	public void onClickNoSuccess(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result","2");
		setResult(RESULT_OK,returnIntent);
		finish();
	}

	@Override
	public void onBackPressed() {
		onClickNoSuccess(null);
	}
	
}
