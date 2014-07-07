package de.lmu.msp.trimmdich.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.android.gms.maps.model.LatLng;

import de.lmu.msp.trimmdich.data.Exercise.EXERCISE_TYPE;

public class Location {
	public final List<EXERCISE_TYPE> possibleExercises;
	public final LatLng location;
	public List<Exercise> selectedExercises;
	// image?
	// description?
	
	public Location(LatLng location, List<EXERCISE_TYPE> possibleExercises) {
		super();
		this.possibleExercises = possibleExercises;
		this.location = location;
		this.selectedExercises = new ArrayList<Exercise>();
	}
	
	public void selectRandomExercise() {
		int exerciseIndex = new Random().nextInt(this.possibleExercises.size());
		EXERCISE_TYPE type = this.possibleExercises.get(exerciseIndex);
		
		Exercise newExercise = new Exercise(type, 5);
		this.selectedExercises.add(newExercise);
	}
}
