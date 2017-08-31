package com.adiaz.munisports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.munisports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.MuniSportsUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;

/**
 * Created by toni on 16/08/2017.
 */

public class CompetitionsAvailableCallback implements Callback<List<CompetitionRestEntity>> {

	private static final String TAG = CompetitionsAvailableCallback.class.getSimpleName();

	private Context mContext;
	private CompetitionsLoadedCallback competitionsLoadedCallback;

	public CompetitionsAvailableCallback(Context mContext, CompetitionsLoadedCallback competitionsLoadedCallback) {
		this.mContext = mContext;
		this.competitionsLoadedCallback = competitionsLoadedCallback;
	}

	public CompetitionsAvailableCallback(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onResponse(Call<List<CompetitionRestEntity>> call, Response<List<CompetitionRestEntity>> response) {
		loadCompetitions(response.body());
		if (this.competitionsLoadedCallback!=null) {
			competitionsLoadedCallback.updateActivityLoadedCompetitions();
		}
	}

	/**
	 * Update Competitions table with the content of json received from the server.
	 * Check if there are any new publication in any already downloaded competition it is necessary
	 * to update and show a notification only for favorites.
	 *
	 * @param competitionsList
	 */
	private void loadCompetitions(List<CompetitionRestEntity> competitionsList) {
		ContentResolver contentResolver = mContext.getContentResolver();
		Map<Long, Long> mapCompetitions = new HashMap<>();
		String[] projection = new String[]{CompetitionsEntry.COLUMN_ID_SERVER, CompetitionsEntry.COLUMN_LAST_UPDATE_APP};
		Cursor cursor = contentResolver.query(CompetitionsEntry.CONTENT_URI, projection, null, null, null);
		try {
			while (cursor.moveToNext()) {
				long id = cursor.getLong(0);
				long lastPublishedOnDevice = cursor.getLong(1);
				mapCompetitions.put(id, lastPublishedOnDevice);
			}
		} finally {
			cursor.close();
		}
		List<ContentValues> competitionsContentValues = new ArrayList<>();
		List<String> competitionsFavs = MuniSportsUtils.getFavorites(mContext, MuniSportsConstants.KEY_FAVORITES_COMPETITIONS);
		for (CompetitionRestEntity competitionsEntity : competitionsList) {
			ContentValues cv = new ContentValues();
			Long idCompetition = competitionsEntity.getId();
			Long lastPublishedApp = -1L;
			if (mapCompetitions.containsKey(idCompetition)) {
				lastPublishedApp = mapCompetitions.get(idCompetition);
			}
			cv.put(CompetitionsEntry.COLUMN_ID_SERVER, idCompetition);
			cv.put(CompetitionsEntry.COLUMN_NAME, competitionsEntity.getName());
			cv.put(CompetitionsEntry.COLUMN_SPORT, competitionsEntity.getSportEntity().getTag());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY, competitionsEntity.getCategoryEntity().getName().toLowerCase());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY_ORDER, competitionsEntity.getCategoryEntity().getOrder());
			cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE_SERVER, competitionsEntity.getLastPublished());
			cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE_APP, lastPublishedApp);
			competitionsContentValues.add(cv);
			if (competitionsFavs.contains(idCompetition.toString())) {
				if (lastPublishedApp < competitionsEntity.getLastPublished()) {
					Log.d(TAG, "loadCompetitions: Show notification!!! " + idCompetition);
					showNotification(idCompetition);
				} else {
					Log.d(TAG, "loadCompetitions: update not necessary. ");

				}
			}
/*			if (competitionsFavs.contains(competitionsEntity.getId().toString())) {

				Uri uriCompetition = CompetitionsEntry.buildCompetitionUriWithServerId(Long.parseLong(idCompetitionServer));
				String[] projection = {	CompetitionsEntry.COLUMN_LAST_UPDATE_SERVER };
				Cursor cursor = contentResolver.query(uriCompetition, projection, null, null, null);
				if (cursor.getCount()>0) {
					cursor.moveToFirst();
					if (cursor.getLong(0) < competitionsEntity.getLastPublished()) {
						Log.d(TAG, "loadCompetitions: update competition it is necessary " + idCompetitionServer);
					} else {
						Log.d(TAG, "loadCompetitions: no update");
					}
				}
				cursor.close();

			}*/

		}

		ContentValues[] competitions = competitionsContentValues.toArray(new ContentValues[competitionsContentValues.size()]);
		contentResolver.delete(CompetitionsEntry.CONTENT_URI, null, null);
		contentResolver.bulkInsert(CompetitionsEntry.CONTENT_URI, competitions);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(MuniSportsConstants.KEY_LASTUPDATE, new Date().getTime());
		editor.commit();
	}

	private void showNotification(Long idCompetition) {

	}

	@Override
	public void onFailure(Call<List<CompetitionRestEntity>> call, Throwable t) {
		Log.e(TAG, "onFailure: " + t.getMessage() , t);
	}

	public interface CompetitionsLoadedCallback {
		void updateActivityLoadedCompetitions();
	}
}
