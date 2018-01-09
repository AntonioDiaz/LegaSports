package com.adiaz.localsports.activities;


import android.content.Context;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiaz.localsports.R;
import com.adiaz.localsports.adapters.FavoriteTeamAdapter;
import com.adiaz.localsports.entities.Competition;
import com.adiaz.localsports.entities.Favorite;
import com.adiaz.localsports.entities.Match;
import com.adiaz.localsports.entities.Team;
import com.adiaz.localsports.fragments.SendIssueDialogFragment;
import com.adiaz.localsports.sync.CompetitionDetailsCallbak;
import com.adiaz.localsports.utilities.CompetitionDbUtils;
import com.adiaz.localsports.utilities.FavoritesUtils;
import com.adiaz.localsports.utilities.MenuActionsUtils;
import com.adiaz.localsports.utilities.LocalSportsConstants;
import com.adiaz.localsports.utilities.LocalSportsUtils;
import com.adiaz.localsports.utilities.NetworkUtilities;
import com.adiaz.localsports.utilities.harcoPro.HeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.localsports.database.LocalSportsDbContract.MatchesEntry;



public class FavoriteTeamActivity extends AppCompatActivity
		implements
				AppBarLayout.OnOffsetChangedListener,
				CompetitionDetailsCallbak.OnFinishLoad,
		SendIssueDialogFragment.OnSendIssue {

	@BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;
	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
	@BindView(R.id.toolbar_header_view)	HeaderView toolbarHeaderView;
	@BindView(R.id.float_header_view) HeaderView floatHeaderView;
	@BindView(R.id.rv_fav_team_jornadas) RecyclerView recyclerView;
	@BindView(R.id.tv_title) TextView tvTitle;
	@BindView(R.id.ll_progress_team) LinearLayout llProgressTeam;
	@Nullable
	@BindView((R.id.layout_activity_team)) View activityView;


	private String mTeamName;
	private Long mIdCompetition;
	private Competition mCompetition;
	private boolean isHideToolbarView = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_team);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mTeamName = getIntent().getStringExtra(LocalSportsConstants.INTENT_TEAM_NAME);
		mIdCompetition = getIntent().getLongExtra(LocalSportsConstants.INTENT_ID_COMPETITION_SERVER, 0L);
		mCompetition = CompetitionDbUtils.queryCompetition(this.getContentResolver(), mIdCompetition);
		String subTitle = mCompetition.name();
		subTitle += " - " + LocalSportsUtils.getStringResourceByName(this, mCompetition.sportName());
		subTitle += " - " + LocalSportsUtils.getStringResourceByName(this, mCompetition.categoryName());
		collapsingToolbar.setTitle(" ");

		toolbarHeaderView.bindTo(mTeamName, subTitle, 0);
		floatHeaderView.bindTo(mTeamName, subTitle, 16);
		appBarLayout.addOnOffsetChangedListener(this);

		showLoading();
		if (NetworkUtilities.isNetworkAvailable(this)) {
			boolean needToUpdate = CompetitionDbUtils.itIsNecesaryUpdate(this.getContentResolver(), mIdCompetition);
			if (needToUpdate) {
				CompetitionDbUtils.updateCompetition(this, this, mIdCompetition);
			} else {
				finishLoad();
			}
		} else {
			LocalSportsUtils.showNoInternetAlert(this, activityView);
			finishLoad();
		}
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(LocalSportsConstants.KEY_TOWN_NAME, null);
		tvTitle.setText(townSelect + " - " + getString(R.string.app_name));
	}

	@Override
	public void finishLoad() {
		Team team = FavoriteTeamActivity.initTeamCompetition(this, mTeamName, mIdCompetition);
		FavoriteTeamAdapter adapter = new FavoriteTeamAdapter(this);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setNestedScrollingEnabled(false);
		adapter.setTeam(team);
		hideLoading();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_competition, menu);
		for(int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getItemId()== R.id.action_favorites) {
				if (FavoritesUtils.isFavoriteTeam(this, mIdCompetition, mTeamName)) {
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favorites:
				AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
				Drawable drawable;
				Favorite favorite = FavoritesUtils.queryFavoriteTeam(this, mIdCompetition, mTeamName);
				if (favorite!=null) {
					FavoritesUtils.removeFavorites(this, favorite.getId());
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
				} else {
					FavoritesUtils.addFavorites(this, mIdCompetition, mTeamName);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
				}
				int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
				final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
				drawable.setColorFilter(colorFilter);
				item.setIcon(drawable);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
	public void showLocation(View view) {
		Match match = (Match) view.getTag();
		MenuActionsUtils.showMatchLocation(this, match);
	}

	public void addEvent(View view) {
		Match match = (Match) view.getTag();
		MenuActionsUtils.addMatchEvent(this, match, mCompetition);
	}

	public void shareMatchDetails(View view) {
		Match match = (Match)view.getTag();
		MenuActionsUtils.shareMatchDetails(this, match, mCompetition);
	}

	public void sendIssue(View view) {
		Match match = (Match)view.getTag();
		SendIssueDialogFragment dialog = SendIssueDialogFragment.newInstance(match, mCompetition);
		dialog.show(getSupportFragmentManager(), "dialog");

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

	private static Team initTeamCompetition(Context context, String teamName, Long idCompetitionServer) {
		Uri uri = MatchesEntry.buildMatchesUriWithCompetitions(idCompetitionServer);
		Cursor cursorMatches = context.getContentResolver().query(uri, MatchesEntry.PROJECTION, null, null, null);
		cursorMatches.moveToPosition(-1);
		Integer weeksNumber = -1;
		while (cursorMatches.moveToNext()) {
			Integer currentWeek = cursorMatches.getInt(MatchesEntry.INDEX_WEEK);
			if (currentWeek>weeksNumber) {
				weeksNumber = currentWeek;
			}
		}
		cursorMatches.moveToPosition(-1);
		Team team = new Team(teamName, weeksNumber);
		while (cursorMatches.moveToNext()) {
			String teamLocal = cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL);
			String teamVisitor = cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR);
			if (teamName.equals(teamLocal) || teamName.equals(teamVisitor)) {
				Match match = MatchesEntry.initEntity(cursorMatches);
				team.add(match.week() - 1, match);
			}
		}
		cursorMatches.close();
		return team;
	}

	private void hideLoading() {
		llProgressTeam.setVisibility(View.INVISIBLE);
		recyclerView.setVisibility(View.VISIBLE);
	}

	private void showLoading() {
		llProgressTeam.setVisibility(View.VISIBLE);
		recyclerView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void doSendIssue(Competition competition, Match match, String description) {
		LocalSportsUtils.sendIssue(this, competition, match, description);
	}
}
