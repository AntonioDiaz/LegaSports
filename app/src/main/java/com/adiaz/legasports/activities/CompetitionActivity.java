package com.adiaz.legasports.activities;

import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.adiaz.legasports.R;
import com.adiaz.legasports.entities.JornadaEntity;
import com.adiaz.legasports.entities.TeamEntity;
import com.adiaz.legasports.fragments.CalendarFragment;
import com.adiaz.legasports.fragments.ClassificationFragment;
import com.adiaz.legasports.fragments.TeamsFragment;
import com.adiaz.legasports.utilities.LegaSportsConstants;
import com.adiaz.legasports.utilities.Utils;
import com.adiaz.legasports.utilities.ViewPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.adiaz.legasports.database.LegaSportsDbContract.MatchesEntry;

public class CompetitionActivity extends AppCompatActivity {

	private static final String TAG = CompetitionActivity.class.getSimpleName();

	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
	@BindView(R.id.tabs) TabLayout tabLayout;
	@BindView(R.id.viewpager) ViewPager viewPager;


	private String sportTitle;
	public static String idCompetitionServer;
	public static List<TeamEntity> teams;
	public static List<JornadaEntity> jornadas;

	@Override
	protected void 	onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition);
		ButterKnife.bind(this);
		String sportTag = getIntent().getStringExtra(LegaSportsConstants.INTENT_SPORT_TAG);
		String categoryTag = getIntent().getStringExtra(LegaSportsConstants.INTENT_CATEGORY_TAG);
		String competitionName = getIntent().getStringExtra(LegaSportsConstants.INTENT_COMPETITION_NAME);
		String sport = Utils.getStringResourceByName(this, sportTag);
		String category = Utils.getStringResourceByName(this, categoryTag);
		idCompetitionServer = getIntent().getStringExtra(LegaSportsConstants.INTENT_ID_COMPETITION_SERVER);
		sportTitle = sportTag + " (" + categoryTag + ")";

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		collapsingToolbar.setTitle(competitionName + " (" + category + ") " + sport);
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);

		/*loading structures for the tabs: */
		Uri uriMatches = MatchesEntry.buildMatchesUriWithCompetitions(idCompetitionServer);
		Cursor cursorMatches = getContentResolver().query(uriMatches, null, null, null, null);
		teams = Utils.initTeams(cursorMatches);
		jornadas = Utils.initCalendar(cursorMatches);
		cursorMatches.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_competition, menu);
		for(int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getItemId()== R.id.action_favorites) {
				String key = getString(R.string.key_favorites_competitions);
				if (Utils.checkIfFavoritSelected(this, idCompetitionServer, key)) {
					AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
					Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
					menu.getItem(i).setIcon(drawable);
				}
				Drawable icon = menu.getItem(i).getIcon();
				int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
				final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
				icon.setColorFilter(colorFilter);
			}
		}
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new TeamsFragment(), getString(R.string.teams));
		adapter.addFragment(new ClassificationFragment(), getString(R.string.classification));
		adapter.addFragment(new CalendarFragment(), getString(R.string.calendar));
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favorites:
				String key = getString(R.string.key_favorites_competitions);
				AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
				Drawable drawable;
				if (Utils.checkIfFavoritSelected(this, idCompetitionServer, key)) {
					Utils.unMarkFavoriteTeam(this, idCompetitionServer, key);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
				} else {
					Utils.markFavoriteTeam(this, idCompetitionServer, key);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
				}
				int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
				final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
				drawable.setColorFilter(colorFilter);
				item.setIcon(drawable);

				break;
			case android.R.id.home:
				onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	public void selectFavorite(View view) {
		ImageView imageView = (ImageView)view.findViewById(R.id.iv_favorites);
		String myTeamId = Utils.generateTeamKey((String)imageView.getTag(), idCompetitionServer);
		String keyFavorites = getString(R.string.key_favorites_teams);
		if (Utils.checkIfFavoritSelected(this, myTeamId, keyFavorites)) {
			imageView.setImageResource(R.drawable.ic_favorite);
			Utils.unMarkFavoriteTeam(this, myTeamId, keyFavorites);
		} else {
			imageView.setImageResource(R.drawable.ic_favorite_fill);
			Utils.markFavoriteTeam(this, myTeamId, keyFavorites);
		}
	}
}
