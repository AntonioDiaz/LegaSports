package com.adiaz.legasports;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static com.adiaz.legasports.CategoriesActivity.EXTRA_CATEGORY_CHOSEN;
import static com.adiaz.legasports.MainActivity.EXTRA_SPORT_CHOSEN;

public class FavoritesActivity extends AppCompatActivity {

	private static final String TAG = FavoritesActivity.class.getSimpleName();
	public static final String TEAM_NAME = "team_name";

	private TabLayout tabLayout;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(getString(R.string.favorites));

		tabLayout = (TabLayout)findViewById(R.id.tabs);
		viewPager = (ViewPager)findViewById(R.id.viewpager);
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);


	}


	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new FavoritesTeamsFragment(), getString(R.string.teams));
		adapter.addFragment(new FavoritesChampionshipFragment(), getString(R.string.championship));
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	public void openFavoriteTeam(View view) {
		Intent intent = new Intent(this, FavoriteTeamActivity.class);
		intent.putExtra(TEAM_NAME, (String)view.getTag());
		startActivity(intent);
	}

	public void openFavoriteChampionShip(View view) {
		Intent intent = new Intent(this, ChampionshipActivity.class);
		String championshipName = (String)view.getTag();
		championshipName = championshipName.replaceAll("\\(", "");
		championshipName = championshipName.replaceAll("\\)", "");

		intent.putExtra(EXTRA_SPORT_CHOSEN, championshipName.split(" ")[0]);
		intent.putExtra(EXTRA_CATEGORY_CHOSEN, championshipName.split(" ")[1]);
		startActivity(intent);
	}

}
