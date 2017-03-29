package com.adiaz.legasports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	public static final String EXTRA_SPORT_CHOSEN = "extra_sport_chosen";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
	}

	public void openSport(View view) {
		Intent intent = new Intent(this, CategoriesActivity.class);
		ViewGroup viewGroup = (ViewGroup)view;
		CharSequence textSport = ((TextView) viewGroup.getChildAt(1)).getText();
		intent.putExtra(EXTRA_SPORT_CHOSEN, textSport);
		startActivity(intent);
	}

	public void openSportOthers(View view) {
		Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
	}

	public void openFavorites(View view) {
		startActivity(new Intent(this, FavoritesActivity.class));
	}
}
