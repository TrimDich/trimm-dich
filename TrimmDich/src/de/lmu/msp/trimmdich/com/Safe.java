package de.lmu.msp.trimmdich.com;

import android.content.Context;

/**
 * Das Übergeben und Speichern von Daten zwischen den Acitivties kann öfters etwas umständlich sein. Diese Klasse vereinfacht das ganze.
 * 
 * Bitte alle Variablen hier eintragen und entsprechende Getter/Setter generieren.
 *
 */
public class Safe {
	//Vars
	//Getter
	//Setter
	
	
	//Fürs Speichern und Laden. Entweder SharedMemory oder SQLite
	public void save(Context con){
		//TODO: write save-code
	}
	public void retrieve(Context con){
		//TODO: write retrieve-code
	}
	//Singelton
	private static Safe me = null;
	private Safe(){}
	
	public static Safe getInstance(){
		if(me == null)
			me = new Safe();
		return me;
	}
}
