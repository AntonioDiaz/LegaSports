package com.adiaz.localsports.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
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

import com.adiaz.localsports.R;
import com.adiaz.localsports.adapters.TownsAdapter;
import com.adiaz.localsports.sync.CompetitionsAvailableCallback;
import com.adiaz.localsports.sync.LocalSportsSyncUtils;
import com.adiaz.localsports.sync.TownsAvailableCallback;
import com.adiaz.localsports.sync.retrofit.LocalSportsRestApi;
import com.adiaz.localsports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.localsports.sync.retrofit.entities.town.TownRestEntity;
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
import static com.adiaz.localsports.database.LocalSportsDbContract.ClassificationEntry;
import static com.adiaz.localsports.database.LocalSportsDbContract.CompetitionsEntry;
import static com.adiaz.localsports.database.LocalSportsDbContract.FavoritesEntry;
import static com.adiaz.localsports.database.LocalSportsDbContract.MatchesEntry;
import static com.adiaz.localsports.database.LocalSportsDbContract.SportCourtsEntry;


public class MainActivity extends AppCompatActivity
		implements
		TownsAvailableCallback.TownsLoadedCallback,
		TownsAdapter.ListItemClickListener,
		SharedPreferences.OnSharedPreferenceChangeListener,
		CompetitionsAvailableCallback.CompetitionsLoadedCallback {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Nullable
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@Nullable
	@BindView(R.id.tv_title)
	TextView tvTitle;
	@Nullable
	@BindView((R.id.layout_activity_main))
	View activityView;
	@Nullable
	@BindView((R.id.layout_activity_splash))
	View activitySplash;
	@Nullable
	@BindView(R.id.ll_progress)
	LinearLayout llProgress;
	@Nullable
	@BindView(R.id.rv_towns)
	RecyclerView rvTowns;
	@Nullable
	@BindView(R.id.ll_progress_competitions)
	LinearLayout llProgressCompetition;
	@Nullable
	@BindView(R.id.gl_sports)
	GridLayout glSports;
	private List<TownRestEntity> mTownRestEntityList;
	private Menu mMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Get town from preferences .*/
		getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		String town = PreferencesUtils.queryPreferenceTown(this);
		if (TextUtils.isEmpty(town)) {
			setContentView(R.layout.activity_splash);
			ButterKnife.bind(this);
			if (NetworkUtilities.isNetworkAvailable(this)) {
				startLoadingTowns();
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(LocalSportsConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				LocalSportsRestApi localSportsRestApi = retrofit.create(LocalSportsRestApi.class);
				Call<List<TownRestEntity>> call = localSportsRestApi.townsQuery();
				call.enqueue(new TownsAvailableCallback(this));
			} else {
				LocalSportsUtils.showNoInternetAlert(this, activitySplash);
				llProgress.setVisibility(View.INVISIBLE);
			}
		} else {
			setContentView(R.layout.activity_main);
			ButterKnife.bind(this);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.mipmap.ic_launcher);
			tvTitle.setText(town + " - " + getString(R.string.app_name));
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
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
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
				contentResolver.delete(CompetitionsEntry.CONTENT_URI, null, null);
				contentResolver.delete(MatchesEntry.CONTENT_URI, null, null);
				contentResolver.delete(ClassificationEntry.CONTENT_URI, null, null);
				contentResolver.delete(SportCourtsEntry.CONTENT_URI, null, null);
				contentResolver.delete(FavoritesEntry.CONTENT_URI, null, null);
				/* stop the FirebaseJob. */
				LocalSportsSyncUtils.stopJob(this);
				finish();
				startActivity(getIntent());
				break;
			}
		return super.onOptionsItemSelected(item);
	}


	private void startLoadingTowns() {
		if (llProgress != null) {
			llProgress.setVisibility(View.VISIBLE);
		}
		if (rvTowns != null) {
			rvTowns.setVisibility(View.INVISIBLE);
		}

	}

	private void endLoadingTowns() {
		if (llProgress != null) {
			llProgress.setVisibility(View.INVISIBLE);
		}
		if (rvTowns != null) {
			rvTowns.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (PreferencesUtils.queryPreferenceTownId(this)!=null && NetworkUtilities.isNetworkAvailable(this)) {
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
	public void updateActivity(List<TownRestEntity> townRestEntityList) {
		// TODO: 03/08/2017 show alert when there is no towns.
		TownsAdapter townsAdapter = new TownsAdapter(townRestEntityList, this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rvTowns.setLayoutManager(linearLayoutManager);
		rvTowns.setAdapter(townsAdapter);
		mTownRestEntityList = townRestEntityList;
		endLoadingTowns();
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(LocalSportsConstants.KEY_TOWN_NAME, mTownRestEntityList.get(clickedItemIndex).getName());
		editor.putLong(LocalSportsConstants.KEY_TOWN_ID, mTownRestEntityList.get(clickedItemIndex).getId());
		editor.commit();
		finish();
		startActivity(getIntent());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, "onSharedPreferenceChanged: should update key " + key);
		if (key.equals(LocalSportsConstants.KEY_LASTUPDATE) && sharedPreferences.contains(key)) {
			updateLastUpdateMenuItem(this.mMenu);
		}
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
}
