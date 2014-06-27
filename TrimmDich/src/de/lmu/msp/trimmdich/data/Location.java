package de.lmu.msp.trimmdich.data;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.data.Exercise.EXERCISE_TYPE;

public class Location {
	public final List<EXERCISE_TYPE> possibleExercises;
	public final LatLng location;
	// image?
	// description?
	
	public Location(LatLng location, List<EXERCISE_TYPE> possibleExercises) {
		super();
		this.possibleExercises = possibleExercises;
		this.location = location;
	}
}
