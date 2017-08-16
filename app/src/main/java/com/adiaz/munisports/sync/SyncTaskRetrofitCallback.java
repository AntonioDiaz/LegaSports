package com.adiaz.munisports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.munisports.sync.retrofit.entities.Classification;
import com.adiaz.munisports.sync.retrofit.entities.CompetitionRestEntity;
import com.adiaz.munisports.sync.retrofit.entities.Match;
import com.adiaz.munisports.utilities.MuniSportsConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;



/**
 * Created by toni on 21/04/2017.
 */

public class SyncTaskRetrofitCallback implements Callback<List<CompetitionRestEntity>> {

	private static final String TAG = SyncTaskRetrofitCallback.class.getSimpleName();

	private Context mContext;


	public SyncTaskRetrofitCallback(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onResponse(Call<List<CompetitionRestEntity>> call, Response<List<CompetitionRestEntity>> response) {
		List<ContentValues> newsCompetitions = new ArrayList<>();
		List<ContentValues> newsMatches = new ArrayList<>();
		List<ContentValues> classificationList = new ArrayList<>();
		try {
			for (CompetitionRestEntity competitionsEntity : response.body()) {
				ContentValues cv = new ContentValues();
				cv.put(CompetitionsEntry.COLUMN_ID_SERVER, competitionsEntity.getId());
				cv.put(CompetitionsEntry.COLUMN_NAME, competitionsEntity.getName());
				cv.put(CompetitionsEntry.COLUMN_SPORT, competitionsEntity.getSportEntity().getName().toUpperCase());
				cv.put(CompetitionsEntry.COLUMN_CATEGORY, competitionsEntity.getCategoryEntity().getName().toLowerCase());
				cv.put(CompetitionsEntry.COLUMN_CATEGORY_ORDER, competitionsEntity.getCategoryEntity().getOrder());
				cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE, new Date().toString());
				newsCompetitions.add(cv);
				Log.d(TAG, "onResponse: competition" + cv);
				for (Match match : competitionsEntity.getMatches()) {
					ContentValues cvMatch = new ContentValues();
					cvMatch.put(MatchesEntry.COLUMN_LAST_UPDATE, new Date().toString());
					cvMatch.put(MatchesEntry.COLUMN_TEAM_LOCAL, match.getTeamLocalEntity().getName());
					cvMatch.put(MatchesEntry.COLUMN_TEAM_VISITOR, match.getTeamVisitorEntity().getName());
					cvMatch.put(MatchesEntry.COLUMN_SCORE_LOCAL, match.getScoreLocal());
					cvMatch.put(MatchesEntry.COLUMN_SCORE_VISITOR, match.getScoreVisitor());
					cvMatch.put(MatchesEntry.COLUMN_WEEK, match.getWeek());
					cvMatch.put(MatchesEntry.COLUMN_PLACE, match.getSportCenterCourt().getNameWithCenter());
					cvMatch.put(MatchesEntry.COLUMN_DATE, match.getDate());
					cvMatch.put(MatchesEntry.COLUMN_ID_SERVER, match.getId());
					cvMatch.put(MatchesEntry.COLUMN_ID_COMPETITION_SERVER, competitionsEntity.getId());
					newsMatches.add(cvMatch);
					Log.d(TAG, "onResponse: match" + cvMatch);
				}
				for (Classification classification : competitionsEntity.getClassification()) {
					ContentValues cvClassification = new ContentValues();
					cvClassification.put(ClassificationEntry.COLUMN_POSITION, classification.getPosition());
					cvClassification.put(ClassificationEntry.COLUMN_TEAM, classification.getTeam());
					cvClassification.put(ClassificationEntry.COLUMN_POINTS, classification.getPoints());
					cvClassification.put(ClassificationEntry.COLUMN_MATCHES_PLAYED, classification.getMatchesPlayed());
					cvClassification.put(ClassificationEntry.COLUMN_MATCHES_WON, classification.getMatchesWon());
					cvClassification.put(ClassificationEntry.COLUMN_MATCHES_DRAWN, classification.getMatchesDrawn());
					cvClassification.put(ClassificationEntry.COLUMN_MATCHES_LOST, classification.getMatchesLost());
					cvClassification.put(ClassificationEntry.COLUMN_ID_COMPETITION_SERVER, competitionsEntity.getId());
					classificationList.add(cvClassification);
				}
			}
			ContentResolver muniSportsContentResolver = mContext.getContentResolver();
			ContentValues[] competitions = newsCompetitions.toArray(new ContentValues[newsCompetitions.size()]);
			ContentValues[] matches = newsMatches.toArray(new ContentValues[newsMatches.size()]);
			ContentValues[] classification = classificationList.toArray(new ContentValues[classificationList.size()]);
			muniSportsContentResolver.bulkInsert(CompetitionsEntry.CONTENT_URI, competitions);
			muniSportsContentResolver.bulkInsert(MatchesEntry.CONTENT_URI, matches);
			muniSportsContentResolver.bulkInsert(ClassificationEntry.CONTENT_URI, classification);
			Log.d(TAG, "onResponse: finished classification.length " + classification.length);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong(MuniSportsConstants.KEY_LASTUPDATE, new Date().getTime());
			editor.commit();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "onResponse: " + e.getMessage(), e);
		}
	}

	@Override
	public void onFailure(Call<List<CompetitionRestEntity>> call, Throwable t) {
		Log.d(TAG, "onFailure: " + t.getMessage());
		t.printStackTrace();
	}
}
