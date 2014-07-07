package de.lmu.msp.trimmdich.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.data.Route.RouteDataPoint;
import de.lmu.msp.trimmdich.exercise.ExerciseActivity;

/* 
 * WorkoutTracker
 * 
 * A shared class that keeps track of location and exercise activity.
 * 
 * Every activity that uses it should register itself as the active activity.
 * 
 *  workoutTracker.setCurrentActivity(this)
 * 
 */
public class WorkoutTracker implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	// Tracks the progress in the exercise
	// Tracks location

	private int nextLocation;

	// Global constants
	// Singelton
	private WorkoutTracker() {
	}

	private static class Holder {
		private static final WorkoutTracker DAD = new WorkoutTracker();
	}

	public static WorkoutTracker getInstance() {
		return Holder.DAD;
	}

	// Static
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000; // Define
																			// a
																			// request
																			// code
																			// to
																			// send
																			// to
																			// Google
																			// Play
																			// services.
																			// This
																			// code
																			// is
																			// returned
																			// in
																			// Activity.onActivityResult
	// Request code to use when launching the resolution activity
	private static final int REQUEST_RESOLVE_ERROR = 1001;
	// Unique tag for the error dialog fragment
	private static final String DIALOG_ERROR = "dialog_error";

	// Instance
	// The currently active activity
	private Activity currentActivity;
	// The location client
	LocationClient locationClient;
	// Bool to track whether the app is already resolving an error
	private boolean mResolvingError = false;
	private Route activeRoute;
	private Location lastLocation;

	public Location getLastLocation() {
		return lastLocation;
	}

	public Route getActiveRoute() {
		return activeRoute;
	}

	public void setActiveRoute(Route activeRoute) {
		this.activeRoute = activeRoute;
	}

	public LocationClient locationClient() {
		return locationClient;
	}

	public void setCurrentActivity(Activity activity) {
		// unregister events for old activity
		if (locationClient != null) {
			// disconnect old location client
			locationClient.disconnect();
		}

		currentActivity = activity;

		// If it's the first time registering check if play services are
		// available
		if (currentActivity == null) {
			try {
				this.testIfPlayServicesConnected();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (activity == null) {
			// if activity is set to a null value disconnect?
			// maybe not because we want to keep tracking location updates
		} else {

			locationClient = new LocationClient(
					currentActivity.getApplicationContext(), this, this);
			locationClient.connect();
		}
	}

	private void refreshLocationUpdateInterval() {
		LocationRequest locationRequest = new LocationRequest();
		// TODO update accuracy depending on whether exercise is active
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setFastestInterval(1000);
		locationRequest.setSmallestDisplacement(4);

		locationClient.requestLocationUpdates(locationRequest, this);

	}

	private boolean testIfPlayServicesConnected() throws Exception {
		if (currentActivity == null) {
			Log.d("Location Updates", "No activity set.");
			throw new Exception("No activity set");
		}

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(currentActivity
						.getApplicationContext());
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			// ConnectionResult.
			// int errorCode = connectionResult.getErrorCode();
			// // Get the error dialog from Google Play services
			// Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
			// errorCode,
			// currentActivity,
			// CONNECTION_FAILURE_RESOLUTION_REQUEST);
			//
			// // If Google Play services can provide an error dialog
			// if (errorDialog != null) {
			// // Create a new DialogFragment for the error dialog
			// ErrorDialogFragment errorFragment =
			// new ErrorDialogFragment();
			// // Set the dialog in the DialogFragment
			// errorFragment.setDialog(errorDialog);
			// // Show the error dialog in the DialogFragment
			// errorFragment.show(currentActivity.getFragmentManager(),
			// "Location Updates");
			// }
			Log.d("Location Updates", "Connection failed");
			return false;
		}
	}

	// The rest of this code is all about building the error dialog

	/* Creates a dialog for an error message */
	private void showErrorDialog(int errorCode) {
		// Create a fragment for the error dialog
		ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
		// Pass the error that should be displayed
		Bundle args = new Bundle();
		args.putInt(DIALOG_ERROR, errorCode);
		dialogFragment.setArguments(args);
		dialogFragment
				.show(currentActivity.getFragmentManager(), "errordialog");
	}

	/* Called from ErrorDialogFragment when the dialog is dismissed. */
	public void onDialogDismissed() {
		mResolvingError = false;
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */

		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(currentActivity,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Toast.makeText(currentActivity, "Connected",
		// Toast.LENGTH_SHORT).show();

		this.onLocationChanged(locationClient.getLastLocation());
		refreshLocationUpdateInterval();

	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(currentActivity, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onLocationChanged(Location location) {

		lastLocation = location;

		// TODO save location updates to current route if running
		if (activeRoute != null) {

			LatLng position = new LatLng(location.getLatitude(),
					location.getLongitude());
			Date timestamp = new Date();

			activeRoute.dataPoints.add(new RouteDataPoint(position, timestamp,
					null));

			de.lmu.msp.trimmdich.data.Location destinationLocation = activeRoute.locations
					.get(nextLocation);
			LatLng currentLocation = new LatLng(location.getLatitude(),
					location.getLatitude());

			double distance = Helpers.distance(destinationLocation.location,
					currentLocation);

			double distanceInKM = round(distance / 1000, 2);
			if (distanceInKM <= 0.02) {

				Intent intent = new Intent(currentActivity,
						ExerciseActivity.class);
				intent.putExtra("next_location", nextLocation);
				currentActivity.startActivity(intent);

			}

		}

		if (currentActivity != null
				&& currentActivity instanceof LocationListener) {
			LocationListener listener = (LocationListener) currentActivity;
			listener.onLocationChanged(location);
		}
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public int getNextLocation() {
		return nextLocation;
	}

	public void moveToNextLocation(){
		this.nextLocation++;
	}

	public List<Exercise> getCurrentLocationExcercices() {
		return activeRoute.locations.get(nextLocation).selectedExercises;//TODO: fix possible IndexOutOfBoundsExeception
	}

	public LatLng getDestinationLocation() {
		return activeRoute.locations.get(nextLocation).location;
	}

	public boolean isLastLocation() {
		return activeRoute != null
				&& nextLocation == activeRoute.locations.size() - 1;
	}

}
