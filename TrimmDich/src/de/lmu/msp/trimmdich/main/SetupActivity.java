package de.lmu.msp.trimmdich.main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.RouteGenerator.RouteProperties;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.run.CompassActivity;

public class SetupActivity extends Activity implements LocationListener {
	RouteProperties routeProperties = new RouteProperties();
	WorkoutTracker workoutTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		ActionBar actionbar = getActionBar();
		actionbar.setIcon(R.drawable.running_white_48);
		routeProperties.desiredExercises = 10;
		routeProperties.desiredLengthInKm = 4;
		routeProperties.startPosition = new LatLng(48.1735192, 11.5920656);

		//
		// Workout Tracker
		//
		workoutTracker = WorkoutTracker.getInstance();
		workoutTracker.setCurrentActivity(this);

	}

	public void generateRun(View view) {

		// Intent newIntent = new Intent(this, RunPreviewActivity.class);
		// Jut for testing
		Intent newIntent = new Intent(this, CompassActivity.class);

		startActivity(newIntent);
	}

	@Override
	public void onLocationChanged(Location location) {
		routeProperties.startPosition = new LatLng(location.getLatitude(),
				location.getLongitude());
	}

}
