package de.lmu.msp.trimmdich.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class ExerciseDialogFragement extends DialogFragment {

	public static ExerciseDialogFragement newInstance() {
		ExerciseDialogFragement f = new ExerciseDialogFragement();
		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

}
