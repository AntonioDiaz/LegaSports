package com.adiaz.localsports.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.adiaz.localsports.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by toni on 03/08/2017.
 */

public class SettingsActivity extends AppCompatActivity {

	@Nullable
	@BindView(R.id.toolbar)
	Toolbar toolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

		ActionBar actionBar = this.getSupportActionBar();

		// Set the action bar back button to look like an up button
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// When the home button is pressed, take the user back to the VisualizerActivity
		if (id == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
