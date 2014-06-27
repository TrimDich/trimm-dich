package de.lmu.msp.trimmdich.data;

import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Route {
	public List<Location> locations;
	public List<RouteDataPoint> dataPoints;
	
	public static class RouteDataPoint {
		public LatLng position;
		public Date timestamp;
		public Exercise exercise;	
	}
}
