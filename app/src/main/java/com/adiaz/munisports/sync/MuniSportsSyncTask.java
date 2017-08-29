package com.adiaz.munisports.sync;

import android.content.Context;
import android.content.SharedPreferences;

import com.adiaz.munisports.sync.retrofit.MuniSportsRestApi;
import com.adiaz.munisports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.NetworkUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by toni on 21/04/2017.
 */

public class MuniSportsSyncTask {

	private static Context mContext;
	private static final String TAG = MuniSportsSyncTask.class.getSimpleName();
	
	
	synchronized public static void syncCompetitions (Context context) {
		try {
			SharedPreferences preferences = getDefaultSharedPreferences(context);
			MuniSportsSyncTask.mContext = context;
			if (NetworkUtilities.isNetworkAvailable(context) && preferences.contains(MuniSportsConstants.KEY_TOWN_ID)) {
				Long idTownSelect = preferences.getLong(MuniSportsConstants.KEY_TOWN_ID, -1L);
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(MuniSportsConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				MuniSportsRestApi muniSportsRestApi = retrofit.create(MuniSportsRestApi.class);
				Call<List<CompetitionRestEntity>> repositoriesCall = muniSportsRestApi.competitionsQuery(idTownSelect);
				repositoriesCall.enqueue(new CompetitionsAvailableCallback(context));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
