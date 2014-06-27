package de.lmu.msp.trimmdich.data;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.data.Exercise.EXERCISE_TYPE;
import static java.util.Arrays.asList;

public class RouteGenerator {

	public static ArrayList<Location> locationsAround(LatLng location, float radiusInKm) {
		ArrayList<Location> allLocations = new ArrayList<Location>();
		
		// Some Dummy Locations
		allLocations.add(new Location( new LatLng(48.172325,11.59049), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.174729,11.58637), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.176217,11.594095), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.169964,11.598001), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.169878,11.598816), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.168518,11.605253), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.163996,11.594224), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.162264,11.596498), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.167373,11.607335), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.174099,11.610575), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.175359,11.594696), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.180109,11.603751), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.175974,11.584783), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.177104,11.580212), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.169778,11.577423), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.170479,11.573088), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.174629,11.571865), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.175201,11.567359), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		
		// TODO, filter locations based on distance
		
		return allLocations;
	}
	
	public static Route generateRoute(RouteProperties routeProperties) {
		Route newRoute = new Route();
		
		// Get locations within half the distance of desired distance
		ArrayList<Location> availableLocations = RouteGenerator.locationsAround(routeProperties.startPosition, 123);
		newRoute.locations = availableLocations;
		
		// Distribute the exercises across the location nodes
		// TODO
		
		
		return newRoute;
	}
	
	public static class RouteProperties {
		public LatLng startPosition;
		public float desiredLengthInKm = 0;
		public int desiredExercises = 0;
		public RouteProperties() { }
		public RouteProperties(Intent intent) { 
			startPosition = new LatLng(intent.getDoubleExtra("startPositionLat", 0), intent.getDoubleExtra("startPositionLon", 0));
			desiredLengthInKm = intent.getFloatExtra("desiredLengthInKm", 2);
			desiredExercises = intent.getIntExtra("desiredExercises", 5);
		}
		public void saveToIntent(Intent intent) {
			intent.putExtra("startPositionLat", startPosition.latitude);
			intent.putExtra("startPositionLon", startPosition.longitude);
			intent.putExtra("desiredLengthInKm", desiredLengthInKm);
			intent.putExtra("desiredExercises", desiredExercises);
		}
	}
}
