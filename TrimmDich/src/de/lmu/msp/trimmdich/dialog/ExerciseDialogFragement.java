package de.lmu.msp.trimmdich.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.main.MainActivity;
import de.lmu.msp.trimmdich.util.Constants;

public class ExerciseDialogFragement extends DialogFragment {

	private CheckBox mPullUpCheckBox;
	private CheckBox mPushUpCheckBox;
	private CheckBox mSquatsCheckBox;
	private CheckBox mDipsCheckBox;

	private SharedPreferences mSPrefs;

	public static ExerciseDialogFragement newInstance() {
		ExerciseDialogFragement f = new ExerciseDialogFragement();
		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mSPrefs = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				getActivity());

		View v = getActivity().getLayoutInflater().inflate(
				R.layout.exercise_type_picker_layout, null);

		mDipsCheckBox = (CheckBox) v.findViewById(R.id.dipsCheckBox);
		mPullUpCheckBox = (CheckBox) v.findViewById(R.id.pullUpCheckBox);
		mPushUpCheckBox = (CheckBox) v.findViewById(R.id.pushUpCheckBox);
		mSquatsCheckBox = (CheckBox) v.findViewById(R.id.squatsCheckBox);

		alertBuilder.setView(v);
		alertBuilder.setTitle(getString(R.string.dialog_exercise));

		boolean dips = mSPrefs.getBoolean(Constants.DIPS_EXERCISE_SPREF, true);
		boolean pullup = mSPrefs.getBoolean(Constants.PULLUP_EXERCISE_SPREF,
				true);
		boolean pushup = mSPrefs.getBoolean(Constants.PUSHUP_EXERCISE_SPREF,
				true);
		boolean squats = mSPrefs.getBoolean(Constants.SQUATS_EXERCISE_SPREF,
				true);

		mDipsCheckBox.setChecked(dips);
		mPullUpCheckBox.setChecked(pullup);
		mPushUpCheckBox.setChecked(pushup);
		mSquatsCheckBox.setChecked(squats);

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

			editor.putBoolean(Constants.DIPS_EXERCISE_SPREF,
					mDipsCheckBox.isChecked());
			editor.putBoolean(Constants.SQUATS_EXERCISE_SPREF,
					mSquatsCheckBox.isChecked());
			editor.putBoolean(Constants.PULLUP_EXERCISE_SPREF,
					mPullUpCheckBox.isChecked());
			editor.putBoolean(Constants.PUSHUP_EXERCISE_SPREF,
					mPushUpCheckBox.isChecked());
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
