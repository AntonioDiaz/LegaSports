package com.adiaz.legasports.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.adiaz.legasports.R;
import com.adiaz.legasports.utilities.Utils;

public class MainActivity extends AppCompatActivity {

	public static final String EXTRA_SPORT_CHOSEN = "extra_sport_chosen";
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);

		Log.d(TAG, "onCreate: " + Utils.getCategories(this, getString(R.string.football_7)));
		
	}

	public void openSport(View view) {
		Intent intent = new Intent(this, CategoriesActivity.class);
		intent.putExtra(EXTRA_SPORT_CHOSEN, (String)view.getTag());
		startActivity(intent);
	}

	public void openSportFootball(View view) {
		startActivity(new Intent(this, FootballActivity.class));
	}

	public void openFavorites(View view) {
		startActivity(new Intent(this, FavoritesActivity.class));
	}
}
