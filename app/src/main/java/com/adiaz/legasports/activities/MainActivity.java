package com.adiaz.legasports.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adiaz.legasports.R;
import com.adiaz.legasports.database.LegaSportsDbContract;
import com.adiaz.legasports.sync.LegaSportsSyncUtils;
import com.adiaz.legasports.utilities.LegaSportsConstants;
import com.adiaz.legasports.utilities.NetworkUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView((R.id.layout_activity_main)) View activityView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (NetworkUtilities.isNetworkAvailable(this)) {
			LegaSportsSyncUtils.initialize(this);
		} else {
			Cursor cursor = getContentResolver().query(
					LegaSportsDbContract.CompetitionsEntry.CONTENT_URI, null, null, null, null);
			if (cursor.getCount()==0) {
				String strError = getString(R.string.internet_required);
				final Snackbar snackbar = Snackbar.make(activityView, strError, Snackbar.LENGTH_LONG);
				snackbar.show();
				// TODO: 26/04/2017 should disabled all link (sports and favorites).
			}
		}
	}

	public void openSport(View view) {
		Intent intent = new Intent(this, SelectCompetitionActivity.class);
		intent.putExtra(LegaSportsConstants.INTENT_SPORT_TAG, (String)view.getTag());
		startActivity(intent);
	}

	public void openSportFootball(View view) {
		startActivity(new Intent(this, FootballActivity.class));
	}

	public void openFavorites(View view) {
		startActivity(new Intent(this, FavoritesActivity.class));
	}
}
