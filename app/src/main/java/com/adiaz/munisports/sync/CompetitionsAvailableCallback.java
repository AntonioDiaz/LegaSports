package com.adiaz.munisports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.munisports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.munisports.utilities.MuniSportsConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * Update Competitions table with the content of json receiver from the server.
	 *
	 * @param competitionsList
	 */
	private void loadCompetitions(List<CompetitionRestEntity> competitionsList) {
		List<ContentValues> competitionsContentValues = new ArrayList<>();
		for (CompetitionRestEntity competitionsEntity : competitionsList) {
			ContentValues cv = new ContentValues();
			cv.put(CompetitionsEntry.COLUMN_ID_SERVER, competitionsEntity.getId());
			cv.put(CompetitionsEntry.COLUMN_NAME, competitionsEntity.getName());
			cv.put(CompetitionsEntry.COLUMN_SPORT, competitionsEntity.getSportEntity().getTag());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY, competitionsEntity.getCategoryEntity().getName().toLowerCase());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY_ORDER, competitionsEntity.getCategoryEntity().getOrder());
			cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE_SERVER, competitionsEntity.getLastPublished());
			cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE_LOCAL, 0l);
			competitionsContentValues.add(cv);
		}
		ContentValues[] competitions = competitionsContentValues.toArray(new ContentValues[competitionsContentValues.size()]);
		ContentResolver contentResolver = mContext.getContentResolver();
		contentResolver.delete(CompetitionsEntry.CONTENT_URI, null, null);
		contentResolver.bulkInsert(CompetitionsEntry.CONTENT_URI, competitions);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(MuniSportsConstants.KEY_LASTUPDATE, new Date().getTime());
		editor.commit();
	}

	@Override
	public void onFailure(Call<List<CompetitionRestEntity>> call, Throwable t) {
		Log.e(TAG, "onFailure: " + t.getMessage() , t);
	}

	public interface CompetitionsLoadedCallback {
		void updateActivityLoadedCompetitions();
	}
}
