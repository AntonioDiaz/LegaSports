package com.adiaz.legasports.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.adiaz.legasports.R;
import com.adiaz.legasports.adapters.FavoriteTeamAdapter;
import com.adiaz.legasports.entities.TeamEntity;
import com.adiaz.legasports.entities.TeamMatchEntity;
import com.adiaz.legasports.utilities.LegaSportsConstants;
import com.adiaz.legasports.utilities.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FavoriteTeamActivity extends AppCompatActivity {

	private String teamName;
	private String idCompetitionServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_team);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
		teamName = getIntent().getStringExtra(LegaSportsConstants.INTENT_TEAM_NAME);
		idCompetitionServer = getIntent().getStringExtra(LegaSportsConstants.INTENT_ID_COMPETITION_SERVER);
		collapsingToolbarLayout.setTitle(teamName);

		TeamEntity teamEntity = Utils.initTeamCompetition(this, teamName, idCompetitionServer);

		FavoriteTeamAdapter adapter = new FavoriteTeamAdapter(this);
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_fav_team_jornadas);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setNestedScrollingEnabled(false);
		adapter.setTeamEntity(teamEntity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_competition, menu);
		String favoriteTeamId = Utils.composeFavoriteTeamId(teamName, idCompetitionServer);
		for(int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getItemId()== R.id.action_favorites) {
				String key = getString(R.string.key_favorites_teams);
				if (Utils.checkIfFavoritSelected(this, favoriteTeamId, key)) {
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
				String favoriteTeamId = Utils.composeFavoriteTeamId(teamName, idCompetitionServer);
				String key = getString(R.string.key_favorites_teams);
				AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
				Drawable drawable;
				if (Utils.checkIfFavoritSelected(this, favoriteTeamId, key)) {
					Utils.unMarkFavoriteTeam(this, favoriteTeamId, key);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
				} else {
					Utils.markFavoriteTeam(this, favoriteTeamId, key);
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
		TeamMatchEntity teamMatchEntity = (TeamMatchEntity)view.getTag();
		Uri addressUri = Uri.parse("geo:0,0?q=leganes, pabellon europa");
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(addressUri);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}

	public void addEvent(View view) {
		TeamMatchEntity teamMatchEntity = (TeamMatchEntity)view.getTag();

		Calendar beginTime = Calendar.getInstance();
		beginTime.setTime(teamMatchEntity.getDate());
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(teamMatchEntity.getDate());

		endTime.add(Calendar.HOUR, 2);

		String titleStr = getString(R.string.calendar_title, teamName, teamMatchEntity.getOpponent());
		String descStr = getString(R.string.calendar_description, teamName, teamMatchEntity.getOpponent());

		Intent intent = new Intent(Intent.ACTION_INSERT)
				.setData(CalendarContract.Events.CONTENT_URI)
				.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
				.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
				.putExtra(CalendarContract.Events.TITLE, titleStr)
				.putExtra(CalendarContract.Events.DESCRIPTION, descStr)
				.putExtra(CalendarContract.Events.EVENT_LOCATION, teamMatchEntity.getPlace());
		startActivity(intent);
	}

	public void shareMatchDetails(View view) {
		String mimeType = "text/plain";
		TeamMatchEntity teamMatchEntity = (TeamMatchEntity)view.getTag();
		String title = getString(R.string.calendar_title, teamName, teamMatchEntity.getOpponent());
		DateFormat df = new SimpleDateFormat(LegaSportsConstants.DATE_FORMAT);
		String strDate = df.format(teamMatchEntity.getDate());
		String subject = getString(R.string.share_description, teamName, teamMatchEntity.getOpponent(), strDate, teamMatchEntity.getPlace());
		ShareCompat.IntentBuilder
				.from(this)
				.setChooserTitle(title)
				.setType(mimeType)
				.setText(subject)
				.startChooser();
	}

}
