package com.adiaz.deportelocal.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.entities.Competition;
import com.adiaz.deportelocal.entities.TeamFavorite;
import com.adiaz.deportelocal.fragments.FavoritesCompetitionsFragment;
import com.adiaz.deportelocal.fragments.FavoritesTeamsFragment;
import com.adiaz.deportelocal.utilities.FavoritesUtils;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.PreferencesUtils;
import com.adiaz.deportelocal.utilities.ViewPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class FavoritesActivity extends AppCompatActivity {

	private static final String TAG = FavoritesActivity.class.getSimpleName();
	public static final String TEAM_NAME = "team_name";

	@BindView(R.id.tabs) TabLayout tabLayout;
	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.viewpager) ViewPager viewPager;

	public static List<Competition> competitionsFavorites;
	public static List<TeamFavorite> teamsFavorites;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.favorites));
        String town = PreferencesUtils.queryPreferenceTown(this);
        String subtitle = getString(R.string.app_name) + " - " + town;
        getSupportActionBar().setSubtitle(subtitle);
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(DeporteLocalConstants.KEY_TOWN_NAME, null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		competitionsFavorites = FavoritesUtils.getCompetitionsFavorites(this);
		teamsFavorites = FavoritesUtils.getTeamsFavorites(this);
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
