package com.adiaz.munisports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.munisports.entities.Competition;
import com.adiaz.munisports.entities.Favorite;
import com.adiaz.munisports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.munisports.sync.retrofit.entities.competition.TeamsDeref;
import com.adiaz.munisports.utilities.CompetitionDbUtils;
import com.adiaz.munisports.utilities.FavoritesUtils;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.MuniSportsUtils;
import com.adiaz.munisports.utilities.NotificationUtils;

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
		Map<Long, Competition> mapCompetitions = new HashMap<>();
		Cursor cursor = contentResolver.query(CompetitionsEntry.CONTENT_URI, CompetitionsEntry.PROJECTION, null, null, null);
		try {
			while (cursor.moveToNext()) {
				Competition competition = CompetitionsEntry.initEntity(cursor);
				mapCompetitions.put(competition.serverId(), competition);
			}
		} finally {
			cursor.close();
		}
		List<ContentValues> competitionsContentValues = new ArrayList<>();
		for (CompetitionRestEntity competitionsEntity : competitionsList) {
			ContentValues cv = new ContentValues();
			Long idCompetition = competitionsEntity.getId();
			Long lastPublishedApp = -1L;
			Long lastNofitication = -1L;
			if (mapCompetitions.containsKey(idCompetition)) {
				lastPublishedApp = mapCompetitions.get(idCompetition).lastUpdateApp();
			}
			cv.put(CompetitionsEntry.COLUMN_ID_SERVER, idCompetition);
			cv.put(CompetitionsEntry.COLUMN_NAME, competitionsEntity.getName());
			cv.put(CompetitionsEntry.COLUMN_SPORT, competitionsEntity.getSportEntity().getTag());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY, competitionsEntity.getCategoryEntity().getName().toLowerCase());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY_ORDER, competitionsEntity.getCategoryEntity().getOrder());
			cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE_SERVER, competitionsEntity.getLastPublished());
			cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE_APP, lastPublishedApp);

			/* check if it is necessary to show notification: if any of the teams affected is on favorites. */
			List<TeamsDeref> teamsAffected = competitionsEntity.getTeamsAffectedByLastUpdateDeref();
			for (TeamsDeref teamsDeref : teamsAffected) {
				Favorite favorite = FavoritesUtils.queryFavoriteTeam(mContext, idCompetition, teamsDeref.getName());
				if (favorite!=null && favorite.getLastNotification()<competitionsEntity.getLastPublished()) {
					Competition competition = CompetitionDbUtils.queryCompetition(mContext.getContentResolver(), idCompetition);
					if (competition!=null) {
						showNotificationTeam(competition, favorite);
					}
				}
			}
			competitionsContentValues.add(cv);
		}

		ContentValues[] competitions = competitionsContentValues.toArray(new ContentValues[competitionsContentValues.size()]);
		contentResolver.delete(CompetitionsEntry.CONTENT_URI, null, null);
		contentResolver.bulkInsert(CompetitionsEntry.CONTENT_URI, competitions);
		List<Favorite> competitionsFavorites = FavoritesUtils.queryFavoritesCompetitions(mContext);
		for (Favorite fav : competitionsFavorites) {
			Competition competition = CompetitionDbUtils.queryCompetition(mContext.getContentResolver(), fav.getIdCompetition());
			if (fav.getLastNotification() < competition.lastUpdateApp()) {
				showNotificationCompetition(competition, fav);
			}
		}
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(MuniSportsConstants.KEY_LASTUPDATE, new Date().getTime());
		editor.commit();
	}

	private void showNotificationTeam(Competition competition, Favorite favorite) {
		if (MuniSportsUtils.isShowNotification(mContext)) {
			NotificationUtils.remindUpdatedTeam(mContext, competition, favorite.getTeamName());
		}
		FavoritesUtils.updateLastNotification(mContext.getContentResolver(), favorite.getId(), competition.lastUpdateServer());
	}

	private void showNotificationCompetition(Competition competition, Favorite favorite) {
		if (MuniSportsUtils.isShowNotification(mContext)) {
			NotificationUtils.remindUpdatedCompetition(mContext, competition);
		}
		//disable notification for this favorite.
		FavoritesUtils.updateLastNotification(mContext.getContentResolver(), favorite.getId(), competition.lastUpdateServer());
	}

	@Override
	public void onFailure(Call<List<CompetitionRestEntity>> call, Throwable t) {
		Log.e(TAG, "onFailure: " + t.getMessage() , t);
	}

	public interface CompetitionsLoadedCallback {
		void updateActivityLoadedCompetitions();
	}
}
