package de.lmu.msp.trimmdich.exercise;

import java.io.File;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.layout;
import de.lmu.msp.trimmdich.com.Safe;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class ExerciseActivity extends Activity implements SensorEventListener{
	public static final String TAG = "TD-Exercise";
	
	private Safe safe;
	private SensorManager sensorManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		safe = Safe.getInstance();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.activity_exercise);
		Log.d(TAG,"DIRECTORY_DOWNLOADS: "+Environment.DIRECTORY_DOWNLOADS);
	}
	protected void onStop() {
		sensorManager.unregisterListener(this);
	}
	
	public void finishExercise(View view) {
		Intent newIntent = new Intent(this, ExerciseReviewActivity.class);
		startActivity(newIntent);
	}
	
	public void onStartBtPressed(View view){
		Log.w(TAG,"onStartBtPress");
		if(isExternalStorageWritable()){
			sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
			sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	public void onStopBtPressed(){
		Log.w(TAG,"onStopBtPress");
		sensorManager.unregisterListener(this);
	}
	//TODO: evtl. in eigenen Thread auslagern
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			Log.d(TAG,event.timestamp+" ::ACC:: "+event.values[0]+"|"+event.values[1]+"|"+event.values[2]);
			break;
		case Sensor.TYPE_GYROSCOPE:
			Log.d(TAG,event.timestamp+" ::GYR:: "+event.values.length);
			break;
		default:
			Log.d(TAG,event.timestamp+" ::???:: Type= "+event.sensor.getType());
			break;
		}
	}
	
	/*Methoden für Erhebung der Testdaten und Speicherung*/
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	public File getStorageDir(String albumName) {
	    // Get the directory for the user's public pictures directory.
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_DOWNLOADS), albumName);
	    if (!file.mkdirs()) {
	        Log.e(TAG, "Directory not created");
	    }
	    return file;
	}

}
