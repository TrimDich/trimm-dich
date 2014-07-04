package de.lmu.msp.trimmdich.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import de.lmu.msp.trimmdich.R;

public class ProgressDialogFragement extends DialogFragment {

	public static ProgressDialogFragement newInstance(){
		ProgressDialogFragement f = new ProgressDialogFragement();
		return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
		View v = getActivity().getLayoutInflater().inflate(R.layout.progress_dialog_layout, null);
		alertBuilder.setView(v);
		AlertDialog dialog = alertBuilder.create();
		dialog.setCancelable(false);
		return dialog;
	}
}
