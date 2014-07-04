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
	
	Exercise(EXERCISE_TYPE type, int repetitionGoal){
		this.repetitionGoal = repetitionGoal;
		this.type = type;
		this.repetitionActual = 0;
	}
	
	public int doRepetition(){
		repetitionActual++;
		return (repetitionGoal-repetitionActual);
	}
	public int getRepetitionsActual(){
		return this.repetitionActual;
	}
	public int getRepetitionsGoal(){
		return this.repetitionGoal;
	}
}
