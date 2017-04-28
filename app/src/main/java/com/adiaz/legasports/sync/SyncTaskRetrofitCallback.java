package com.adiaz.legasports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;


import com.adiaz.legasports.sync.retrofit.entities.CompetitionRestEntity;
import com.adiaz.legasports.sync.retrofit.entities.MatchRestEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.legasports.database.LegaSportsDbContract.CompetitionsEntry;
import static com.adiaz.legasports.database.LegaSportsDbContract.MatchesEntry;



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
		for (CompetitionRestEntity competitionsEntity : response.body()) {
			ContentValues cv = new ContentValues();
			cv.put(CompetitionsEntry.COLUMN_ID_SERVER, competitionsEntity.getId());
			cv.put(CompetitionsEntry.COLUMN_NAME, competitionsEntity.getName());
			cv.put(CompetitionsEntry.COLUMN_SPORT, competitionsEntity.getSportEntity().getName());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY, competitionsEntity.getCategoryEntity().getName().toLowerCase());
			cv.put(CompetitionsEntry.COLUMN_CATEGORY_ORDER, competitionsEntity.getCategoryEntity().getOrder());
			cv.put(CompetitionsEntry.COLUMN_LAST_UPDATE, new Date().toString());
			newsCompetitions.add(cv);
			Log.d(TAG, "onResponse: for ->" + competitionsEntity.getId() + " there are " + competitionsEntity.getMatches().size());
			for (MatchRestEntity match : competitionsEntity.getMatches()) {
				ContentValues cvMatch = new ContentValues();
				cvMatch.put(MatchesEntry.COLUMN_LAST_UPDATE, new Date().toString());
				cvMatch.put(MatchesEntry.COLUMN_TEAM_LOCAL, match.getTeamLocal());
				cvMatch.put(MatchesEntry.COLUMN_TEAM_VISITOR, match.getTeamVisitor());
				cvMatch.put(MatchesEntry.COLUMN_SCORE_LOCAL, match.getScoreLocal());
				cvMatch.put(MatchesEntry.COLUMN_SCORE_VISITOR, match.getScoreVisitor());
				cvMatch.put(MatchesEntry.COLUMN_WEEK, match.getWeek());
				cvMatch.put(MatchesEntry.COLUMN_PLACE, match.getPlace());
				cvMatch.put(MatchesEntry.COLUMN_DATE, match.getDate());
				cvMatch.put(MatchesEntry.COLUMN_ID_SERVER, match.getId());
				cvMatch.put(MatchesEntry.COLUMN_ID_COMPETITION_SERVER, competitionsEntity.getId());
				newsMatches.add(cvMatch);
			}
		}
		ContentResolver legaSportContentResolver = mContext.getContentResolver();
		ContentValues[] competitions = newsCompetitions.toArray(new ContentValues[newsCompetitions.size()]);
		ContentValues[] matches = newsMatches.toArray(new ContentValues[newsMatches.size()]);
		legaSportContentResolver.bulkInsert(CompetitionsEntry.CONTENT_URI, competitions);
		legaSportContentResolver.bulkInsert(MatchesEntry.CONTENT_URI, matches);
		Log.d(TAG, "onResponse: finished");
	}

	@Override
	public void onFailure(Call<List<CompetitionRestEntity>> call, Throwable t) {
		Log.d(TAG, "onFailure: " + t.getMessage());
		t.printStackTrace();
	}
}
