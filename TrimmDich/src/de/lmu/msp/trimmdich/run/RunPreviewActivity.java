package de.lmu.msp.trimmdich.run;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RunPreviewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_preview);
	}

	public void startRun(View view) {
		Intent newIntent = new Intent(this, CompassActivity.class);
		startActivity(newIntent);
	}
}
