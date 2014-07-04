package de.lmu.msp.trimmdich.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.dialog.NumberPickerFragement;
import de.lmu.msp.trimmdich.util.Constants;

public class MainActivity extends Activity {

	private SharedPreferences mSPrefs;

	private LinearLayout mDistanceLinearLayout;
	private LinearLayout mExerciseCountLinearLayout;
	private LinearLayout mExerciseTypesLinearLayout;

	private TextView mDistanceTextView;
	private TextView mExerciseCountTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDistanceLinearLayout = (LinearLayout) findViewById(R.id.distanceLinearLayout);
		mExerciseCountLinearLayout = (LinearLayout) findViewById(R.id.excerciseCountLinearLayout);
		mExerciseTypesLinearLayout = (LinearLayout) findViewById(R.id.excerciseTypesLinearLayout);

		mDistanceTextView = (TextView) findViewById(R.id.setupDistanceTextView);
		mExerciseCountTextView = (TextView) findViewById(R.id.setupExerciseCountTextView);

		mDistanceLinearLayout.setTag(Constants.DISTANCE_PICKER_TYPE);
		mDistanceLinearLayout.setOnClickListener(mOnLinearLayoutClick);

		mExerciseCountLinearLayout.setTag(Constants.EXERCISE_COUNT_PICKER_TYPE);
		mExerciseCountLinearLayout.setOnClickListener(mOnLinearLayoutClick);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateTextViews();
	}

	private OnClickListener mOnLinearLayoutClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String type = (String) v.getTag();
			NumberPickerFragement f = NumberPickerFragement.newInstance(type);
			f.show(getFragmentManager(), "dialog");
		}
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void startNewRun(View view) {
		Intent newIntent = new Intent(this, SetupActivity.class);
		startActivity(newIntent);
	}

	public void doPositiveClick() {
		updateTextViews();
	}

	public void updateTextViews() {
		mSPrefs = getSharedPreferences(Constants.PREFS_NAME, 0);

		mDistanceTextView.setText(""
				+ mSPrefs.getInt(Constants.DISTANCE_SPREF, 1));
		mExerciseCountTextView.setText(""
				+ mSPrefs.getInt(Constants.EXERCISE_COUNT_SPREF, 1));
	}

}
