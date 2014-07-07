package de.lmu.msp.trimmdich.summary;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Location;
import de.lmu.msp.trimmdich.data.Route;
import de.lmu.msp.trimmdich.data.Route.RouteDataPoint;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.main.MainActivity;

public class MapResultActivity extends Activity {

	GoogleMap map;
	Route route;
	private WorkoutTracker mWorkoutTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWorkoutTracker = WorkoutTracker.getInstance();
		mWorkoutTracker.setCurrentActivity(this);
		setContentView(R.layout.activity_mapresult);
		ActionBar actionbar = getActionBar();
		actionbar.setIcon(R.drawable.running_white_48);
		setTitle("Zurückgelegte Strecke");
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		// Fetch the runned route from the previous activities

		route = mWorkoutTracker.getActiveRoute();

		
		 
		 PolylineOptions line = new PolylineOptions();
		 for (RouteDataPoint rdp : route.dataPoints) {
			 line.add(rdp.position);
			 CameraUpdate update = CameraUpdateFactory.newLatLngZoom(rdp.position, 18);
			 map.animateCamera(update);
			 }
		 	
		 /*map.addPolyline(line);
		 LatLngBounds.Builder builder = new LatLngBounds.Builder();
		 for (LatLng e : latlngList) {
		     builder.include(e);
		 }
		 LatLngBounds bounds = builder.build();
		 int padding = 0; // offset from edges of the map in pixels
		 CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		 map.animateCamera(cu);*/
	}
	
	public void onLocationChanged(android.location.Location location) {	
		LatLng lastLatLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lastLatLng, 17);
		map.animateCamera(update);
	}

	public void showStatistics(View view) {
		Intent newIntent = new Intent(this, StatisticsActivity.class);
		startActivity(newIntent);
	}
	
	@Override
	public void onBackPressed() {
		//disables back button of smartphone
		//Ask the user if they want to quit
        new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Zurück")
        .setMessage("Willst du zurück zum Hauptmenü?")
        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Intent newIntent = new Intent(getBaseContext(), MainActivity.class);
        		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        		startActivity(newIntent);  
            }

        })
        .setNegativeButton("Nein", null)
        .show();
	}
}
