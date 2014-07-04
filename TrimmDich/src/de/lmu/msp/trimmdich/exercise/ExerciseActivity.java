package de.lmu.msp.trimmdich.exercise;

import java.io.IOException;
import java.util.Locale;

import de.lmu.msp.trimmdich.R;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.activity_exercise);
		countView = (TextView) findViewById(R.id.exercise_countView);
		setTitle(R.string.exercise_title);
	}

	@Override
	protected void onStart() {
		super.onStart();
		tts = new TextToSpeech(this, this);
		onStartBtPressed(null);
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(this);
		onStopBtPressed(null);
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
	}

	public void finishExercise(View view) {
		// Intent newIntent = new Intent(this, ExerciseReviewActivity.class);
		// startActivity(newIntent);
	}

	public void onStartBtPressed(View view) {
		Log.w(TAG, "onStartBtPress");
		squadCount = 0;
		squatCounter = new SquatCounter();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		Toast.makeText(this, "START", Toast.LENGTH_SHORT).show();
	}

	public void onStopBtPressed(View view) {
		Log.w(TAG, "onStopBtPress");
		sensorManager.unregisterListener(this);
		try {
			squatCounter.fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		squatCounter = null;
		firstTime = 0;
		// try {
		// fos.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Toast.makeText(this, sensor+": "+accuracy,
		// Toast.LENGTH_SHORT).show();
	}

	long firstTime = 0;

	float[] gyroVal = { 0, 0, 0 };
	GolayFilter goleyFilter;
	GolayFilter goleyFilterVektor;

	private int squadCount = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (firstTime == 0)
			firstTime = event.timestamp;
		squatCounter.addAccValues(event.values[0], event.values[1],
				event.values[2], event.timestamp,
				(double) ((event.timestamp - firstTime) / 1000000000.));
		if (squatCounter.checkForSquat()) {
			squadCount++;
			// Toast.makeText(this, "Squat: "+squadCount,
			// Toast.LENGTH_SHORT).show();
			countView.setText("" + squadCount);
			if (squadCount == 1)
				tts.speak("Eine Kniebeuge", TextToSpeech.QUEUE_FLUSH, null);
			else
				tts.speak(squadCount + " Kniebeugen", TextToSpeech.QUEUE_FLUSH,
						null);

			if (squadCount == 5)
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
