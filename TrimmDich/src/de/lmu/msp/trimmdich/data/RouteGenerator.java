package de.lmu.msp.trimmdich.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.data.Exercise.EXERCISE_TYPE;
import static java.util.Arrays.asList;

public class RouteGenerator {
	
	

	public static ArrayList<Location> locationsAround(LatLng location, float radiusInKm) {
		
		
		return null;
	}
	
	public static List<Location> getAllLocations() {	
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
		
		allLocations.add(new Location( new LatLng(48.147862,11.571672), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.148778,11.57032), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.153359,11.582165), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.149751,11.587658), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.148577,11.590447), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		allLocations.add(new Location( new LatLng(48.145972,11.588044), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		
		allLocations.add(new Location( new LatLng(48.156508,11.580362), asList(EXERCISE_TYPE.DIPS, EXERCISE_TYPE.PULL_UP)));
		
		return allLocations;
	}
	
	public static Route generateRoute(RouteProperties routeProperties) {
		List<Location> selectedLocations = new ArrayList<Location>();
		List<Location> availableLocations = getAllLocations();
		Location lastAddedLocation;
		Route newRoute = new Route();
		double routeLengthInKm = 0;
		
		// Set start and end points
		
		selectedLocations.add(new Location(routeProperties.startPosition, asList(Exercise.EXERCISE_TYPE.SQUATS)));
		selectedLocations.add(new Location(routeProperties.startPosition, asList(Exercise.EXERCISE_TYPE.SQUATS)));
		lastAddedLocation = selectedLocations.get(0);
		
		while (routeLengthInKm < routeProperties.desiredLengthInKm) {
			// get available locations
			availableLocations = Helpers.filterLocationsByDistance(
					availableLocations, 
					lastAddedLocation.location, 
					(routeProperties.desiredLengthInKm - routeLengthInKm) / 2);
			
			if(availableLocations.size() == 0) {
				break;
			}
			
			// pop a random one
			int itemToRemove = (int)(Math.random()*availableLocations.size()); 
			lastAddedLocation = availableLocations.remove(itemToRemove);
			selectedLocations.add(selectedLocations.size() / 2, lastAddedLocation);
			routeLengthInKm = Helpers.lengthOfRoute(selectedLocations);
			Log.d("RouteGenerator", "Adding route node: " + lastAddedLocation);
			Log.d("RouteGenerator", "New route length: " + routeLengthInKm);
			
		}
		
		// Get locations within half the distance of desired distance
//		ArrayList<Location> availableLocations = RouteGenerator.locationsAround(routeProperties.startPosition, routeProperties.desiredLengthInKm);
		newRoute.locations = selectedLocations;
		newRoute.dataPoints = new ArrayList<Route.RouteDataPoint>();
		
		// Distribute the exercises across the location nodes
		for(int i = 0; i < routeProperties.desiredExercises; i++) {
			int locationIndex = new Random().nextInt(newRoute.locations.size());
			Location location = newRoute.locations.get(locationIndex);
			
			int exerciseIndex = new Random().nextInt(location.possibleExercises.size());
			EXERCISE_TYPE type = location.possibleExercises.get(exerciseIndex);
			
			Exercise newExercise = new Exercise(type, 10);
			location.selectedExercises.add(newExercise);
		}
		
		return newRoute;
	}
	
	public static class RouteProperties {
		public LatLng startPosition;
		public double desiredLengthInKm = 0;
		public int desiredExercises = 0;
		public RouteProperties() { }
		public RouteProperties(Intent intent) { 
			startPosition = new LatLng(intent.getDoubleExtra("startPositionLat", 0), intent.getDoubleExtra("startPositionLon", 0));
			desiredLengthInKm = intent.getFloatExtra("desiredLengthInKm", 5);
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
