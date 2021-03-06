package de.lmu.msp.trimmdich.exercise;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.summary.MapResultActivity;
import de.lmu.msp.trimmdich.summary.StatisticsMapActivity;

public class ExerciseActivity extends Activity implements SensorEventListener,
		OnInitListener, ExerciseEventListener {
	public static final String TAG = "TD-Exercise";

	private SensorManager sensorManager;
	private TextToSpeech tts;

	private ExerciseCounter exerciseCounter;
	private TextView countView;
	private TextView infoView;
	private TextView overviewView;

	private Iterator<Exercise> currentExercises;
	private Exercise currentExercise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		setTitle(R.string.excercise_activity_title);
		ActionBar bar = getActionBar();
		bar.setIcon(R.drawable.running_white_48);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		countView = (TextView) findViewById(R.id.exercise_countView);
		infoView = (TextView) findViewById(R.id.exercise_exercise_name);
		overviewView = (TextView) findViewById(R.id.exercise_overview_default);

		WorkoutTracker.getInstance().setCurrentActivity(this);
		// do stuff from onStart()
		currentExercises = WorkoutTracker.getInstance()
				.getCurrentLocationExcercices().iterator();
		prepairNextExercise();
		if (WorkoutTracker.getInstance().getCurrentLocationExcercices().size() > 0) {
			overviewView.setText(getResources().getString(
					R.string.exercise_overview,
					1,
					WorkoutTracker.getInstance().getCurrentLocationExcercices()
							.size()));
			countView.setText("" + 0);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		tts = new TextToSpeech(this, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "ExerciseActivity: onResume()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(this);
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		// //TODO: Das Logging komplett entfernen
		// try {
		// exerciseCounter.fos.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		exerciseCounter = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sensorManager = null;
		tts = null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "ExerciseActivity::onActivityResult()  " + requestCode + "|"
				+ resultCode);
		switch (Integer.valueOf((String) data.getExtras().get("result"))) {
		case 0:
			currentExercise.setDone();
			break;
		case 1:
			currentExercise.setNearlyDone();
			break;
		}
		prepairNextExercise();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean prepairNextExercise() {
		sensorManager.unregisterListener(this);
		try {
			currentExercise = currentExercises.next();
			Intent intent = new Intent(this, ExerciseActivityChooser.class);
			intent.putExtra(Exercise.INTENT_EXTRA_COUNT_GOAL,
					currentExercise.getRepetitionsGoal());
			switch (currentExercise.getType()) {
			case SQUATS:
				infoView.setText(getResources().getString(
						R.string.exercise_repetition_goal_pushup,
						currentExercise.getRepetitionsGoal()));
				exerciseCounter = new SquatCounter(currentExercise, this);
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_NORMAL);
				break;
			case PULL_UP:
				intent.putExtra(Exercise.INTENT_EXTRA_TYPE, 2);
				startActivityForResult(intent, 7);
				break;
			case PUSH_UP:
				intent.putExtra(Exercise.INTENT_EXTRA_TYPE, 3);
				startActivityForResult(intent, 7);
				break;
			case DIPS:
				intent.putExtra(Exercise.INTENT_EXTRA_TYPE, 4);
				startActivityForResult(intent, 7);
				break;
			default:
				intent.putExtra(Exercise.INTENT_EXTRA_TYPE, 5);
				startActivityForResult(intent, 7);
				break;
			}
			return true;
		} catch (NoSuchElementException e) {
			if (WorkoutTracker.getInstance().isLastLocation()) {
				startActivity(new Intent(this, StatisticsMapActivity.class));

				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

				v.vibrate(500);

			} else {
				WorkoutTracker.getInstance().moveToNextLocation();
				this.finish();
			}
			return false;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		exerciseCounter.addAccValues(event);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = tts.setLanguage(Locale.GERMAN);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e(TAG, "This Language is not supported");
			}
		} else {
			Log.e(TAG, "Initilization Failed!");
		}
	}

	public void onBtNextClick(View view) {
		prepairNextExercise();
	}

	@Override
	public void onExerciseIterationDetected() {
		countView.setText(""
				+ exerciseCounter.getExercise().getRepetitionsActual());
		tts.speak(exerciseCounter.getExercise().getRepetitionsActual() + "",
				TextToSpeech.QUEUE_FLUSH, null);

		if (exerciseCounter.getExercise().isRepetitionsReached()) {
			tts.speak(getString(R.string.exercise_end),
					TextToSpeech.QUEUE_FLUSH, null);
			prepairNextExercise();
		}
	}

}
