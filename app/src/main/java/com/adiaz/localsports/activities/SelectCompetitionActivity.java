package com.adiaz.localsports.activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adiaz.localsports.R;
import com.adiaz.localsports.adapters.CompetitionsAdapter;
import com.adiaz.localsports.database.LocalSportsDbContract;
import com.adiaz.localsports.entities.Competition;
import com.adiaz.localsports.entities.Sport;
import com.adiaz.localsports.utilities.LocalSportsConstants;
import com.adiaz.localsports.utilities.LocalSportsUtils;
import com.adiaz.localsports.utilities.PreferencesUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.localsports.database.LocalSportsDbContract.CompetitionsEntry;

public class SelectCompetitionActivity extends AppCompatActivity implements CompetitionsAdapter.ListItemClickListener {

    private static final String TAG = SelectCompetitionActivity.class.getSimpleName();

    @BindView(R.id.rv_competitions)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_list_item)
    TextView tvEmptyListItem;

	private String sportTag;
	private String sportTitle;
	private Cursor mCursor;
	InterstitialAd mInterstitialAd;
	int mClickedItemIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_competition);
		ButterKnife.bind(this);
		sportTag = getIntent().getStringExtra(LocalSportsConstants.INTENT_SPORT_TAG);
		sportTitle = LocalSportsUtils.getStringResourceByName(this, sportTag);
		if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(sportTitle);
            String town = PreferencesUtils.queryPreferenceTown(this);
            String subtitle = getString(R.string.app_name) + " - " + town;
            getSupportActionBar().setSubtitle(subtitle);
        }
		// TODO: 25/04/2017 should get the competitions from the contentprovider.
		Uri uriWithSport = LocalSportsDbContract.CompetitionsEntry.buildCompetitionsUriWithSports(sportTag);
		mCursor = getContentResolver().query(uriWithSport, CompetitionsEntry.PROJECTION, null, null, CompetitionsEntry.COLUMN_CATEGORY_ORDER);
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
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(LocalSportsConstants.INTESTITIAL_AD_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                SelectCompetitionActivity.this.onAdClosed();
            }
        });

	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onListItemClick(int clickedItemIndex) {
        mClickedItemIndex = clickedItemIndex;
        SportsActivity.interstitialCount++;
        if (mInterstitialAd.isLoaded() && SportsActivity.interstitialCount % LocalSportsConstants.INTERSTITIAL_FRECUENCY==0) {
            mInterstitialAd.show();
        } else {
            onAdClosed();
        }
	}

	@Override
	protected void onDestroy() {
		mCursor.close();
		super.onDestroy();
	}

    private void onAdClosed() {
        mCursor.moveToPosition(mClickedItemIndex);
        Intent intent = new Intent(this, CompetitionActivity.class);
        Competition competition = CompetitionsEntry.initEntity(mCursor);
        intent.putExtra(LocalSportsConstants.INTENT_COMPETITION, competition);
        startActivity(intent);
    }
}

