package de.lmu.msp.trimmdich.data;

import java.util.List;

public class Exercise {
	
	private int repetitionGoal;
	private int repetitionActual;
	private EXERCISE_TYPE type;
	
	public enum EXERCISE_TYPE {
	    PULL_UP,
	    PUSH_UP,
	    DIPS,
	    SQUATS
	}
	/**
	 * 
	 * @param type
	 * @param repetitionGoal
	 */
	public Exercise(EXERCISE_TYPE type, int repetitionGoal){
		this.repetitionGoal = repetitionGoal;
		this.type = type;
		this.repetitionActual = 0;
	}
	
	/**
	 * Adds one repetition and returns the remaining repetitions
	 * @return int remaining repetitions
	 */
	public int doRepetition(){
		repetitionActual++;
		return (repetitionGoal-repetitionActual);
	}
	
	/**
	 * Return the actual repetition
	 * @return int actual repetitions
	 */
	public int getRepetitionsActual(){
		return this.repetitionActual;
	}
	
	/**
	 * Returns the aimed repetition count
	 * @return int aimed repetitions
	 */
	public int getRepetitionsGoal(){
		return this.repetitionGoal;
	}
}
