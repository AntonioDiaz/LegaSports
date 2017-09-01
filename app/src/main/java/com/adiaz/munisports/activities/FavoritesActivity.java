package com.adiaz.munisports.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.Competition;
import com.adiaz.munisports.entities.TeamFavorite;
import com.adiaz.munisports.fragments.FavoritesCompetitionsFragment;
import com.adiaz.munisports.fragments.FavoritesTeamsFragment;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.MuniSportsUtils;
import com.adiaz.munisports.utilities.ViewPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class FavoritesActivity extends AppCompatActivity {
	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
	private static final String TAG = FavoritesActivity.class.getSimpleName();
	public static final String TEAM_NAME = "team_name";


	@BindView(R.id.tabs) TabLayout tabLayout;
	@BindView(R.id.viewpager) ViewPager viewPager;
	@BindView(R.id.tv_title) TextView tvTitle;

	public static List<Competition> competitionsFavorites;
	public static List<TeamFavorite> teamsFavorites;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		collapsingToolbar.setTitle(getString(R.string.favorites));
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
		tvTitle.setText(townSelect + " - " + getString(R.string.app_name));
	}

	@Override
	protected void onResume() {
		super.onResume();
		competitionsFavorites = MuniSportsUtils.getCompetitionsFavorites(this);
		teamsFavorites = MuniSportsUtils.getTeamsFavorites(this);
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new FavoritesTeamsFragment(), getString(R.string.teams));
		adapter.addFragment(new FavoritesCompetitionsFragment(), getString(R.string.competitions));
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
