package de.lmu.msp.trimmdich.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Helpers {
	
	/*
	 * calculate distance between two LatLng, taken from http://stackoverflow.com/a/16006272
	 */
	public static double distance(LatLng StartP, LatLng EndP) {
	    double lat1 = StartP.latitude;
	    double lat2 = EndP.latitude;
	    double lon1 = StartP.longitude;
	    double lon2 = EndP.longitude;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLon = Math.toRadians(lon2-lon1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.asin(Math.sqrt(a));
	    return 6366000 * c;
	}
	
	public static List<Location> filterLocationsByDistance(List<Location> allLocations, LatLng location, double radiusInKm) {
		// filter locations based on distance
		ArrayList<Location> filteredLocations = new ArrayList<Location>();
		for (int i = 0; i < allLocations.size() ; i++) {
			Location newLocation = allLocations.get(i);
			double distanceInKm = Helpers.distance(location, newLocation.location) / 1000.0;
			if(distanceInKm < radiusInKm) {
				filteredLocations.add(newLocation);
			}
		}
		return filteredLocations;
	}
	
	public static double lengthOfRoute (List<Location> locations) {
		double length = 0;
		Location lastLocation = null;
		for (Iterator<Location> iterator = locations.iterator(); iterator.hasNext();) {
			Location location = (Location) iterator.next();
			if(lastLocation != null) {
				length += distance(lastLocation.location, location.location) / 1000.0;
			}
			lastLocation = location;
		}
		return length;
	}
}
