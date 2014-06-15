package de.lmu.msp.trimmdich;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
	}
	
	public void generateRun(View view) {
		Intent newIntent = new Intent(this, RunPreviewActivity.class);
		startActivity(newIntent);
	}
	
}
