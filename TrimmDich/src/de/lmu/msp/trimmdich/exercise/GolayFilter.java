package de.lmu.msp.trimmdich.exercise;

public class GolayFilter {
	private final double[] x;

	public GolayFilter() {
		x = new double[8];
	}

	public double calcGolay(double value) {
		x[0] = x[1];
		x[1] = x[2];
		x[2] = x[3];
		x[3] = x[4];
		x[4] = x[5];
		x[5] = x[6];
		x[6] = x[7];
		x[7] = value;
		return (x[0] * -7d + x[1] * 11d + x[2] * 22d + x[3] * 25d + x[4] * 22d + x[5] * 11d + x[6] * -7d) / 77d;
	}
}
