package de.lmu.msp.trimmdich.summary;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import de.lmu.msp.trimmdich.R;

public class StatisticsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		TextView distanceMetric = (TextView) findViewById(R.id.distanceMetric);
		TextView squatMetric = (TextView) findViewById(R.id.squatMetric);
		TextView speedMetric = (TextView) findViewById(R.id.speedMetric);
		//Dummy values
		double distanceRun = 5.0;
		double timeTaken = 20.0;
		double speed = distanceRun / timeTaken;
		int squatReps = 30;
		String distanceText = String.valueOf(distanceRun) + " m";
		String squatText = String.valueOf(squatReps);
		String speedText = String.valueOf(speed) + " km/h";
		//Set text inside view
		distanceMetric.setText(distanceText);
		speedMetric.setText(speedText);
		squatMetric.setText(squatText);
		
	}
}
