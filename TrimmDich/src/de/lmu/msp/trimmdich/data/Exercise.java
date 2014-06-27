package de.lmu.msp.trimmdich.data;

import java.util.List;

public class Exercise {
	private EXERCISE_TYPE type;
	private int repetitionGoal;
	private int repetitionActual;
	
	public enum EXERCISE_TYPE {
	    PULL_UP,
	    PUSH_UP,
	    DIPS
	}
}
