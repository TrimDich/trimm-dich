package de.lmu.msp.trimmdich.run;

//import com.google.android.gms.maps.MapView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.data.Exercise.EXERCISE_TYPE;
import de.lmu.msp.trimmdich.data.Location;
import de.lmu.msp.trimmdich.data.Route;
import de.lmu.msp.trimmdich.data.RouteGenerator;
import de.lmu.msp.trimmdich.data.RouteGenerator.RouteProperties;
import de.lmu.msp.trimmdich.data.WorkoutTracker;

public class RunPreviewActivity extends Activity implements LocationListener {

	GoogleMap map;
	RouteProperties routeProperties;
	Route route;
	WorkoutTracker workoutTracker;
	TextView distanceIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_preview);

		ActionBar actionbar = getActionBar();
		actionbar.setIcon(R.drawable.running_white_48);
		setTitle(R.string.runpreview_activity_title);
		//
		// Google Map
		//
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		distanceIndicator = (TextView) findViewById(R.id.distanceIndicator);

		// configure map
		map.setMyLocationEnabled(true);

		// load route properties
		routeProperties = new RouteProperties(getIntent());

		//
		// Workout Tracker
		//
		workoutTracker = WorkoutTracker.getInstance();
		workoutTracker.setCurrentActivity(this);

		generateRoute();
	}

	private void generateRoute() {
		route = RouteGenerator.generateRoute(routeProperties);

		distanceIndicator.setText("ca. "
				+ WorkoutTracker.round(route.flightPathInKm, 2) + " km");

		// add route to map
		map.clear();
		PolylineOptions line = new PolylineOptions();

		for (Location location : route.locations) {
			line.add(location.location);

			MarkerOptions options = new MarkerOptions().position(location.location)
					.title("location");
			if(location.selectedExercises.size() == 0) {
				options.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag32));
				options.anchor(0.5f, 0.9f);

			} else {
				Exercise exercise = (Exercise)location.selectedExercises.get(0);
//				int resource;
//				switch (exercise.getType()) {
//				case DIPS:
//					resource = R.drawable.marker_dips;
//					break;
//				case PULL_UP:
//					resource = R.drawable.marker_pullup;
//					break;
//				case PUSH_UP:
//					resource = R.drawable.marker_pushups;
//					break;
//				case SQUATS:
//					resource = R.drawable.marker_squats;
//					break;
//
//				default:
//					resource = R.drawable.marker_squats;
//					break;
//				}
				int resource = R.drawable.empty2;
				options.icon(BitmapDescriptorFactory.fromResource(resource));
			}

			map.addMarker(options);
		}
		// map.addPolyline(line).setColor(0xFF1BA39C);
		map.addPolyline(line).setColor(0xCF222222); //.setColor(0xCF2574A9);

	}

	public void startRun(View view) {
		Intent newIntent = new Intent(this, CompassActivity.class);
		workoutTracker.setActiveRoute(route);
		// TODO pass active route to next screen
		startActivity(newIntent);
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		// android.location.Location lastLocation =
		// workoutTracker.locationClient().getLastLocation();
		LatLng lastLatLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lastLatLng, 13);
		map.animateCamera(update, 50, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.run_preview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.differentRoute) {
			generateRoute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
