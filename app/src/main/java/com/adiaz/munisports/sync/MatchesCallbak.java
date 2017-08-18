package com.adiaz.munisports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.adiaz.munisports.sync.retrofit.entities.match.MatchRestEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;

/**
 * Created by toni on 18/08/2017.
 */

public class MatchesCallbak implements Callback<List<MatchRestEntity>> {

	private static final String TAG = MatchesCallbak.class.getSimpleName();
	private Context mContext;
	private Long idCompetitionServer;
	private OnFinishLoad onFinishLoad;

	public MatchesCallbak(Context mContext, Long idCompetitionServer, OnFinishLoad onFinishLoad) {
		this.mContext = mContext;
		this.idCompetitionServer = idCompetitionServer;
		this.onFinishLoad = onFinishLoad;
	}

	@Override
	public void onResponse(Call<List<MatchRestEntity>> call, Response<List<MatchRestEntity>> response) {
		Log.d(TAG, "onResponse: id " + idCompetitionServer);
		List<ContentValues> cvMatcheList = new ArrayList<>();
		for (MatchRestEntity match : response.body()) {
			ContentValues cvMatch = new ContentValues();
			cvMatch.put(MatchesEntry.COLUMN_TEAM_LOCAL, match.getTeamLocalEntity().getName());
			cvMatch.put(MatchesEntry.COLUMN_TEAM_VISITOR, match.getTeamVisitorEntity().getName());
			cvMatch.put(MatchesEntry.COLUMN_SCORE_LOCAL, match.getScoreLocal());
			cvMatch.put(MatchesEntry.COLUMN_SCORE_VISITOR, match.getScoreVisitor());
			cvMatch.put(MatchesEntry.COLUMN_WEEK, match.getWeek());
			cvMatch.put(MatchesEntry.COLUMN_PLACE, match.getSportCenterCourt().getNameWithCenter());
			cvMatch.put(MatchesEntry.COLUMN_DATE, match.getDate());
			cvMatch.put(MatchesEntry.COLUMN_ID_SERVER, match.getId());
			cvMatch.put(MatchesEntry.COLUMN_ID_COMPETITION_SERVER, idCompetitionServer);
			cvMatcheList.add(cvMatch);
		}
		ContentValues[] matches = cvMatcheList.toArray(new ContentValues[cvMatcheList.size()]);
		ContentResolver muniSportsContentResolver = mContext.getContentResolver();
		muniSportsContentResolver.bulkInsert(MatchesEntry.CONTENT_URI, matches);
		Log.d(TAG, "onResponse: finished matches.length " + matches.length);
		onFinishLoad.finishLoad();

	}

	@Override
	public void onFailure(Call<List<MatchRestEntity>> call, Throwable t) {
		Log.d(TAG, "onFailure: " + t.getMessage());
	}

	public interface OnFinishLoad {
		public void finishLoad();
	}
}
