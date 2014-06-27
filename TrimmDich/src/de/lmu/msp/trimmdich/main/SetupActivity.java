package de.lmu.msp.trimmdich.main;

import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.layout;
import de.lmu.msp.trimmdich.data.RouteGenerator.RouteProperties;
import de.lmu.msp.trimmdich.run.RunPreviewActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetupActivity extends Activity {
	RouteProperties routeProperties = new RouteProperties();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		
		routeProperties.desiredExercises = 10;
		routeProperties.desiredLengthInKm = 2;
		routeProperties.startPosition = new LatLng(48.1735192,11.5920656);
		
	}
	
	public void generateRun(View view) {
		Intent newIntent = new Intent(this, RunPreviewActivity.class);
		routeProperties.saveToIntent(newIntent);
		startActivity(newIntent);
	}
	
}
