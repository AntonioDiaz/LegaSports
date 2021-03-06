package com.adiaz.deportelocal.activities;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.adapters.SportsAdapter;
import com.adiaz.deportelocal.database.DeporteLocalDbContract;
import com.adiaz.deportelocal.database.DeporteLocalDbHelper;
import com.adiaz.deportelocal.entities.Sport;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.DeporteLocalUtils;
import com.adiaz.deportelocal.utilities.NetworkUtilities;
import com.adiaz.deportelocal.utilities.PreferencesUtils;
import com.adiaz.deportelocal.utilities.retrofit.DeporteLocalRestApi;
import com.adiaz.deportelocal.utilities.retrofit.callbacks.CompetitionsAvailableCallback;
import com.adiaz.deportelocal.utilities.retrofit.callbacks.SportsCallback;
import com.adiaz.deportelocal.utilities.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.deportelocal.utilities.retrofit.entities.sport.SportsRestEntity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

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
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.SportsEntry;

/**
 * Created by adiaz on 9/1/18.
 */

public class SportsActivity extends AppCompatActivity implements
        CompetitionsAvailableCallback.CompetitionsLoadedCallback,
        SharedPreferences.OnSharedPreferenceChangeListener,
        SportsCallback.SportsLoadedCallback,
        SportsAdapter.ListItemClickListener {

    private static final String TAG = SportsActivity.class.getSimpleName();
    private Menu mMenu;
    private boolean mFinishLoadCompetitions;
    private boolean mFinishLoadSports;
    private Cursor mCursorSports;
    public static Integer interstitialCount = 0;
    BroadcastReceiver broadcastReceiver;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @BindView((R.id.layout_activity_sports))
    View activityView;

    @BindView(R.id.ll_progress_competitions)
    LinearLayout llProgressCompetition;

    @BindView(R.id.rv_sports)
    RecyclerView rvSports;

    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        MobileAds.initialize(this, DeporteLocalConstants.ADMOB_ID);
        ButterKnife.bind(this);
        String town = PreferencesUtils.queryPreferenceTown(this);
        if (TextUtils.isEmpty(town)) {
            Intent intent = new Intent(this, TownsActivity.class);
            startActivity(intent);
            finish();
        } else {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    updateCompetitions();
                }
            };
            registerReceiver(broadcastReceiver, new IntentFilter(DeporteLocalDbHelper.REFRESH_BROADCAST));
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setIcon(R.mipmap.ic_launcher);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                getSupportActionBar().setTitle("   " + town);
                getSupportActionBar().setSubtitle("    " + getString(R.string.app_name));
            }
            boolean containsLastUpdateKey = getDefaultSharedPreferences(this).contains(DeporteLocalConstants.KEY_LASTUPDATE);
            if (!containsLastUpdateKey) {
                if (NetworkUtilities.isNetworkAvailable(this)) {
                    updateCompetitions();
                } else {
                    DeporteLocalUtils.showNoInternetAlert(this, activityView);
                    llProgressCompetition.setVisibility(View.INVISIBLE);
                }
            } else {
                mFinishLoadCompetitions = true;
                mFinishLoadSports = true;
                endLoadingCompetitions();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mMenu = menu;
        updateLastUpdateMenuItem(this.mMenu);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null) {
            if (menu.findItem(R.id.action_user_management)!=null) {
                String actionLogin = getString(R.string.action_login);
                menu.findItem(R.id.action_user_management).setTitle(actionLogin);
            }
        }
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
            case R.id.action_user_management:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.action_changetown:
                // TODO: 03/08/2017 ask user confirmation.
                /* cleaning preferences. */
                SharedPreferences.Editor editor = getDefaultSharedPreferences(this).edit();
                editor.remove(DeporteLocalConstants.KEY_TOWN_NAME);
                editor.remove(DeporteLocalConstants.KEY_TOWN_ID);
                editor.remove(DeporteLocalConstants.KEY_FAVORITES_TEAMS);
                editor.remove(DeporteLocalConstants.KEY_FAVORITES_COMPETITIONS);
                editor.remove(DeporteLocalConstants.KEY_LASTUPDATE);
                editor.commit();
                /* cleaning database. */
                ContentResolver contentResolver = this.getContentResolver();
                contentResolver.delete(DeporteLocalDbContract.CompetitionsEntry.CONTENT_URI, null, null);
                contentResolver.delete(DeporteLocalDbContract.MatchesEntry.CONTENT_URI, null, null);
                contentResolver.delete(DeporteLocalDbContract.ClassificationEntry.CONTENT_URI, null, null);
                contentResolver.delete(DeporteLocalDbContract.SportCourtsEntry.CONTENT_URI, null, null);
                contentResolver.delete(DeporteLocalDbContract.FavoritesEntry.CONTENT_URI, null, null);
                contentResolver.delete(SportsEntry.CONTENT_URI, null, null);
                SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
                String fcmTopic = sharedPreferences.getString(DeporteLocalConstants.KEY_TOWN_TOPIC, null);
                if (!TextUtils.isEmpty(fcmTopic)) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(fcmTopic);
                }
                Intent intentTowns = new Intent(this, TownsActivity.class);
                startActivity(intentTowns);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        SportsActivity.this.recreate();
                    }
                });
    }

    private void updateCompetitions() {
        startLoadingCompetitions();
        Long idTownSelect = PreferencesUtils.queryPreferenceTownId(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DeporteLocalConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DeporteLocalRestApi deporteLocalRestApi = retrofit.create(DeporteLocalRestApi.class);
        Call<List<CompetitionRestEntity>> callCompetitions = deporteLocalRestApi.competitionsQuery(idTownSelect);
        callCompetitions.enqueue(new CompetitionsAvailableCallback(this, this));
        Call<List<SportsRestEntity>> callSports = deporteLocalRestApi.sportsQuery(idTownSelect);
        callSports.enqueue(new SportsCallback(this, this));
    }

    private void endLoadingCompetitions() {
        if (mFinishLoadSports && mFinishLoadCompetitions) {
            llProgressCompetition.setVisibility(View.INVISIBLE);
            ContentResolver contentResolver = getContentResolver();
            mCursorSports = contentResolver.query(SportsEntry.CONTENT_URI, SportsEntry.PROJECTION, null, null, SportsEntry.COLUMN_ORDER);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            SportsAdapter sportsAdapter = new SportsAdapter(this, this);
            rvSports.setLayoutManager(gridLayoutManager);
            rvSports.setAdapter(sportsAdapter);
            sportsAdapter.swapCursor(mCursorSports);
            rvSports.setVisibility(View.VISIBLE);
        }
    }

    private void startLoadingCompetitions() {
        llProgressCompetition.setVisibility(View.VISIBLE);
        rvSports.setVisibility(View.INVISIBLE);
    }

    private void updateLastUpdateMenuItem(Menu menu) {
        SharedPreferences preferences = getDefaultSharedPreferences(this);
        if (menu != null) {
            String title;
            MenuItem item = menu.findItem(R.id.action_update);
            if (preferences.contains(DeporteLocalConstants.KEY_LASTUPDATE)) {
                Long dateLong = preferences.getLong(DeporteLocalConstants.KEY_LASTUPDATE, 1L);
                DateFormat dateFormat = new SimpleDateFormat(DeporteLocalConstants.DATE_FORMAT);
                title = getString(R.string.action_update, dateFormat.format(new Date(dateLong)));
            } else {
                title = getString(R.string.action_update, "");
            }
            item.setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        Log.d(TAG, "onResume: registerListener");
        getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DeporteLocalConstants.KEY_LASTUPDATE) && sharedPreferences.contains(key)) {
            updateLastUpdateMenuItem(this.mMenu);
        }
    }

    @Override
    public void finishLoadCompetitions() {
        mFinishLoadCompetitions = true;
        endLoadingCompetitions();
    }

    @Override
    public void finishLoadSports() {
        mFinishLoadSports = true;
        endLoadingCompetitions();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (clickedItemIndex == 0) {
            startActivity(new Intent(this, FavoritesActivity.class));
        } else {
            mCursorSports.moveToPosition(clickedItemIndex - 1);
            Sport sport = SportsEntry.initEntity(mCursorSports);
            Intent intent = new Intent(this, SelectCompetitionActivity.class);
            intent.putExtra(DeporteLocalConstants.INTENT_SPORT_TAG, (String) sport.tag());
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().isEmailVerified()) {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        firebaseAuth.getCurrentUser().reload();
                    }
                }
            };
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        try {
            if (broadcastReceiver!=null) {
                unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {
            Log.e(TAG, "onStop: unregistering" + e.getMessage(), e);
        }
    }
}
