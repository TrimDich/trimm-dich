package de.lmu.msp.trimmdich.main;

import de.lmu.msp.trimmdich.R;
import de.lmu.msp.trimmdich.R.id;
import de.lmu.msp.trimmdich.R.layout;
import de.lmu.msp.trimmdich.R.menu;
import de.lmu.msp.trimmdich.run.RunPreviewActivity;
import de.lmu.msp.trimmdich.summary.StatisticsActivity;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		Intent newIntent = new Intent(this, StatisticsActivity.class);
		startActivity(newIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startNewRun(View view) {
		Intent newIntent = new Intent(this, SetupActivity.class);
		startActivity(newIntent);
	}

}
