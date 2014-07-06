package de.lmu.msp.trimmdich.summary;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.data.Route;
import de.lmu.msp.trimmdich.data.Route.RouteDataPoint;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.main.MainActivity;
import de.lmu.msp.trimmdich.main.SetupActivity;

public class StatisticsActivity extends Activity {
	
	private WorkoutTracker mWorkoutTracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		ActionBar actionbar = getActionBar();
		actionbar.setIcon(R.drawable.running_white_48);

		TextView distanceMetric = (TextView) findViewById(R.id.distanceMetric);
		TextView squatMetric = (TextView) findViewById(R.id.squatMetric);
		TextView speedMetric = (TextView) findViewById(R.id.speedMetric);
		// Dummy values
		//double distanceRun = 0.0;
		//double timeTaken = 1.0;
		//double speed = 0.0 / timeTaken;
		int squatReps = 0;
		
		
		
		//The total distance that was run
		double totalDistance = 0.0;
		mWorkoutTracker = WorkoutTracker.getInstance();
		mWorkoutTracker.setCurrentActivity(this);
		Route route = mWorkoutTracker.getActiveRoute();
		long diffInMillies = 0;
		double diffInHours = 0;
		Log.v("Yes", String.valueOf(route.dataPoints.size()));
		if(route.dataPoints.size() >=2)
			{
			Date t1 = route.dataPoints.get(0).timestamp;
			Date t2 = route.dataPoints.get(route.dataPoints.size()-1).timestamp;
			Log.v("t1", t1.toString());
			Log.v("t2", t2.toString());
			diffInMillies = t2.getTime() - t1.getTime();
		    //diffInHours = TimeUnit.HOURS.convert(diffInMillies,TimeUnit.MILLISECONDS);
			diffInHours = diffInMillies / (60.0 * 60.0 * 1000.0);
		    Log.v("Diff in Millis", String.valueOf(diffInMillies));
		    Log.v("Diff in Hours", String.valueOf(diffInHours));
			for (int i = 0; i + 1 < route.dataPoints.size(); i++) {
			    LatLng a = route.dataPoints.get(i).position;
			    LatLng b = route.dataPoints.get(i+1).position;
			    
			    float[] result = new float[4];
			    try
			    {
			    	Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, result);
			    	totalDistance += result[0];
			    }
			    //If distance is 0
			    catch (Exception e){Log.v("Distance calculation","failed.");}
			   
			}
			for(de.lmu.msp.trimmdich.data.Location l: route.locations)
		    {
				Log.v("Route Locations", "There is a location.");
				for(Exercise e: l.selectedExercises)
				{
					Log.v("Actual repitions",String.valueOf(e.getRepetitionsActual()));
					Log.v("Goal repitions",String.valueOf(e.getRepetitionsGoal()));
					squatReps += e.getRepetitionsActual();
				}
		    }
		}
		Log.v("Total Distance", String.valueOf(mWorkoutTracker.round(totalDistance, 1)));
		Log.v("Hour Difference", String.valueOf(diffInHours));
		double speed = mWorkoutTracker.round(totalDistance / diffInHours / 1000.0, 1);
		String distanceText = String.valueOf(mWorkoutTracker.round(totalDistance, 1)) + " m";
		distanceMetric.setText(distanceText);
		String squatText = String.valueOf(squatReps);
		String speedText = String.valueOf(speed) + " km/h";
		// Set text inside view		
		speedMetric.setText(speedText);
		squatMetric.setText(squatText);

	}
	public void mainMenu(View view) {
		Intent newIntent = new Intent(this, MainActivity.class);
		startActivity(newIntent);
	}
}
