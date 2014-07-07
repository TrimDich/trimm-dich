package de.lmu.msp.trimmdich.exercise;

import android.hardware.SensorEvent;
import de.lmu.msp.trimmdich.data.Exercise;
import de.lmu.msp.trimmdich.exercise.GolayFilter;

/**
 * Implementierung eines ExerciseCounters für die Erkennung von Kniebeugen
 */
public class SquatCounter implements ExerciseCounter {
	private static final String TAG = "SquatCounter"; // Tag fürs Loggen

	private static double BIAS = 9.49; // Zieht die Schwerkraft ab, der Wert
										// liegt seltsamerweise etwas unter
										// SensorManager.STANDARD_GRAVITY -
										// könnte von Gerät zu Gerät abweichen
	private static int BUFFER_SIZE_VALUE = 10; // Buffergröße für
												// Beschleunungswerte
	private static int BUFFER_SIZE_SLOPE = 6; // Buffergröße für letzte
												// Steigungen des Graphen

	private static int RULE_MIN_POS_SLOPE = 2; // Mindestanzahl an positiven
												// Steigungen
	private static int RULE_MIN_NEG_SLOPE = 3; // Mindestanzahl an negativen
												// Steigungen
	private static double RULE_MIN_VALUE = -2.; // Wert der unterschritten
												// werden muss
	private static double RULE_MAX_VALUE = 2.5; // Wert der übertroffen werden
												// muss

	private Exercise exercise; // Die Repräsentations der aktuellen Übung
	private ExerciseEventListener display; // Display(=Activity), welche über
											// Veränderungen informiert werden
											// soll

	private GolayFilter filter; // Ein Golay Filter
	private Ringbuffer bufferVal; // Ringbuffer für Beschleunigungswerte
	private Ringbuffer bufferSlope; // Ringbuffer für letze Steigungen
	private long lastTime; // Zeitwert des letzten Beschleunigswertes für
							// Berechnung der Steigung

	/**
	 * 
	 * @param exercise
	 * @param display
	 */
	SquatCounter(Exercise exercise, ExerciseEventListener display) {
		this.display = display;
		this.exercise = exercise;

		filter = new GolayFilter();
		bufferVal = new Ringbuffer(BUFFER_SIZE_VALUE);
		bufferVal.initWithObject(Double.valueOf(0));
		bufferSlope = new Ringbuffer(BUFFER_SIZE_SLOPE);
		bufferSlope.initWithObject(Boolean.FALSE);
		lastTime = 0;
	}

	/**
	 * Berechnet die Länge eines Vektors
	 * 
	 * @param x
	 *            x-Komponente
	 * @param y
	 *            y-Komponente
	 * @param z
	 *            z-Komponente
	 * @return
	 */
	private static double getVectorMagnitude(double x, double y, double z) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	/**
	 * Berechnet die Steigungs zwischen zwei Punkten
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private static boolean getSlope(double x1, double y1, double x2, double y2) {
		return ((x2 - x1) / (y2 - y1)) >= 0;
	}

	/**
	 * Getter für die gesetzte Übung
	 * 
	 * @return Exercise: Die aktuelle Übung
	 */
	public Exercise getExercise() {
		return this.exercise;
	}

	/**
	 * Überprüft die aktuellen Werte im Buffer, ob eine Wiederholungs
	 * stattgefunden hat und gibt zurück, ob eine Wiederholung erkannt wurde
	 * 
	 * @return boolean Erfolg
	 */
	@Override
	public boolean checkForRepetition() {
		boolean rule1 = false; // wurde der maximale Grenzwert überstiegen
		boolean rule2 = false; // wurde der minimale Grenzwert unterschritten
		// Durcjlauf des Wertebuffers und Testen, ob es die Werte gibt, welche
		// die Grenzwerte übersteigen
		for (int i = 0; i < bufferVal.size(); i++) {
			double workingVal = (Double) bufferVal.getElement(i);
			if (workingVal > RULE_MAX_VALUE)
				rule1 = true;
			if (workingVal < RULE_MIN_VALUE)
				rule2 = true;
		}
		// Aufaddierung der letzten drei Steigungen
		int firstIntervall = ((Boolean) bufferSlope.getElement(0) ? 1 : -1)
				+ ((Boolean) bufferSlope.getElement(1) ? 1 : -1)
				+ ((Boolean) bufferSlope.getElement(2) ? 1 : -1);
		// Aufaddierung der letzten drei bis sechs Steigungen
		int secIntervall = ((Boolean) bufferSlope.getElement(3) ? 1 : -1)
				+ ((Boolean) bufferSlope.getElement(4) ? 1 : -1)
				+ ((Boolean) bufferSlope.getElement(5) ? 1 : -1);
		// Test ob alle vier Regeln erfüllt sind
		if (firstIntervall >= RULE_MIN_POS_SLOPE
				&& secIntervall == (-1) * RULE_MIN_NEG_SLOPE && rule1 && rule2) {
			this.exercise.doRepetition(); // Erhöht in der Übung die geschafften
											// Wiederholungen
			return true;
		}
		return false;
	}

	/**
	 * Fügt ein SensorEvent mit den Beschleunigswerten dem Counter hinzu und
	 * überprüft, ob einmal die Übung absolviert wurde
	 */
	@Override
	public void addAccValues(SensorEvent event) {
		bufferVal.add(Double.valueOf(filter.calcGolay(getVectorMagnitude(
				event.values[0], event.values[1], event.values[2]) - BIAS))); // Die
																				// berechnetete
																				// Vektorlänge
																				// wird
																				// mit
																				// dem
																				// Goley-Filter
																				// gefiltert
																				// und
																				// im
																				// Ringbuffer
																				// abgelegt
		bufferSlope.add(getSlope((Double) bufferVal.getSecondLatestElement(),
				lastTime, (Double) bufferVal.getLatestElement(),
				event.timestamp)); // Die aktuelle Steigung wird berechnet und
									// im Ringbuffer abgelegt
		// Überprüfung ob einmal die Übung absolviert wurde und wenn ja,
		// Benachrichtigung der anzeigenden Schicht
		if (checkForRepetition() && display != null)
			display.onExerciseIterationDetected();
		lastTime = event.timestamp; // merke den Zeitstempel
	}
}
