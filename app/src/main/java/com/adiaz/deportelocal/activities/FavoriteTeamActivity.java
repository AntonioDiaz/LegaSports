package com.adiaz.deportelocal.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.adapters.FavoriteTeamAdapter;
import com.adiaz.deportelocal.entities.Competition;
import com.adiaz.deportelocal.entities.Favorite;
import com.adiaz.deportelocal.entities.Match;
import com.adiaz.deportelocal.entities.Team;
import com.adiaz.deportelocal.fragments.SendIssueDialogFragment;
import com.adiaz.deportelocal.sync.retrofit.callbacks.CompetitionDetailsCallbak;
import com.adiaz.deportelocal.utilities.CompetitionDbUtils;
import com.adiaz.deportelocal.utilities.FavoritesUtils;
import com.adiaz.deportelocal.utilities.MenuActionsUtils;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.DeporteLocalUtils;
import com.adiaz.deportelocal.utilities.NetworkUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.MatchesEntry;



public class FavoriteTeamActivity extends AppCompatActivity
		implements
				CompetitionDetailsCallbak.OnFinishLoad,
				SendIssueDialogFragment.OnSendIssue, FavoriteTeamAdapter.ListItemClickListener {

	@BindView(R.id.rv_fav_team_jornadas) RecyclerView recyclerView;
	@BindView(R.id.ll_progress_team) LinearLayout llProgressTeam;
	@BindView((R.id.layout_activity_team)) View activityView;


	private String mTeamName;
	private Long mIdCompetition;
	private Competition mCompetition;
	private boolean isHideToolbarView = false;
	private Team mTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_team);
		ButterKnife.bind(this);
		mTeamName = getIntent().getStringExtra(DeporteLocalConstants.INTENT_TEAM_NAME);
		mIdCompetition = getIntent().getLongExtra(DeporteLocalConstants.INTENT_ID_COMPETITION_SERVER, 0L);
		mCompetition = CompetitionDbUtils.queryCompetition(this.getContentResolver(), mIdCompetition);
		String subTitle = mCompetition.name();
		subTitle += " - " + DeporteLocalUtils.getStringResourceByName(this, mCompetition.sportName());
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(mTeamName);
		getSupportActionBar().setSubtitle(subTitle);
	}

	@Override
	protected void onResume() {
		super.onResume();
        showLoading();
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
	public void finishLoad() {
		mTeam = FavoriteTeamActivity.initTeamCompetition(this, mTeamName, mIdCompetition);
		FavoriteTeamAdapter adapter = new FavoriteTeamAdapter(this, this);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setNestedScrollingEnabled(false);
		adapter.setTeam(mTeam);
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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Match match = (Match)v.getTag();
		if (match !=null) {
			menu.setHeaderTitle(getString(R.string.menu_match_title));
			MenuInflater menuInflater = this.getMenuInflater();
			menuInflater.inflate(R.menu.menu_match, menu);
			menu.findItem(R.id.action_add_calendar).setEnabled(false);
            if (match.isDateDefined() && match.state()== DeporteLocalConstants.STATE_PENDING) {
                menu.findItem(R.id.action_add_calendar).setEnabled(true);
            }
			menu.findItem(R.id.action_view_map).setEnabled(false);
            if (match.isCourtDefinded(this) &&
                    (match.state() == DeporteLocalConstants.STATE_PENDING || match.state()==DeporteLocalConstants.STATE_PLAYED)) {
			    menu.findItem(R.id.action_view_map).setEnabled(true);
            }
		}
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
                    Toast.makeText(this, getString(R.string.favorites_team_removed), Toast.LENGTH_SHORT).show();
                } else {
					FavoritesUtils.addFavorites(this, mIdCompetition, mTeamName);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
                    Toast.makeText(this, R.string.favorites_team_added, Toast.LENGTH_SHORT).show();
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
    public boolean onContextItemSelected(MenuItem item) {
        Match match = (Match) activityView.getTag();
        switch (item.getItemId()) {
            case R.id.action_add_calendar:
                MenuActionsUtils.addMatchEvent(this, match, mCompetition);
                break;
            case R.id.action_view_map:
                MenuActionsUtils.showMatchLocation(this, match);
                break;
            case R.id.action_share:
                MenuActionsUtils.shareMatchDetails(this, match, mCompetition);
                break;
            case R.id.action_notify_error:
                SendIssueDialogFragment dialog = SendIssueDialogFragment.newInstance(match, mCompetition);
                dialog.show(getSupportFragmentManager(), "dialog");
        }
        return super.onContextItemSelected(item);

    }

    @Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
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
		DeporteLocalUtils.sendIssue(this, competition, match, description);
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		Match match = mTeam.getMatches()[clickedItemIndex];
		activityView.setTag(match);
		registerForContextMenu(activityView);
		openContextMenu(activityView);
	}
}
