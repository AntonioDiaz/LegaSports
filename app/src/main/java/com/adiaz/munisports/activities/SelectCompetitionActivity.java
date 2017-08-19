package com.adiaz.munisports.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.adapters.CompetitionsAdapter;
import com.adiaz.munisports.database.MuniSportsDbContract;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;

public class SelectCompetitionActivity extends AppCompatActivity implements CompetitionsAdapter.ListItemClickListener {

	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
	@BindView(R.id.rv_competitions) RecyclerView recyclerView;
	@BindView(R.id.tv_title) TextView tvTitle;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyListItem;

	private static final String TAG = SelectCompetitionActivity.class.getSimpleName();
	private String sportTag;
	private String sportTitle;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_competition);
		ButterKnife.bind(this);
		sportTag = getIntent().getStringExtra(MuniSportsConstants.INTENT_SPORT_TAG);
		sportTitle = Utils.getStringResourceByName(this, sportTag);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		collapsingToolbar.setTitle(sportTitle);
		// TODO: 25/04/2017 should get the competitions from the contentprovider.
		Uri uriWithSport = MuniSportsDbContract.CompetitionsEntry.buildCompetitionsUriWithSports(sportTag);
		mCursor = getContentResolver().query(uriWithSport, CompetitionsEntry.PROJECTION, null, null, CompetitionsEntry.COLUMN_CATEGORY_ORDER);
		Log.d(TAG, "onCreate: "  + mCursor.getCount());
		if (mCursor.getCount()==0) {
			recyclerView.setVisibility(View.INVISIBLE);
			tvEmptyListItem.setVisibility(View.VISIBLE);
		} else {
			recyclerView.setVisibility(View.VISIBLE);
			tvEmptyListItem.setVisibility(View.INVISIBLE);
			CompetitionsAdapter competitionsAdapter = new CompetitionsAdapter(this, this);
			competitionsAdapter.setCompetitions(mCursor);
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			recyclerView.setHasFixedSize(true);
			recyclerView.setAdapter(competitionsAdapter);
			recyclerView.setNestedScrollingEnabled(false);
		}
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
		tvTitle.setText(townSelect + " - " + getString(R.string.app_name));
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		mCursor.moveToPosition(clickedItemIndex);
		Intent intent = new Intent(this, CompetitionActivity.class);
		intent.putExtra(MuniSportsConstants.INTENT_ID_COMPETITION_SERVER, mCursor.getString(CompetitionsEntry.INDEX_ID_SERVER));
		intent.putExtra(MuniSportsConstants.INTENT_SPORT_TAG, mCursor.getString(CompetitionsEntry.INDEX_SPORT));
		intent.putExtra(MuniSportsConstants.INTENT_CATEGORY_TAG, mCursor.getString(CompetitionsEntry.INDEX_CATEGORY));
		intent.putExtra(MuniSportsConstants.INTENT_COMPETITION_NAME, mCursor.getString(CompetitionsEntry.INDEX_NAME));
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		mCursor.close();
		super.onDestroy();
	}
}

