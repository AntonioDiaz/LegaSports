package com.adiaz.localsports.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by toni on 21/04/2017.
 */

public class LocalSportsSyncIntentService extends IntentService {

	public LocalSportsSyncIntentService() {
		super("LocalSportsSyncIntentService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		LocalSportsSyncTask.syncCompetitions(this);
	}
}
