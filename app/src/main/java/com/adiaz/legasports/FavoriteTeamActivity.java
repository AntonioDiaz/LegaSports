package com.adiaz.legasports;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adiaz.legasports.entities.TeamEntity;
import com.adiaz.legasports.entities.TeamMatchEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FavoriteTeamActivity extends AppCompatActivity {

	private String teamName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_team);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
		teamName = getIntent().getStringExtra(FavoritesActivity.TEAM_NAME);
		collapsingToolbarLayout.setTitle(teamName);

		TeamEntity teamEntity = Utils.initTeam(this, teamName);

		FavoriteTeamAdapter adapter = new FavoriteTeamAdapter(this);
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_fav_team_jornadas);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setNestedScrollingEnabled(false);
		adapter.setTeamEntity(teamEntity);
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
