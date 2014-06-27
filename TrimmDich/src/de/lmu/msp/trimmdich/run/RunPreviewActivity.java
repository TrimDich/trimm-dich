package de.lmu.msp.trimmdich.run;

//import com.google.android.gms.maps.MapView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.MarkerOptions;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.layout;
import de.lmu.msp.trimmdich.data.Location;
import de.lmu.msp.trimmdich.data.Route;
import de.lmu.msp.trimmdich.data.RouteGenerator;
import de.lmu.msp.trimmdich.data.RouteGenerator.RouteProperties;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RunPreviewActivity extends Activity {
	
	GoogleMap map;
	RouteProperties routeProperties;
	Route route;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_preview);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		// load route properties
		routeProperties = new RouteProperties(getIntent());
		route = RouteGenerator.generateRoute(routeProperties);
		
		// add route to map
		for (Location location : route.locations) {
			map.addMarker(new MarkerOptions().position(location.location).title("location"));
		}
		
			
	}

	public void startRun(View view) {
		Intent newIntent = new Intent(this, CompassActivity.class);
		startActivity(newIntent);
	}
}
