package de.lmu.msp.trimmdich.run;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.exercise.ExerciseActivity;

public class CompassActivity extends Activity implements LocationListener,
		SensorEventListener {

	private WorkoutTracker mWorkoutTracker;

	private SensorManager mSensorManager;

	private Chronometer mChronometer;

	private TextView mDistanceTextView;
	private TextView mBearingTextView;
	private TextView mSpeedTextView;

	private Location mDestinationLocation = new Location("Destination");

	private CompassView mCompassIMageView;

	private Vibrator mVibrator;

	GoogleMap map;
	Marker marker;

	// // Max-Weber-Platz
	// private final static double MWP_LONG = 11.598032;
	// private final static double MWP_LAT = 48.136158;
	//
	// // Herkomerplatz
	// private final static double HKP_LONG = 11.6094243;
	// private final static double HKP_LAT = 48.1507574;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);

		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.running_white_48);

		mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		mDistanceTextView = (TextView) findViewById(R.id.distanceTextView);
		mBearingTextView = (TextView) findViewById(R.id.bearingTextView);
		mSpeedTextView = (TextView) findViewById(R.id.speedTextView);

		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		mChronometer.setText("00:00:00");
		mChronometer
				.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						CharSequence text = chronometer.getText();
						if (text.length() == 5) {
							chronometer.setText("00:" + text);
						} else if (text.length() == 7) {
							chronometer.setText("0" + text);
						}
					}
				});
		mChronometer.start();

		mCompassIMageView = (CompassView) findViewById(R.id.compassImageView);

		//
		// Google Map
		//
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// configure map
		map.setMyLocationEnabled(true);
		UiSettings settings = map.getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setMyLocationButtonEnabled(false);
		settings.setZoomControlsEnabled(false);

		LatLng lastLoc = new LatLng(0, 0);
		marker = map.addMarker(new MarkerOptions().position(lastLoc).title(
				"Goal"));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// mLocationManager.requestLocationUpdates(mProvider, 1000, 5, this);
		mWorkoutTracker = WorkoutTracker.getInstance();
		mWorkoutTracker.setCurrentActivity(this);

		mDestinationLocation.setLongitude(mWorkoutTracker
				.getDestinationLocation().longitude);
		mDestinationLocation.setLatitude(mWorkoutTracker
				.getDestinationLocation().latitude);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);

	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void arriveAtExercise(View view) {

		mVibrator.vibrate(1000);
		Intent newIntent = new Intent(this, ExerciseActivity.class);
		startActivity(newIntent);
	}

	@Override
	public void onLocationChanged(Location location) {

		float distance = location.distanceTo(mDestinationLocation);

		double distanceInKM = round(distance / 1000, 2);
		String distanceStr = new Double(distanceInKM).toString();
		Toast.makeText(this, "UPDATED LOCATION" + distanceStr, 3000).show();

		String speedTxt = "-";
		if (location.hasSpeed()) {
			double speed = location.getSpeed() * 3.6;
			speedTxt = "" + round(speed, 1);
			Toast.makeText(this, mSpeedTextView.getText(), Toast.LENGTH_SHORT)
					.show();
		}

		mDistanceTextView.setText("" + distanceInKM);
		mSpeedTextView.setText(speedTxt);

		// update map
		LatLng lastLoc = new LatLng(location.getLatitude(),
				location.getLongitude());
		LatLng destLoc = new LatLng(mDestinationLocation.getLatitude(),
				mDestinationLocation.getLongitude());
		marker.setPosition(destLoc);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lastLoc, 14);
		map.animateCamera(update);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		Location currentLocation = mWorkoutTracker.getLastLocation();

		// azimuth Richtungwinkel
		float azimuth = event.values[0];

		GeomagneticField geoField = new GeomagneticField(Double.valueOf(
				currentLocation.getLatitude()).floatValue(), Double.valueOf(
				currentLocation.getLongitude()).floatValue(), Double.valueOf(
				currentLocation.getAltitude()).floatValue(),
				System.currentTimeMillis());

		azimuth -= geoField.getDeclination(); // converts magnetic north into
												// true north

		// Store the bearingTo in the bearTo variable
		float bearTo = currentLocation.bearingTo(mDestinationLocation);
		// Log.d("CompassActivity", "Bear To: " + bearTo);

		// If the bearTo is smaller than 0, add 360 to get the rotation
		// clockwise.
		if (bearTo < 0) {
			bearTo = bearTo + 360;
		}

		// This is where we choose to point it
		float direction = bearTo - azimuth;

		// If the direction is smaller than 0, add 360 to get the rotation
		// clockwise.
		if (direction < 0) {
			direction = direction + 360;
		}

		mCompassIMageView.setRotation(direction);
		mBearingTextView.setText("" + bearTo);

	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
