package com.adiaz.munisports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.Classification;
import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.CompetitionDetails;
import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.Match;
import com.adiaz.munisports.utilities.MuniSportsConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;

/**
 * Created by toni on 18/08/2017.
 */

public class MatchesCallbak implements Callback<CompetitionDetails> {

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
	public void onResponse(Call<CompetitionDetails> call, Response<CompetitionDetails> response) {
		Log.d(TAG, "onResponse: id " + idCompetitionServer);

		List<Match> matches = response.body().getMatches();
		loadMatches(matches, this.idCompetitionServer, this.mContext);

		List<Classification> classification = response.body().getClassification();
		loadClassification(classification, this.idCompetitionServer, this.mContext);

		Log.d(TAG, "onResponse: finished");
		onFinishLoad.finishLoad();
	}

	private void loadClassification(List<Classification> classificationList, Long idCompetitionServer, Context mContext) {
		List<ContentValues> cvClassificationList = new ArrayList<>();
		for (Classification classification : classificationList) {
			ContentValues cvClassification = new ContentValues();
			cvClassification.put(ClassificationEntry.COLUMN_POSITION, classification.getPosition());
			cvClassification.put(ClassificationEntry.COLUMN_TEAM, classification.getTeam());
			cvClassification.put(ClassificationEntry.COLUMN_POINTS, classification.getPoints());
			cvClassification.put(ClassificationEntry.COLUMN_MATCHES_PLAYED, classification.getMatchesPlayed());
			cvClassification.put(ClassificationEntry.COLUMN_MATCHES_WON, classification.getMatchesWon());
			cvClassification.put(ClassificationEntry.COLUMN_MATCHES_DRAWN, classification.getMatchesDrawn());
			cvClassification.put(ClassificationEntry.COLUMN_MATCHES_LOST, classification.getMatchesLost());
			cvClassification.put(ClassificationEntry.COLUMN_ID_COMPETITION_SERVER, idCompetitionServer);
			cvClassificationList.add(cvClassification);
		}
		ContentValues[] classificationArray = cvClassificationList.toArray(new ContentValues[cvClassificationList.size()]);
		ContentResolver muniSportsContentResolver = mContext.getContentResolver();
		Uri uri = ClassificationEntry.buildClassificationUriWithCompetitions(idCompetitionServer.toString());
		int delete = muniSportsContentResolver.delete(uri, null, null);
		Log.d(TAG, "loadClassification: delete " + delete);
		muniSportsContentResolver.bulkInsert(ClassificationEntry.CONTENT_URI, classificationArray);
	}

	private void loadMatches(List<Match> matchList, Long idCompetitionServer, Context mContext) {
		List<ContentValues> cvMatcheList = new ArrayList<>();
		for (Match match : matchList) {
			ContentValues cvMatch = new ContentValues();
			String teamLocal = match.getTeamLocalEntity()==null ? MuniSportsConstants.UNDEFINDED_FIELD : match.getTeamLocalEntity().getName();
			String teamVisitor = match.getTeamVisitorEntity()==null ? MuniSportsConstants.UNDEFINDED_FIELD : match.getTeamVisitorEntity().getName();
			String sportCenterCourt = match.getSportCenterCourt()==null ? MuniSportsConstants.UNDEFINDED_FIELD : match.getSportCenterCourt().getNameWithCenter();
			cvMatch.put(MatchesEntry.COLUMN_TEAM_LOCAL, teamLocal);
			cvMatch.put(MatchesEntry.COLUMN_TEAM_VISITOR, teamVisitor);
			cvMatch.put(MatchesEntry.COLUMN_SCORE_LOCAL, match.getScoreLocal());
			cvMatch.put(MatchesEntry.COLUMN_SCORE_VISITOR, match.getScoreVisitor());
			cvMatch.put(MatchesEntry.COLUMN_WEEK, match.getWeek());
			cvMatch.put(MatchesEntry.COLUMN_PLACE, sportCenterCourt);
			cvMatch.put(MatchesEntry.COLUMN_DATE, match.getDate());
			cvMatch.put(MatchesEntry.COLUMN_ID_SERVER, match.getId());
			cvMatch.put(MatchesEntry.COLUMN_ID_COMPETITION_SERVER, idCompetitionServer);
			cvMatcheList.add(cvMatch);
		}
		ContentValues[] matchesArray = cvMatcheList.toArray(new ContentValues[cvMatcheList.size()]);
		ContentResolver muniSportsContentResolver = mContext.getContentResolver();
		Uri uri = MatchesEntry.buildMatchesUriWithCompetitions(idCompetitionServer.toString());
		int delete = muniSportsContentResolver.delete(uri, null, null);
		Log.d(TAG, "loadMatches: delete " + delete);
		muniSportsContentResolver.bulkInsert(MatchesEntry.CONTENT_URI, matchesArray);
	}

	@Override
	public void onFailure(Call<CompetitionDetails> call, Throwable t) {
		Log.d(TAG, "onFailure: " + t.getMessage());
	}

	public interface OnFinishLoad {
		public void finishLoad();
	}
}
