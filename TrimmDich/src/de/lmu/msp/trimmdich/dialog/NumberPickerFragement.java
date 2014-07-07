package de.lmu.msp.trimmdich.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.main.MainActivity;
import de.lmu.msp.trimmdich.util.Constants;

public class NumberPickerFragement extends DialogFragment {

	private static final int DISTANCE_MIN_VALUE = 1;
	private static final int DISTANCE_MAX_VALUE = 50;

	private static final int EX_COUNT_MIN_VALUE = 1;
	private static final int EX_COUNT_MAX_VALUE = 15;

	private NumberPicker mNumberPicker;

	private String mPickerSPref;

	private SharedPreferences mSPrefs;

	public static NumberPickerFragement newInstance(String pickerType) {

		NumberPickerFragement f = new NumberPickerFragement();

		Bundle args = new Bundle();
		args.putString("pickerType", pickerType);
		f.setArguments(args);

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mSPrefs = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);

		String pickerType = getArguments().getString("pickerType");

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				getActivity());

		View v = getActivity().getLayoutInflater().inflate(
				R.layout.number_picker_layout, null);
		TextView txt = (TextView) v.findViewById(R.id.number_picker_unit_txt);
		mNumberPicker = (NumberPicker) v.findViewById(R.id.number_picker);

		String title = "";

		if (pickerType.equals(Constants.DISTANCE_PICKER_TYPE)) {
			txt.setText(R.string.km);
			mPickerSPref = Constants.DISTANCE_SPREF;
			title = this.getString(R.string.dialog_route);
			mNumberPicker.setMaxValue(DISTANCE_MAX_VALUE);
			mNumberPicker.setMinValue(DISTANCE_MIN_VALUE);
		} else if (pickerType.equals(Constants.EXERCISE_COUNT_PICKER_TYPE)) {
			txt.setText(R.string.exercise_type_txt);
			mPickerSPref = Constants.EXERCISE_COUNT_SPREF;
			title = this.getString(R.string.dialog_count);
			mNumberPicker.setMaxValue(EX_COUNT_MAX_VALUE);
			mNumberPicker.setMinValue(EX_COUNT_MIN_VALUE);
		}

		mNumberPicker.setValue(mSPrefs.getInt(mPickerSPref, 1));

		alertBuilder.setView(v);
		alertBuilder.setTitle(title);

		alertBuilder.setPositiveButton(R.string.dialog_ok,
				mOnPositiveBtnListener);
		alertBuilder.setNegativeButton(R.string.dialog_cancel,
				mOnNegativeBtnListener);
		AlertDialog dialog = alertBuilder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	private OnClickListener mOnPositiveBtnListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			mSPrefs = getActivity().getSharedPreferences(Constants.PREFS_NAME,
					0);
			SharedPreferences.Editor editor = mSPrefs.edit();
			editor.putInt(mPickerSPref, mNumberPicker.getValue());
			editor.commit();

			((MainActivity) getActivity()).doPositiveClick();

		}
	};

	private OnClickListener mOnNegativeBtnListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// do nothing

		}
	};
}
