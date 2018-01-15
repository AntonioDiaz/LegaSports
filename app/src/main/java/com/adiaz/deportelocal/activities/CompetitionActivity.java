package com.adiaz.deportelocal.activities;


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.entities.Classification;
import com.adiaz.deportelocal.entities.Competition;
import com.adiaz.deportelocal.entities.Court;
import com.adiaz.deportelocal.entities.Favorite;
import com.adiaz.deportelocal.entities.Match;
import com.adiaz.deportelocal.entities.Team;
import com.adiaz.deportelocal.fragments.CalendarFragment;
import com.adiaz.deportelocal.fragments.ClassificationFragment;
import com.adiaz.deportelocal.fragments.SendIssueDialogFragment;
import com.adiaz.deportelocal.fragments.TeamsFragment;
import com.adiaz.deportelocal.sync.retrofit.callbacks.CompetitionDetailsCallbak;
import com.adiaz.deportelocal.utilities.CompetitionDbUtils;
import com.adiaz.deportelocal.utilities.FavoritesUtils;
import com.adiaz.deportelocal.utilities.MenuActionsUtils;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.DeporteLocalUtils;
import com.adiaz.deportelocal.utilities.NetworkUtilities;
import com.adiaz.deportelocal.utilities.PreferencesUtils;
import com.adiaz.deportelocal.utilities.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.adiaz.deportelocal.database.DeporteLocalDbContract.ClassificationEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.MatchesEntry;


public class CompetitionActivity extends AppCompatActivity implements CompetitionDetailsCallbak.OnFinishLoad, SendIssueDialogFragment.OnSendIssue {

	private static final String TAG = CompetitionActivity.class.getSimpleName();
	public static final String BUNDLE_KEY_ID_COMPETITION = "BUNDLE_KEY_ID_COMPETITION";

	@Nullable
	@BindView((R.id.layout_activity_competition))
    View activityView;

	@BindView(R.id.tabs)
	TabLayout tabLayout;

	@BindView(R.id.viewpager)
	ViewPager viewPager;

    @BindView(R.id.ll_progress_competition_details)
    LinearLayout llProgress;


	private boolean isHideToolbarView = false;

