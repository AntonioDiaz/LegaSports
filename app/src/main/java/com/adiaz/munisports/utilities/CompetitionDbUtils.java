package com.adiaz.munisports.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.adiaz.munisports.entities.Competition;
import com.adiaz.munisports.sync.CompetitionDetailsCallbak;
import com.adiaz.munisports.sync.retrofit.MuniSportsRestApi;
import com.adiaz.munisports.sync.retrofit.entities.competitiondetails.CompetitionDetails;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.adiaz.munisports.activities.CompetitionActivity.idCompetitionServer;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;

/**
 * Created by toni on 01/09/2017.
 */

public class CompetitionDbUtils {

	public static final Competition queryCompetition (ContentResolver contentResolver, Long idCompetition) {
		Competition competition = null;
		Uri uri = CompetitionsEntry.buildCompetitionUriWithServerId(idCompetition);
		Cursor cursor = contentResolver.query(uri, CompetitionsEntry.PROJECTION, null, null, null);
		try {
			if (cursor.moveToNext()) {
				competition = CompetitionsEntry.initEntity(cursor);
			}
		} finally {
			cursor.close();
		}
		return competition;
	}

	public static final boolean itIsNecesaryUpdate(ContentResolver contentResolver, Long idCompetition) {
		Uri uri = CompetitionsEntry.buildCompetitionUriWithServerId(idCompetition);
		String[] projection = {	CompetitionsEntry.COLUMN_LAST_UPDATE_SERVER, CompetitionsEntry.COLUMN_LAST_UPDATE_APP };
		Cursor cursor = contentResolver.query(uri, projection, null, null, null);
		long lastPublishedOnServer;
		long lastPublishedOnApp;
		try {
			cursor.moveToFirst();
			lastPublishedOnServer = cursor.getLong(0);
			lastPublishedOnApp = cursor.getLong(1);
		} finally {
			cursor.close();

		}
		return lastPublishedOnApp<lastPublishedOnServer;
	}

	public static final void updateCompetition (Context context, CompetitionDetailsCallbak.OnFinishLoad onFinishLoad, Long idCompetition) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MuniSportsConstants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		MuniSportsRestApi muniSportsRestApi = retrofit.create(MuniSportsRestApi.class);
		Call<CompetitionDetails> listCall = muniSportsRestApi.competitionDetailsQuery(new Long(idCompetitionServer));
		CompetitionDetailsCallbak competitionDetailsCallbak = new CompetitionDetailsCallbak(context, idCompetition, onFinishLoad);
		listCall.enqueue(competitionDetailsCallbak);
	}

}
