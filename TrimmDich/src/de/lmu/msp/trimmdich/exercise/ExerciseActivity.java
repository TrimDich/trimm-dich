package de.lmu.msp.trimmdich.exercise;

import java.io.IOException;
import java.util.Locale;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.data.Exercise.EXERCISE_TYPE;
import android.app.Activity;
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
		OnInitListener {
	public static final String TAG = "TD-Exercise";

	private SensorManager sensorManager;
	private TextToSpeech tts;

	private SquatCounter squatCounter;
	private TextView countView;
	private Exercise currentExercise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		setTitle(R.string.exercise_title);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		countView = (TextView) findViewById(R.id.exercise_countView);
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		tts = new TextToSpeech(this, this);
		currentExercise = new Exercise(EXERCISE_TYPE.PULL_UP, 5); //TODO: von WorkoutTracker holen
		((TextView) findViewById(R.id.exercise_repetition_goal)).setText(getResources().getString(R.string.exercise_repetition_goal, currentExercise.getRepetitionsGoal()));
	
		squatCounter = new SquatCounter(currentExercise);
		sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(this);
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		//TODO: Das Logging komplet entfernen
		try {
			squatCounter.fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		squatCounter = null;
	}
	
	@Override
	protected void onDestroy() {
		sensorManager = null;
		tts = null;
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	
	
	long firstTime = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (firstTime == 0)
			firstTime = event.timestamp;
		squatCounter.addAccValues(event.values[0], event.values[1],
				event.values[2], event.timestamp,
				(double) ((event.timestamp - firstTime) / 1000000000.));
		if (squatCounter.checkForSquat()) {
			countView.setText("" + squatCounter.getExercise().getRepetitionsActual());
			if (squatCounter.getExercise().getRepetitionsActual() == 1)
				tts.speak("Eine Kniebeuge", TextToSpeech.QUEUE_FLUSH, null);
			else
				tts.speak(squatCounter.getExercise().getRepetitionsActual() + " Kniebeugen", TextToSpeech.QUEUE_FLUSH,
						null);

			if (squatCounter.getExercise().getRepetitionsActual() == 5)
				tts.speak(getString(R.string.exercise_end),
						TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = tts.setLanguage(Locale.GERMAN);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}
			tts.speak(getString(R.string.exercise_start),
					TextToSpeech.QUEUE_FLUSH, null);
		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	public void onBtNextClick(View view) {
		Toast.makeText(this, "keine Funktion", Toast.LENGTH_SHORT).show();
		// Intent newIntent = new Intent(this, SetupActivity.class);
		// startActivity(newIntent);
	}

}
