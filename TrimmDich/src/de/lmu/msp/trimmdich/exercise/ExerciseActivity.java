package de.lmu.msp.trimmdich.exercise;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.data.Exercise.EXERCISE_TYPE;
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
		setTitle(R.string.exercise_title);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		countView = (TextView) findViewById(R.id.exercise_countView);
		infoView = (TextView) findViewById(R.id.exercise_exercise_name);
		overviewView = (TextView) findViewById(R.id.exercise_overview_default);
		
		WorkoutTracker.getInstance().setCurrentActivity(this);
		//do stuff from onStart()
		currentExercises = WorkoutTracker.getInstance().getCurrentLocationExcercices().iterator();
		prepairNextExercise();
		if(WorkoutTracker.getInstance().getCurrentLocationExcercices().size() > 0){
			overviewView.setText(getResources().getString(R.string.exercise_overview, 1,WorkoutTracker.getInstance().getCurrentLocationExcercices().size()));
			countView.setText(""+0);
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
		Log.v(TAG,"ExerciseActivity: onResume()");
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

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG,"ExerciseActivity::onActivityResult()  "+requestCode+"|"+resultCode);
		switch(Integer.valueOf((String)data.getExtras().get("result"))){
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

	private boolean prepairNextExercise(){
		sensorManager.unregisterListener(this);
		try {
			currentExercise = currentExercises.next();
			if(currentExercise.getType() == EXERCISE_TYPE.PUSH_UP){
				infoView.setText(getResources().getString(R.string.exercise_repetition_goal_pushup, currentExercise.getRepetitionsGoal()));
				exerciseCounter = new SquatCounter(currentExercise, this);
				sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
			}else 
				startActivityForResult(new Intent(this, ExerciseActivityChooser.class),7);
			return true;
		} catch (NoSuchElementException e) {
			if(WorkoutTracker.getInstance().isLastLocation()) {
				startActivity(new Intent(this, MapResultActivity.class));
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
