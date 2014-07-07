package de.lmu.msp.trimmdich.main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.RouteGenerator.RouteProperties;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.dialog.ExerciseDialogFragement;
import de.lmu.msp.trimmdich.dialog.NumberPickerFragement;
import de.lmu.msp.trimmdich.run.RunPreviewActivity;
import de.lmu.msp.trimmdich.util.Constants;

public class MainActivity extends Activity implements LocationListener {

	private RouteProperties routeProperties = new RouteProperties();
	private WorkoutTracker workoutTracker;

	private SharedPreferences mSPrefs;

	private LinearLayout mDistanceLinearLayout;
	private LinearLayout mExerciseCountLinearLayout;
	private LinearLayout mExerciseTypesLinearLayout;

	private TextView mDistanceTextView;
	private TextView mExerciseCountTextView;
	private TextView mExerciseTypesTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		ActionBar actionbar = getActionBar();
		actionbar.setIcon(R.drawable.running_white_48);
		mDistanceLinearLayout = (LinearLayout) findViewById(R.id.distanceLinearLayout);
		mExerciseCountLinearLayout = (LinearLayout) findViewById(R.id.excerciseCountLinearLayout);
		mExerciseTypesLinearLayout = (LinearLayout) findViewById(R.id.excerciseTypesLinearLayout);

		mDistanceTextView = (TextView) findViewById(R.id.setupDistanceTextView);
		mExerciseCountTextView = (TextView) findViewById(R.id.setupExerciseCountTextView);
		mExerciseTypesTextView = (TextView) findViewById(R.id.setupExerciseTypesTextView);

		mDistanceLinearLayout.setTag(Constants.DISTANCE_PICKER_TYPE);
		mDistanceLinearLayout.setOnClickListener(mOnLinearLayoutClick);

		mExerciseCountLinearLayout.setTag(Constants.EXERCISE_COUNT_PICKER_TYPE);
		mExerciseCountLinearLayout.setOnClickListener(mOnLinearLayoutClick);

		mExerciseTypesLinearLayout
				.setOnClickListener(mOnExerciseTypeLinearLayoutClick);
		mSPrefs = getSharedPreferences(Constants.PREFS_NAME, 0);

		mDistanceTextView.setText(""
				+ mSPrefs.getInt(Constants.DISTANCE_SPREF, 1));
		mExerciseCountTextView.setText(""
				+ mSPrefs.getInt(Constants.EXERCISE_COUNT_SPREF, 1));

		routeProperties.desiredExercises = mSPrefs.getInt(
				Constants.DISTANCE_SPREF, 1);
		routeProperties.desiredLengthInKm = mSPrefs.getInt(
				Constants.EXERCISE_COUNT_SPREF, 1);
		routeProperties.startPosition = new LatLng(48.1735192, 11.5920656);

	}

	@Override
	protected void onResume() {
		super.onResume();

		workoutTracker = WorkoutTracker.getInstance();
		workoutTracker.setCurrentActivity(this);

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

	private OnClickListener mOnExerciseTypeLinearLayoutClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ExerciseDialogFragement f = ExerciseDialogFragement.newInstance();
			f.show(getFragmentManager(), "dialog");

		}
	};

	public void startNewRun(View view) {
		Intent newIntent = new Intent(this, RunPreviewActivity.class);
		routeProperties.saveToIntent(newIntent);
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

		String exercises = "";

		boolean dips = mSPrefs.getBoolean(Constants.DIPS_EXERCISE_SPREF, true);
		boolean pullup = mSPrefs.getBoolean(Constants.PULLUP_EXERCISE_SPREF,
				true);
		boolean pushup = mSPrefs.getBoolean(Constants.PUSHUP_EXERCISE_SPREF,
				true);
		boolean squats = mSPrefs.getBoolean(Constants.SQUATS_EXERCISE_SPREF,
				true);
		if (dips) {
			exercises = getResources().getString(R.string.dips) + ", ";
		}
		if (pullup) {
			exercises += getResources().getString(R.string.pullUps) + ", ";
			;
		}
		if (pushup) {
			exercises += getResources().getString(R.string.pushUps) + ", ";
			;
		}
		if (squats) {
			exercises += getResources().getString(R.string.squats);
			;
		}

		mExerciseTypesTextView.setText(exercises);

		routeProperties.desiredExercises = mSPrefs.getInt(
				Constants.EXERCISE_COUNT_SPREF, 1);
		routeProperties.desiredLengthInKm = (double) mSPrefs.getInt(
				Constants.DISTANCE_SPREF, 1);

	}

	@Override
	public void onLocationChanged(Location location) {

		LatLng position = new LatLng(location.getLatitude(),
				location.getLongitude());
		routeProperties.startPosition = position;

	}

}
