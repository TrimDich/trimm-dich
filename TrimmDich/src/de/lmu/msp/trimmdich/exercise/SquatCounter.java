package de.lmu.msp.trimmdich.exercise;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;
import de.lmu.msp.trimmdich.exercise.GolayFilter;

public class SquatCounter{
	public static final String TAG = "SquatCounter";
	private static double BIAS = 9.49;
	private static int BUFFER_SIZE_VALUE = 10;
	private static int BUFFER_SIZE_SLOPE = 6;

	private GolayFilter filter;
	private Ringbuffer bufferVal;
	private Ringbuffer bufferSlope;
	private Rule ruleOne;
	private Rule ruleTwo;
	private double sumRule;
	double workingVal;
	long lastTime;
	//----------------------------------------
	public FileOutputStream fos;
	
	SquatCounter(){
		filter = new GolayFilter();
		bufferVal = new Ringbuffer(BUFFER_SIZE_VALUE);
		bufferVal.initWithObject(Double.valueOf(0));
		bufferSlope = new Ringbuffer(BUFFER_SIZE_SLOPE);
		bufferSlope.initWithObject(Boolean.FALSE);
		lastTime = 0;
		ruleOne = new Rule(1, 6., false);
		ruleTwo = new Rule(1, -2., true);
		
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
	
	public void addAccValues(double x, double y, double z,long time, double logTime){
		bufferVal.add(Double.valueOf(filter.calcGolay(getVectorMagnitude(x,y,z)-BIAS)));
		bufferSlope.add(getSlope((Double)bufferVal.getSecondLatestElement(), lastTime, (Double)bufferVal.getLatestElement(), time));
		
		double highest = 0;
		for(int i=0;i<bufferVal.size();i++){
			if(highest<(Double) bufferVal.getElement(i))
				highest = (Double) bufferVal.getElement(i);
		}
		double faktor = 6./highest;
		
		String string = 
				logTime+";"+x+";"+y+";"+z+";"+
				bufferVal.getLatestElement()+";"+
				(faktor*(Double)bufferVal.getLatestElement())+";"+
				((boolean)bufferSlope.getLatestElement()?"1":"-1")+";"+
				((boolean)checkForSquat()?"1":"-1")+";"
				+"\n";
		string = string.replace('.', ',');
	    byte[] data = string.getBytes();
	    try {
			fos.write(data);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    lastTime = time;

	}
	
	public boolean checkForSquat(){
		ruleOne.resetCount();
		ruleTwo.resetCount();
		boolean rule1 = false;
		boolean rule2 = false;
		for(int i=0;i<bufferVal.size();i++){
			workingVal = (Double) bufferVal.getElement(i);
			if(workingVal>2.5)
				rule1 = true;
			if(workingVal<-2)
				rule2 = true;
				
			//ruleOne.checkVal(workingVal);
			//ruleTwo.checkVal(workingVal);
		}
		int firstIntervall = ((boolean)bufferSlope.getElement(0)?1:-1)+((boolean)bufferSlope.getElement(1)?1:-1)+((boolean)bufferSlope.getElement(2)?1:-1);
		int secIntervall = ((boolean)bufferSlope.getElement(3)?1:-1)+((boolean)bufferSlope.getElement(4)?1:-1)+((boolean)bufferSlope.getElement(5)?1:-1);
		return firstIntervall>=2 && secIntervall==-3 && rule1 && rule2;
	}
	
	private static double getVectorMagnitude(double x, double y, double z){
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
	}
	private static boolean getSlope(double x1,double y1,double x2,double y2){
		return ((x2-x1)/(y2-y1))>=0;
	}
	
	private class Rule{
		private int minCount;
		private double limit;
		private int count;
		private boolean smallerAs = false;
		
		
		Rule(int minCount, double limit, boolean smallerAs){
			this.minCount = minCount;
			this.limit = limit;
			this.smallerAs = smallerAs;
			this.resetCount();
		}
		
		private void resetCount(){
			this.count = 0;
		}
		
		private void checkVal(double val){
			if(smallerAs){
				if(val<this.limit)
					this.count++;
			}else{
				if(val>this.limit)
					this.count++;
			}
		}
		
		private boolean val(){
			return this.count>=this.minCount;
		}
	}
	
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
}
