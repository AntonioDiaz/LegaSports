package com.adiaz.localsports.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.adiaz.localsports.R;
import com.adiaz.localsports.database.LocalSportsDbContract;
import com.adiaz.localsports.sync.CompetitionsAvailableCallback;
import com.adiaz.localsports.sync.LocalSportsSyncUtils;
import com.adiaz.localsports.sync.retrofit.LocalSportsRestApi;
import com.adiaz.localsports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.localsports.utilities.LocalSportsConstants;
import com.adiaz.localsports.utilities.LocalSportsUtils;
import com.adiaz.localsports.utilities.NetworkUtilities;
import com.adiaz.localsports.utilities.PreferencesUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by adiaz on 9/1/18.
 */

public class SportsActivity extends AppCompatActivity implements CompetitionsAvailableCallback.CompetitionsLoadedCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SportsActivity.class.getSimpleName();
    private Menu mMenu;

    @BindView((R.id.layout_activity_sports))
    View activityView;

    @BindView(R.id.ll_progress_competitions)
    LinearLayout llProgressCompetition;

    @BindView(R.id.gl_sports)
    GridLayout glSports;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        ButterKnife.bind(this);
        String town = PreferencesUtils.queryPreferenceTown(this);
        Log.d(TAG, "onCreate: town -->" + town);
        if (TextUtils.isEmpty(town)) {
            Intent intent = new Intent(this, TownsActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (getSupportActionBar()!=null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setIcon(R.mipmap.ic_launcher);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                getSupportActionBar().setTitle(" " + town);
            }
            getSupportActionBar().setSubtitle(" " + getString(R.string.app_name));
            if (!getDefaultSharedPreferences(this).contains(LocalSportsConstants.KEY_LASTUPDATE)) {
                if (NetworkUtilities.isNetworkAvailable(this)) {
                    updateCompetitions();
                } else {
                    LocalSportsUtils.showNoInternetAlert(this, activityView);
                    llProgressCompetition.setVisibility(View.INVISIBLE);
                }
            } else {
                endLoadingCompetitions();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mMenu = menu;
        updateLastUpdateMenuItem(this.mMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_preferences:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.action_update:
                updateCompetitions();
                break;
            case R.id.action_changetown:
                // TODO: 03/08/2017 ask user confirmation.
				/* cleaning preferences. */
                SharedPreferences.Editor editor = getDefaultSharedPreferences(this).edit();
                editor.remove(LocalSportsConstants.KEY_TOWN_NAME);
                editor.remove(LocalSportsConstants.KEY_TOWN_ID);
                editor.remove(LocalSportsConstants.KEY_FAVORITES_TEAMS);
                editor.remove(LocalSportsConstants.KEY_FAVORITES_COMPETITIONS);
                editor.remove(LocalSportsConstants.KEY_LASTUPDATE);
                editor.commit();
				/* cleaning database. */
                ContentResolver contentResolver = this.getContentResolver();
                contentResolver.delete(LocalSportsDbContract.CompetitionsEntry.CONTENT_URI, null, null);
                contentResolver.delete(LocalSportsDbContract.MatchesEntry.CONTENT_URI, null, null);
                contentResolver.delete(LocalSportsDbContract.ClassificationEntry.CONTENT_URI, null, null);
                contentResolver.delete(LocalSportsDbContract.SportCourtsEntry.CONTENT_URI, null, null);
                contentResolver.delete(LocalSportsDbContract.FavoritesEntry.CONTENT_URI, null, null);
				/* stop the FirebaseJob. */
                LocalSportsSyncUtils.stopJob(this);
                Intent intentTowns = new Intent(this, TownsActivity.class);
                startActivity(intentTowns);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCompetitions() {
        startLoadingCompetitions();
        Long idTownSelect = PreferencesUtils.queryPreferenceTownId(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LocalSportsConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LocalSportsRestApi localSportsRestApi = retrofit.create(LocalSportsRestApi.class);
        Call<List<CompetitionRestEntity>> call = localSportsRestApi.competitionsQuery(idTownSelect);
        call.enqueue(new CompetitionsAvailableCallback(this, this));
    }

    private void endLoadingCompetitions() {
        llProgressCompetition.setVisibility(View.INVISIBLE);
        glSports.setVisibility(View.VISIBLE);
    }

    private void startLoadingCompetitions() {
        llProgressCompetition.setVisibility(View.VISIBLE);
        glSports.setVisibility(View.INVISIBLE);
    }

    private void updateLastUpdateMenuItem(Menu menu) {
        Log.d(TAG, "updateLastUpdateMenuItem: " + menu);
        SharedPreferences preferences = getDefaultSharedPreferences(this);
        if (menu != null) {
            String title;
            MenuItem item = menu.findItem(R.id.action_update);
            if (preferences.contains(LocalSportsConstants.KEY_LASTUPDATE)) {
                Long dateLong = preferences.getLong(LocalSportsConstants.KEY_LASTUPDATE, 1L);
                DateFormat dateFormat = new SimpleDateFormat(LocalSportsConstants.DATE_FORMAT);
                title = getString(R.string.action_update, dateFormat.format(new Date(dateLong)));
            } else {
                title = getString(R.string.action_update, "");
            }
            item.setTitle(title);
        }
    }

    @Override
    public void updateActivityLoadedCompetitions() {
        endLoadingCompetitions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferencesUtils.queryPreferenceTownId(this)!=null) {
            LocalSportsSyncUtils.initialize(this);
        }
    }


    public void openSport(View view) {
        Intent intent = new Intent(this, SelectCompetitionActivity.class);
        intent.putExtra(LocalSportsConstants.INTENT_SPORT_TAG, (String) view.getTag());
        startActivity(intent);
    }

    public void openSportFootball(View view) {
        startActivity(new Intent(this, FootballActivity.class));
    }

    public void openFavorites(View view) {
        startActivity(new Intent(this, FavoritesActivity.class));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged: should update key " + key);
        if (key.equals(LocalSportsConstants.KEY_LASTUPDATE) && sharedPreferences.contains(key)) {
            updateLastUpdateMenuItem(this.mMenu);
        }
    }
}
