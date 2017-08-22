package com.adiaz.munisports.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.adiaz.munisports.database.MuniSportsDbContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by toni on 21/04/2017.
 */

public class MuniSportsSyncUtils {


	private static final int SYNC_INTERVAL_MINUTES = 60;
	private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_MINUTES);
	private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

	private static final String MUNISPORTS_SYNC_TAG = "munisports-sync";
	private static final String TAG = MuniSportsSyncUtils.class.getSimpleName();

	private static boolean sInitialized;

	static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
		Driver driver = new GooglePlayDriver(context);
		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
		Job syncSunshineJob = dispatcher.newJobBuilder()
				.setService(MuniSportsFirebaseJobService.class)
				.setTag(MUNISPORTS_SYNC_TAG)
				.setConstraints(Constraint.ON_ANY_NETWORK)
				.setLifetime(Lifetime.FOREVER)
				.setRecurring(true)
				.setTrigger(Trigger.executionWindow(
						SYNC_INTERVAL_SECONDS,
						SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
				.setReplaceCurrent(true)
				.build();
		dispatcher.schedule(syncSunshineJob);

	}

	synchronized public static void	initializeSimple(@NonNull final Context context) {
		startInmediateSync(context);
	}

	synchronized public static void	initialize(@NonNull final Context context) {
		if (sInitialized) {
			return;
		}
		sInitialized = true;
		scheduleFirebaseJobDispatcherSync(context);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				/* checking if it is necessary to sync. */
				Uri competitionQuery = MuniSportsDbContract.CompetitionsEntry.CONTENT_URI;
				Cursor cursor = context.getContentResolver().query(competitionQuery, null, null, null, null);
				if (cursor==null || cursor.getCount()==0) {
					startInmediateSync(context);
				}
				cursor.close();
				return null;
			}
		}.execute();
	}

	private static void startInmediateSync(Context context) {
		Intent intentToSyncImmediately = new Intent(context, MuniSportsSyncIntentService.class);
		context.startService(intentToSyncImmediately);
	}
}
