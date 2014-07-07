package de.lmu.msp.trimmdich.exercise;

import de.lmu.msp.trimmdich.data.Exercise;
import android.hardware.SensorEvent;

public interface ExerciseCounter {
	public boolean checkForRepetition();
	public void addAccValues(SensorEvent event);
	public Exercise getExercise();
}
