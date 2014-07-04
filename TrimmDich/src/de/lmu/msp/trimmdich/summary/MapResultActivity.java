package de.lmu.msp.trimmdich.summary;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Location;
import de.lmu.msp.trimmdich.data.Route;
import de.lmu.msp.trimmdich.main.SetupActivity;

public class MapResultActivity extends Activity {
	
	GoogleMap map;
	Route route;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapresult);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		// Fetch the runned route from the previous activities
		
		/*route = null;
		
		// add route to map
		PolylineOptions line = new PolylineOptions();
		
		for (Location location : route.locations) {
			line.add(location.location);
			map.addMarker(new MarkerOptions().position(location.location).title("location"));
		}
		map.addPolyline(line);*/
		
			
	}
	public void showStatistics(View view) {
		Intent newIntent = new Intent(this, StatisticsActivity.class);
		startActivity(newIntent);
	}
}

