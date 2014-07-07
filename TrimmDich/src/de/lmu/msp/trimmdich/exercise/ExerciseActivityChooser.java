package de.lmu.msp.trimmdich.exercise;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;

public class ExerciseActivityChooser extends Activity {
	public static final String TAG = "TD-Exercise";

	private TextView mExerciseTextView;
	private TextView mRepetitionTextView;
	private ImageView mExerciseImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_done_layout);
		setTitle(R.string.exercise_title);

		ActionBar bar = getActionBar();
		bar.setIcon(R.drawable.running_actionbar_white);

		mExerciseTextView = (TextView) findViewById(R.id.currentExerciseTextView);
		mRepetitionTextView = (TextView) findViewById(R.id.repetitionGoalTextView);
		mExerciseImageView = (ImageView) findViewById(R.id.exerciseImg);

	}

	public void onClickSuccess(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", "0");
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void onClickNearSuccess(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", "1");
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void onClickNoSuccess(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", "2");
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	@Override
	public void onBackPressed() {
		onClickNoSuccess(null);
	}

}
