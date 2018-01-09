package com.adiaz.localsports.sync;

import android.content.Context;
import android.content.SharedPreferences;

import com.adiaz.localsports.sync.retrofit.LocalSportsRestApi;
import com.adiaz.localsports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.localsports.utilities.LocalSportsConstants;
import com.adiaz.localsports.utilities.NetworkUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by toni on 21/04/2017.
 */

public class LocalSportsSyncTask {

	private static Context mContext;
	private static final String TAG = LocalSportsSyncTask.class.getSimpleName();
	
	
	synchronized public static void syncCompetitions (Context context) {
		try {
			SharedPreferences preferences = getDefaultSharedPreferences(context);
			LocalSportsSyncTask.mContext = context;
			if (NetworkUtilities.isNetworkAvailable(context) && preferences.contains(LocalSportsConstants.KEY_TOWN_ID)) {
				Long idTownSelect = preferences.getLong(LocalSportsConstants.KEY_TOWN_ID, -1L);
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(LocalSportsConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				LocalSportsRestApi localSportsRestApi = retrofit.create(LocalSportsRestApi.class);
				Call<List<CompetitionRestEntity>> repositoriesCall = localSportsRestApi.competitionsQuery(idTownSelect);
				repositoriesCall.enqueue(new CompetitionsAvailableCallback(context));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
