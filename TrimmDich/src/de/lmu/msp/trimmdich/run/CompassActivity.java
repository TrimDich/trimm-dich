package de.lmu.msp.trimmdich.run;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.exercise.ExerciseActivity;

public class CompassActivity extends Activity implements LocationListener, SensorEventListener{

	private LocationManager mLocationManager;
	private SensorManager mSensorManager;
	private String mProvider;
	
	private Chronometer mChronometer;


	private TextView mDistanceTextView;
	private TextView mBearingTextView;

	private Location mDestinationLocation = new Location("Destination");

	private ImageView mCompassIMageView;

	private float currentDegree = 0f;

	// Max-Weber-Platz
	private final static double MWP_LONG = 11.598032;
	private final static double MWP_LAT = 48.136158;

	//Herkomerplatz
	private final static double HKP_LONG = 11.6094243;
	private final static double HKP_LAT = 48.1507574;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

		mDistanceTextView = (TextView) findViewById(R.id.distanceTextView);
		mBearingTextView = (TextView) findViewById(R.id.bearingTextView);
		
		mChronometer = (Chronometer)findViewById(R.id.chronometer);
		mChronometer.start();

		mDestinationLocation.setLongitude(HKP_LONG);
		mDestinationLocation.setLatitude(HKP_LAT);

		mCompassIMageView = (ImageView) findViewById(R.id.compassImageView);

		setUpCompassActivity();
	}


	@Override
	protected void onResume() {
		super.onResume();
		mLocationManager.requestLocationUpdates(mProvider, 1000, 5, this);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(this);
		mSensorManager.unregisterListener(this);
		mChronometer.stop();
	}

	private void setUpCompassActivity() {
		

		Criteria criteria = new Criteria();

		mProvider = mLocationManager.getBestProvider(criteria, true);

		Location currentLocation = mLocationManager
				.getLastKnownLocation(mProvider);

		float distance = currentLocation.distanceTo(mDestinationLocation);
		float bearing = currentLocation.bearingTo(mDestinationLocation);

		mDistanceTextView.setText("" + distance);
		mBearingTextView.setText("" + bearing);

	}

	public void arriveAtExercise(View view) {
		Intent newIntent = new Intent(this, ExerciseActivity.class);
		startActivity(newIntent);
	}

	@Override
	public void onLocationChanged(Location location) {

		float distance = location.distanceTo(mDestinationLocation);
		float bearing = location.bearingTo(mDestinationLocation);

		mDistanceTextView.setText("" + distance);
		mBearingTextView.setText("" + bearing);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(this.getClass().getSimpleName(), "Provider disabeld " + provider);

	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(this.getClass().getSimpleName(), "New Provider enabled "
				+ provider);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		
		Criteria criteria = new Criteria();
		mProvider = mLocationManager.getBestProvider(criteria, true);
		Location currentLocation = mLocationManager
				.getLastKnownLocation(mProvider);
		
        //azimuth Richtungwinkel
        float azimuth = event.values[0];

        GeomagneticField geoField = new GeomagneticField( Double
            .valueOf( currentLocation.getLatitude() ).floatValue(), Double
            .valueOf( currentLocation.getLongitude() ).floatValue(),
            Double.valueOf( currentLocation.getAltitude() ).floatValue(),
            System.currentTimeMillis() );

        azimuth -= geoField.getDeclination(); // converts magnetic north into true north

        // Store the bearingTo in the bearTo variable
        float bearTo = currentLocation.bearingTo( mDestinationLocation );
        Log.d("CompassActivity", "Bear To: "+ bearTo);

        // If the bearTo is smaller than 0, add 360 to get the rotation clockwise.
        if (bearTo < 0) {
            bearTo = bearTo + 360;
        }

        //This is where we choose to point it
        float direction = bearTo - azimuth;

        // If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction < 0) {
            direction = direction + 360;
        }

        rotateImageView( mCompassIMageView, R.drawable.navigation, direction );

        mBearingTextView.setText(""+bearTo);

	}
	
	private void rotateImageView( ImageView imageView, int drawable, float rotate ) {

	    // Decode the drawable into a bitmap
	    Bitmap bitmapOrg = BitmapFactory.decodeResource( getResources(),
	            drawable );

	    // Get the width/height of the drawable
	    DisplayMetrics dm = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(dm);
	    int width = bitmapOrg.getWidth(), height = bitmapOrg.getHeight();

	    // Initialize a new Matrix
	    Matrix matrix = new Matrix();

	    // Decide on how much to rotate
	    rotate = rotate % 360;

	    // Actually rotate the image
	    matrix.postRotate( rotate, width, height );

	    // recreate the new Bitmap via a couple conditions
	    Bitmap rotatedBitmap = Bitmap.createBitmap( bitmapOrg, 0, 0, width, height, matrix, true );
	    //BitmapDrawable bmd = new BitmapDrawable( rotatedBitmap );

	    //imageView.setImageBitmap( rotatedBitmap );
	    imageView.setImageDrawable(new BitmapDrawable(getResources(), rotatedBitmap));
	    imageView.setScaleType( ScaleType.CENTER );
	}

}