	public static Long mIdCompetition;
	public static Competition mCompetition;
	public static List<Team> mTeams = new ArrayList<>();
	public static List<List<Match>> mWeeks = new ArrayList<>();
	public static List<Classification> mClassificationList = new ArrayList<>();
	private Map<Long, Court> courtsMap = new HashMap<>();
	private Match mMatch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition);
		ButterKnife.bind(this);
		String townSelect = PreferencesUtils.queryPreferenceTown(this);
		mCompetition = getIntent().getParcelableExtra(DeporteLocalConstants.INTENT_COMPETITION);
		mIdCompetition = mCompetition.serverId();
		String sportTag = mCompetition.sportName();
		String categoryTag = mCompetition.categoryName();
		String competitionName = mCompetition.name();

		String sport = DeporteLocalUtils.getStringResourceByName(this, sportTag);
		String category = DeporteLocalUtils.getStringResourceByName(this, categoryTag);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(competitionName);
        getSupportActionBar().setSubtitle(category + " - " + sport);
        llProgress.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (NetworkUtilities.isNetworkAvailable(this)) {
			boolean needToUpdate = CompetitionDbUtils.itIsNecesaryUpdate(this.getContentResolver(), mIdCompetition);
			if (needToUpdate) {
				CompetitionDbUtils.updateCompetition(this, this, mIdCompetition);
			} else {
				finishLoad();
			}
		} else {
			DeporteLocalUtils.showNoInternetAlert(this, activityView);
			finishLoad();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_competition, menu);
		for (int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getItemId() == R.id.action_favorites) {
				if (FavoritesUtils.isFavoriteCompetition(this, mIdCompetition)) {
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
				Favorite favorite = FavoritesUtils.queryFavoriteCompetition(this, mIdCompetition);
				if (favorite!=null) {
					FavoritesUtils.removeFavorites(this, favorite.getId());
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
                    Toast.makeText(this, R.string.favorites_competition_removed, Toast.LENGTH_SHORT).show();
                } else {
					FavoritesUtils.addFavorites(this, mIdCompetition);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
                    Toast.makeText(this, R.string.favorites_competition_added, Toast.LENGTH_SHORT).show();
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
		Favorite favorite = FavoritesUtils.queryFavoriteTeam(this, mIdCompetition, idTeam);
        if (favorite!=null) {
			imageView.setImageResource(R.drawable.ic_favorite);
			FavoritesUtils.removeFavorites(this, favorite.getId());
            Toast.makeText(this, R.string.favorites_team_removed, Toast.LENGTH_SHORT).show();

        } else {
			imageView.setImageResource(R.drawable.ic_favorite_fill);
			FavoritesUtils.addFavorites(this, mIdCompetition, idTeam);
            Toast.makeText(this, R.string.favorites_team_added, Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void finishLoad() {
		ContentResolver contentResolver = this.getContentResolver();
		Uri uriMatches = MatchesEntry.buildMatchesUriWithCompetitions(mIdCompetition);
		Uri uriClassification = ClassificationEntry.buildClassificationUriWithCompetitions(mIdCompetition);
		Cursor cursorMatches = contentResolver.query(uriMatches, MatchesEntry.PROJECTION, null, null, null);
		Cursor cursorClassification = contentResolver.query(uriClassification, ClassificationEntry.PROJECTION, null, null, null);
		try {
			this.mTeams = initTeams(cursorMatches);
			this.mWeeks = initCalendar(cursorMatches);
			this.mClassificationList = initClassification(cursorClassification);
		} finally {
			cursorMatches.close();
			cursorClassification.close();
		}
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.setVisibility(View.VISIBLE);
		llProgress.setVisibility(View.INVISIBLE);
	}

	private static List<Team> initTeams(Cursor cursorMatches) {
		List<Team> teamsList = new ArrayList<>();
		Integer maxWeek = -1;
		cursorMatches.moveToPosition(-1);
		while (cursorMatches.moveToNext()) {
			Integer currentWeek = cursorMatches.getInt(MatchesEntry.INDEX_WEEK);
			if (currentWeek>maxWeek) {
				maxWeek = currentWeek;
			}
		}
		Map<String, Match[]> teamsMatchesMap = new HashMap<>();
		cursorMatches.moveToPosition(-1);
		while (cursorMatches.moveToNext()) {
			String teamLocal = cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL);
			String teamVisitor = cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR);
			Integer currentWeek = cursorMatches.getInt(MatchesEntry.INDEX_WEEK) - 1;
			if (!teamLocal.equals(DeporteLocalConstants.UNDEFINDED_FIELD)) {
				if (!teamsMatchesMap.containsKey(teamLocal)) {
					teamsMatchesMap.put(teamLocal, new Match[maxWeek]);
				}
				Match match = MatchesEntry.initEntity(cursorMatches);
				teamsMatchesMap.get(teamLocal)[currentWeek] = match;
			}
			if (!teamVisitor.equals(DeporteLocalConstants.UNDEFINDED_FIELD)) {
				if (!teamsMatchesMap.containsKey(teamVisitor)) {
					teamsMatchesMap.put(teamVisitor, new Match[maxWeek]);
				}
				Match match = MatchesEntry.initEntity(cursorMatches);
				teamsMatchesMap.get(teamVisitor)[currentWeek] = match;
			}
		}
		List<String> teamsNamesList = new ArrayList<>(teamsMatchesMap.keySet());
		Collections.sort(teamsNamesList);
		for (String teamName : teamsNamesList) {
			Match[] matches = teamsMatchesMap.get(teamName);
			Team team = new Team(teamName, matches);
			teamsList.add(team);
		}
		return teamsList;
	}

	/**
	 * from cursor of matches sorted by weeknumber, returns a list of list of matches to print out in the fragment.
	 *
	 * @param cursorMatches
	 * @return
	 */
	private static List<List<Match>> initCalendar(Cursor cursorMatches) {
		List<List<Match>> weeksList = new ArrayList<>();
		cursorMatches.moveToPosition(-1);
		while (cursorMatches.moveToNext()) {
			Integer week = cursorMatches.getInt(MatchesEntry.INDEX_WEEK);
			Match match = MatchesEntry.initEntity(cursorMatches);
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
			list.add(ClassificationEntry.initEntity(cursorClassification));
		}
		return list;
	}
	@Override
	public void doSendIssue(Competition competition, Match match, String description) {
		DeporteLocalUtils.sendIssue(this, competition, match, description);
	}


	public void openMenu(View view) {
		registerForContextMenu(view);
		openContextMenu(view);
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Match match = (Match)v.getTag();
		if (match !=null) {
			mMatch = match;
			menu.setHeaderTitle(getString(R.string.menu_match_title));
			MenuInflater menuInflater = this.getMenuInflater();
			menuInflater.inflate(R.menu.menu_match, menu);
			menu.findItem(R.id.action_view_map).setEnabled(mMatch.isCourtDefinded(this));
			menu.findItem(R.id.action_add_calendar).setEnabled(mMatch.isDateDefined());
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add_calendar:
				MenuActionsUtils.addMatchEvent(this, mMatch, CompetitionActivity.mCompetition);
				break;
			case R.id.action_view_map:
				MenuActionsUtils.showMatchLocation(this, mMatch);
				break;
			case R.id.action_share:
				MenuActionsUtils.shareMatchDetails(this, mMatch, CompetitionActivity.mCompetition);
				break;
			case R.id.action_notify_error:
				//Match zipotegato = Match.builder().setName("zipotegato").build();
				//String name = zipotegato.name();
				//Match.Builder zipotegago = Match.builder().setName("zipotegago").setYear(2109).build();

				SendIssueDialogFragment dialog = SendIssueDialogFragment.newInstance(mMatch, CompetitionActivity.mCompetition);
				dialog.show(getSupportFragmentManager(), "dialog");
				break;
		}
		return super.onContextItemSelected(item);
	}
}

