package de.lmu.msp.trimmdich.summary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Route;

public class MapResultActivity extends Activity {

	GoogleMap map;
	Route route;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapresult);
		ActionBar actionbar = getActionBar();
		actionbar.setIcon(R.drawable.running_white_48);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Fetch the runned route from the previous activities

		route = null;

		/*
		 * route = null;
		 * 
		 * >>>>>>> 8720dee367b9192141a54db10a764a0bd859421d // add route to map
		 * PolylineOptions line = new PolylineOptions();
		 * 
		 * for (Location location : route.locations) {
		 * line.add(location.location); map.addMarker(new
		 * MarkerOptions().position(location.location) .title("location")); }
		 * <<<<<<< HEAD map.addPolyline(line);
		 * 
		 * } ======= map.addPolyline(line);
		 */

	}

	public void showStatistics(View view) {
		Intent newIntent = new Intent(this, StatisticsActivity.class);
		startActivity(newIntent);
	}
}
