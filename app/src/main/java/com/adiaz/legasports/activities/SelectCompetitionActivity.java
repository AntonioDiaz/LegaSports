package com.adiaz.legasports.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adiaz.legasports.adapters.CompetitionsAdapter;
import com.adiaz.legasports.R;
import com.adiaz.legasports.utilities.Utils;

import java.util.List;

import static com.adiaz.legasports.activities.MainActivity.EXTRA_SPORT_CHOSEN;

public class SelectCompetitionActivity extends AppCompatActivity {

	public static final String EXTRA_COMPETITION_CHOSEN = "extra_competition_chosen";
	private static final String TAG = SelectCompetitionActivity.class.getSimpleName();
	private String sportTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_competition);
		sportTitle = getIntent().getStringExtra(MainActivity.EXTRA_SPORT_CHOSEN);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(sportTitle);
		// TODO: 25/04/2017 should get the competitions from the contentprovider.
		List<String> categories = Utils.getCategories(this, sportTitle);
		CompetitionsAdapter competitionsAdapter = new CompetitionsAdapter(this);
		competitionsAdapter.setCompetitions(categories);
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_competitions);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(competitionsAdapter);
		recyclerView.setNestedScrollingEnabled(false);
	}

	public void openCategory(View view) {
		Intent intent = new Intent(this, ChampionshipActivity.class);
		intent.putExtra(EXTRA_COMPETITION_CHOSEN, (String)view.getTag());
		intent.putExtra(EXTRA_SPORT_CHOSEN, sportTitle);
		startActivity(intent);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
