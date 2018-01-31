package com.adiaz.deportelocal.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.adapters.CompetitionsAdapter;
import com.adiaz.deportelocal.database.DeporteLocalDbContract;
import com.adiaz.deportelocal.entities.Competition;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.DeporteLocalUtils;
import com.adiaz.deportelocal.utilities.PreferencesUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.adiaz.deportelocal.database.DeporteLocalDbContract.CompetitionsEntry;

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
        sportTag = getIntent().getStringExtra(DeporteLocalConstants.INTENT_SPORT_TAG);
        sportTitle = DeporteLocalUtils.getStringResourceByName(this, sportTag);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(sportTitle);
            String town = PreferencesUtils.queryPreferenceTown(this);
            String subtitle = getString(R.string.app_name) + " - " + town;
            getSupportActionBar().setSubtitle(subtitle);
        }
        // TODO: 25/04/2017 should get the competitions from the contentprovider.
        Uri uriWithSport = DeporteLocalDbContract.CompetitionsEntry.buildCompetitionsUriWithSports(sportTag);
        mCursor = getContentResolver().query(uriWithSport, CompetitionsEntry.PROJECTION, null, null, CompetitionsEntry.COLUMN_CATEGORY_ORDER);
        if (mCursor.getCount() == 0) {
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
        mInterstitialAd.setAdUnitId(DeporteLocalConstants.INTESTITIAL_AD_ID);
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
        if (mInterstitialAd.isLoaded() && SportsActivity.interstitialCount % DeporteLocalConstants.INTERSTITIAL_FRECUENCY == 0) {
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
        intent.putExtra(DeporteLocalConstants.INTENT_COMPETITION, competition);
        startActivity(intent);
    }
}

