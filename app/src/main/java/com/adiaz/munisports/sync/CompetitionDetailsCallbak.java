package com.adiaz.munisports.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.adiaz.munisports.database.MuniSportsDbContract;
import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.Classification;
import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.CompetitionDetails;
import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.Match;
import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.SportCenterCourt;
import com.adiaz.munisports.utilities.MuniSportsConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;

/**
 * Created by toni on 18/08/2017.
 */

public class CompetitionDetailsCallbak implements Callback<CompetitionDetails> {

	private static final String TAG = CompetitionDetailsCallbak.class.getSimpleName();
	private Context mContext;
	private Long idCompetitionServer;
	private OnFinishLoad onFinishLoad;

	public CompetitionDetailsCallbak(Context mContext, Long idCompetitionServer, OnFinishLoad onFinishLoad) {
		this.mContext = mContext;
		this.idCompetitionServer = idCompetitionServer;
		this.onFinishLoad = onFinishLoad;
	}

	@Override
	public void onResponse(Call<CompetitionDetails> call, Response<CompetitionDetails> response) {
		ContentResolver contentResolver = mContext.getContentResolver();
		/* check if there has been any change since last update.  */
		Uri uri = CompetitionsEntry.buildCompetitionUriWithServerId(this.idCompetitionServer);
		Cursor query = contentResolver.query(uri, CompetitionsEntry.PROJECTION, null, null, null);
		query.moveToFirst();
		long lastPublishedServerInDb = query.getLong(CompetitionsEntry.INDEX_LAST_UDPATE_SERVER);
		long lastPublishedServerInServer = response.body().getLastPublished();
		query.close();
		Uri uriMatches = MatchesEntry.buildMatchesUriWithCompetitions(this.idCompetitionServer.toString());
		Cursor queryMatches = contentResolver.query(uriMatches, MatchesEntry.PROJECTION, null, null, null);
		if (lastPublishedServerInDb<lastPublishedServerInServer || queryMatches.getCount()==0) {
			List<Match> matches = response.body().getMatches();
			loadMatches(matches, this.idCompetitionServer, this.mContext);
			List<Classification> classification = response.body().getClassification();
			loadClassification(classification, this.idCompetitionServer, this.mContext);
			loadSportCourts(matches, this.mContext);
		}
		/* Updating local server update date. */
		long timeInMillis = Calendar.getInstance().getTimeInMillis();
		Uri uriCompetitionWithTime = CompetitionsEntry.buildCompetitionUriWithServerIdAndTime(idCompetitionServer, timeInMillis);
		contentResolver.update(uriCompetitionWithTime, null, null, null);
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
		muniSportsContentResolver.delete(uri, null, null);
		muniSportsContentResolver.bulkInsert(ClassificationEntry.CONTENT_URI, classificationArray);
	}

	private void loadSportCourts(List<Match> matchList, Context mContext) {
		List<ContentValues> cvSportCourtsList = new ArrayList<>();
		Set<Long> sportCourtsIdInserted = new HashSet<>();
		for (Match match : matchList) {
			SportCenterCourt court = match.getSportCenterCourt();
			if (court !=null && !sportCourtsIdInserted.contains(court.getId())) {
				sportCourtsIdInserted.add(court.getId());
				ContentValues cv = new ContentValues();
				cv.put(MuniSportsDbContract.SportCourtsEntry.COLUMN_ID_SERVER, court.getId());
				cv.put(MuniSportsDbContract.SportCourtsEntry.COLUMN_COURT_NAME, court.getName());
				cv.put(MuniSportsDbContract.SportCourtsEntry.COLUMN_CENTER_NAME, court.getSportCenter().getName());
				cv.put(MuniSportsDbContract.SportCourtsEntry.COLUMN_CENTER_ADDRESS, court.getSportCenter().getAddress());
				cvSportCourtsList.add(cv);
			}
		}
		ContentValues[] cvArray = cvSportCourtsList.toArray(new ContentValues[cvSportCourtsList.size()]);
		ContentResolver contentResolver = mContext.getContentResolver();
		contentResolver.bulkInsert(MuniSportsDbContract.SportCourtsEntry.CONTENT_URI, cvArray);
	}

	private void loadMatches(List<Match> matchList, Long idCompetitionServer, Context mContext) {
		List<ContentValues> cvMatcheList = new ArrayList<>();
		for (Match match : matchList) {
			ContentValues cvMatch = new ContentValues();
			String teamLocal = match.getTeamLocalEntity()==null ? MuniSportsConstants.UNDEFINDED_FIELD : match.getTeamLocalEntity().getName();
			String teamVisitor = match.getTeamVisitorEntity()==null ? MuniSportsConstants.UNDEFINDED_FIELD : match.getTeamVisitorEntity().getName();
			Long sportCenterCourt = match.getSportCenterCourt()==null ? null : match.getSportCenterCourt().getId();
			cvMatch.put(MatchesEntry.COLUMN_TEAM_LOCAL, teamLocal);
			cvMatch.put(MatchesEntry.COLUMN_TEAM_VISITOR, teamVisitor);
			cvMatch.put(MatchesEntry.COLUMN_SCORE_LOCAL, match.getScoreLocal());
			cvMatch.put(MatchesEntry.COLUMN_SCORE_VISITOR, match.getScoreVisitor());
			cvMatch.put(MatchesEntry.COLUMN_WEEK, match.getWeek());
			cvMatch.put(MatchesEntry.COLUMN_ID_SPORTCENTER, sportCenterCourt);
			cvMatch.put(MatchesEntry.COLUMN_DATE, match.getDate());
			cvMatch.put(MatchesEntry.COLUMN_ID_SERVER, match.getId());
			cvMatch.put(MatchesEntry.COLUMN_ID_COMPETITION_SERVER, idCompetitionServer);
			cvMatcheList.add(cvMatch);
		}
		ContentValues[] matchesArray = cvMatcheList.toArray(new ContentValues[cvMatcheList.size()]);
		ContentResolver muniSportsContentResolver = mContext.getContentResolver();
		Uri uri = MatchesEntry.buildMatchesUriWithCompetitions(idCompetitionServer.toString());
		muniSportsContentResolver.delete(uri, null, null);
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
