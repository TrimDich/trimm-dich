package de.lmu.msp.trimmdich.run;

//import com.google.android.gms.maps.MapView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.layout;
import de.lmu.msp.trimmdich.data.Location;
import de.lmu.msp.trimmdich.data.Route;
import de.lmu.msp.trimmdich.data.RouteGenerator;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.data.RouteGenerator.RouteProperties;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RunPreviewActivity extends Activity implements LocationListener {
	
	GoogleMap map;
	RouteProperties routeProperties;
	Route route;
	WorkoutTracker workoutTracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_preview);
		
		//
		// Google Map
		//
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		// configure map
		map.setMyLocationEnabled(true);
		
		// load route properties
		routeProperties = new RouteProperties(getIntent());
		route = RouteGenerator.generateRoute(routeProperties);
		
		// add route to map
		PolylineOptions line = new PolylineOptions();
		
		for (Location location : route.locations) {
			line.add(location.location);
			map.addMarker(new MarkerOptions().position(location.location).title("location"));
		}
		map.addPolyline(line);	
		
		//
		// Workout Tracker
		//
		workoutTracker = WorkoutTracker.getInstance();
		workoutTracker.setCurrentActivity(this);
		
	}

	public void startRun(View view) {
		Intent newIntent = new Intent(this, CompassActivity.class);
		// TODO pass active route to next screen
		startActivity(newIntent);
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		android.location.Location lastLocation = workoutTracker.locationClient().getLastLocation();
		LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
		CameraUpdate update =  CameraUpdateFactory.newLatLngZoom(lastLatLng, 13);
		map.animateCamera(update);
	}
}
