package de.lmu.msp.trimmdich.summary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.data.Route;
import de.lmu.msp.trimmdich.data.WorkoutTracker;
import de.lmu.msp.trimmdich.data.Route.RouteDataPoint;
import de.lmu.msp.trimmdich.main.MainActivity;

public class StatisticsMapActivity extends Activity {

	private GoogleMap mMap;
	private Route mRoute;
	private WorkoutTracker mWorkoutTracker;

	private TextView mDistanceTextView;
	private TextView mDurationTextView;
	private TextView mPullUpRepsTextView;
	private TextView mPushUpRepsTextView;
	private TextView mDipsRepsTextView;
	private TextView mSquatsRepsTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics_map_layout);

		ActionBar actionbar = getActionBar();
		actionbar.setIcon(R.drawable.running_white_48);

		mDistanceTextView = (TextView) findViewById(R.id.distance_statistics_txt);
		mDurationTextView = (TextView) findViewById(R.id.duration_statistics_txt);
		mPullUpRepsTextView = (TextView) findViewById(R.id.pullup_reps_statistics_txt);
		mPushUpRepsTextView = (TextView) findViewById(R.id.pushup_reps_statistics_txt);
		mSquatsRepsTextView = (TextView) findViewById(R.id.squats_reps_statistics_txt);
		mDipsRepsTextView = (TextView) findViewById(R.id.dips_reps_statistics_txt);

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMyLocationEnabled(true);

		mWorkoutTracker = WorkoutTracker.getInstance();
		mWorkoutTracker.setCurrentActivity(this);

		mRoute = mWorkoutTracker.getActiveRoute();

		drawActualRoute();

		int squatReps = 0;
		int dipReps = 0;
		int pullupReps = 0;
		int pushupReps = 0;
		String duration = "";

		// The total distance that was run
		double totalDistance = 0.0;
		mWorkoutTracker = WorkoutTracker.getInstance();
		mWorkoutTracker.setCurrentActivity(this);
		Route route = mWorkoutTracker.getActiveRoute();
		long diffInMillies = 0;
		double diffInHours = 0;
		Log.v("Yes", String.valueOf(route.dataPoints.size()));
		if (route.dataPoints.size() >= 2) {
			Date t1 = route.dataPoints.get(0).timestamp;
			Date t2 = route.dataPoints.get(route.dataPoints.size() - 1).timestamp;
			Log.v("t1", t1.toString());
			Log.v("t2", t2.toString());
			diffInMillies = t2.getTime() - t1.getTime();
			Date date = new Date(diffInMillies);

			SimpleDateFormat simpleFormat = (SimpleDateFormat) DateFormat
					.getDateInstance();
			simpleFormat.applyPattern("HH:mm:ss");
			simpleFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

			// diffInHours =
			// TimeUnit.HOURS.convert(diffInMillies,TimeUnit.MILLISECONDS);
			diffInHours = diffInMillies / (60.0 * 60.0 * 1000.0);
			Log.v("Diff in Millis", String.valueOf(diffInMillies));
			Log.v("Diff in Hours", String.valueOf(diffInHours));
			for (int i = 0; i + 1 < route.dataPoints.size(); i++) {
				LatLng a = route.dataPoints.get(i).position;
				LatLng b = route.dataPoints.get(i + 1).position;

				float[] result = new float[4];
				try {
					Location.distanceBetween(a.latitude, a.longitude,
							b.latitude, b.longitude, result);
					totalDistance += result[0];
				}
				// If distance is 0
				catch (Exception e) {
					Log.v("Distance calculation", "failed.");
				}

			}
			for (de.lmu.msp.trimmdich.data.Location l : route.locations) {
				Log.v("Route Locations", "There is a location.");
				for (Exercise e : l.selectedExercises) {
					switch (e.getType()) {
					case SQUATS:
						squatReps += e.getRepetitionsActual();
						break;
					case DIPS:
						dipReps += e.getRepetitionsActual();
						break;
					case PULL_UP:
						pullupReps += e.getRepetitionsActual();
						break;
					case PUSH_UP:
						pushupReps += e.getRepetitionsActual();
						break;
					default:
						break;
					}
					Log.v("Actual repitions", String.valueOf(e.getType()));
					Log.v("Actual repitions",
							String.valueOf(e.getRepetitionsActual()));
					Log.v("Goal repitions",
							String.valueOf(e.getRepetitionsGoal()));

				}
			}
			String distanceText = String.valueOf(mWorkoutTracker.round(
					totalDistance / 1000, 2));

			mDistanceTextView.setText("" + distanceText + " km");
			mDurationTextView.setText(simpleFormat.format(date));
			mPullUpRepsTextView.setText("" + pullupReps);
			mSquatsRepsTextView.setText("" + squatReps);
			mPushUpRepsTextView.setText("" + pushupReps);
			mDipsRepsTextView.setText("" + dipReps);
		}
		Log.v("Total Distance",
				String.valueOf(mWorkoutTracker.round(totalDistance, 1)));
		Log.v("Hour Difference", String.valueOf(diffInHours));
		// durch 1000 da distance in m angegeben
		double speed = mWorkoutTracker.round(totalDistance / diffInHours
				/ 1000.0, 1);

	}

	private void drawActualRoute() {

		PolylineOptions line = new PolylineOptions();
		for (RouteDataPoint rdp : mRoute.dataPoints) {
			line.add(rdp.position);
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
					rdp.position, 18);
			mMap.animateCamera(update, 50, null);
		}
		// map.addPolyline(line).setColor(0xFF1BA39C);
		mMap.addPolyline(line).setColor(0xCF2574A9);
	}

	@Override
	public void onBackPressed() {
		// disables back button of smartphone
		// Ask the user if they want to quit
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.back_dialog_title)
				.setMessage(R.string.back_dialog_msg)
				.setPositiveButton(R.string.back_dialog_yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent newIntent = new Intent(getBaseContext(),
										MainActivity.class);
								newIntent
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(newIntent);
							}

						}).setNegativeButton(R.string.back_dialog_no, null)
				.show();
	}
}
