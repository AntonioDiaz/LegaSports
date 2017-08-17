package com.adiaz.munisports.sync;

import android.content.Context;


/**
 * Created by toni on 21/04/2017.
 */

public class MuniSportsSyncTask {

	private static Context mContext;
	private static final String TAG = MuniSportsSyncTask.class.getSimpleName();
	
	
	synchronized public static void syncCompetitions (Context context) {
/*		try {
			Log.d(TAG, "syncCompetitions: 01");
			MuniSportsSyncTask.mContext = context;
			if (NetworkUtilities.isNetworkAvailable(context)) {
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(MuniSportsConstants.BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				MuniSportsRestApi muniSportsRestApi = retrofit.create(MuniSportsRestApi.class);
				Call<List<CompetitionRestEntity>> repositoriesCall = muniSportsRestApi.competitionsQuery();
				repositoriesCall.enqueue(new SyncTaskRetrofitCallback(context));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
