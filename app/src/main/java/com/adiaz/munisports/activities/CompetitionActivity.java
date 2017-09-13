package com.adiaz.munisports.activities;


import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.Classification;
import com.adiaz.munisports.entities.Court;
import com.adiaz.munisports.entities.Favorite;
import com.adiaz.munisports.entities.Match;
import com.adiaz.munisports.entities.Team;
import com.adiaz.munisports.entities.TeamMatch;
import com.adiaz.munisports.fragments.CalendarFragment;
import com.adiaz.munisports.fragments.ClassificationFragment;
import com.adiaz.munisports.fragments.TeamsFragment;
import com.adiaz.munisports.sync.CompetitionDetailsCallbak;
import com.adiaz.munisports.utilities.CompetitionDbUtils;
import com.adiaz.munisports.utilities.FavoritesUtils;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.MuniSportsUtils;
import com.adiaz.munisports.utilities.NetworkUtilities;
import com.adiaz.munisports.utilities.ViewPagerAdapter;
import com.adiaz.munisports.utilities.harcoPro.HeaderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;


public class CompetitionActivity extends AppCompatActivity
		implements AppBarLayout.OnOffsetChangedListener, CompetitionDetailsCallbak.OnFinishLoad {

	private static final String TAG = CompetitionActivity.class.getSimpleName();
	public static final String BUNDLE_KEY_ID_COMPETITION = "BUNDLE_KEY_ID_COMPETITION";

	@Nullable
	@BindView((R.id.layout_activity_competition)) View activityView;


	@BindView(R.id.app_bar_layout)
	AppBarLayout appBarLayout;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.collapsing_toolbar)
	CollapsingToolbarLayout collapsingToolbar;
	@BindView(R.id.toolbar_header_view)
	HeaderView toolbarHeaderView;
	@BindView(R.id.float_header_view)
	HeaderView floatHeaderView;
	@BindView(R.id.tabs)
	TabLayout tabLayout;
	@BindView(R.id.viewpager)
	ViewPager viewPager;
	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.ll_progress_competition)
	LinearLayout llCompetition;

	private boolean isHideToolbarView = false;

	private String sportTitle;
	// TODO: 18/08/2017 idCompetitionServer should be Long
	public static String idCompetitionServer;
	public static Long idCompetitionServerLong;
	public static List<Team> mTeams = new ArrayList<>();
	public static List<List<Match>> mWeeks = new ArrayList<>();
	public static List<Classification> mClassificationList = new ArrayList<>();
	private Map<Long, Court> courtsMap = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition);
		ButterKnife.bind(this);
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
		tvTitle.setText(townSelect + " - " + getString(R.string.app_name));

		idCompetitionServer = getIntent().getStringExtra(MuniSportsConstants.INTENT_ID_COMPETITION_SERVER);
		idCompetitionServerLong = new Long (idCompetitionServer);

		String sportTag = getIntent().getStringExtra(MuniSportsConstants.INTENT_SPORT_TAG);
		String categoryTag = getIntent().getStringExtra(MuniSportsConstants.INTENT_CATEGORY_TAG);
		String competitionName = getIntent().getStringExtra(MuniSportsConstants.INTENT_COMPETITION_NAME);

		String sport = MuniSportsUtils.getStringResourceByName(this, sportTag);
		String category = MuniSportsUtils.getStringResourceByName(this, categoryTag);

		sportTitle = sport + " (" + category + ")";

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		collapsingToolbar.setTitle(" ");

		toolbarHeaderView.bindTo(competitionName, sportTitle, 0);
		floatHeaderView.bindTo(competitionName, sportTitle, 16);

		appBarLayout.addOnOffsetChangedListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		showLoading();
		if (NetworkUtilities.isNetworkAvailable(this)) {
			boolean needToUpdate = CompetitionDbUtils.itIsNecesaryUpdate(this.getContentResolver(), idCompetitionServerLong);
			if (needToUpdate) {
				CompetitionDbUtils.updateCompetition(this, this, idCompetitionServerLong);
			} else {
				finishLoad();
			}
		} else {
			hideLoading();
			MuniSportsUtils.showNoInternetAlert(this, activityView);
			finishLoad();
		}
	}

	private void hideLoading() {
		llCompetition.setVisibility(View.INVISIBLE);
		viewPager.setVisibility(View.VISIBLE);
	}

	private void showLoading() {
		llCompetition.setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_competition, menu);
		for (int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getItemId() == R.id.action_favorites) {
				String key = MuniSportsConstants.KEY_FAVORITES_COMPETITIONS;
				if (FavoritesUtils.isFavoriteCompetition(this, idCompetitionServerLong)) {
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
		adapter.addFragment(new CalendarFragment(), getString(R.string.calendar));
		adapter.addFragment(new ClassificationFragment(), getString(R.string.classification));
		adapter.addFragment(new TeamsFragment(), getString(R.string.teams));
		adapter.notifyDataSetChanged();
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favorites:
				AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
				Drawable drawable;
				Favorite favorite = FavoritesUtils.queryFavoriteCompetition(this, idCompetitionServerLong);
				if (favorite!=null) {
					FavoritesUtils.removeFavorites(this, favorite.getId());
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
				} else {
					FavoritesUtils.addFavorites(this, idCompetitionServerLong);
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
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_favorites);
		String idTeam = (String) imageView.getTag();
		Log.d(TAG, "selectFavorite: " + idTeam);
		Favorite favorite = FavoritesUtils.queryFavoriteTeam(this, idCompetitionServerLong, idTeam);
		if (favorite!=null) {
			imageView.setImageResource(R.drawable.ic_favorite);
			FavoritesUtils.removeFavorites(this, favorite.getId());
		} else {
			imageView.setImageResource(R.drawable.ic_favorite_fill);
			FavoritesUtils.addFavorites(this, idCompetitionServerLong, idTeam);
		}
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
		int maxScroll = appBarLayout.getTotalScrollRange();
		float percentage = (float) Math.abs(offset) / (float) maxScroll;
		if (percentage == 1f && isHideToolbarView) {
			toolbarHeaderView.setVisibility(View.VISIBLE);
			isHideToolbarView = !isHideToolbarView;
		} else if (percentage < 1f && !isHideToolbarView) {
			toolbarHeaderView.setVisibility(View.GONE);
			isHideToolbarView = !isHideToolbarView;
		}
	}

	@Override
	public void finishLoad() {
		ContentResolver contentResolver = this.getContentResolver();
		Uri uriMatches = MatchesEntry.buildMatchesUriWithCompetitions(idCompetitionServer);
		Uri uriClassification = ClassificationEntry.buildClassificationUriWithCompetitions(idCompetitionServer);
		Cursor cursorMatches = contentResolver.query(uriMatches, MatchesEntry.PROJECTION, null, null, null);
		Cursor cursorClassification = contentResolver.query(uriClassification, ClassificationEntry.PROJECTION, null, null, null);
		try {
			this.courtsMap = MuniSportsUtils.initCourts(this);
			this.mTeams = initTeams(cursorMatches, this.courtsMap);
			this.mWeeks = initCalendar(cursorMatches, this.courtsMap);
			this.mClassificationList = initClassification(cursorClassification);
		} finally {
			cursorMatches.close();
			cursorClassification.close();
		}
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);
		hideLoading();
	}

	private static List<Team> initTeams(Cursor cursorMatches, Map<Long, Court> courtsMap) {
		Integer maxWeek = -1;
		cursorMatches.moveToPosition(-1);
		while (cursorMatches.moveToNext()) {
			Integer currentWeek = cursorMatches.getInt(MatchesEntry.INDEX_WEEK);
			if (currentWeek>maxWeek) {
				maxWeek = currentWeek;
			}
		}
		Map<String, TeamMatch[]> teamsMatchesMap = new HashMap<>();
		cursorMatches.moveToPosition(-1);
		while (cursorMatches.moveToNext()) {
			String teamLocal = cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL);
			String teamVisitor = cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR);
			Integer currentWeek = cursorMatches.getInt(MatchesEntry.INDEX_WEEK) - 1;
			if (!teamLocal.equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
				if (!teamsMatchesMap.containsKey(teamLocal)) {
					teamsMatchesMap.put(teamLocal, new TeamMatch[maxWeek]);
				}
				TeamMatch teamMatch = initMatchEntity(cursorMatches, true, courtsMap);
				teamsMatchesMap.get(teamLocal)[currentWeek] = teamMatch;
			}
			if (!teamVisitor.equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
				if (!teamsMatchesMap.containsKey(teamVisitor)) {
					teamsMatchesMap.put(teamVisitor, new TeamMatch[maxWeek]);
				}
				TeamMatch teamMatch = initMatchEntity(cursorMatches, false, courtsMap);
				teamsMatchesMap.get(teamVisitor)[currentWeek] = teamMatch;
			}
		}
		List<Team> teamsList = new ArrayList<>();
		List<String> teamsNamesList = new ArrayList<>(teamsMatchesMap.keySet());
		Collections.sort(teamsNamesList);
		for (String teamName : teamsNamesList) {
			TeamMatch[] teamMatchEntities = teamsMatchesMap.get(teamName);
			Team team = new Team(teamName, teamMatchEntities);
			teamsList.add(team);
		}
		return teamsList;
	}

	private static TeamMatch initMatchEntity(Cursor cursorMatches, boolean isLocal, Map<Long, Court> courtsMap) {
		TeamMatch teamMatch = new TeamMatch();
		String teamLocal = cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL);
		String teamVisitor = cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR);
		Long idSportCenter = cursorMatches.getLong(MatchesEntry.INDEX_ID_SPORTCENTER);
		Long dateLong = cursorMatches.getLong(MatchesEntry.INDEX_DATE);
		Integer scoreLocal = cursorMatches.getInt(MatchesEntry.INDEX_SCORE_LOCAL);
		Integer scoreVisitor = cursorMatches.getInt(MatchesEntry.INDEX_SCORE_VISITOR);
		teamMatch.setPlaceName(courtsMap.get(idSportCenter).getCourtFullName());
		teamMatch.setDate(new Date(dateLong));
		teamMatch.setTeamScore(scoreLocal);
		teamMatch.setOpponentScore(scoreVisitor);
		teamMatch.setLocal(isLocal);
		if (isLocal) {
			teamMatch.setOpponent(teamVisitor);
		} else {
			teamMatch.setOpponent(teamLocal);
		}
		return teamMatch;
	}

	/**
	 * from cursor of matches sorted by weeknumber, returns a list of list of matches to print out in the fragment.
	 *
	 * @param cursorMatches
	 * @param courtsMap
	 * @return
	 */
	private static List<List<Match>> initCalendar(Cursor cursorMatches, Map<Long, Court> courtsMap) {
		List<List<Match>> weeksList = new ArrayList<>();
		cursorMatches.moveToPosition(-1);
		while (cursorMatches.moveToNext()) {
			Integer week = cursorMatches.getInt(MatchesEntry.INDEX_WEEK);
			Match match = new Match();
			match.setTeamLocal(cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL));
			match.setTeamVisitor(cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR));
			match.setScoreLocal(cursorMatches.getInt(MatchesEntry.INDEX_SCORE_LOCAL));
			match.setScoreVisitor(cursorMatches.getInt(MatchesEntry.INDEX_SCORE_VISITOR));
			long idSportCenter = cursorMatches.getLong(MatchesEntry.INDEX_ID_SPORTCENTER);
			if (courtsMap.get(idSportCenter)!=null) {
				match.setPlaceName(courtsMap.get(idSportCenter).getCourtFullName());
				match.setPlaceAddress(courtsMap.get(idSportCenter).getCenterAddress());
			} else {
				match.setPlaceName(MuniSportsConstants.UNDEFINDED_FIELD);
				match.setPlaceAddress(MuniSportsConstants.UNDEFINDED_FIELD);
			}
			match.setDate(new Date(cursorMatches.getLong(MatchesEntry.INDEX_DATE)));
			match.setState(cursorMatches.getInt(MatchesEntry.INDEX_STATE));
			if (weeksList.size()<week) {
				List<Match> emptyList = new ArrayList<>();
				weeksList.add(emptyList);

			}
			weeksList.get(week - 1).add(match);
		}

		return weeksList;
	}

	public static List<Classification> initClassification(Cursor cursorClassification) {
		List<Classification> list = new ArrayList<>();
		while (cursorClassification.moveToNext()) {
			Classification classificationEntry = new Classification();
			classificationEntry.setPosition(cursorClassification.getInt(ClassificationEntry.INDEX_POSITION));
			classificationEntry.setTeam(cursorClassification.getString(ClassificationEntry.INDEX_TEAM));
			classificationEntry.setPoints(cursorClassification.getInt(ClassificationEntry.INDEX_POINTS));
			classificationEntry.setMatchesPlayed(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_PLAYED));
			classificationEntry.setMatchesWon(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_WON));
			classificationEntry.setMatchesDrawn(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_DRAWN));
			classificationEntry.setMatchesLost(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_LOST));
			list.add(classificationEntry);
		}
		return list;
	}

}

