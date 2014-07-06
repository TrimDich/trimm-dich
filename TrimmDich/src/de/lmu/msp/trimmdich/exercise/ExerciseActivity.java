package de.lmu.msp.trimmdich.exercise;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.main.MainActivity;
import de.lmu.msp.trimmdich.summary.MapResultActivity;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		setTitle(R.string.exercise_title);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		countView = (TextView) findViewById(R.id.exercise_countView);
		infoView = (TextView) findViewById(R.id.exercise_exercise_name);
		overviewView = (TextView) findViewById(R.id.exercise_overview_default);
	}

	@Override
	protected void onStart() {
		super.onStart();
		tts = new TextToSpeech(this, this);
		currentExercises = WorkoutTracker.getInstance().getCurrentLocationExcercices().iterator();
		prepairNextExercise();
		if(WorkoutTracker.getInstance().getCurrentLocationExcercices().size() > 0){
			overviewView.setText(getResources().getString(R.string.exercise_overview, 1,WorkoutTracker.getInstance().getCurrentLocationExcercices().size()));
			countView.setText(""+0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		WorkoutTracker.getInstance().setCurrentActivity(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(this);
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
//		//TODO: Das Logging komplett entfernen
//		try {
//			exerciseCounter.fos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		exerciseCounter = null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sensorManager = null;
		tts = null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	
	private boolean prepairNextExercise(){
		sensorManager.unregisterListener(this);
		try {
			Exercise exercise = currentExercises.next();
			infoView.setText(getResources().getString(R.string.exercise_repetition_goal_pushup, exercise.getRepetitionsGoal()));
			exerciseCounter = new SquatCounter(exercise, this);
			sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
			return true;
		} catch (NoSuchElementException e) {
			WorkoutTracker.getInstance().moveToNextLocation();
			if(WorkoutTracker.getInstance().isLastLocation())
				startActivity(new Intent(this, MapResultActivity.class));
			else
				this.finish();
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
		countView.setText("" + exerciseCounter.getExercise().getRepetitionsActual());
		if (exerciseCounter.getExercise().getRepetitionsActual() == 1)
			tts.speak("Eine Kniebeuge", TextToSpeech.QUEUE_FLUSH, null); 
		else
			tts.speak(exerciseCounter.getExercise().getRepetitionsActual() + " Kniebeugen", TextToSpeech.QUEUE_FLUSH,null);
		if (exerciseCounter.getExercise().getRepetitionsActual() == 5)
			tts.speak(getString(R.string.exercise_end),	TextToSpeech.QUEUE_FLUSH, null);
		if(exerciseCounter.getExercise().isRepetitionsReached())
			prepairNextExercise();
	}

}
