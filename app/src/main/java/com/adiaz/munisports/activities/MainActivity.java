package com.adiaz.munisports.activities;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.adapters.TownsAdapter;
import com.adiaz.munisports.sync.CompetitionsAvailableCallback;
import com.adiaz.munisports.sync.TownsAvailableCallback;
import com.adiaz.munisports.sync.retrofit.MuniSportsRestApi;
import com.adiaz.munisports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.munisports.sync.retrofit.entities.town.TownRestEntity;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.NetworkUtilities;
import com.adiaz.munisports.utilities.Utils;

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
import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;


public class MainActivity extends AppCompatActivity
		implements
			TownsAvailableCallback.TownsLoadedCallback,
			TownsAdapter.ListItemClickListener,
			SharedPreferences.OnSharedPreferenceChangeListener,
			CompetitionsAvailableCallback.CompetitionsLoadedCallback {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Nullable @BindView(R.id.toolbar) Toolbar toolbar;
	@Nullable @BindView(R.id.tv_title) TextView tvTitle;
	@Nullable @BindView((R.id.layout_activity_main)) View activityView;
	@Nullable @BindView((R.id.layout_activity_splash)) View activitySplash;
	@Nullable @BindView(R.id.ll_progress) LinearLayout llProgress;
	@Nullable @BindView(R.id.rv_towns) RecyclerView rvTowns;
	@Nullable @BindView(R.id.ll_progress_competitions) LinearLayout llProgressCompetition;
	@Nullable @BindView(R.id.gl_sports)	GridLayout glSports;
	private List<TownRestEntity> mTownRestEntityList;
	private Menu mMenu;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Get town from preferences .*/
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(this);
		if (!preferences.contains(MuniSportsConstants.KEY_TOWN_NAME)) {
			setContentView(R.layout.activity_splash);
			ButterKnife.bind(this);
			if (NetworkUtilities.isNetworkAvailable(this)) {
				startLoadingTowns();
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(MuniSportsConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				MuniSportsRestApi muniSportsRestApi = retrofit.create(MuniSportsRestApi.class);
				Call<List<TownRestEntity>> call = muniSportsRestApi.townsQuery();
				call.enqueue(new TownsAvailableCallback(this));
			} else {
				Utils.showNoInternetAlert(this, activitySplash);
				llProgress.setVisibility(View.INVISIBLE);
			}
		} else {
			setContentView(R.layout.activity_main);
			ButterKnife.bind(this);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_launcher);
			String townSelect = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
			Long idTownSelect = preferences.getLong(MuniSportsConstants.KEY_TOWN_ID, -1L);
			tvTitle.setText(townSelect + " - " + getString(R.string.app_name));
			if (!preferences.contains(MuniSportsConstants.KEY_LASTUPDATE)){
				if (NetworkUtilities.isNetworkAvailable(this)) {
					startLoadingCompetitions();
					Retrofit retrofit = new Retrofit.Builder()
							.baseUrl(MuniSportsConstants.BASE_URL)
							.addConverterFactory(GsonConverterFactory.create())
							.build();
					MuniSportsRestApi muniSportsRestApi = retrofit.create(MuniSportsRestApi.class);
					Call<List<CompetitionRestEntity>> call = muniSportsRestApi.competitionsQuery(idTownSelect);
					call.enqueue(new CompetitionsAvailableCallback(this, this));
				} else {
					Utils.showNoInternetAlert(this, activityView);
					llProgressCompetition.setVisibility(View.INVISIBLE);
				}
			} else {
				endLoadingCompetitions();
			}
		}
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
		if (itemId==R.id.action_preferences) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		if (itemId==R.id.action_changetown) {
			// TODO: 03/08/2017 ask user confirmation.
			/* cleaning preferences. */
			SharedPreferences.Editor editor = getDefaultSharedPreferences(this).edit();
			editor.remove(MuniSportsConstants.KEY_TOWN_NAME);
			editor.remove(MuniSportsConstants.KEY_TOWN_ID);
			editor.remove(this.getString(R.string.key_favorites_teams));
			editor.remove(this.getString(R.string.key_favorites_competitions));
			editor.remove(MuniSportsConstants.KEY_LASTUPDATE);
			editor.commit();
			/* cleaning database. */
			ContentResolver contentResolver = this.getContentResolver();
			contentResolver.delete(CompetitionsEntry.CONTENT_URI, null, null);
			contentResolver.delete(MatchesEntry.CONTENT_URI, null, null);
			contentResolver.delete(ClassificationEntry.CONTENT_URI, null, null);
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

		/*SharedPreferences preferences = getDefaultSharedPreferences(this);
		if (preferences.contains(MuniSportsConstants.KEY_TOWN_ID)) {
			if (NetworkUtilities.isNetworkAvailable(this)) {
				MuniSportsSyncUtils.initializeSimple(this);
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
		}*/
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
		editor.putString(MuniSportsConstants.KEY_TOWN_NAME, mTownRestEntityList.get(clickedItemIndex).getName());
		editor.putLong(MuniSportsConstants.KEY_TOWN_ID, mTownRestEntityList.get(clickedItemIndex).getId());
		editor.commit();
		finish();
		startActivity(getIntent());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(MuniSportsConstants.KEY_LASTUPDATE) && sharedPreferences.contains(key)) {
			updateLastUpdateMenuItem(this.mMenu);
		}
	}

	private void updateLastUpdateMenuItem(Menu menu) {
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		if (preferences.contains(MuniSportsConstants.KEY_LASTUPDATE)) {
			Long dateLong = preferences.getLong(MuniSportsConstants.KEY_LASTUPDATE, 1L);
			String lastUpdateTitle = getString(R.string.action_lastupdate);
			DateFormat dateFormat = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
			String title = lastUpdateTitle + " " + dateFormat.format(new Date(dateLong));
			if (menu!=null) {
				MenuItem item = menu.findItem(R.id.action_lastupdate);
				item.setTitle(title);
			}
		}
	}

	@Override
	public void updateActivityLoadedCompetitions() {
		endLoadingCompetitions();
	}
}
