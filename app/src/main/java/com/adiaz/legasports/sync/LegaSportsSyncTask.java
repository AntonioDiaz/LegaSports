package com.adiaz.legasports.sync;

import android.content.Context;
import android.util.Log;

import com.adiaz.legasports.sync.retrofit.CompetitionRestEntity;
import com.adiaz.legasports.sync.retrofit.LegaSportsRestApi;
import com.adiaz.legasports.utilities.LegaSportsConstants;
import com.adiaz.legasports.utilities.NetworkUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by toni on 21/04/2017.
 */

public class LegaSportsSyncTask {

	private static Context mContext;
	private static final String TAG = LegaSportsSyncTask.class.getSimpleName();
	
	
	synchronized public static void syncCompetitions (Context context) {
		try {
			Log.d(TAG, "syncCompetitions: 01");
			LegaSportsSyncTask.mContext = context;
			if (NetworkUtilities.isNetworkAvailable(context)) {
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(LegaSportsConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				LegaSportsRestApi legaSportsRestApi = retrofit.create(LegaSportsRestApi.class);
				Call<List<CompetitionRestEntity>> repositoriesCall = legaSportsRestApi.competitionsQuery();
				repositoriesCall.enqueue(new SyncTaskRetrofitCallback(context));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
