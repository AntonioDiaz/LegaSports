package com.adiaz.localsports.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by toni on 23/04/2017.
 */

public class LocalSportsFirebaseJobService extends JobService {

	private AsyncTask<Void, Void, Void> mFetchCompetitions;

	@Override
	public boolean onStartJob(final JobParameters jobParameters) {
		mFetchCompetitions = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				Context context = getApplicationContext();
				LocalSportsSyncTask.syncCompetitions(context);
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				jobFinished(jobParameters, false);
			}
		};
		mFetchCompetitions.execute();
		return true;
	}

	@Override
	public boolean onStopJob(JobParameters job) {
		if (mFetchCompetitions!=null) {
			mFetchCompetitions.cancel(true);
		}
		return true;
	}
}
