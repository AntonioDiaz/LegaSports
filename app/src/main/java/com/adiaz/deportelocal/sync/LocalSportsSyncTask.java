package com.adiaz.deportelocal.sync;

import android.content.Context;
import android.content.SharedPreferences;

import com.adiaz.deportelocal.sync.retrofit.DeporteLocalRestApi;
import com.adiaz.deportelocal.sync.retrofit.callbacks.CompetitionsAvailableCallback;
import com.adiaz.deportelocal.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.NetworkUtilities;

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
			if (NetworkUtilities.isNetworkAvailable(context) && preferences.contains(DeporteLocalConstants.KEY_TOWN_ID)) {
				Long idTownSelect = preferences.getLong(DeporteLocalConstants.KEY_TOWN_ID, -1L);
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(DeporteLocalConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				DeporteLocalRestApi deporteLocalRestApi = retrofit.create(DeporteLocalRestApi.class);
				Call<List<CompetitionRestEntity>> repositoriesCall = deporteLocalRestApi.competitionsQuery(idTownSelect);
				repositoriesCall.enqueue(new CompetitionsAvailableCallback(context));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
