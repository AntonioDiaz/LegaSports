package com.adiaz.munisports.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.adapters.TownsAdapter;
import com.adiaz.munisports.database.MuniSportsDbContract;
import com.adiaz.munisports.sync.MuniSportsSyncUtils;
import com.adiaz.munisports.sync.TownsAvailableCallback;
import com.adiaz.munisports.sync.retrofit.MuniSportsRestApi;
import com.adiaz.munisports.sync.retrofit.entities.Town;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.NetworkUtilities;

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

public class MainActivity extends AppCompatActivity
		implements TownsAvailableCallback.TownsLoadedCallback, TownsAdapter.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Nullable @BindView(R.id.toolbar) Toolbar toolbar;
	@Nullable @BindView(R.id.tv_title) TextView tvTitle;
	@Nullable @BindView((R.id.layout_activity_main)) View activityView;
	@Nullable @BindView((R.id.layout_activity_splash)) View activitySplash;
	@Nullable @BindView(R.id.ll_progress) LinearLayout llProgress;
	@Nullable @BindView(R.id.rv_towns) RecyclerView rvTowns;
	private List<Town> mTownList;
	private Menu mMenu;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Get town from preferences .*/
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(MuniSportsConstants.TOWN_SELECTED_NAME, null);
		preferences.registerOnSharedPreferenceChangeListener(this);
		Log.d(TAG, "onCreate: " + townSelect);
		if (TextUtils.isEmpty(townSelect)) {
			setContentView(R.layout.activity_splash);
			ButterKnife.bind(this);
			if (NetworkUtilities.isNetworkAvailable(this)) {
				startLoadingTowns();
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(MuniSportsConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				MuniSportsRestApi muniSportsRestApi = retrofit.create(MuniSportsRestApi.class);
				Call<List<Town>> call = muniSportsRestApi.townsQuery();
				call.enqueue(new TownsAvailableCallback(this));
			} else {
				String strError = getString(R.string.internet_required);
				final Snackbar snackbar = Snackbar.make(activitySplash, strError, Snackbar.LENGTH_INDEFINITE);
				snackbar.show();
				llProgress.setVisibility(View.INVISIBLE);
			}
		} else {
			setContentView(R.layout.activity_main);
			ButterKnife.bind(this);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_launcher);
			tvTitle.setText(townSelect + " - " + getString(R.string.app_name));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		this.mMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId==R.id.action_preferences) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		if (itemId==R.id.action_changetown) {
			// TODO: 03/08/2017 ask user confirmation. 
			SharedPreferences.Editor editor = getDefaultSharedPreferences(this).edit();
			editor.remove(MuniSportsConstants.TOWN_SELECTED_NAME);
			editor.remove(MuniSportsConstants.TOWN_SELECTED_ID);
			editor.remove(this.getString(R.string.key_favorites_teams));
			editor.remove(this.getString(R.string.key_favorites_competitions));
			editor.commit();
			finish();
			startActivity(getIntent());
		}
		return super.onOptionsItemSelected(item);
	}


	private void startLoadingTowns() {
		if (llProgress!=null) {
			llProgress.setVisibility(View.VISIBLE);
		}
		if (rvTowns!=null) {
			rvTowns.setVisibility(View.INVISIBLE);
		}
	}

	private void endLoadingTowns() {
		if (llProgress!=null) {
			llProgress.setVisibility(View.INVISIBLE);
		}
		if (rvTowns!=null) {
			rvTowns.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		Log.d(TAG, "onResume: " + preferences.contains(MuniSportsConstants.TOWN_SELECTED_ID));
		if (preferences.contains(MuniSportsConstants.TOWN_SELECTED_ID)) {
			if (NetworkUtilities.isNetworkAvailable(this)) {
				MuniSportsSyncUtils.initialize(this);
			} else {
				Cursor cursor = getContentResolver().query(
						MuniSportsDbContract.CompetitionsEntry.CONTENT_URI, null, null, null, null);
				if (cursor.getCount() == 0) {
					String strError = getString(R.string.internet_required);
					final Snackbar snackbar = Snackbar.make(activityView, strError, Snackbar.LENGTH_LONG);
					snackbar.show();
					// TODO: 26/04/2017 should disabled all link (sports and favorites).
				}
			}
		}
	}

	public void openSport(View view) {
		Intent intent = new Intent(this, SelectCompetitionActivity.class);
		intent.putExtra(MuniSportsConstants.INTENT_SPORT_TAG, (String)view.getTag());
		startActivity(intent);
	}

	public void openSportFootball(View view) {
		startActivity(new Intent(this, FootballActivity.class));
	}

	public void openFavorites(View view) {
		startActivity(new Intent(this, FavoritesActivity.class));
	}

	@Override
	public void updateActivity(List<Town> townList) {
		// TODO: 03/08/2017 show alert when there is no towns.
		TownsAdapter townsAdapter = new TownsAdapter(townList, this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rvTowns.setLayoutManager(linearLayoutManager);
		rvTowns.setAdapter(townsAdapter);
		mTownList = townList;
		endLoadingTowns();
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		Log.d(TAG, "onListItemClick: " + clickedItemIndex);
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(MuniSportsConstants.TOWN_SELECTED_NAME, mTownList.get(clickedItemIndex).getName());
		editor.putLong(MuniSportsConstants.TOWN_SELECTED_ID, mTownList.get(clickedItemIndex).getId());
		editor.commit();
		finish();
		startActivity(getIntent());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, "onSharedPreferenceChanged: key" + key);
		if (key.equals(MuniSportsConstants.KEY_LASTUPDATE) && sharedPreferences.contains(key)) {
			long dateLong = sharedPreferences.getLong(key, 0l);
			MenuItem item = this.mMenu.findItem(R.id.action_lastupdate);
			String lastUpdateTitle = getString(R.string.action_lastupdate);
			DateFormat dateFormat = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
			item.setTitle(lastUpdateTitle + " " + dateFormat.format(new Date(dateLong)));
		}
	}
}
