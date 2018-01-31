package com.adiaz.deportelocal.utilities.retrofit.callbacks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.deportelocal.utilities.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.deportelocal.database.DeporteLocalDbContract.CompetitionsEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.MatchesEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.ClassificationEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.SportCourtsEntry;

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
		if (response.body()!=null) {
			loadCompetitions(response.body());
			if (this.competitionsLoadedCallback!=null) {
				competitionsLoadedCallback.finishLoadCompetitions();
			}
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
		ContentValues[] competitions = new ContentValues[competitionsList.size()];
		for (int i = 0; i < competitionsList.size(); i++) {
			ContentValues cv = new ContentValues();
			CompetitionRestEntity competitionEntity = competitionsList.get(i);
			cv.put(CompetitionsEntry.COLUMN_ID_SERVER, competitionEntity.getId());
			cv.put(CompetitionsEntry.COLUMN_NAME, competitionEntity.getName());
			cv.put(CompetitionsEntry.COLUMN_SPORT, competitionEntity.getSportEntity().getTag());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY, competitionEntity.getCategoryEntity().getName());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY_ORDER, competitionEntity.getCategoryEntity().getOrder());
			cv.put(CompetitionsEntry.COLUMN_IS_DIRTY, 1);
			competitions[i] = cv;
		}
		ContentResolver contentResolver = mContext.getContentResolver();
		contentResolver.delete(MatchesEntry.CONTENT_URI, null, null);
		contentResolver.delete(SportCourtsEntry.CONTENT_URI, null, null);
		contentResolver.delete(ClassificationEntry.CONTENT_URI, null, null);
		contentResolver.delete(CompetitionsEntry.CONTENT_URI, null, null);
		contentResolver.bulkInsert(CompetitionsEntry.CONTENT_URI, competitions);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(DeporteLocalConstants.KEY_LASTUPDATE, new Date().getTime());
		editor.apply();
	}

	@Override
	public void onFailure(Call<List<CompetitionRestEntity>> call, Throwable t) {
		Log.e(TAG, "onFailure: " + t.getMessage() , t);
	}

	public interface CompetitionsLoadedCallback {
		void finishLoadCompetitions();
	}
}
