package de.lmu.msp.trimmdich.exercise;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.hardware.SensorEvent;
import android.os.Environment;
import android.util.Log;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.exercise.GolayFilter;

public class SquatCounter implements ExerciseCounter{
	public static final String TAG = "SquatCounter";
	private static double BIAS = 9.49;
	private static int BUFFER_SIZE_VALUE = 10;
	private static int BUFFER_SIZE_SLOPE = 6;
	
	private static int ABUFFER_SIZE_VALUE = 2;
	private static int ABUFFER_SIZE_SLOPE = -3;

	private Exercise exercise;
	private ExerciseEventListener display;
	
	private GolayFilter filter;
	private Ringbuffer bufferVal;
	private Ringbuffer bufferSlope;
	double workingVal;
	long lastTime;
	long firstTime;
	//----------------------------------------
	public FileOutputStream fos;
	
	SquatCounter(Exercise exercise, ExerciseEventListener display){
		this.display = display;
		this.exercise = exercise;
		
		filter = new GolayFilter();
		bufferVal = new Ringbuffer(BUFFER_SIZE_VALUE);
		bufferVal.initWithObject(Double.valueOf(0));
		bufferSlope = new Ringbuffer(BUFFER_SIZE_SLOPE);
		bufferSlope.initWithObject(Boolean.FALSE);
		lastTime = 0;
		
		//---------------------------
		if(isExternalStorageWritable()){
	
			try {
			    String filename = "trimmdich_"+System.currentTimeMillis()+".txt";
			    File myFile = new File(Environment.getExternalStorageDirectory(), filename);
			    if (!myFile.exists())
			        myFile.createNewFile();
			    Log.e(TAG,"PATH: "+myFile.getAbsolutePath());
			    fos = new FileOutputStream(myFile);
			    String string = "Zeit;Achse-1;Achse-2;Achse-3;Buffer[0];TempBuffer[0];Slope;Squat\n";
			    byte[] data = string.getBytes();
			    try {
					fos.write(data);
					fos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static double getVectorMagnitude(double x, double y, double z){
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
	}
	private static boolean getSlope(double x1,double y1,double x2,double y2){
		return ((x2-x1)/(y2-y1))>=0;
	}
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public Exercise getExercise(){
		return this.exercise;
	}

	@Override
	public boolean checkForExercise() {
		boolean rule1 = false;
		boolean rule2 = false;
		for(int i=0;i<bufferVal.size();i++){
			workingVal = (Double) bufferVal.getElement(i);
			if(workingVal>2.5)
				rule1 = true;
			if(workingVal<-2)
				rule2 = true;
		}
		int firstIntervall = ((Boolean)bufferSlope.getElement(0)?1:-1)+((Boolean)bufferSlope.getElement(1)?1:-1)+((Boolean)bufferSlope.getElement(2)?1:-1);
		int secIntervall = ((Boolean)bufferSlope.getElement(3)?1:-1)+((Boolean)bufferSlope.getElement(4)?1:-1)+((Boolean)bufferSlope.getElement(5)?1:-1);
		if(firstIntervall>=2 && secIntervall==-3 && rule1 && rule2)
			this.exercise.doRepetition();
		return firstIntervall>=2 && secIntervall==-3 && rule1 && rule2;
	}

	@Override
	public void addAccValues(SensorEvent event) {
		bufferVal.add(Double.valueOf(filter.calcGolay(getVectorMagnitude(event.values[0],event.values[1],event.values[2])-BIAS)));
		bufferSlope.add(getSlope((Double)bufferVal.getSecondLatestElement(), lastTime, (Double)bufferVal.getLatestElement(), event.timestamp));
		if(checkForExercise()) display.onExerciseIterationDetected();
		//====================================LOGGING
//		double highest = 0;
//		for(int i=0;i<bufferVal.size();i++){
//			if(highest<(Double) bufferVal.getElement(i))
//				highest = (Double) bufferVal.getElement(i);
//		}
//		double faktor = 6./highest;
//		
//		String string = 
//				logTime+";"+x+";"+y+";"+z+";"+
//				bufferVal.getLatestElement()+";"+
//				(faktor*(Double)bufferVal.getLatestElement())+";"+
//				((Boolean)bufferSlope.getLatestElement()?"1":"-1")+";"+
//				((Boolean)checkForExercise()?"1":"-1")+";"
//				+"\n";
//		string = string.replace('.', ',');
//	    byte[] data = string.getBytes();
//	    try {
//			fos.write(data);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//====================================
	    lastTime = event.timestamp;
		
	}
}
