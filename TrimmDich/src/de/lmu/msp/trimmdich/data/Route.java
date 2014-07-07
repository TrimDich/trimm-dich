package de.lmu.msp.trimmdich.data;

import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Route {
	public List<Location> locations;
	public List<RouteDataPoint> dataPoints;
	public double flightPathInKm;

	public static class RouteDataPoint {
		public LatLng position;
		public Date timestamp;
		public Exercise exercise;

		public RouteDataPoint(LatLng position, Date timestamp, Exercise exercise) {

			this.position = position;
			this.timestamp = timestamp;
			this.exercise = exercise;
		}
	}
}
